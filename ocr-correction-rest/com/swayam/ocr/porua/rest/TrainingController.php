<?php

namespace com\swayam\ocr\porua\rest;

use Doctrine\ORM\EntityManager;
use Psr\Log\LoggerInterface;
use Psr\Http\Message\ResponseInterface as Response;
use Psr\Http\Message\ServerRequestInterface as Request;
use com\swayam\ocr\porua\model\Book;
use com\swayam\ocr\porua\model\PageImage;
use com\swayam\ocr\porua\model\OcrWord;

require_once __DIR__ . '/../model/Book.php';
require_once __DIR__ . '/../model/PageImage.php';
require_once __DIR__ . '/../model/OcrWord.php';

class TrainingController {

    const IMAGE_STORE = '/kaaj/source/porua/tesseract-ocr-rest/images/';

    private $entityManager;
    private $logger;

    public function __construct(EntityManager $entityManager, LoggerInterface $logger) {
        $this->entityManager = $entityManager;
        $this->logger = $logger;
    }

    public function getAllBooks(Request $request, Response $response) {
        $books = $this->entityManager->getRepository(Book::class)->findAll();
        $payload = json_encode($books, JSON_PRETTY_PRINT);
        $response->getBody()->write($payload);
        return $response->withHeader('Content-Type', 'application/json');
    }

    public function getPageCountInBook(Request $request, Response $response, $bookId) {
        $pageCount = $this->entityManager->getRepository(PageImage::class)->count(array(
            'book' => $bookId
        ));
        $response->getBody()->write("$pageCount");
        return $response->withHeader('Content-Type', 'application/json');
    }

    public function getPagesInBook(Request $request, Response $response) {
        $bookId = $request->getQueryParams()["bookId"];
        $pages = $this->entityManager->getRepository(PageImage::class)->findBy(array(
            'book' => $bookId
        ));
        $payload = json_encode($pages, JSON_PRETTY_PRINT);
        $response->getBody()->write($payload);
        return $response->withHeader('Content-Type', 'application/json');
    }

    public function getWordsInPage(Request $request, Response $response) {
        $queryParams = $request->getQueryParams();
        $bookId = $queryParams["bookId"];
        $pageImageId = $queryParams["pageImageId"];
        $words = $this->entityManager->getRepository(OcrWord::class)->findBy(array(
            'ocrWordId.bookId' => $bookId,
            'ocrWordId.pageImageId' => $pageImageId
        ));
        $payload = json_encode($words, JSON_PRETTY_PRINT);
        $response->getBody()->write($payload);
        return $response->withHeader('Content-Type', 'application/json');
    }

    public function getWordImage(Request $request, Response $response) {
        $queryParams = $request->getQueryParams();
        $bookId = $queryParams["bookId"];
        $pageImageId = $queryParams["pageImageId"];
        $wordSequenceId = $queryParams["wordSequenceId"];
        $pageName = $this->entityManager->getRepository(PageImage::class)->findOneBy(array(
                    'id' => $pageImageId
                ))->getName();

        $pageNameTokens = explode('.', $pageName);
        $imageExtension = strtolower($pageNameTokens[count($pageNameTokens) - 1]);

        $ocrWord = $this->entityManager->getRepository(OcrWord::class)->findOneBy(array(
            'ocrWordId.bookId' => $bookId,
            'ocrWordId.pageImageId' => $pageImageId,
            'ocrWordId.wordSequenceId' => $wordSequenceId
        ));
        
        $wordImage = $this->getWordImageToWrite(self::IMAGE_STORE . $pageName, $ocrWord, $imageExtension);

        $imageWriteFunctionName = "image" . $imageExtension;

        $imageWriteFunctionName($wordImage);

        imagedestroy($wordImage);

        return $response->withHeader('Content-Type', "image/" . $imageExtension);
    }

    private function getWordImageToWrite(string $imageFullPath, OcrWord $ocrWord, string $imageExtension) {
        $sourceX = $ocrWord->getX1();
        $sourceY = $ocrWord->getY1();
        $width = $ocrWord->getX2() - $sourceX;
        $height = $ocrWord->getY2() - $sourceY;

        $imageReadFunctionName = "imagecreatefrom" . $imageExtension;

        $this->logger->info("Reading image: {img}, with imageFunction: {imgReadFunc}",
                array(
                    'img' => $imageFullPath,
                    'imgReadFunc' => $imageReadFunctionName,
                    $sourceX, $sourceY, $width, $height
        ));

        $imageSource = $imageReadFunctionName($imageFullPath);
        $imageDest = imagecreate($width, $height);
        imagecopyresampled($imageDest, $imageSource, 0, 0, $sourceX, $sourceY, $width, $height, $width, $height);
        imagedestroy($imageSource);
        return $imageDest;
    }

}
