package com.swayam.ocr.core.impl;

import static org.bytedeco.leptonica.global.lept.L_CLONE;
import static org.bytedeco.leptonica.global.lept.boxaGetBox;
import static org.bytedeco.leptonica.global.lept.pixDestroy;
import static org.bytedeco.leptonica.global.lept.pixRead;

import java.nio.IntBuffer;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.leptonica.BOX;
import org.bytedeco.leptonica.BOXA;
import org.bytedeco.leptonica.PIX;
import org.bytedeco.leptonica.PIXA;
import org.bytedeco.tesseract.ETEXT_DESC;
import org.bytedeco.tesseract.ResultIterator;
import org.bytedeco.tesseract.TessBaseAPI;
import org.bytedeco.tesseract.global.tesseract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.swayam.ocr.core.model.RawOcrWord;

public class TesseractOcrWordAnalyser {

    private static final Logger LOGGER = LoggerFactory.getLogger(TesseractOcrWordAnalyser.class);

    private static final String TESSDATA_DIRECTORY = "/kaaj/installs/tesseract/tessdata_best-4.0.0";
    private static final String LANGUAGE_CODE = "ben";

    private final Path imagePath;

    public TesseractOcrWordAnalyser(Path imagePath) {
	this.imagePath = imagePath;
    }

    public Collection<RawOcrWord> getDetectedWords() {
	LOGGER.info("Image file to analyse with Tesseract OCR: {}", imagePath);
	analyzeLinesInImage();
	return Collections.unmodifiableList(extractWordsFromImage());
    }

    private void analyzeLinesInImage() {

	LOGGER.info("Image file to analyse with Tesseract OCR: {}", imagePath);

	try (TessBaseAPI api = new TessBaseAPI();) {
	    int returnCode = api.Init(TESSDATA_DIRECTORY, LANGUAGE_CODE);
	    if (returnCode != 0) {
		throw new RuntimeException("could not initialize tesseract, error code: " + returnCode);
	    }

	    PIX image = pixRead(imagePath.toFile().getAbsolutePath());

	    api.SetImage(image);

	    BOXA boxes = api.GetComponentImages(tesseract.RIL_TEXTLINE, true, (PIXA) null, (IntBuffer) null);

	    LOGGER.info("Found {} textline image components.", boxes.n());

	    for (int i = 0; i < boxes.n(); i++) {
		BOX box = boxaGetBox(boxes, i, L_CLONE);
		api.SetRectangle(box.x(), box.y(), box.w(), box.h());
		BytePointer ocrResult = api.GetUTF8Text();
		String ocrText = ocrResult.getString().trim();
		ocrResult.deallocate();
		int conf = api.MeanTextConf();
		LOGGER.info("Box[{}]: x={}, y={}, w={}, h={}, confidence: {}, text: {}", i, box.x(), box.y(), box.w(), box.h(), conf, ocrText);
	    }

	    api.End();
	    api.close();
	    pixDestroy(image);
	}

    }

    private List<RawOcrWord> extractWordsFromImage() {
	Supplier<IntPointer> intPointerSupplier = () -> new IntPointer(new int[1]);

	List<RawOcrWord> words = new ArrayList<>();

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

	    try (ResultIterator ri = api.GetIterator();) {
		int level = tesseract.RIL_WORD;

		do {
		    BytePointer ocrResult = ri.GetUTF8Text(level);
		    String ocrText = ocrResult.getString().trim();

		    float conf = ri.Confidence(level);
		    IntPointer x1 = intPointerSupplier.get();
		    IntPointer y1 = intPointerSupplier.get();
		    IntPointer x2 = intPointerSupplier.get();
		    IntPointer y2 = intPointerSupplier.get();
		    boolean foundRectangle = ri.BoundingBox(level, x1, y1, x2, y2);

		    if (!foundRectangle) {
			throw new IllegalArgumentException("Could not find any rectangle here");
		    }

		    RawOcrWord textBox = new RawOcrWord(x1.get(), y1.get(), x2.get(), y2.get(), conf, ocrText);
		    LOGGER.info("{}", textBox);

		    words.add(textBox);

		    x1.deallocate();
		    y1.deallocate();
		    x2.deallocate();
		    y2.deallocate();
		    ocrResult.deallocate();
		} while (ri.Next(level));

		ri.deallocate();
	    }
	    api.End();
	    api.deallocate();
	    pixDestroy(image);
	}

	return words;
    }

}
