package com.swayam.ocr.porua.tesseract.service;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;

public class QuickPdfTest {

    public static void main(String[] args) throws IOException {
	PDDocument document = PDDocument.load(new File("/home/paawak/Downloads/rajshekhar.mahabharat.bangla.pdf"));
	int pageCount = 1;
	PDPageTree list = document.getPages();
	for (PDPage page : list) {
	    PDResources pdResources = page.getResources();
	    for (COSName c : pdResources.getXObjectNames()) {
		PDXObject o = pdResources.getXObject(c);
		if (o instanceof org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject) {
		    File file = new File("/kaaj/source/porua/tesseract-ocr-rest/target/img/" + pageCount++ + ".png");
		    ImageIO.write(((org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject) o).getImage(), "png", file);
		}
	    }
	}
    }

}
