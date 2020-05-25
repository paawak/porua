package com.swayam.ocr.porua.tesseract.service;

import java.util.Collection;

import org.springframework.stereotype.Repository;

import com.swayam.ocr.porua.tesseract.OcrWordId;
import com.swayam.ocr.porua.tesseract.model.Book;
import com.swayam.ocr.porua.tesseract.model.OcrWord;
import com.swayam.ocr.porua.tesseract.model.PageImage;
import com.swayam.ocr.porua.tesseract.repo.BookRepository;
import com.swayam.ocr.porua.tesseract.repo.OcrWordRepository;
import com.swayam.ocr.porua.tesseract.repo.PageImageRepository;

@Repository
public class OcrDataStoreServiceImpl implements OcrDataStoreService {

    private final BookRepository bookRepository;
    private final PageImageRepository pageImageRepository;
    private final OcrWordRepository ocrWordRepository;

    public OcrDataStoreServiceImpl(BookRepository bookRepository, PageImageRepository pageImageRepository, OcrWordRepository ocrWordRepository) {
	this.bookRepository = bookRepository;
	this.pageImageRepository = pageImageRepository;
	this.ocrWordRepository = ocrWordRepository;
    }

    @Override
    public Book addBook(Book book) {
	return bookRepository.save(book);
    }

    @Override
    public Iterable<Book> getBooks() {
	return bookRepository.findAll();
    }

    @Override
    public PageImage addPageImage(PageImage pageImage) {
	return pageImageRepository.save(pageImage);
    }

    @Override
    public int getWordCount(long bookId, long rawImageId) {
	return ocrWordRepository.countByOcrWordIdBookIdAndOcrWordIdPageImageId(bookId, rawImageId);
    }

    @Override
    public Collection<OcrWord> getWords(long bookId, long rawImageId) {
	return ocrWordRepository.findByOcrWordIdBookIdAndOcrWordIdPageImageId(bookId, rawImageId);
    }

    @Override
    public OcrWord getWord(OcrWordId ocrWordId) {
	return ocrWordRepository.findById(ocrWordId).get();
    }

    @Override
    public OcrWord addOcrWord(OcrWord ocrWord) {
	return ocrWordRepository.save(ocrWord);
    }

    @Override
    public void updateCorrectTextInOcrWord(OcrWordId ocrWordId, String correctedText) {
	OcrWord ocrWord = getWord(ocrWordId);
	ocrWord.setCorrectedText(correctedText);
	ocrWordRepository.save(ocrWord);
    }

    @Override
    public void removeWord(OcrWordId ocrWordId) {
	ocrWordRepository.deleteById(ocrWordId);
    }

}
