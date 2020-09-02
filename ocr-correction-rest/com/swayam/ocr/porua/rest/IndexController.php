<?php

namespace com\swayam\ocr\porua\rest;

use Doctrine\ORM\EntityManager;

require_once __DIR__ . '/../model/Book.php';

class IndexController {

    /**
     * @var EntityManager
     */
    private $entityManager;

    public function __construct(EntityManager $entityManager) {
        $this->entityManager = $entityManager;
    }

    public function get($request, $response) {
        $book = $this->entityManager->find('com\swayam\ocr\porua\model\Book', 1);
        $bookName = $book->getName();
        $response->getBody()->write("Book: $bookName");
        return $response;
    }

}
