package com.swayam.ocr.porua.tesseract.service;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;

@Service
public class FileSystemUtil {

    private final String imageWriteDirectory;

    public FileSystemUtil(@Value("${app.config.server.image-write-directory}") String imageWriteDirectory) {
	this.imageWriteDirectory = imageWriteDirectory;

    }

    public Path getImageSaveLocation(String imageFileName) {
	return Paths.get(imageWriteDirectory, imageFileName);
    }

    public Path saveMultipartFileAsImage(FilePart imageFile) {
	Path imageOutputFilePath = getImageSaveLocation(imageFile.filename());
	imageFile.transferTo(imageOutputFilePath).block();
	return imageOutputFilePath;
    }

}
