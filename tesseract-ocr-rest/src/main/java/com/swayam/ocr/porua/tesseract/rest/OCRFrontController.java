package com.swayam.ocr.porua.tesseract.rest;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.swayam.ocr.porua.tesseract.model.ImageData;
import com.swayam.ocr.porua.tesseract.model.Language;
import com.swayam.ocr.porua.tesseract.service.TesseractInvokerService;

@RestController
@RequestMapping("/rest")
public class OCRFrontController {

	private static final Logger LOGGER = LoggerFactory.getLogger(OCRFrontController.class);

	private static final List<String> SUPPORTED_CONTENT_TYPES = Arrays.asList(MediaType.IMAGE_JPEG_VALUE,
			MediaType.IMAGE_PNG_VALUE, "image/tiff");

	private final TesseractInvokerService tesseractInvokerService;

	public OCRFrontController(TesseractInvokerService tesseractInvokerService) {
		this.tesseractInvokerService = tesseractInvokerService;
	}

	@PostMapping(value = "/ocr", produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> uploadImageFile(@RequestParam("language") Language language,
			@RequestParam("image") MultipartFile image) throws IOException {

		LOGGER.info("language: {}", language);
		LOGGER.info("FileName: {}, ContentType: {}, Size: {}", image.getOriginalFilename(), image.getContentType(),
				image.getSize());

		if (!SUPPORTED_CONTENT_TYPES.contains(image.getContentType())) {
			return ResponseEntity.badRequest().body("unsupported content-type: " + image.getContentType());
		}

		return ResponseEntity.ok(tesseractInvokerService.submitToOCR(language,
				new ImageData(image.getOriginalFilename(), image.getInputStream(), image.getContentType())));
	}

}
