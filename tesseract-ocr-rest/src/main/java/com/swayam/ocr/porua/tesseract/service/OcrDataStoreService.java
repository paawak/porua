package com.swayam.ocr.porua.tesseract.service;

import java.util.Collection;

import com.swayam.ocr.porua.tesseract.OcrWordId;
import com.swayam.ocr.porua.tesseract.model.OcrWord;
import com.swayam.ocr.porua.tesseract.model.PageImage;

public interface OcrDataStoreService {

    PageImage addImageFile(PageImage rawImage);

    int getWordCount(long bookId, long rawImageId);

    Collection<OcrWord> getWords(long bookId, long rawImageId);

    OcrWord addOcrWord(OcrWord rawOcrWord);

    void updateCorrectTextInOcrWord(OcrWordId ocrWordId, String correctedText);

    OcrWord getWord(OcrWordId ocrWordId);

    void removeWord(OcrWordId ocrWordId);

}
