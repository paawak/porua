package com.swayam.ocr.porua;

import static org.bytedeco.javacpp.lept.L_CLONE;
import static org.bytedeco.javacpp.lept.boxaGetBox;
import static org.bytedeco.javacpp.lept.pixDestroy;
import static org.bytedeco.javacpp.lept.pixRead;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.IntBuffer;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.lept.BOX;
import org.bytedeco.javacpp.lept.BOXA;
import org.bytedeco.javacpp.lept.PIX;
import org.bytedeco.javacpp.lept.PIXA;
import org.bytedeco.javacpp.tesseract;
import org.bytedeco.javacpp.tesseract.ResultIterator;
import org.bytedeco.javacpp.tesseract.TessBaseAPI;
import org.junit.Ignore;
import org.junit.Test;

public class BasicTesseractExampleTest {

	private static final String TESSDATA = "/kaaj/BanglaOCR/tesseract/tessdata";
	private static final String BANGLA_IMAGE_FILE =  "/kaaj/github/porua/porua-frontend/image-store/training/bangla/rajshekhar-basu-mahabharat/Bangla-mahabharat-1-page_1/Bangla-mahabharat-1-page_1.png";

	@Test
	public void givenTessBaseApi_whenImageOcrd_thenTextDisplayed_eng() throws Exception {

		// Open input image with leptonica library
		String imageFile = BasicTesseractExampleTest.class.getResource("/com/swayam/ocr/porua/res/test-english.png").getPath();
		PIX image = pixRead(imageFile);
		BytePointer outText = null;

		TessBaseAPI api = new TessBaseAPI();
		if (api.Init(TESSDATA, "eng") != 0) {
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

		String expected = readFile(BasicTesseractExampleTest.class.getResource("/com/swayam/ocr/porua/res/test-english.txt").getPath());

		assertEquals(expected, ocrText);

	}

	@Test
	public void givenTessBaseApi_whenImageOcrd_thenTextDisplayed_ben() throws Exception {
		// Open input image with leptonica library
		String imageFile = BasicTesseractExampleTest.class.getResource("/com/swayam/ocr/porua/res/Bangla-300-short.png").getPath();
		PIX image = pixRead(imageFile);
		BytePointer outText = null;

		TessBaseAPI api = new TessBaseAPI();
		if (api.Init(TESSDATA, "ben") != 0) {
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

		String expected = readFile(BasicTesseractExampleTest.class.getResource("/com/swayam/ocr/porua/res/Bangla-300-short.txt").getPath());

		assertEquals(expected, ocrText);
	}

	@Ignore
	@Test
	public void givenTessBaseApi_GetComponentImages() throws Exception {
		String imageFile = "/kaaj/github/porua/porua-frontend/image-store/training/bangla/rajshekhar-basu-mahabharat/Bangla-mahabharat-1-page_1/Bangla-mahabharat-1-page_1.png";
		PIX image = pixRead(imageFile);

		TessBaseAPI api = new TessBaseAPI();
		if (api.Init(TESSDATA, "ben") != 0) {
			fail("Could not initialize tesseract.");
		}

		api.SetImage(image);
		BOXA boxes = api.GetComponentImages(tesseract.RIL_TEXTLINE, true, (PIXA) null, (IntBuffer) null);

		System.out.printf("Found %d textline image components.%n", boxes.n());

		for (int i = 0; i < boxes.n(); i++) {
			BOX box = boxaGetBox(boxes, i, L_CLONE);
			api.SetRectangle(box.x(), box.y(), box.w(), box.h());
			BytePointer ocrResult = api.GetUTF8Text();
			String ocrText = ocrResult.getString().trim();
			ocrResult.deallocate();
			int conf = api.MeanTextConf();
			System.out.printf("Box[%d]: x=%d, y=%d, w=%d, h=%d, confidence: %d, text: %s%n", i, box.x(), box.y(), box.w(), box.h(), conf, ocrText);
		}

		// Destroy used object and release memory
		api.End();
		api.close();
		pixDestroy(image);

	}

	@Test
	public void givenTessBaseApi_ResultIterator() throws Exception {
		PIX image = pixRead(BANGLA_IMAGE_FILE);

		TessBaseAPI api = new TessBaseAPI();
		if (api.Init(TESSDATA, "ben") != 0) {
			fail("Could not initialize tesseract.");
		}

		api.SetImage(image);
		api.Recognize(null);

		ResultIterator ri = api.GetIterator();
		int level = tesseract.RIL_WORD;

		while (ri.Next(level)) {
			BytePointer word = ri.GetUTF8Text(level);
			String ocrText = word.getString().trim();
			float conf = ri.Confidence(level);
			IntBuffer x1 = IntBuffer.allocate(50);
			IntBuffer y1 = IntBuffer.allocate(50);
			IntBuffer x2 = IntBuffer.allocate(50);
			IntBuffer y2 = IntBuffer.allocate(50);

			ri.BoundingBox(level, x1, y1, x2, y2);
			System.out.printf("word: '%s';  \tconf: %.2f; BoundingBox: %d,%d,%d,%d;%n", ocrText, conf, x1.get(), y1.get(), x2.get(), y2.get());
		}

		// Destroy used object and release memory
		api.End();
		api.close();
		pixDestroy(image);

	}

	@Ignore
	@Test
	public void givenTessBaseApi_rajshekhar_basu_mahabharat() throws Exception {

		// Open input image with leptonica library
		PIX image = pixRead(BANGLA_IMAGE_FILE);
		BytePointer outText = null;

		TessBaseAPI api = new TessBaseAPI();
		if (api.Init(TESSDATA, "ben") != 0) {
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

		assertTrue(!ocrText.isEmpty());

		System.out.println(ocrText.replaceAll("\\s", "\n"));

	}

	private String readFile(String path) throws IOException {

		StringBuilder allLines = new StringBuilder(200);

		try (BufferedReader reader = new BufferedReader(new FileReader(path));) {

			reader.lines().forEach((String line) -> {
				allLines.append(line).append("\n");
			});

		}

		return allLines.toString().trim();

	}
}