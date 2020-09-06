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

}
