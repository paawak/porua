package com.swayam.ocr.porua.tesseract.model;

import java.io.InputStream;

public class ImageData {

	private final String fileName;
	private final InputStream fileContents;
	private final String contentType;

	public ImageData(String fileName, InputStream fileContents, String contentType) {
		this.fileName = fileName;
		this.fileContents = fileContents;
		this.contentType = contentType;
	}

	public String getFileName() {
		return fileName;
	}

	public InputStream getFileContents() {
		return fileContents;
	}

	public String getContentType() {
		return contentType;
	}

}
