package com.swayam.ocr.porua.tesseract.rest;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.swayam.ocr.porua.tesseract.OcrWordId;
import com.swayam.ocr.porua.tesseract.model.Book;
import com.swayam.ocr.porua.tesseract.model.OcrWord;
import com.swayam.ocr.porua.tesseract.model.PageImage;
import com.swayam.ocr.porua.tesseract.rest.dto.OcrCorrectionDto;
import com.swayam.ocr.porua.tesseract.service.FileSystemUtil;
import com.swayam.ocr.porua.tesseract.service.ImageProcessor;
import com.swayam.ocr.porua.tesseract.service.OcrDataStoreService;
import com.swayam.ocr.porua.tesseract.service.TesseractOcrWordAnalyser;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/train")
public class OCRTrainingController {

    private static final Logger LOG = LoggerFactory.getLogger(OCRTrainingController.class);

    private final OcrDataStoreService ocrDataStoreService;
    private final ImageProcessor imageProcessor;
    private final FileSystemUtil fileSystemUtil;

    public OCRTrainingController(OcrDataStoreService ocrDataStoreService, ImageProcessor imageProcessor, FileSystemUtil fileSystemUtil) {
	this.ocrDataStoreService = ocrDataStoreService;
	this.imageProcessor = imageProcessor;
	this.fileSystemUtil = fileSystemUtil;
    }

    @GetMapping(value = "/book", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<Book> getBooks() {
	return Flux.fromIterable(ocrDataStoreService.getBooks());
    }

    @GetMapping(value = "/book/{bookId}/page-count", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Integer> getPagesInBook(@PathVariable("bookId") final long bookId) {
	return Mono.just(ocrDataStoreService.getPageCount(bookId));
    }

    @GetMapping(value = "/page", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<PageImage> getPages(@RequestParam("bookId") final long bookId) {
	return Flux.fromIterable(ocrDataStoreService.getPages(bookId));
    }

    @PutMapping(value = "/page/ignore/{pageImageId}")
    public ResponseEntity<Integer> markPageAsIgnored(@PathVariable("pageImageId") final long pageImageId) {
	int rowsAffected = ocrDataStoreService.markPageAsIgnored(pageImageId);
	return ResponseEntity.ok(rowsAffected);
    }

    @PutMapping(value = "/page/complete/{pageImageId}")
    public ResponseEntity<Integer> markPageAsCorrectionCompleted(@PathVariable("pageImageId") final long pageImageId) {
	int rowsAffected = ocrDataStoreService.markPageAsCorrectionCompleted(pageImageId);
	return ResponseEntity.ok(rowsAffected);
    }

    @GetMapping(value = "/word", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<OcrWord> getOcrWords(@RequestParam("bookId") final long bookId, @RequestParam("pageImageId") final long pageImageId) {
	LOG.info("Retrieving OCR Words for Book Id {} and PageId {}", bookId, pageImageId);
	return Flux.fromIterable(ocrDataStoreService.getWords(bookId, pageImageId));
    }

    @PostMapping(value = "/word", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<OcrWord> submitPageAndAnalyzeWords(@RequestPart("bookId") final String bookIdString, @RequestPart("pageNumber") final String pageNumberString,
	    @RequestPart("image") final FilePart image) throws IOException, URISyntaxException {

	LOG.info("BookId: {}, PageNumber: {}, Uploaded fileName: {}", bookIdString, pageNumberString, image.filename());

	String imageFileName = image.filename();
	Path savedImagePath = fileSystemUtil.saveMultipartFileAsImage(image);

	return imageProcessor.submitPageForAnalysis(Long.valueOf(bookIdString), Integer.parseInt(pageNumberString), imageFileName, savedImagePath);

    }

    @PostMapping(value = "/pdf", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> uploadEBookInPdfFormat(@RequestPart("bookId") final String bookIdAsString, @RequestPart("image") FilePart eBookAsPdf) throws IOException {

	String eBookName = eBookAsPdf.filename();
	MediaType mediaType = eBookAsPdf.headers().getContentType();

	LOG.info("bookId: {}", bookIdAsString);
	LOG.info("FileName: {}, ContentType: {}, Size: {}", eBookName, mediaType, eBookAsPdf.headers().getContentLength());

	if (!MediaType.APPLICATION_PDF.equals(mediaType)) {
	    return ResponseEntity.badRequest().body("Only PDF docs supported. Unsupported content-type: " + mediaType);
	}

	Path savedEBookPath = fileSystemUtil.saveMultipartFileAsImage(eBookAsPdf);

	int extractedPageCount = imageProcessor.processEBookInPdf(Long.valueOf(bookIdAsString), eBookName, savedEBookPath);

	return ResponseEntity.ok(Integer.toString(extractedPageCount));
    }

    @PutMapping(value = "/word", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<Integer> applyCorrectionToOcrWords(@RequestBody final List<OcrCorrectionDto> ocrWordsForCorrection) {
	return Flux.fromIterable(ocrWordsForCorrection)
		.map(ocrWordForCorrection -> ocrDataStoreService.updateCorrectTextInOcrWord(ocrWordForCorrection.getOcrWordId(), ocrWordForCorrection.getCorrectedText()));
    }

    @PutMapping(value = "/word/ignore", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<Integer> markOcrWordAsIgnored(@RequestBody final List<OcrWordId> wordsToIgnore) {
	return Flux.fromIterable(wordsToIgnore).map(ocrDataStoreService::markWordAsIgnored);
    }

    @GetMapping(value = "/word/image")
    public Mono<ResponseEntity<byte[]>> getOcrWordImage(@RequestParam("bookId") final long bookId, @RequestParam("pageImageId") final long pageImageId,
	    @RequestParam("wordSequenceId") int wordSequenceId) throws IOException {
	String pageImageName = ocrDataStoreService.getPageImage(pageImageId).getName();
	Path imagePath = fileSystemUtil.getImageSaveLocation(pageImageName);

	OcrWord ocrText = ocrDataStoreService.getWord(new OcrWordId(bookId, pageImageId, wordSequenceId));
	BufferedImage fullImage = ImageIO.read(Files.newInputStream(imagePath));
	Rectangle wordArea = TesseractOcrWordAnalyser.getWordArea(ocrText);
	BufferedImage wordImage = fullImage.getSubimage(wordArea.x, wordArea.y, wordArea.width, wordArea.height);
	String[] tokens = imagePath.toFile().getName().split("\\.");
	String extension = tokens[tokens.length - 1];

	LOG.trace("serving ocr word image for id: {} and page image name: {}, location: {}, extension: {}", wordSequenceId, pageImageName, imagePath, extension);

	ByteArrayOutputStream bos = new ByteArrayOutputStream();
	ImageIO.write(wordImage, extension, bos);
	bos.flush();
	HttpHeaders responseHeaders = new HttpHeaders();
	responseHeaders.set("content-type", "image/" + extension);
	return Mono.just(new ResponseEntity<byte[]>(bos.toByteArray(), responseHeaders, HttpStatus.OK));
    }

}
