package com.swayam.ocr.porua.tesseract.service;

import java.io.IOException;

import com.swayam.ocr.porua.tesseract.model.ImageData;
import com.swayam.ocr.porua.tesseract.model.Language;

public interface TesseractInvokerService {

	String submitToOCR(Language language, ImageData imageData) throws IOException;

}
