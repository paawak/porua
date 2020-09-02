<?php

namespace com\swayam\ocr\porua\rest;

use Doctrine\ORM\EntityManager;
use Psr\Log\LoggerInterface;

require_once __DIR__ . '/../model/Book.php';

class IndexController {

    /**
     * @var EntityManager
     */
    private $entityManager;
    
     /**
     * @var LoggerInterface
     */
    private $logger;

    public function __construct(EntityManager $entityManager, LoggerInterface $logger) {
        $this->entityManager = $entityManager;
        $this->logger = $logger;
    }

    public function get($request, $response) {
        $this->logger->info("in index, get");
        $book = $this->entityManager->find('com\swayam\ocr\porua\model\Book', 1);
        $bookName = $book->getName();
        $response->getBody()->write("Book: $bookName");
        return $response;
    }

}
