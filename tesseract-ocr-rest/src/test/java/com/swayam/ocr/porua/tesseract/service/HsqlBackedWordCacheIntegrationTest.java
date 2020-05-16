package com.swayam.ocr.porua.tesseract.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;

import com.swayam.ocr.porua.tesseract.model.CachedOcrText;
import com.swayam.ocr.porua.tesseract.model.Language;
import com.swayam.ocr.porua.tesseract.model.RawOcrWord;

@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
@SpringBootTest
class HsqlBackedWordCacheIntegrationTest {

    @Autowired
    private HsqlBackedWordCache testClass;

    @Autowired
    private JdbcOperations jdbcTemplate;

    @Test
    void testStoreRawOcrWords() throws SQLException {
	// given
	RawOcrWord rawOcrWord1 = new RawOcrWord(11, 22, 33, 44, 55.55f, "ABC123", 1);
	RawOcrWord rawOcrWord2 = new RawOcrWord(111, 222, 333, 444, 555.555f, "DEF456", 2);
	RawOcrWord rawOcrWord3 = new RawOcrWord(1111, 2222, 3333, 4444, 5555.5555f, "GHI789", 3);
	List<RawOcrWord> rawTexts = Arrays.asList(rawOcrWord1, rawOcrWord2, rawOcrWord3);

	List<CachedOcrText> expected = Arrays.asList(new CachedOcrText(1, rawOcrWord1, null, -1), new CachedOcrText(2, rawOcrWord2, null, -1), new CachedOcrText(3, rawOcrWord3, null, -1));

	// when
	testClass.storeRawOcrWords("MyRandomFileForTest.tiff", Language.ben, rawTexts);

	// then
	Map<Integer, Object> rawImageRow = jdbcTemplate.queryForObject("SELECT id, name, language FROM raw_image", (ResultSet res, int rowNum) -> {
	    Map<Integer, Object> row = new HashMap<>();
	    row.put(1, res.getInt(1));
	    row.put(2, res.getString(2));
	    row.put(3, res.getString(3));
	    return row;
	});
	assertEquals(1, rawImageRow.get(1));
	assertEquals("MyRandomFileForTest.tiff", rawImageRow.get(2));
	assertEquals("ben", rawImageRow.get(3));

	List<CachedOcrText> resultsOcr = jdbcTemplate.query("SELECT id, raw_ocr_word, corrected_text, raw_image_id FROM ocr_word", (ResultSet res, int rowNum) -> {
	    int id = res.getInt(1);
	    RawOcrWord rawOcrWord = (RawOcrWord) res.getObject(2);
	    String correctedText = res.getString(3);
	    assertEquals(1, res.getInt(4));
	    return new CachedOcrText(id, rawOcrWord, correctedText, -1);
	});

	assertEquals(expected, resultsOcr);
    }

    @Test
    void testRemoveWord() throws SQLException {
	// given
	RawOcrWord rawOcrWord1 = new RawOcrWord(11, 22, 33, 44, 55.55f, "ABC123", 1);
	RawOcrWord rawOcrWord2 = new RawOcrWord(111, 222, 333, 444, 555.555f, "DEF456", 2);
	RawOcrWord rawOcrWord3 = new RawOcrWord(1111, 2222, 3333, 4444, 5555.5555f, "GHI789", 3);
	List<RawOcrWord> rawTexts = Arrays.asList(rawOcrWord1, rawOcrWord2, rawOcrWord3);

	List<CachedOcrText> expected = Arrays.asList(new CachedOcrText(1, rawOcrWord1, null, -1), new CachedOcrText(3, rawOcrWord3, null, -1));

	testClass.storeRawOcrWords("MyRandomFileForTest.tiff", Language.ben, rawTexts);

	// when
	testClass.removeWord(2);

	// then
	List<CachedOcrText> results = jdbcTemplate.query("SELECT id, raw_ocr_word, corrected_text FROM ocr_word", (ResultSet res, int rowNum) -> {
	    int id = res.getInt(1);
	    RawOcrWord rawOcrWord = (RawOcrWord) res.getObject(2);
	    String correctedText = res.getString(3);
	    return new CachedOcrText(id, rawOcrWord, correctedText, -1);
	});

	assertEquals(expected, results);
    }

    @Test
    void testClearAllEntries() throws SQLException {
	// given
	RawOcrWord rawOcrWord1 = new RawOcrWord(11, 22, 33, 44, 55.55f, "ABC123", 1);
	RawOcrWord rawOcrWord2 = new RawOcrWord(111, 222, 333, 444, 555.555f, "DEF456", 2);
	RawOcrWord rawOcrWord3 = new RawOcrWord(1111, 2222, 3333, 4444, 5555.5555f, "GHI789", 3);
	List<RawOcrWord> rawTexts = Arrays.asList(rawOcrWord1, rawOcrWord2, rawOcrWord3);

	testClass.storeRawOcrWords("MyRandomFileForTest.tiff", Language.ben, rawTexts);

	// when
	testClass.clearAllEntries("MyRandomFileForTest.tiff");

	// then
	String sql = "SELECT COUNT(*) FROM ocr_word";
	int count = jdbcTemplate.queryForObject(sql, Integer.class);
	assertEquals(0, count);
    }

    @Test
    void testModifyWord() throws SQLException {
	// given
	RawOcrWord rawOcrWord1 = new RawOcrWord(11, 22, 33, 44, 55.55f, "ABC123", 1);
	RawOcrWord rawOcrWord2 = new RawOcrWord(111, 222, 333, 444, 555.555f, "DEF456", 2);
	RawOcrWord rawOcrWord3 = new RawOcrWord(1111, 2222, 3333, 4444, 5555.5555f, "GHI789", 3);
	List<RawOcrWord> rawTexts = Arrays.asList(rawOcrWord1, rawOcrWord2, rawOcrWord3);

	List<CachedOcrText> expected = Arrays.asList(new CachedOcrText(1, rawOcrWord1, null, -1), new CachedOcrText(2, rawOcrWord2, "I have changed", -1), new CachedOcrText(3, rawOcrWord3, null, -1));

	testClass.storeRawOcrWords("MyRandomFileForTest.tiff", Language.ben, rawTexts);

	// when
	testClass.modifyWord(2, "I have changed");

	// then
	List<CachedOcrText> results = jdbcTemplate.query("SELECT id, raw_ocr_word, corrected_text FROM ocr_word", (ResultSet res, int rowNum) -> {
	    int id = res.getInt(1);
	    RawOcrWord rawOcrWord = (RawOcrWord) res.getObject(2);
	    String correctedText = res.getString(3);
	    return new CachedOcrText(id, rawOcrWord, correctedText, -1);
	});

	assertEquals(expected, results);
    }

    @Test
    void testGetWords() {
	// given
	RawOcrWord rawOcrWord1 = new RawOcrWord(11, 22, 33, 44, 55.55f, "ABC123", 1);
	RawOcrWord rawOcrWord2 = new RawOcrWord(111, 222, 333, 444, 555.555f, "DEF456", 2);
	RawOcrWord rawOcrWord3 = new RawOcrWord(1111, 2222, 3333, 4444, 5555.5555f, "GHI789", 3);
	List<RawOcrWord> rawTexts = Arrays.asList(rawOcrWord1, rawOcrWord2, rawOcrWord3);

	List<CachedOcrText> expected = Arrays.asList(new CachedOcrText(1, rawOcrWord1, null, -1), new CachedOcrText(2, rawOcrWord2, null, -1), new CachedOcrText(3, rawOcrWord3, null, -1));

	testClass.storeRawOcrWords("MyRandomFileForTest.tiff", Language.ben, rawTexts);

	// when
	Collection<CachedOcrText> results = testClass.getWords("MyRandomFileForTest.tiff");

	// then
	assertEquals(expected, results);
    }

    @Test
    void testGetWord() {
	// given
	RawOcrWord rawOcrWord1 = new RawOcrWord(11, 22, 33, 44, 55.55f, "ABC123", 1);
	RawOcrWord rawOcrWord2 = new RawOcrWord(111, 222, 333, 444, 555.555f, "DEF456", 2);
	RawOcrWord rawOcrWord3 = new RawOcrWord(1111, 2222, 3333, 4444, 5555.5555f, "GHI789", 3);
	List<RawOcrWord> rawTexts = Arrays.asList(rawOcrWord1, rawOcrWord2, rawOcrWord3);

	testClass.storeRawOcrWords("MyRandomFileForTest.tiff", Language.ben, rawTexts);

	// when
	CachedOcrText result = testClass.getWord(3);

	// then
	assertEquals(new CachedOcrText(3, rawOcrWord3, null, -1), result);
    }

}
