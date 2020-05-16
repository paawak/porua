package com.swayam.ocr.porua.tesseract.rest;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.swayam.ocr.porua.tesseract.model.CachedOcrText;
import com.swayam.ocr.porua.tesseract.model.Language;
import com.swayam.ocr.porua.tesseract.model.RawOcrWord;
import com.swayam.ocr.porua.tesseract.service.HsqlBackedWordCache;
import com.swayam.ocr.porua.tesseract.service.TesseractOcrWordAnalyser;
import com.swayam.ocr.porua.tesseract.service.WordCache;

@RestController
@RequestMapping("/train")
public class OCRTrainingController {

    private static final Logger LOG = LoggerFactory.getLogger(OCRTrainingController.class);

    private final WordCache wordCache;

    public OCRTrainingController() {
	wordCache = new HsqlBackedWordCache("jdbc:hsqldb:file:./target/hsql-db/ocrdb;shutdown=true");
    }

    @GetMapping(value = "/word", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<RawOcrWord> getDetectedText(@RequestParam("imagePath") Path imagePath, @RequestParam("language") Language language) {

	String imageFile = imagePath.toFile().getName();

	if (wordCache.getWordCount(imageFile) == 0) {
	    List<RawOcrWord> words = new TesseractOcrWordAnalyser(imagePath, language).getDetectedText();
	    wordCache.storeRawOcrWords(imageFile, language, words);
	    return words;
	}

	LOG.warn("Entries already present for {}: using existing entries", imageFile);

	return wordCache.getWords(imageFile).stream().map(CachedOcrText::getRawOcrText).collect(Collectors.toList());

    }

    @GetMapping(value = "/word/image")
    public void getOcrWordImage(@RequestParam("wordId") int wordId, @RequestParam("imagePath") Path imagePath, HttpServletResponse response) throws IOException {
	CachedOcrText ocrText = wordCache.getWord(wordId);
	BufferedImage fullImage = ImageIO.read(Files.newInputStream(imagePath));
	Rectangle wordArea = TesseractOcrWordAnalyser.getWordArea(ocrText.getRawOcrText());
	BufferedImage wordImage = fullImage.getSubimage(wordArea.x, wordArea.y, wordArea.width, wordArea.height);
	String extension = imagePath.toFile().getName().split("\\.")[1];
	response.setContentType("image/" + extension);
	ByteArrayOutputStream bos = new ByteArrayOutputStream();
	ImageIO.write(wordImage, extension, bos);
	bos.flush();
	IOUtils.copy(new ByteArrayInputStream(bos.toByteArray()), response.getOutputStream());
    }

}
