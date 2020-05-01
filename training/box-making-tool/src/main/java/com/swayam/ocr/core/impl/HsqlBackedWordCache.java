package com.swayam.ocr.core.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.swayam.ocr.core.model.CachedOcrText;
import com.swayam.ocr.core.model.Language;
import com.swayam.ocr.core.model.RawOcrWord;

public class HsqlBackedWordCache implements WordCache {

    private static final Logger LOG = LoggerFactory.getLogger(HsqlBackedWordCache.class);

    private static final String INIT_SCRIPT = "/sql/ocr_word_schema.sql";

    private final String hsqlDataStoreJdbcUrl;

    public HsqlBackedWordCache(String hsqlDataStoreJdbcUrl) {
	this.hsqlDataStoreJdbcUrl = hsqlDataStoreJdbcUrl;
	initSchema();
    }

    @Override
    public void clearAllEntries(String rawImageFileName) {
	String sql = "DELETE FROM ocr_word WHERE raw_image_id = (SELECT id FROM raw_image WHERE name = ?)";

	executePreparedStatement(pstat -> {
	    try {
		pstat.setString(1, rawImageFileName);
		pstat.executeUpdate();
	    } catch (SQLException e) {
		LOG.warn("failed to delete records for the " + rawImageFileName, e);
	    }
	    return null;
	}, sql);
    }

    @Override
    public int getWordCount(String rawImageFileName) {
	String sql = "SELECT COUNT(*) FROM ocr_word ocr, raw_image img WHERE ocr.raw_image_id = img.id AND img.name = ?";

	return executePreparedStatement(pstat -> {
	    try {
		pstat.setString(1, rawImageFileName);
	    } catch (SQLException e) {
		throw new RuntimeException(e);
	    }
	    try (ResultSet res = pstat.executeQuery()) {
		res.next();
		return res.getInt(1);
	    } catch (SQLException e) {
		throw new RuntimeException(e);
	    }
	}, sql);
    }

    @Override
    public void storeRawOcrWords(String rawImageFileName, Language language, List<RawOcrWord> rawTexts) {

	String rawImageInsertSql = "INSERT INTO raw_image (id, name, language) VALUES (DEFAULT, ?, ?)";
	int rawImageId;

	try (Connection con = getHsqlDbConnection(); PreparedStatement pstat = con.prepareStatement(rawImageInsertSql, Statement.RETURN_GENERATED_KEYS);) {
	    pstat.setString(1, rawImageFileName);
	    pstat.setString(2, language.name());
	    int rowsChanged = pstat.executeUpdate();
	    if (rowsChanged == 0) {
		throw new RuntimeException("Insert to raw_image failed");
	    }
	    try (ResultSet generatedKeys = pstat.getGeneratedKeys()) {
		if (generatedKeys.next()) {
		    rawImageId = generatedKeys.getInt(1);
		} else {
		    throw new RuntimeException("Insert to raw_image failed, no ID obtained.");
		}
	    }
	} catch (SQLException e) {
	    throw new RuntimeException(e);
	}

	String wordInsertSql = "INSERT INTO ocr_word (id, raw_ocr_word, raw_image_id) VALUES (DEFAULT, ?, ?)";

	executePreparedStatement(pstat -> {
	    rawTexts.forEach(rawText -> {
		try {
		    pstat.setObject(1, rawText);
		    pstat.setInt(2, rawImageId);
		    pstat.executeUpdate();
		} catch (SQLException e) {
		    throw new RuntimeException(e);
		}
	    });
	    return null;
	}, wordInsertSql);
    }

    @Override
    public Collection<CachedOcrText> getWords(String rawImageFileName) {
	String sql = "SELECT ocr.id, ocr.raw_ocr_word, ocr.corrected_text FROM ocr_word ocr, raw_image img WHERE ocr.raw_image_id = img.id AND img.name = ?";
	return executePreparedStatement(pstat -> {
	    try {
		pstat.setString(1, rawImageFileName);
	    } catch (SQLException e) {
		throw new RuntimeException(e);
	    }
	    try (ResultSet res = pstat.executeQuery()) {
		List<CachedOcrText> words = new ArrayList<>();
		while (res.next()) {
		    words.add(getCachedOcrText(res));
		}
		return words;
	    } catch (SQLException e) {
		throw new RuntimeException(e);
	    }
	}, sql);
    }

    @Override
    public CachedOcrText getWord(int wordId) {
	String sql = "SELECT id, raw_ocr_word, corrected_text FROM ocr_word WHERE id = ?";
	return executePreparedStatement(pstat -> {
	    try {
		pstat.setInt(1, wordId);
	    } catch (SQLException e) {
		throw new RuntimeException(e);
	    }
	    try (ResultSet res = pstat.executeQuery()) {
		if (res.next()) {
		    return getCachedOcrText(res);
		}
		return null;
	    } catch (SQLException e) {
		throw new RuntimeException(e);
	    }
	}, sql);
    }

    @Override
    public void modifyWord(int wordId, String text) {
	String sql = "UPDATE ocr_word SET corrected_text = ? WHERE id = ?";

	executePreparedStatement(pstat -> {
	    try {
		pstat.setString(1, text);
		pstat.setInt(2, wordId);
		pstat.executeUpdate();
	    } catch (SQLException e) {
		throw new RuntimeException(e);
	    }
	    return null;
	}, sql);
    }

    @Override
    public void removeWord(int wordId) {
	String sql = "DELETE FROM ocr_word WHERE id = ?";

	executePreparedStatement(pstat -> {
	    try {
		pstat.setInt(1, wordId);
		pstat.executeUpdate();
	    } catch (SQLException e) {
		throw new RuntimeException(e);
	    }
	    return null;
	}, sql);
    }

    private void initSchema() {

	try (BufferedReader br = new BufferedReader(new InputStreamReader(HsqlBackedWordCache.class.getResourceAsStream(INIT_SCRIPT)))) {
	    StringBuilder allLines = new StringBuilder();

	    try {

		while (true) {

		    String line = br.readLine();

		    if (line == null) {
			break;
		    }

		    allLines.append(line).append('\n');

		}

		try (Connection con = getHsqlDbConnection(); Statement stat = con.createStatement();) {
		    stat.execute(allLines.toString());
		}

	    } catch (IOException e) {
		throw new RuntimeException(e);
	    } catch (SQLException e) {
		throw new RuntimeException(e);
	    }
	} catch (IOException e) {
	    throw new RuntimeException(e);
	}

    }

    private CachedOcrText getCachedOcrText(ResultSet res) throws SQLException {
	int id = res.getInt(1);
	RawOcrWord rawOcrWord = (RawOcrWord) res.getObject(2);
	String correctedText = res.getString(3);
	return new CachedOcrText(id, rawOcrWord, correctedText, -1);
    }

    private <R> R executePreparedStatement(Function<PreparedStatement, R> task, String sql) {
	try (Connection con = getHsqlDbConnection(); PreparedStatement pstat = getPreparedStatement(con, sql);) {
	    return task.apply(pstat);
	} catch (SQLException e) {
	    throw new RuntimeException(e);
	}
    }

    private PreparedStatement getPreparedStatement(Connection con, String sql) throws SQLException {
	return con.prepareStatement(sql);
    }

    private Connection getHsqlDbConnection() throws SQLException {
	return DriverManager.getConnection(hsqlDataStoreJdbcUrl, "SA", "");
    }

}
