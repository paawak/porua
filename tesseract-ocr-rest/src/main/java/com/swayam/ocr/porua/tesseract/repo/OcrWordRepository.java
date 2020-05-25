package com.swayam.ocr.porua.tesseract.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.swayam.ocr.porua.tesseract.OcrWordId;
import com.swayam.ocr.porua.tesseract.model.OcrWord;

public interface OcrWordRepository extends CrudRepository<OcrWord, OcrWordId> {

    int countByOcrWordIdBookIdAndOcrWordIdPageImageId(long bookId, long pageImageId);

    List<OcrWord> findByOcrWordIdBookIdAndOcrWordIdPageImageId(long bookId, long pageImageId);

}
