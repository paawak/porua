package com.swayam.ocr.porua.tesseract.service;

import static org.bytedeco.javacpp.lept.pixDestroy;
import static org.bytedeco.javacpp.lept.pixRead;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.lept.PIX;
import org.bytedeco.javacpp.tesseract.TessBaseAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.swayam.ocr.porua.tesseract.model.ImageData;
import com.swayam.ocr.porua.tesseract.model.Language;

import reactor.core.publisher.Flux;

@Service
public class TesseractInvokerServiceImpl implements TesseractInvokerService {

	private static final Logger LOGGER = LoggerFactory.getLogger(TesseractInvokerServiceImpl.class);

	private final String tessdataDirectory;
	private final String imageWriteDirectory;

	public TesseractInvokerServiceImpl(@Value("${app.config.ocr.tesseract.tessdata-location}") String tessdataDirectory,
			@Value("${app.config.server.image-write-directory}") String imageWriteDirectory) {
		this.tessdataDirectory = tessdataDirectory;
		this.imageWriteDirectory = imageWriteDirectory;
	}

	@Override
	public Flux<String> submitToOCR(Language language, ImageData imageData) throws IOException {

		Path imagePath = saveFile(imageData);

		LOGGER.info("saved image file at: {}", imagePath);

		try (TessBaseAPI api = new TessBaseAPI();) {
			int returnCode = api.Init(tessdataDirectory, language.name());
			if (returnCode != 0) {
				return Flux.error(new RuntimeException("could not initialize tesseract, error code: " + returnCode));
			}

			PIX image = pixRead(imagePath.toFile().getAbsolutePath());

			api.SetImage(image);

			BytePointer outText = api.GetUTF8Text();
			String ocrText = outText.getString().trim();

			LOGGER.info("ocrText: {}", ocrText);

			api.End();
			outText.close();
			outText.deallocate();
			pixDestroy(image);

			return Flux.just(ocrText);

		} finally {
			Files.delete(imagePath);
		}

	}

	private Path saveFile(ImageData imageData) throws IOException {
		Path outputFile = Paths.get(imageWriteDirectory, System.currentTimeMillis() + "_" + imageData.getFileName());
		Files.copy(imageData.getFileContents(), outputFile);
		return outputFile;
	}

}
