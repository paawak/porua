package com.swayam.ocr.porua.tesseract.rest;

import java.nio.file.Path;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.swayam.ocr.porua.tesseract.model.Language;
import com.swayam.ocr.porua.tesseract.model.RawOcrWord;
import com.swayam.ocr.porua.tesseract.service.HsqlBackedWordCache;
import com.swayam.ocr.porua.tesseract.service.TesseractOcrWordAnalyser;
import com.swayam.ocr.porua.tesseract.service.WordCache;

@RestController
@RequestMapping("/train")
public class OCRTrainingController {

    @GetMapping(value = "/word", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<RawOcrWord> getDetectedText(@RequestParam("imagePath") Path imagePath, @RequestParam("language") Language language) {
	List<RawOcrWord> words = new TesseractOcrWordAnalyser(imagePath, language).getDetectedText();
	WordCache cache = new HsqlBackedWordCache("jdbc:hsqldb:file:./target/hsql-db/ocrdb;shutdown=true");
	cache.storeRawOcrWords(imagePath.getFileName().toString(), language, words);
	return words;
    }

}
