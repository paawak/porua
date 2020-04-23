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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.swayam.ocr.core.util.BinaryImage;
import com.swayam.ocr.core.util.Glyph;
import com.swayam.ocr.core.util.Script;
import com.swayam.ocr.core.util.Typeface;

/**
 * 
 * @author paawak
 *
 */
public class HsqlGlyphStore implements GlyphStore {
	
	private static final Logger LOG = Logger.getLogger(HsqlGlyphStore.class);
	
	private static final String GET_GLYPHS_QUERY = "SELECT gl.id, tp.name, gl.binary_image, gl.unicode, gl.description FROM script sc, typeface tp, glyph gl WHERE "
			+ " sc.id = tp.script_id AND tp.id = gl.typeface_id AND sc.name = ? ORDER BY gl.unicode"; 
	
	private static final String TYPEFACE_QUERY = "SELECT id FROM typeface WHERE name = ?";
	
	private static final String INSERT_GLYPH = "INSERT INTO glyph VALUES (DEFAULT, ?, ?, ?, (" + TYPEFACE_QUERY + "))";
	
	private static final String INSERT_TYPEFACE = "INSERT INTO typeface VALUES (DEFAULT, ?, ?, (SELECT id FROM script WHERE name = ?))";
	
	private static final String SCRIPT_QUERY = "SELECT id FROM script WHERE name = ?";
	
	private static final String INSERT_SCRIPT = "INSERT INTO script VALUES (DEFAULT, ?)";
	
	private static final String INIT_SCRIPT = "/com/swayam/ocr/res/sql/CreateTable.sql";
	
	public static final GlyphStore INSTANCE = new HsqlGlyphStore();
	
	private HsqlGlyphStore() {
		
		InputStream is = HsqlGlyphStore.class.getResourceAsStream(INIT_SCRIPT);
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		
		StringBuilder sb = new StringBuilder();
		
		try {
			
			while (true) {
				
				String line = reader.readLine();
				
				if (line == null) {
					break;
				}
				
				sb.append(line).append('\n'); 
				
			}
			
		} catch (IOException e) {
			LOG.error("could not read init script", e);
		}
		
		if (sb.length() > 0) {
			
			try {
				
				Connection con = getDbConnection();
				Statement stat = con.createStatement();
				
				String sql = sb.toString();
				
				stat.execute(sql);
				
				stat.close();
				con.close();
				
			} catch (SQLException e) {
				LOG.error("could not execute init script", e);
			}
			
		}
		
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
	
	private Connection getDbConnection() throws SQLException {
		
		String dbLoc = System.getProperty("user.home") + "/.ocr/db/ocrdb";
		
		Connection con = DriverManager.getConnection("jdbc:hsqldb:file:" + dbLoc +
				";shutdown=true", "SA", "");
		
		return con;
		
	}

}
