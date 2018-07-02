package com.swayam.ocr.porua.tesseract.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.swayam.ocr.porua.tesseract.model.Language;

@RestController
@RequestMapping("/rest")
public class OCRFrontController {

	private final Logger LOGGER = LoggerFactory.getLogger(OCRFrontController.class);

	@PostMapping("/ocr")
	public String uploadFileMulti(@RequestParam("language") Language language,
			@RequestParam("images") MultipartFile[] images) {
		LOGGER.info("language: {}", language);
		for (MultipartFile image : images) {
			LOGGER.info("FileName: {}, ContentType: {}, Size: {}", image.getOriginalFilename(), image.getContentType(),
					image.getSize());
		}
		// return Flux.fromArray(new String[] { "success" });
		return "success";
	}

}
