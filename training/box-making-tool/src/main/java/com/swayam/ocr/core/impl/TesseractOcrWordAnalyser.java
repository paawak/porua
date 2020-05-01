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
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

import com.swayam.ocr.core.model.CachedOcrText;
import com.swayam.ocr.core.model.Language;
import com.swayam.ocr.core.model.RawOcrLine;
import com.swayam.ocr.core.model.RawOcrWord;

public class TesseractOcrWordAnalyser {

    private static final Logger LOGGER = LoggerFactory.getLogger(TesseractOcrWordAnalyser.class);

    private static final String TESSDATA_DIRECTORY = "/kaaj/installs/tesseract/tessdata_best-4.0.0";

    private final Path imagePath;
    private final Language language;

    public TesseractOcrWordAnalyser(Path imagePath, Language language) {
	this.imagePath = imagePath;
	this.language = language;
    }

    public List<RawOcrWord> getDetectedText() {
	LOGGER.info("Image file to analyse with Tesseract OCR: {}", imagePath);
	return extractWordsFromImage();
    }

    public List<String> getBoxStrings(Collection<CachedOcrText> words) {
	ExtractedLineDetails extractedLineDetails = extractLinesFromImage();
	List<RawOcrLine> lines = extractedLineDetails.lines;

	Map<Integer, RawOcrLine> linesAsMap = lines.parallelStream().collect(Collectors.toMap(line -> line.lineNumber, Function.identity()));

	Map<Integer, List<CachedOcrText>> wordsGroupedByLineNumber = lines.parallelStream().collect(Collectors.toMap(line -> line.lineNumber, line -> {
	    return words.stream()
		    .filter(cachedOcrText -> line.rawOcrText.getRectangle().contains(cachedOcrText.rawOcrText.getRectangle()) && line.rawOcrText.text.contains(cachedOcrText.rawOcrText.text))
		    .map(cachedOcrText -> new CachedOcrText(cachedOcrText.id, cachedOcrText.rawOcrText, cachedOcrText.correctText, line.lineNumber)).collect(Collectors.toList());
	}));

	Set<Integer> orderedLineNumbers = new TreeSet<>(linesAsMap.keySet());

	LOGGER.info("orderedLineNumbers: {}", orderedLineNumbers);

	return orderedLineNumbers.stream().filter(lineNumber -> !wordsGroupedByLineNumber.get(lineNumber).isEmpty()).flatMap(lineNumber -> {
	    int leftTopX = linesAsMap.get(lineNumber).rawOcrText.x1;
	    int leftTopY = linesAsMap.get(lineNumber).rawOcrText.y1;
	    int rightBottomX = linesAsMap.get(lineNumber).rawOcrText.x2;
	    int rightBottomY = linesAsMap.get(lineNumber).rawOcrText.y2;

	    int leftBottomX = leftTopX;
	    int leftBottomY = extractedLineDetails.imageHeight - rightBottomY;
	    // +5 pixels to be on the safer side
	    int rightTopX = rightBottomX + 5;
	    int rightTopY = extractedLineDetails.imageHeight - leftTopY;

	    String positionData = String.format(" %d %d %d %d 0", leftBottomX, leftBottomY, rightTopX, rightTopY);

	    int lastWordSequence = wordsGroupedByLineNumber.get(lineNumber).parallelStream().mapToInt(cachedOcrText -> cachedOcrText.rawOcrText.wordSequenceNumber).max().getAsInt();

	    List<String> boxes = wordsGroupedByLineNumber.get(lineNumber).stream().map(cachedOcrText -> {
		if (lastWordSequence == cachedOcrText.rawOcrText.wordSequenceNumber) {
		    return cachedOcrText.rawOcrText.text;
		}
		return cachedOcrText.rawOcrText.text + " ";
	    }).flatMap(text -> text.chars().mapToObj(c -> Character.toString((char) c))).map(text -> text + positionData).collect(Collectors.toList());
	    boxes.add("\t" + positionData);
	    return boxes.stream();
	}).collect(Collectors.toList());
    }

    private ExtractedLineDetails extractLinesFromImage() {

	LOGGER.info("Analyzing image file for lines...");
	List<RawOcrLine> lines;
	int imageHeight;

	try (TessBaseAPI api = new TessBaseAPI();) {
	    int returnCode = api.Init(TESSDATA_DIRECTORY, language.code);
	    if (returnCode != 0) {
		throw new RuntimeException("could not initialize tesseract, error code: " + returnCode);
	    }

	    PIX image = pixRead(imagePath.toFile().getAbsolutePath());

	    imageHeight = image.h();

	    api.SetImage(image);

	    BOXA boxes = api.GetComponentImages(tesseract.RIL_TEXTLINE, true, (PIXA) null, (IntBuffer) null);

	    LOGGER.info("Found {} textline image components.", boxes.n());

	    lines = IntStream.range(0, boxes.n()).mapToObj((int lineNumber) -> {
		BOX box = boxaGetBox(boxes, lineNumber, L_CLONE);
		api.SetRectangle(box.x(), box.y(), box.w(), box.h());
		BytePointer ocrResult = api.GetUTF8Text();
		String ocrLineText = ocrResult.getString().trim();
		ocrResult.deallocate();
		int confidence = api.MeanTextConf();
		int x1 = box.x();
		int y1 = box.y();
		int width = box.w();
		int height = box.h();

		// line number starts from 1
		RawOcrLine lineTextBox = new RawOcrLine(lineNumber + 1, new RawOcrWord(x1, y1, x1 + width, y1 + height, confidence, ocrLineText, -1));
		LOGGER.info("lineTextBox: {}", lineTextBox);
		return lineTextBox;
	    }).collect(Collectors.toList());

	    api.End();
	    api.close();
	    pixDestroy(image);
	}

	return new ExtractedLineDetails(imageHeight, Collections.unmodifiableList(lines));

    }

    private List<RawOcrWord> extractWordsFromImage() {
	LOGGER.info("Analyzing image file for words...");

	List<RawOcrWord> words = new ArrayList<>();

	try (TessBaseAPI api = new TessBaseAPI();) {
	    int returnCode = api.Init(TESSDATA_DIRECTORY, language.code);
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
		int wordNumber = 0;
		Supplier<IntPointer> intPointerSupplier = () -> new IntPointer(new int[1]);
		do {
		    BytePointer ocrResult = ri.GetUTF8Text(level);
		    String ocrText = ocrResult.getString().trim();

		    float confidence = ri.Confidence(level);
		    IntPointer x1 = intPointerSupplier.get();
		    IntPointer y1 = intPointerSupplier.get();
		    IntPointer x2 = intPointerSupplier.get();
		    IntPointer y2 = intPointerSupplier.get();
		    boolean foundRectangle = ri.BoundingBox(level, x1, y1, x2, y2);

		    if (!foundRectangle) {
			throw new IllegalArgumentException("Could not find any rectangle here");
		    }

		    RawOcrWord wordTextBox = new RawOcrWord(x1.get(), y1.get(), x2.get(), y2.get(), confidence, ocrText, wordNumber++);
		    LOGGER.info("wordTextBox: {}", wordTextBox);

		    words.add(wordTextBox);

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

	return Collections.unmodifiableList(words);
    }

    private static class ExtractedLineDetails {
	private final int imageHeight;
	private final List<RawOcrLine> lines;

	ExtractedLineDetails(int imageHeight, List<RawOcrLine> lines) {
	    this.imageHeight = imageHeight;
	    this.lines = lines;
	}
    }

}
