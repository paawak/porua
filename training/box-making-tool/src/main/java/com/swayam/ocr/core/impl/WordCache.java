package com.swayam.ocr.core.impl;

import java.util.Collection;

import com.swayam.ocr.core.model.CachedOcrText;
import com.swayam.ocr.core.model.RawOcrWord;

public interface WordCache {

    void clearAllEntries();

    int getWordCount();

    void storeRawOcrWords(Collection<RawOcrWord> texts);

    Collection<CachedOcrText> getWords();

    CachedOcrText getWord(int wordId);

    void modifyWord(int wordId, String text);

    void removeWord(int wordId);

}
