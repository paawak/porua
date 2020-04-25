package com.swayam.ocr.core.impl;

import java.util.Collection;

import com.swayam.ocr.core.model.TextBox;

public interface WordCache {

    void clearAllEntries();

    int getWordCount();

    void storeWords(Collection<TextBox> texts);

    Collection<TextBox> getWords();

    TextBox getWord(int wordId);

    void modifyWord(int wordId, String text);

    void removeWord(int wordId);

}
