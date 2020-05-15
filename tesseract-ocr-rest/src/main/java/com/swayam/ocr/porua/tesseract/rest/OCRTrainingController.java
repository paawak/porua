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
import com.swayam.ocr.porua.tesseract.service.TesseractOcrWordAnalyser;

@RestController
@RequestMapping("/train")
public class OCRTrainingController {

    @GetMapping(value = "/words", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<RawOcrWord> getDetectedText(@RequestParam("imagePath") Path imagePath, @RequestParam("language") Language language) {
	return new TesseractOcrWordAnalyser(imagePath, language).getDetectedText();
    }

}
