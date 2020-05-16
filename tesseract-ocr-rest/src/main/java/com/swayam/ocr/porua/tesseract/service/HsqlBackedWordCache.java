package com.swayam.ocr.porua.tesseract.service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;

import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.swayam.ocr.porua.tesseract.model.CachedOcrText;
import com.swayam.ocr.porua.tesseract.model.Language;
import com.swayam.ocr.porua.tesseract.model.RawOcrWord;

@Repository
public class HsqlBackedWordCache implements WordCache {

    private final JdbcOperations jdbcTemplate;

    public HsqlBackedWordCache(JdbcOperations jdbcTemplate) {
	this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void clearAllEntries(String rawImageFileName) {
	String sql = "DELETE FROM ocr_word WHERE raw_image_id = (SELECT id FROM raw_image WHERE name = ?)";
	jdbcTemplate.update(sql, rawImageFileName);
    }

    @Override
    public int getWordCount(String rawImageFileName) {
	String sql = "SELECT COUNT(*) FROM ocr_word ocr, raw_image img WHERE ocr.raw_image_id = img.id AND img.name = ?";
	return jdbcTemplate.queryForObject(sql, Integer.class, rawImageFileName);
    }

    @Override
    public int storeImageFile(String rawImageFileName, Language language) {
	String rawImageInsertSql = "INSERT INTO raw_image (id, name, language) VALUES (DEFAULT, ?, ?)";

	KeyHolder keyHolder = new GeneratedKeyHolder();

	jdbcTemplate.update(connection -> {
	    PreparedStatement pstat = connection.prepareStatement(rawImageInsertSql, Statement.RETURN_GENERATED_KEYS);
	    pstat.setString(1, rawImageFileName);
	    pstat.setString(2, language.name());
	    return pstat;
	}, keyHolder);

	return keyHolder.getKey().intValue();
    }

    @Override
    public void storeRawOcrWord(int imageFileId, RawOcrWord rawOcrWord) {
	String wordInsertSql = "INSERT INTO ocr_word (id, raw_ocr_word, raw_image_id) VALUES (DEFAULT, ?, ?)";
	jdbcTemplate.update(wordInsertSql, rawOcrWord, imageFileId);
    }

    @Override
    public Collection<CachedOcrText> getWords(String rawImageFileName) {
	String sql = "SELECT ocr.id, ocr.raw_ocr_word, ocr.corrected_text FROM ocr_word ocr, raw_image img WHERE ocr.raw_image_id = img.id AND img.name = ? ORDER BY ocr.id ASC";
	return jdbcTemplate.query(sql, this::getCachedOcrText, rawImageFileName);
    }

    @Override
    public CachedOcrText getWord(int wordId) {
	String sql = "SELECT id, raw_ocr_word, corrected_text FROM ocr_word WHERE id = ?";
	return jdbcTemplate.queryForObject(sql, this::getCachedOcrText, wordId);
    }

    @Override
    public void modifyWord(int wordId, String text) {
	String sql = "UPDATE ocr_word SET corrected_text = ? WHERE id = ?";
	jdbcTemplate.update(sql, text, wordId);
    }

    @Override
    public void removeWord(int wordId) {
	String sql = "DELETE FROM ocr_word WHERE id = ?";
	jdbcTemplate.update(sql, wordId);
    }

    private CachedOcrText getCachedOcrText(ResultSet res, int rowNum) throws SQLException {
	int id = res.getInt(1);
	RawOcrWord rawOcrWord = (RawOcrWord) res.getObject(2);
	String correctedText = res.getString(3);
	return new CachedOcrText(id, rawOcrWord, correctedText, -1);
    }

}
