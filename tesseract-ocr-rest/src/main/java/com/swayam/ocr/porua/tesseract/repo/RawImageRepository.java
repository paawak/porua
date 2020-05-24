package com.swayam.ocr.porua.tesseract.repo;

import org.springframework.data.repository.CrudRepository;

import com.swayam.ocr.porua.tesseract.model.RawImage;

public interface RawImageRepository extends CrudRepository<RawImage, Long> {

}
