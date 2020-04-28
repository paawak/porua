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
import com.swayam.ocr.core.model.RawOcrWord;

public class HsqlBackedWordCache implements WordCache {

    private static final Logger LOG = LoggerFactory.getLogger(HsqlBackedWordCache.class);

    private static final String INIT_SCRIPT = "/sql/ocr_word_schema.sql";

    public HsqlBackedWordCache() {
	initSchema();
    }

    @Override
    public void clearAllEntries() {
	String sql = "TRUNCATE TABLE ocr_word";

	executePreparedStatement(pstat -> {
	    try {
		pstat.executeUpdate();
	    } catch (SQLException e) {
		LOG.warn("failed to truncate table ocr_word", e);
	    }
	    return null;
	}, sql);
    }

    @Override
    public int getWordCount() {
	String sql = "SELECT COUNT(*) FROM ocr_word";

	return executePreparedStatement(pstat -> {
	    try (ResultSet res = pstat.executeQuery()) {
		res.next();
		return res.getInt(1);
	    } catch (SQLException e) {
		throw new RuntimeException(e);
	    }
	}, sql);
    }

    @Override
    public void storeRawOcrWords(List<RawOcrWord> rawTexts) {
	String sql = "INSERT INTO ocr_word (id, raw_ocr_word) VALUES (DEFAULT, ?)";

	executePreparedStatement(pstat -> {
	    rawTexts.forEach(rawText -> {
		try {
		    pstat.setObject(1, rawText);
		    pstat.executeUpdate();
		} catch (SQLException e) {
		    throw new RuntimeException(e);
		}
	    });
	    return null;
	}, sql);
    }

    @Override
    public Collection<CachedOcrText> getWords() {
	String sql = "SELECT id, raw_ocr_word, corrected_text, line_number FROM ocr_word";
	return executePreparedStatement(pstat -> {
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
	String sql = "SELECT id, raw_ocr_word, corrected_text, line_number FROM ocr_word WHERE id = ?";
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
	int lineNumber = res.getInt(4);
	return new CachedOcrText(id, rawOcrWord, correctedText, lineNumber);
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
	return DriverManager.getConnection("jdbc:hsqldb:file:./target/ocr/db/ocrdb;shutdown=true", "SA", "");
    }

}
