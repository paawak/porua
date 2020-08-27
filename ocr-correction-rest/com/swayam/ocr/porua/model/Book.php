<?php

namespace com\swayam\ocr\porua\model;

/**
 * @Entity
 * @Table(name="book")
 */
class Book {

    /**
     * @Id
     * @Column(type="bigint")
     * @GeneratedValue(strategy="IDENTITY")
     */
    private $id;

    /** @Column(type="string") */
    private $name;

    /** @Column(type="string") */
    private $language;

}
