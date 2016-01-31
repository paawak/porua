package com.swayam.ocr.porua;

import static org.bytedeco.javacpp.lept.pixDestroy;
import static org.bytedeco.javacpp.lept.pixRead;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.lept.PIX;
import org.bytedeco.javacpp.tesseract.TessBaseAPI;
import org.junit.Test;

public class BasicTesseractExampleTest {

	@Test
	public void givenTessBaseApi_whenImageOcrd_thenTextDisplayed_eng()
			throws Exception {

		// Open input image with leptonica library
		String imageFile = BasicTesseractExampleTest.class.getResource(
				"/com/swayam/ocr/porua/res/test-english.png").getPath();
		PIX image = pixRead(imageFile);
		BytePointer outText = null;

		TessBaseAPI api = new TessBaseAPI();
		if (api.Init("/usr/share/tesseract/", "eng") != 0) {
			fail("Could not initialize tesseract.");
		}

		api.SetImage(image);
		// Get OCR result
		outText = api.GetUTF8Text();
		String ocrText = outText.getString().trim();

		// Destroy used object and release memory
		api.End();
		api.close();
		outText.deallocate();
		pixDestroy(image);

		System.out.printf("OCR output:%s\n", ocrText);
		assertTrue(!ocrText.isEmpty());

		String expected = readFile(BasicTesseractExampleTest.class.getResource(
				"/com/swayam/ocr/porua/res/test-english.txt").getPath());

		assertEquals(expected, ocrText);

	}

	@Test
	public void givenTessBaseApi_whenImageOcrd_thenTextDisplayed_ben()
			throws Exception {

		// Open input image with leptonica library
		String imageFile = BasicTesseractExampleTest.class.getResource(
				"/com/swayam/ocr/porua/res/Bangla-300-short.png").getPath();
		PIX image = pixRead(imageFile);
		BytePointer outText = null;

		TessBaseAPI api = new TessBaseAPI();
		if (api.Init("/usr/share/tesseract/", "ben") != 0) {
			fail("Could not initialize tesseract.");
		}

		api.SetImage(image);
		// Get OCR result
		outText = api.GetUTF8Text();
		String ocrText = outText.getString().trim();

		// Destroy used object and release memory
		api.End();
		api.close();
		outText.deallocate();
		pixDestroy(image);

		System.out.printf("OCR output:%s\n", ocrText);
		assertTrue(!ocrText.isEmpty());

		String expected = readFile(BasicTesseractExampleTest.class.getResource(
				"/com/swayam/ocr/porua/res/Bangla-300-short.txt").getPath());

		assertEquals(expected, ocrText);

	}

	private String readFile(String path) {

		StringBuilder allLines = new StringBuilder(200);

		try (BufferedReader reader = new BufferedReader(new FileReader(path));) {

			reader.lines().forEach((String line) -> {
				allLines.append(line).append("\n");
			});

		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return allLines.toString().trim();

	}
}