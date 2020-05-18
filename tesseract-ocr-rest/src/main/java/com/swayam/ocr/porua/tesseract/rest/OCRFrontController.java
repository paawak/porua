package com.swayam.ocr.porua.tesseract.rest;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.swayam.ocr.porua.tesseract.model.Language;
import com.swayam.ocr.porua.tesseract.service.TesseractInvokerService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/rest")
public class OCRFrontController {

    private static final Logger LOGGER = LoggerFactory.getLogger(OCRFrontController.class);

    private static final List<MediaType> SUPPORTED_CONTENT_TYPES = Arrays.asList(MediaType.IMAGE_JPEG, MediaType.IMAGE_PNG, MediaType.APPLICATION_OCTET_STREAM, new MediaType("image", "tiff"));

    private final TesseractInvokerService tesseractInvokerService;
    private final String imageWriteDirectory;

    public OCRFrontController(TesseractInvokerService tesseractInvokerService, @Value("${app.config.server.image-write-directory}") String imageWriteDirectory) {
	this.tesseractInvokerService = tesseractInvokerService;
	this.imageWriteDirectory = imageWriteDirectory;

    }

    @PostMapping(value = "/ocr", produces = MediaType.TEXT_PLAIN_VALUE)
    public Mono<ResponseEntity<String>> uploadImageFile(@RequestPart("language") String languageAsString, @RequestPart("image") FilePart image) throws IOException {
	LOGGER.info("languageAsString: {}", languageAsString);

	Language language = Language.valueOf(languageAsString);

	MediaType contentType = image.headers().getContentType();

	LOGGER.info("FileName: {}, ContentType: {}", image.filename(), contentType);

	if (!SUPPORTED_CONTENT_TYPES.contains(contentType)) {
	    LOGGER.error("Un-supported ContentType: {}", contentType);
	    return Mono.just(ResponseEntity.badRequest().body("unsupported content-type: " + contentType));
	}

	Path savedPath = saveFile(image);

	return Mono.just(ResponseEntity.ok(tesseractInvokerService.submitToOCR(language, savedPath)));
    }

    private Path saveFile(FilePart image) throws IOException {
	Path outputFile = Paths.get(imageWriteDirectory, System.currentTimeMillis() + "_" + image.filename());
	image.transferTo(outputFile).block();
	return outputFile;
    }

}
