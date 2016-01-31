package com.swayam.ocr.porua;

import static org.bytedeco.javacpp.lept.pixDestroy;
import static org.bytedeco.javacpp.lept.pixRead;
import static org.junit.Assert.assertTrue;

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

		try (TessBaseAPI api = new TessBaseAPI();) {
			if (api.Init("/usr/share/tesseract/", "eng") != 0) {
				System.err.println("Could not initialize tesseract.");
				System.exit(1);
			}

			api.SetImage(image);
			// Get OCR result
			outText = api.GetUTF8Text();
			String string = outText.getString();
			assertTrue(!string.isEmpty());
			System.out.println("OCR output:\n" + string);

			// Destroy used object and release memory
			api.End();

		} finally {
			outText.deallocate();
			pixDestroy(image);
		}

	}

	@Test
	public void givenTessBaseApi_whenImageOcrd_thenTextDisplayed_ben()
			throws Exception {

		// Open input image with leptonica library
		String imageFile = BasicTesseractExampleTest.class.getResource(
				"/com/swayam/ocr/porua/res/Bangla-300-short.png").getPath();
		PIX image = pixRead(imageFile);
		BytePointer outText = null;

		try (TessBaseAPI api = new TessBaseAPI();) {
			if (api.Init("/usr/share/tesseract/", "ben") != 0) {
				System.err.println("Could not initialize tesseract.");
				System.exit(1);
			}

			api.SetImage(image);
			// Get OCR result
			outText = api.GetUTF8Text();
			String string = outText.getString();
			assertTrue(!string.isEmpty());
			System.out.println("OCR output:\n" + string);

			// Destroy used object and release memory
			api.End();

		} finally {
			outText.deallocate();
			pixDestroy(image);
		}

	}

}