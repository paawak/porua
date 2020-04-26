package com.swayam.ocr.core.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.swayam.ocr.core.model.CachedOcrText;
import com.swayam.ocr.core.model.RawOcrWord;

class HsqlBackedWordCacheIntegrationTest {

    private Connection con;

    @BeforeEach
    void initDBConnection() throws SQLException {
	con = DriverManager.getConnection("jdbc:hsqldb:file:./target/ocr/db/ocrdb;shutdown=true", "SA", "");
    }

    @AfterEach
    void closeDBConnection() throws SQLException {
	if (con != null) {
	    con.close();
	}
    }

    @Test
    void testStoreRawOcrWords() throws SQLException {
	// given
	RawOcrWord rawOcrWord1 = new RawOcrWord(11, 22, 33, 44, 55.55f, "ABC123");
	RawOcrWord rawOcrWord2 = new RawOcrWord(111, 222, 333, 444, 555.555f, "DEF456");
	RawOcrWord rawOcrWord3 = new RawOcrWord(1111, 2222, 3333, 4444, 5555.5555f, "GHI789");
	Collection<RawOcrWord> rawTexts = Arrays.asList(rawOcrWord1, rawOcrWord2, rawOcrWord3);
	HsqlBackedWordCache testClass = new HsqlBackedWordCache();

	// when
	testClass.storeRawOcrWords(rawTexts);

	// then
	String sql = "SELECT id, raw_ocr_word, corrected_text FROM ocr_word";
	List<CachedOcrText> results = new ArrayList<>();
	try (PreparedStatement pStat = con.prepareStatement(sql); ResultSet res = pStat.executeQuery();) {
	    while (res.next()) {
		int id = res.getInt(1);
		RawOcrWord rawOcrWord = (RawOcrWord) res.getObject(2);
		String correctedText = res.getString(3);
		results.add(new CachedOcrText(id, rawOcrWord, correctedText));
	    }
	}

	System.out.println(results);
    }

}
