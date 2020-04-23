package com.swayam.ocr.core.impl;

import static org.bytedeco.leptonica.global.lept.pixDestroy;
import static org.bytedeco.leptonica.global.lept.pixRead;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.leptonica.PIX;
import org.bytedeco.tesseract.ETEXT_DESC;
import org.bytedeco.tesseract.ResultIterator;
import org.bytedeco.tesseract.TessBaseAPI;
import org.bytedeco.tesseract.global.tesseract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.swayam.ocr.core.WordAnalyser;
import com.swayam.ocr.core.model.TextBox;
import com.swayam.ocr.core.util.BinaryImage;
import com.swayam.ocr.core.util.Rectangle;

public class TesseractOcrWordAnalyser implements WordAnalyser {

    private static final Logger LOGGER = LoggerFactory.getLogger(TesseractOcrWordAnalyser.class);

    private static final String TESSDATA_DIRECTORY = "/kaaj/installs/tesseract/tessdata_best-4.0.0";
    private static final String LANGUAGE_CODE = "ben";

    private final Path imagePath;

    public TesseractOcrWordAnalyser(Path imagePath) {
	this.imagePath = imagePath;
    }

    @Override
    public List<Rectangle> getWordBoundaries() {
	LOGGER.info("Image file to analyse with Tesseract OCR: {}", imagePath);

	List<Rectangle> rects = new ArrayList<>();

	try (TessBaseAPI api = new TessBaseAPI();) {
	    int returnCode = api.Init(TESSDATA_DIRECTORY, LANGUAGE_CODE);
	    if (returnCode != 0) {
		throw new RuntimeException("could not initialize tesseract, error code: " + returnCode);
	    }

	    PIX image = pixRead(imagePath.toFile().getAbsolutePath());

	    api.SetImage(image);
	    int code = api.Recognize(new ETEXT_DESC());

	    if (code != 0) {
		throw new IllegalArgumentException("could not recognize text");
	    }

	    ResultIterator ri = api.GetIterator();
	    int level = tesseract.RIL_WORD;

	    do {
		BytePointer ocrResult = ri.GetUTF8Text(level);
		String ocrText = ocrResult.getString().trim();

		float conf = ri.Confidence(level);
		IntPointer x1 = new IntPointer(new int[1]);
		IntPointer y1 = new IntPointer(new int[1]);
		IntPointer x2 = new IntPointer(new int[1]);
		IntPointer y2 = new IntPointer(new int[1]);
		boolean foundRectangle = ri.BoundingBox(level, x1, y1, x2, y2);

		if (!foundRectangle) {
		    throw new IllegalArgumentException("Could not find any rectangle here");
		}

		TextBox textBox = new TextBox(x1.get(), y1.get(), x2.get(), y2.get(), conf, ocrText);
		LOGGER.info("{}", textBox);

		rects.add(new Rectangle(textBox.x1, textBox.y1, textBox.x2 - textBox.x1, textBox.y2 - textBox.y1));

		x1.deallocate();
		y1.deallocate();
		x2.deallocate();
		y2.deallocate();
		ocrResult.deallocate();
	    } while (ri.Next(level));

	    api.End();
	    api.close();
	    pixDestroy(image);
	}

	return rects;
    }

    @Override
    public BinaryImage getWordMatrix(Rectangle wordBoundary) {
	throw new UnsupportedOperationException();
    }

}
