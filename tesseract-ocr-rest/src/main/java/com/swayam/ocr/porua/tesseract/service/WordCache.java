package com.swayam.ocr.porua.tesseract.service;

import java.util.Collection;

import com.swayam.ocr.porua.tesseract.model.CachedOcrText;
import com.swayam.ocr.porua.tesseract.model.Language;
import com.swayam.ocr.porua.tesseract.model.RawOcrWord;

public interface WordCache {

    void clearAllEntries(String rawImageFileName);

    int getWordCount(String rawImageFileName);

    int storeImageFile(String rawImageFileName, Language language);

    void storeRawOcrWord(int imageFileId, RawOcrWord rawOcrWord);

    Collection<CachedOcrText> getWords(String rawImageFileName);

    CachedOcrText getWord(int wordId);

    void modifyWord(int wordId, String text);

    void removeWord(int wordId);

}
