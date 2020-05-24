package com.swayam.ocr.porua.tesseract.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.ResultSet;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;

import com.swayam.ocr.porua.tesseract.OcrWordId;
import com.swayam.ocr.porua.tesseract.model.OcrWord;

@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
@SpringBootTest
class HsqlBackedWordCacheIntegrationTest {

    private static final String SELECT_FROM_OCR_WORD =
	    "SELECT book_id, raw_image_id, word_sequence_id, raw_text, corrected_text, x1, y1, x2, y2, confidence FROM ocr_word ORDER BY word_sequence_id ASC";

    @Autowired
    private HsqlBackedWordCache testClass;

    @Autowired
    private JdbcOperations jdbcTemplate;

    @BeforeEach
    void setupBookAndRawImage() {
	jdbcTemplate.update("INSERT INTO book (id, name, language) VALUES (1, 'TEST BOOK', 'ben')");
	jdbcTemplate.update("INSERT INTO raw_image (id, name, page_number) VALUES (1, 'TEST IMAGE.jpg', 1)");
    }

    @Test
    void testAddOcrWord() {
	// given
	OcrWord rawOcrWord = getOcrWord(11, 22, 33, 44, 55.55f, "ABC123", 1);

	List<OcrWord> expected = Arrays.asList(rawOcrWord);

	// when
	testClass.addOcrWord(rawOcrWord);

	// then
	List<OcrWord> results = jdbcTemplate.query(SELECT_FROM_OCR_WORD, ocrWordMapper());

	assertEquals(expected, results);
    }

    @Test
    void testUpdateCorrectTextInOcrWord() {
	// given
	OcrWord rawOcrWord1 = getOcrWord(11, 22, 33, 44, 55.55f, "ABC123", 1);
	OcrWord rawOcrWord2 = getOcrWord(111, 222, 333, 444, 555.555f, "DEF456", 2);
	OcrWord rawOcrWord3 = getOcrWord(1111, 2222, 3333, 4444, 5555.5555f, "GHI789", 3);

	OcrWord rawOcrWord2_1 = getOcrWord(111, 222, 333, 444, 555.555f, "DEF456", 2);
	rawOcrWord2_1.setCorrectedText("I have changed");

	List<OcrWord> expected = Arrays.asList(rawOcrWord1, rawOcrWord2_1, rawOcrWord3);

	testClass.addOcrWord(rawOcrWord1);
	testClass.addOcrWord(rawOcrWord2);
	testClass.addOcrWord(rawOcrWord3);

	// when
	testClass.updateCorrectTextInOcrWord(new OcrWordId(1, 1, 2), "I have changed");

	// then
	List<OcrWord> results = jdbcTemplate.query(SELECT_FROM_OCR_WORD, ocrWordMapper());

	assertEquals(expected, results);
    }

    @Test
    void testRemoveWord() {
	// given
	OcrWord rawOcrWord1 = getOcrWord(11, 22, 33, 44, 55.55f, "ABC123", 1);
	OcrWord rawOcrWord2 = getOcrWord(111, 222, 333, 444, 555.555f, "DEF456", 2);
	OcrWord rawOcrWord3 = getOcrWord(1111, 2222, 3333, 4444, 5555.5555f, "GHI789", 3);

	List<OcrWord> expected = Arrays.asList(rawOcrWord1, rawOcrWord3);

	testClass.addOcrWord(rawOcrWord1);
	testClass.addOcrWord(rawOcrWord2);
	testClass.addOcrWord(rawOcrWord3);

	// when
	testClass.removeWord(new OcrWordId(1, 1, 2));

	// then
	List<OcrWord> results = jdbcTemplate.query(SELECT_FROM_OCR_WORD, ocrWordMapper());

	assertEquals(expected, results);
    }

    private RowMapper<OcrWord> ocrWordMapper() {
	return (ResultSet res, int rowNum) -> {
	    OcrWord ocrWord = getOcrWord(res.getInt("x1"), res.getInt("y1"), res.getInt("x2"), res.getInt("y2"), res.getFloat("confidence"), res.getString("raw_text"), res.getInt("word_sequence_id"));
	    ocrWord.setCorrectedText(res.getString("corrected_text"));
	    return ocrWord;
	};
    }

    private OcrWord getOcrWord(int x1, int y1, int x2, int y2, float confidence, String rawText, int wordSequenceId) {
	OcrWord ocrWord = new OcrWord();
	ocrWord.setX1(x1);
	ocrWord.setY1(y1);
	ocrWord.setX2(x2);
	ocrWord.setY2(y2);
	ocrWord.setConfidence(confidence);
	ocrWord.setRawText(rawText);
	ocrWord.setOcrWordId(new OcrWordId(1, 1, wordSequenceId));
	return ocrWord;
    }

}
