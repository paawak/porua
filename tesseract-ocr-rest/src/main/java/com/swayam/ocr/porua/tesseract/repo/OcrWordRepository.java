package com.swayam.ocr.porua.tesseract.repo;

import org.springframework.data.repository.CrudRepository;

import com.swayam.ocr.porua.tesseract.OcrWordId;
import com.swayam.ocr.porua.tesseract.model.OcrWord;

public interface OcrWordRepository extends CrudRepository<OcrWord, OcrWordId> {

}
