<?php

namespace com\swayam\ocr\porua\model;

use Doctrine\ORM\Mapping\Entity;
use Doctrine\ORM\Mapping\Table;
use Doctrine\ORM\Mapping\Id;
use Doctrine\ORM\Mapping\GeneratedValue;
use Doctrine\ORM\Mapping\Column;

/**
 * @Entity
 * @Table(name="book")
 */
class Book implements \JsonSerializable {

    /**
     * @Id
     * @Column(type="integer")
     * @GeneratedValue(strategy="IDENTITY")
     */
    private $id;

    /** @Column(type="string") */
    private $name;

    /** @Column(type="string") */
    private $language;

    public function getId() {
        return $this->id;
    }

    public function getName() {
        return $this->name;
    }

    public function getLanguage() {
        return $this->language;
    }

    public function setId($id): void {
        $this->id = $id;
    }

    public function setName($name): void {
        $this->name = $name;
    }

    public function setLanguage($language): void {
        $this->language = $language;
    }

    public function jsonSerialize() {
        return get_object_vars($this);
    }

}
