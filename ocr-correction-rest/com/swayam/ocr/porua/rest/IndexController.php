<?php

namespace com\swayam\ocr\porua\rest;

use Doctrine\ORM\EntityManager;
use Psr\Log\LoggerInterface;
use Psr\Http\Message\ResponseInterface as Response;
use Psr\Http\Message\ServerRequestInterface as Request;

require_once __DIR__ . '/../model/Book.php';

class IndexController {

    private $entityManager;
    private $logger;

    public function __construct(EntityManager $entityManager, LoggerInterface $logger) {
        $this->entityManager = $entityManager;
        $this->logger = $logger;
    }

    public function get(Request $request, Response $response) {
        $this->logger->info("in index, get");
        $book = $this->entityManager->find('com\swayam\ocr\porua\model\Book', 1);
        $bookName = $book->getName();
        $payload = json_encode(['book' => $bookName], JSON_PRETTY_PRINT);
        $response->getBody()->write($payload);
        return $response->withHeader('Content-Type', 'application/json');
    }

}
