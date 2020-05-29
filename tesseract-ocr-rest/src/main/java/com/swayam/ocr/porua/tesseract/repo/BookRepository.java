package com.swayam.ocr.porua.tesseract.repo;

import org.springframework.data.repository.CrudRepository;

import com.swayam.ocr.porua.tesseract.model.Book;

public interface BookRepository extends CrudRepository<Book, Long> {

}
