package com.swayam.ocr.porua.tesseract.service;

import java.util.Collection;

import org.springframework.stereotype.Repository;

import com.swayam.ocr.porua.tesseract.OcrWordId;
import com.swayam.ocr.porua.tesseract.model.OcrWord;
import com.swayam.ocr.porua.tesseract.model.RawImage;
import com.swayam.ocr.porua.tesseract.repo.OcrWordRepository;
import com.swayam.ocr.porua.tesseract.repo.RawImageRepository;

@Repository
public class HsqlBackedWordCache implements WordCache {

    private final RawImageRepository rawImageRepository;
    private final OcrWordRepository ocrWordRepository;

    public HsqlBackedWordCache(RawImageRepository rawImageRepository, OcrWordRepository ocrWordRepository) {
	this.rawImageRepository = rawImageRepository;
	this.ocrWordRepository = ocrWordRepository;
    }

    @Override
    public RawImage storeImageFile(RawImage rawImage) {
	return rawImageRepository.save(rawImage);
    }

    @Override
    public int getWordCount(long bookId, long rawImageId) {
	return ocrWordRepository.countByOcrWordIdBookIdAndOcrWordIdRawImageId(bookId, rawImageId);
    }

    @Override
    public Collection<OcrWord> getWords(long bookId, long rawImageId) {
	return ocrWordRepository.findByOcrWordIdBookIdAndOcrWordIdRawImageId(bookId, rawImageId);
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
