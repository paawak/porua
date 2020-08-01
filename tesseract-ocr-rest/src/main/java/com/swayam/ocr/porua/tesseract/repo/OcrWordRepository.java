package com.swayam.ocr.porua.tesseract.repo;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.swayam.ocr.porua.tesseract.OcrWordId;
import com.swayam.ocr.porua.tesseract.model.OcrWord;

public interface OcrWordRepository extends CrudRepository<OcrWord, OcrWordId> {

    int countByIgnoredFalseAndOcrWordIdBookIdAndOcrWordIdPageImageId(long bookId, long pageImageId);

    List<OcrWord> findByIgnoredFalseAndOcrWordIdBookIdAndOcrWordIdPageImageIdOrderByOcrWordIdWordSequenceId(long bookId, long pageImageId);

    @Modifying
    @Query("update OcrWord set ignored = TRUE where ocrWordId = :ocrWordId")
    int markWordAsIgnored(@Param("ocrWordId") OcrWordId ocrWordId);

}
