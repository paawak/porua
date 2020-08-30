<?php

namespace com\swayam\ocr\porua\rest;

require_once '/kaaj/source/porua/ocr-correction-rest/com/swayam/ocr/porua/model/Book.php';

use Doctrine\ORM\EntityManager;

class IndexController {

    private $entityManager;

    public function __construct(EntityManager $entityManager) {
        $this->entityManager = $entityManager;
    }

    public function get($request, $response) {
        $book = $this->entityManager->find('com\swayam\ocr\porua\model\Book', 1);
        $response->getBody()->write("Book: " + $book->getName());
        return $response;
    }

}
