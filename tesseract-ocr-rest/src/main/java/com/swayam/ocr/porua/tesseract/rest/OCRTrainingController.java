package com.swayam.ocr.porua.tesseract.rest;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.swayam.ocr.porua.tesseract.OcrWordId;
import com.swayam.ocr.porua.tesseract.model.Language;
import com.swayam.ocr.porua.tesseract.model.OcrWord;
import com.swayam.ocr.porua.tesseract.model.RawImage;
import com.swayam.ocr.porua.tesseract.service.FileSystemUtil;
import com.swayam.ocr.porua.tesseract.service.TesseractOcrWordAnalyser;
import com.swayam.ocr.porua.tesseract.service.OcrDataStoreService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/train")
public class OCRTrainingController {

    private static final Logger LOG = LoggerFactory.getLogger(OCRTrainingController.class);

    private final OcrDataStoreService wordCache;
    private final FileSystemUtil fileSystemUtil;

    public OCRTrainingController(OcrDataStoreService wordCache, FileSystemUtil fileSystemUtil) {
	this.wordCache = wordCache;
	this.fileSystemUtil = fileSystemUtil;

    }

    @PostMapping(value = "/word", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<OcrWord> getDetectedText(@RequestPart("language") final String languageAsString, @RequestPart("bookId") final long bookId,
	    @RequestPart(value = "rawImageId", required = false) final Optional<Long> rawImageId, @RequestPart("pageNumber") final int pageNumber, @RequestPart("image") final FilePart image)
	    throws IOException, URISyntaxException {

	LOG.info("FileName: {}, language: {}", image.filename(), languageAsString);

	Language language = Language.valueOf(languageAsString);

	String imageFileName = image.filename();
	Path savedImagePath = fileSystemUtil.saveMultipartFileAsImage(image);

	if (rawImageId.isEmpty()) {
	    RawImage rawImage = new RawImage();
	    rawImage.setName(imageFileName);
	    rawImage.setPageNumber(pageNumber);
	    long imageFileId = wordCache.storeImageFile(rawImage).getId();
	    return Flux.create((FluxSink<OcrWord> fluxSink) -> {
		new TesseractOcrWordAnalyser(savedImagePath, language).extractWordsFromImage(fluxSink, (wordSequenceId) -> new OcrWordId(bookId, imageFileId, wordSequenceId));
	    }).map(rawText -> wordCache.addOcrWord(rawText));
	}

	LOG.warn("Entries already present for {}: using existing entries", imageFileName);

	return Flux.fromIterable(wordCache.getWords(bookId, rawImageId.get()));

    }

    @GetMapping(value = "/word/image")
    public Mono<ResponseEntity<byte[]>> getOcrWordImage(@RequestParam("bookId") final long bookId, @RequestParam("rawImageId") final long rawImageId,
	    @RequestParam("wordSequenceId") int wordSequenceId, @RequestParam("imagePath") Path imagePath) throws IOException {
	LOG.trace("serving ocr word image for id: {}", wordSequenceId);
	OcrWord ocrText = wordCache.getWord(new OcrWordId(bookId, rawImageId, wordSequenceId));
	BufferedImage fullImage = ImageIO.read(Files.newInputStream(imagePath));
	Rectangle wordArea = TesseractOcrWordAnalyser.getWordArea(ocrText);
	BufferedImage wordImage = fullImage.getSubimage(wordArea.x, wordArea.y, wordArea.width, wordArea.height);
	String extension = imagePath.toFile().getName().split("\\.")[1];
	ByteArrayOutputStream bos = new ByteArrayOutputStream();
	ImageIO.write(wordImage, extension, bos);
	bos.flush();
	HttpHeaders responseHeaders = new HttpHeaders();
	responseHeaders.set("content-type", "image/" + extension);
	return Mono.just(new ResponseEntity<byte[]>(bos.toByteArray(), responseHeaders, HttpStatus.OK));
    }

}
