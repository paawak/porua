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
	public void givenTessBaseApi_whenImageOcrd_thenTextDisplayed()
			throws Exception {
		// System.setProperty("TESSDATA_PREFIX",
		// "/usr/share/tesseract/tessdata");

		BytePointer outText;

		TessBaseAPI api = new TessBaseAPI();
		// api.ReadConfigFile("/usr/share/tesseract/tessdata/configs/api_config");
		// api.SetVariable("TESSDATA_PREFIX", "/usr/share/tesseract/tessdata");
		// Initialize tesseract-ocr with English, without specifying tessdata
		// path
		if (api.Init("/usr/share/tesseract/", "ben+eng") != 0) {
			System.err.println("Could not initialize tesseract.");
			System.exit(1);
		}

		// Open input image with leptonica library
		String imageFile = BasicTesseractExampleTest.class.getResource(
				"/test.png").getPath();
		PIX image = pixRead(imageFile);
		api.SetImage(image);
		// Get OCR result
		outText = api.GetUTF8Text();
		String string = outText.getString();
		assertTrue(!string.isEmpty());
		System.out.println("OCR output:\n" + string);

		// Destroy used object and release memory
		api.End();
		api.close();
		outText.deallocate();
		pixDestroy(image);
	}
}