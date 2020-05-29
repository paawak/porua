package com.swayam.ocr.porua.tesseract.service;

import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Service;

import com.swayam.ocr.porua.tesseract.OcrWordId;
import com.swayam.ocr.porua.tesseract.model.Book;
import com.swayam.ocr.porua.tesseract.model.OcrWord;
import com.swayam.ocr.porua.tesseract.model.PageImage;
import com.swayam.ocr.porua.tesseract.repo.BookRepository;
import com.swayam.ocr.porua.tesseract.repo.OcrWordRepository;
import com.swayam.ocr.porua.tesseract.repo.PageImageRepository;

@Service
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
    public Book getBook(long bookId) {
	return bookRepository.findById(bookId).get();
    }

    @Override
    public PageImage addPageImage(PageImage pageImage) {
	return pageImageRepository.save(pageImage);
    }

    @Override
    public PageImage getPageImage(long pageImageId) {
	return pageImageRepository.findById(pageImageId).get();
    }

    @Override
    public int getPageCount(long bookId) {
	return pageImageRepository.countByBookId(bookId);
    }

    @Override
    public List<PageImage> getPages(long bookId) {
	return pageImageRepository.findByBookId(bookId);
    }

    @Override
    public int getWordCount(long bookId, long pageImageId) {
	return ocrWordRepository.countByOcrWordIdBookIdAndOcrWordIdPageImageId(bookId, pageImageId);
    }

    @Override
    public Collection<OcrWord> getWords(long bookId, long pageImageId) {
	return ocrWordRepository.findByOcrWordIdBookIdAndOcrWordIdPageImageId(bookId, pageImageId);
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
