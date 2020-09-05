<?php

namespace com\swayam\ocr\porua\rest;

use Doctrine\ORM\EntityManager;
use Psr\Log\LoggerInterface;
use Psr\Http\Message\ResponseInterface as Response;
use Psr\Http\Message\ServerRequestInterface as Request;

use com\swayam\ocr\porua\model\Book;

require_once __DIR__ . '/../model/Book.php';

class BookController {

    private $entityManager;
    private $logger;

    public function __construct(EntityManager $entityManager, LoggerInterface $logger) {
        $this->entityManager = $entityManager;
        $this->logger = $logger;
    }

    public function get(Request $request, Response $response) {
        $query = $this->entityManager->createQuery('SELECT b FROM '. Book::class . ' b');
        $books = $query->getResult();        
        
        foreach ($books as $book) {
            echo $book->getId();
        }
        $payload = json_encode($books, JSON_PRETTY_PRINT);
        $response->getBody()->write($payload);
        return $response;//->withHeader('Content-Type', 'application/json');
    }

}
