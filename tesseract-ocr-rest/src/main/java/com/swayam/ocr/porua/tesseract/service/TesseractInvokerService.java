package com.swayam.ocr.porua.tesseract.service;

import java.io.IOException;
import java.nio.file.Path;

import com.swayam.ocr.porua.tesseract.model.Language;

public interface TesseractInvokerService {

    String submitToOCR(Language language, Path imagePath) throws IOException;

}
