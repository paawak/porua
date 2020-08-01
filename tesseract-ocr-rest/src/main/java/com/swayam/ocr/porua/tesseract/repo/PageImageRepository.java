package com.swayam.ocr.porua.tesseract.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.swayam.ocr.porua.tesseract.model.PageImage;

public interface PageImageRepository extends CrudRepository<PageImage, Long> {

    int countByBookId(long bookId);

    List<PageImage> findByBookIdAndIgnoredIsFalseAndCorrectionCompletedIsFalse(long bookId);

}
