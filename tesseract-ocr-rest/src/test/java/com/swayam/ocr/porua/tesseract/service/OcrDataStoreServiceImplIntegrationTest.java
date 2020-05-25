package com.swayam.ocr.porua.tesseract.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.ResultSet;
import java.util.Arrays;
import java.util.Collection;
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
import com.swayam.ocr.porua.tesseract.model.Book;
import com.swayam.ocr.porua.tesseract.model.Language;
import com.swayam.ocr.porua.tesseract.model.OcrWord;
import com.swayam.ocr.porua.tesseract.model.PageImage;

@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
@SpringBootTest
class OcrDataStoreServiceImplIntegrationTest {

    private static final String SELECT_FROM_OCR_WORD =
	    "SELECT book_id, page_image_id, word_sequence_id, raw_text, corrected_text, x1, y1, x2, y2, confidence FROM ocr_word ORDER BY word_sequence_id ASC";

    @Autowired
    private OcrDataStoreServiceImpl testClass;

    @Autowired
    private JdbcOperations jdbcTemplate;

    @BeforeEach
    void setupBookAndRawImage() {
	jdbcTemplate.update("INSERT INTO book (id, name, language) VALUES (1, 'TEST BOOK 1', 'ben')");
	jdbcTemplate.update("INSERT INTO book (id, name, language) VALUES (2, 'TEST BOOK 2', 'eng')");
	jdbcTemplate.update("INSERT INTO page_image (id, book_id, name, page_number) VALUES (1, 1, 'TEST IMAGE 1.jpg', 1)");
	jdbcTemplate.update("INSERT INTO page_image (id, book_id, name, page_number) VALUES (2, 1, 'TEST IMAGE 2.jpg', 2)");
    }

    @Test
    void testAddBook() {
	// given
	Book book = new Book();
	book.setName("TEST BOOK 44");
	book.setLanguage(Language.ben);

	// when
	Book result = testClass.addBook(book);

	// then
	assertEquals(3, result.getId());
	assertEquals("TEST BOOK 44", result.getName());
	assertEquals(Language.ben, result.getLanguage());
    }

    @Test
    void testGetBooks() {
	// given
	Book book1 = new Book();
	book1.setId(1);
	book1.setName("TEST BOOK 1");
	book1.setLanguage(Language.ben);

	Book book2 = new Book();
	book2.setId(2);
	book2.setName("TEST BOOK 2");
	book2.setLanguage(Language.eng);

	List<Book> expected = Arrays.asList(book1, book2);

	// when
	Iterable<Book> results = testClass.getBooks();

	// then
	assertEquals(expected, results);
    }

    @Test
    void testGetPages() {
	// given
	Book book = new Book();
	book.setId(1);
	book.setName("TEST BOOK 1");
	book.setLanguage(Language.ben);

	PageImage pageImage1 = new PageImage();
	pageImage1.setId(1);
	pageImage1.setBook(book);
	pageImage1.setName("TEST IMAGE 1.jpg");
	pageImage1.setPageNumber(1);

	PageImage pageImage2 = new PageImage();
	pageImage2.setId(2);
	pageImage2.setBook(book);
	pageImage2.setName("TEST IMAGE 2.jpg");
	pageImage2.setPageNumber(2);

	// when
	List<PageImage> results = testClass.getPages(1);

	// then
	assertEquals(Arrays.asList(pageImage1, pageImage2), results);
    }

    @Test
    void testGetPageCount() {
	// given
	Book book = new Book();
	book.setId(1);
	book.setName("TEST BOOK 1");
	book.setLanguage(Language.ben);

	PageImage pageImage1 = new PageImage();
	pageImage1.setBook(book);
	pageImage1.setName("TEST IMAGE 3.jpg");
	pageImage1.setPageNumber(10);
	testClass.addPageImage(pageImage1);

	PageImage pageImage2 = new PageImage();
	pageImage2.setBook(book);
	pageImage2.setName("TEST IMAGE 4.jpg");
	pageImage2.setPageNumber(20);
	testClass.addPageImage(pageImage2);

	// when
	int result = testClass.getPageCount(1);

	// then
	assertEquals(4, result);
    }

    @Test
    void testAddPageImage() {
	// given
	Book book = new Book();
	book.setId(1);
	book.setName("TEST BOOK 1");
	book.setLanguage(Language.ben);

	PageImage pageImage = new PageImage();
	pageImage.setBook(book);
	pageImage.setName("TEST IMAGE TIFF 1");
	pageImage.setPageNumber(10);

	// when
	PageImage result = testClass.addPageImage(pageImage);

	// then
	assertEquals(3, result.getId());
	assertEquals(book, result.getBook());
	assertEquals("TEST IMAGE TIFF 1", result.getName());
	assertEquals(10, result.getPageNumber());
    }

    @Test
    void testAddOcrWord() {
	// given
	OcrWord ocrWord = getOcrWord(1, 1, 11, 22, 33, 44, 55.55f, "ABC123", 1);

	List<OcrWord> expected = Arrays.asList(ocrWord);

	// when
	testClass.addOcrWord(ocrWord);

	// then
	List<OcrWord> results = jdbcTemplate.query(SELECT_FROM_OCR_WORD, ocrWordMapper());

	assertEquals(expected, results);
    }

    @Test
    void testUpdateCorrectTextInOcrWord() {
	// given
	OcrWord ocrWord1 = getOcrWord(1, 1, 11, 22, 33, 44, 55.55f, "ABC123", 1);
	OcrWord ocrWord2 = getOcrWord(1, 1, 111, 222, 333, 444, 555.555f, "DEF456", 2);
	OcrWord ocrWord3 = getOcrWord(1, 1, 1111, 2222, 3333, 4444, 5555.5555f, "GHI789", 3);

	OcrWord ocrWord2_1 = getOcrWord(1, 1, 111, 222, 333, 444, 555.555f, "DEF456", 2);
	ocrWord2_1.setCorrectedText("I have changed");

	List<OcrWord> expected = Arrays.asList(ocrWord1, ocrWord2_1, ocrWord3);

	testClass.addOcrWord(ocrWord1);
	testClass.addOcrWord(ocrWord2);
	testClass.addOcrWord(ocrWord3);

	// when
	testClass.updateCorrectTextInOcrWord(new OcrWordId(1, 1, 2), "I have changed");

	// then
	List<OcrWord> results = jdbcTemplate.query(SELECT_FROM_OCR_WORD, ocrWordMapper());

	assertEquals(expected, results);
    }

    @Test
    void testRemoveWord() {
	// given
	OcrWord ocrWord1 = getOcrWord(1, 1, 11, 22, 33, 44, 55.55f, "ABC123", 1);
	OcrWord ocrWord2 = getOcrWord(1, 1, 111, 222, 333, 444, 555.555f, "DEF456", 2);
	OcrWord ocrWord3 = getOcrWord(1, 1, 1111, 2222, 3333, 4444, 5555.5555f, "GHI789", 3);

	List<OcrWord> expected = Arrays.asList(ocrWord1, ocrWord3);

	testClass.addOcrWord(ocrWord1);
	testClass.addOcrWord(ocrWord2);
	testClass.addOcrWord(ocrWord3);

	// when
	testClass.removeWord(new OcrWordId(1, 1, 2));

	// then
	List<OcrWord> results = jdbcTemplate.query(SELECT_FROM_OCR_WORD, ocrWordMapper());

	assertEquals(expected, results);
    }

    @Test
    void testGetWords() {
	// given
	OcrWord ocrWord1 = getOcrWord(1, 1, 11, 22, 33, 44, 55.55f, "ABC123", 1);
	OcrWord ocrWord2 = getOcrWord(1, 1, 111, 222, 333, 444, 555.555f, "DEF456", 2);
	OcrWord ocrWord3 = getOcrWord(1, 1, 1111, 2222, 3333, 4444, 5555.5555f, "GHI789", 3);
	OcrWord ocrWord4 = getOcrWord(1, 2, 11, 22, 33, 44, 55.55f, "ABC123", 1);
	OcrWord ocrWord5 = getOcrWord(1, 2, 111, 222, 333, 444, 555.555f, "DEF456", 2);
	OcrWord ocrWord6 = getOcrWord(1, 2, 1111, 2222, 3333, 4444, 5555.5555f, "GHI789", 3);
	OcrWord ocrWord7 = getOcrWord(2, 2, 11, 22, 33, 44, 55.55f, "ABC123", 1);
	OcrWord ocrWord8 = getOcrWord(2, 2, 111, 222, 333, 444, 555.555f, "DEF456", 2);
	OcrWord ocrWord9 = getOcrWord(2, 2, 1111, 2222, 3333, 4444, 5555.5555f, "GHI789", 3);

	List<OcrWord> toBeInserted = Arrays.asList(ocrWord1, ocrWord2, ocrWord3, ocrWord4, ocrWord5, ocrWord6, ocrWord7, ocrWord8, ocrWord9);

	List<OcrWord> expected = Arrays.asList(ocrWord1, ocrWord2, ocrWord3);

	toBeInserted.forEach(ocrWord -> testClass.addOcrWord(ocrWord));

	// when
	Collection<OcrWord> results = testClass.getWords(1, 1);

	// then
	assertEquals(expected, results);
    }

    @Test
    void testGetWordCount() {
	// given
	OcrWord ocrWord1 = getOcrWord(1, 1, 11, 22, 33, 44, 55.55f, "ABC123", 1);
	OcrWord ocrWord2 = getOcrWord(1, 1, 111, 222, 333, 444, 555.555f, "DEF456", 2);
	OcrWord ocrWord3 = getOcrWord(1, 1, 1111, 2222, 3333, 4444, 5555.5555f, "GHI789", 3);
	OcrWord ocrWord4 = getOcrWord(1, 2, 11, 22, 33, 44, 55.55f, "ABC123", 1);
	OcrWord ocrWord5 = getOcrWord(1, 2, 111, 222, 333, 444, 555.555f, "DEF456", 2);
	OcrWord ocrWord6 = getOcrWord(1, 2, 1111, 2222, 3333, 4444, 5555.5555f, "GHI789", 3);
	OcrWord ocrWord7 = getOcrWord(2, 2, 11, 22, 33, 44, 55.55f, "ABC123", 1);
	OcrWord ocrWord8 = getOcrWord(2, 2, 111, 222, 333, 444, 555.555f, "DEF456", 2);
	OcrWord ocrWord9 = getOcrWord(2, 2, 1111, 2222, 3333, 4444, 5555.5555f, "GHI789", 3);

	List<OcrWord> toBeInserted = Arrays.asList(ocrWord1, ocrWord2, ocrWord3, ocrWord4, ocrWord5, ocrWord6, ocrWord7, ocrWord8, ocrWord9);

	toBeInserted.forEach(ocrWord -> testClass.addOcrWord(ocrWord));

	// when
	int result = testClass.getWordCount(1, 1);

	// then
	assertEquals(3, result);
    }

    private RowMapper<OcrWord> ocrWordMapper() {
	return (ResultSet res, int rowNum) -> {
	    OcrWord ocrWord =
		    getOcrWord(1, 1, res.getInt("x1"), res.getInt("y1"), res.getInt("x2"), res.getInt("y2"), res.getFloat("confidence"), res.getString("raw_text"), res.getInt("word_sequence_id"));
	    ocrWord.setCorrectedText(res.getString("corrected_text"));
	    return ocrWord;
	};
    }

    private OcrWord getOcrWord(int bookId, int pageImageId, int x1, int y1, int x2, int y2, float confidence, String rawText, int wordSequenceId) {
	OcrWord ocrWord = new OcrWord();
	ocrWord.setX1(x1);
	ocrWord.setY1(y1);
	ocrWord.setX2(x2);
	ocrWord.setY2(y2);
	ocrWord.setConfidence(confidence);
	ocrWord.setRawText(rawText);
	ocrWord.setOcrWordId(new OcrWordId(bookId, pageImageId, wordSequenceId));
	return ocrWord;
    }

}
