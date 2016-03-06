/*
 * OcrWorkBench.java
 *
 * Created on Jan 3, 2012
 *
 * Copyright (c) 2002 - 2012 : Swayam Inc.
 *
 * P R O P R I E T A R Y & C O N F I D E N T I A L
 *
 * The copyright of this document is vested in Swayam Inc. without
 * whose prior written permission its contents must not be published,
 * adapted or reproduced in any form or disclosed or
 * issued to any third party.
 */
package com.swayam.ocr.core.matcher;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.swayam.ocr.core.model.WordImage;
import com.swayam.ocr.core.util.BinaryImage;
import com.swayam.ocr.core.util.Glyph;
import com.swayam.ocr.core.util.HsqlConnectionUtil;
import com.swayam.ocr.core.util.Script;
import com.swayam.ocr.core.util.Typeface;

/**
 * 
 * @author paawak
 *
 */
public class HsqlGlyphStore implements GlyphStore {

	private static final Logger LOG = LoggerFactory.getLogger(HsqlGlyphStore.class);

	private static final String GET_GLYPHS_QUERY = "SELECT gl.id, tp.name, gl.binary_image, gl.unicode, gl.description FROM script sc, typeface tp, glyph gl WHERE "
			+ " sc.id = tp.script_id AND tp.id = gl.typeface_id AND sc.name = ? ORDER BY gl.unicode";

	private static final String TYPEFACE_QUERY = "SELECT id FROM typeface WHERE name = ?";

	private static final String INSERT_GLYPH = "INSERT INTO glyph VALUES (DEFAULT, ?, ?, ?, (" + TYPEFACE_QUERY + "))";

	private static final String INSERT_TYPEFACE = "INSERT INTO typeface VALUES (DEFAULT, ?, ?, (SELECT id FROM script WHERE name = ?))";

	private static final String SCRIPT_QUERY = "SELECT id FROM script WHERE name = ?";

	private static final String INSERT_SCRIPT = "INSERT INTO script VALUES (DEFAULT, ?)";

	private static final String INSERT_WORD_IMAGE = "INSERT INTO word_image (id, image_name, tesseract_value) VALUES (DEFAULT, ?, ?)";

	private static final String GET_WORD_IMAGES = "SELECT id, image_name, tesseract_value, actual_value FROM word_image ORDER BY id";

	public static final GlyphStore INSTANCE = new HsqlGlyphStore();

	private HsqlGlyphStore() {

	}

	@Override
	public List<Glyph> getGlyphs(Script script) {

		List<Glyph> glyphs = new ArrayList<Glyph>();

		try {

			Connection con = getDbConnection();

			PreparedStatement pstat = con.prepareStatement(GET_GLYPHS_QUERY);
			pstat.setString(1, script.name());

			ResultSet res = pstat.executeQuery();

			while (res.next()) {

				int glyphId = res.getInt(1);
				String typefaceName = res.getString(2);
				BinaryImage binaryImage = (BinaryImage) res.getObject(3);
				String unicodeString = res.getString(4);
				String desc = res.getString(5);

				Typeface typeFace = new Typeface();
				typeFace.setScript(script);
				typeFace.setName(typefaceName);

				Glyph glyph = new Glyph(glyphId, typeFace, unicodeString, binaryImage, desc);
				glyphs.add(glyph);

			}

			res.close();
			pstat.close();
			con.close();

		} catch (SQLException e) {
			LOG.error("could not get glyphs", e);
		}

		return glyphs;
	}

	@Override
	public void addGlyph(Glyph glyph) {

		try {

			checkAndInsertTypeface(glyph);

			Connection con = getDbConnection();

			PreparedStatement pstat = con.prepareStatement(INSERT_GLYPH);

			pstat.setObject(1, glyph.getImage());
			pstat.setString(2, glyph.getUnicodeText());
			pstat.setString(3, glyph.getDescription());
			pstat.setString(4, glyph.getTypeface().getName());

			pstat.executeUpdate();

			pstat.close();
			con.close();

		} catch (SQLException e) {
			LOG.error("could not add glyph", e);
		}

	}

	@Override
	public void addWordImage(File imageFile, String tesseractValue) {
		try (Connection con = getDbConnection(); PreparedStatement insertStat = con.prepareStatement(INSERT_WORD_IMAGE);) {

			insertStat.setString(1, imageFile.getName());
			insertStat.setString(2, tesseractValue);

			insertStat.executeUpdate();

		} catch (SQLException e) {
			LOG.error("could not add word image", e);
		}

	}

	@Override
	public List<WordImage> getWordImages() {
		List<WordImage> wordImages = new ArrayList<WordImage>();

		try (Connection con = getDbConnection(); PreparedStatement queryStat = con.prepareStatement(GET_WORD_IMAGES);) {
			ResultSet res = queryStat.executeQuery();

			while (res.next()) {

				WordImage wordImage = new WordImage();
				wordImage.setId(res.getLong("id"));
				wordImage.setImageFileName(res.getString("image_name"));
				wordImage.setTesseractValue(res.getString("tesseract_value"));
				wordImage.setActualVale(res.getString("actual_value"));

				wordImages.add(wordImage);

			}

			res.close();
		} catch (SQLException e) {
			LOG.error("could not query word images", e);
		}

		return wordImages;
	}

	private Connection getDbConnection() throws SQLException {
		return HsqlConnectionUtil.INSTANCE.getDbConnection();
	}

	private void checkAndInsertTypeface(Glyph glyph) throws SQLException {

		checkAndInsertScript(glyph);

		Connection con = getDbConnection();

		String typefaceName = glyph.getTypeface().getName();

		PreparedStatement queryStat = con.prepareStatement(TYPEFACE_QUERY);

		queryStat.setString(1, typefaceName);

		ResultSet res = queryStat.executeQuery();

		if (!res.next()) {

			PreparedStatement insertStat = con.prepareStatement(INSERT_TYPEFACE);
			insertStat.setString(1, typefaceName);
			insertStat.setString(2, glyph.getTypeface().getDescription());
			insertStat.setString(3, glyph.getTypeface().getScript().name());
			insertStat.executeUpdate();

			insertStat.close();

		}

		res.close();
		queryStat.close();
		con.close();

	}

	private void checkAndInsertScript(Glyph glyph) throws SQLException {

		Connection con = getDbConnection();

		String scriptName = glyph.getTypeface().getScript().name();

		PreparedStatement queryStat = con.prepareStatement(SCRIPT_QUERY);

		queryStat.setString(1, scriptName);

		ResultSet res = queryStat.executeQuery();

		if (!res.next()) {

			PreparedStatement insertStat = con.prepareStatement(INSERT_SCRIPT);
			insertStat.setString(1, scriptName);
			insertStat.executeUpdate();

			insertStat.close();

		}

		res.close();
		queryStat.close();
		con.close();

	}

}
