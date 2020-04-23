package com.swayam.ocr.core.impl;

import static org.bytedeco.leptonica.global.lept.L_CLONE;
import static org.bytedeco.leptonica.global.lept.boxaGetBox;
import static org.bytedeco.leptonica.global.lept.pixDestroy;
import static org.bytedeco.leptonica.global.lept.pixRead;

import java.nio.IntBuffer;
import java.nio.file.Path;
import java.util.List;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.leptonica.BOX;
import org.bytedeco.leptonica.BOXA;
import org.bytedeco.leptonica.PIX;
import org.bytedeco.leptonica.PIXA;
import org.bytedeco.tesseract.TessBaseAPI;
import org.bytedeco.tesseract.global.tesseract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.swayam.ocr.core.WordAnalyser;
import com.swayam.ocr.core.util.BinaryImage;
import com.swayam.ocr.core.util.Rectangle;

public class TesseractOcrWordAnalyser implements WordAnalyser {

    private static final Logger LOGGER = LoggerFactory.getLogger(TesseractOcrWordAnalyser.class);

    private static final String TESSDATA_DIRECTORY = "/kaaj/installs/tesseract/tessdata_best-4.0.0";
    private static final String LANGUAGE_CODE = "ben";

    private final Path imagePath;
    private final BinaryImage binaryImage;

    public TesseractOcrWordAnalyser(Path imagePath, BinaryImage binaryImage) {
	this.imagePath = imagePath;
	this.binaryImage = binaryImage;
    }

    @Override
    public List<Rectangle> getWordBoundaries() {
	submitToOCR();
	return null;
    }

    @Override
    public BinaryImage getWordMatrix(Rectangle wordBoundary) {
	// TODO Auto-generated method stub
	return null;
    }

    private void submitToOCR() {

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

}
