package com.swayam.ocr.core.impl;

import java.util.Collection;
import java.util.List;

import com.swayam.ocr.core.model.CachedOcrText;
import com.swayam.ocr.core.model.Language;
import com.swayam.ocr.core.model.RawOcrWord;

public interface WordCache {

    void clearAllEntries(String rawImageFileName);

    int getWordCount(String rawImageFileName);

    void storeRawOcrWords(String rawImageFileName, Language language, List<RawOcrWord> texts);

    Collection<CachedOcrText> getWords(String rawImageFileName);

    CachedOcrText getWord(int wordId);

    void modifyWord(int wordId, String text);

    void removeWord(int wordId);

}
