package com.swayam.ocr.core.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.swayam.ocr.core.model.CachedOcrText;
import com.swayam.ocr.core.model.RawOcrWord;

public class HsqlBackedWordCache implements WordCache {

    private static final Logger LOG = LoggerFactory.getLogger(HsqlBackedWordCache.class);

    @Override
    public void clearAllEntries() {
	// TODO Auto-generated method stub

    }

    @Override
    public int getWordCount() {
	// TODO Auto-generated method stub
	return 0;
    }

    @Override
    public void storeRawOcrWords(Collection<RawOcrWord> rawTexts) {
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

    private <R> R executePreparedStatement(Function<PreparedStatement, R> task, String sql) {
	try (Connection con = getHsqlDbConnection(); PreparedStatement pstat = getPreparedStatement(con, sql);) {
	    return task.apply(pstat);
	} catch (SQLException e) {
	    throw new RuntimeException(e);
	}
    }

    @Override
    public Collection<CachedOcrText> getWords() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public CachedOcrText getWord(int wordId) {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public void modifyWord(int wordId, String text) {
	// TODO Auto-generated method stub

    }

    @Override
    public void removeWord(int wordId) {
	// TODO Auto-generated method stub

    }

    private PreparedStatement getPreparedStatement(Connection con, String sql) throws SQLException {
	return con.prepareStatement(sql);
    }

    private Connection getHsqlDbConnection() throws SQLException {
	return DriverManager.getConnection("jdbc:hsqldb:file:./target/ocr/db/ocrdb;shutdown=true", "SA", "");
    }

}
