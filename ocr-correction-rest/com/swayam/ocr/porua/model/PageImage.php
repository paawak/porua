<?php

namespace com\swayam\ocr\porua\model;

/**
 * @Entity
 * @Table(name="page_image")
 */
class PageImage {

    /**
     * @Id
     * @Column(type="bigint")
     * @GeneratedValue(strategy="IDENTITY")
     */
    private $id;

    /**
     * @OneToOne(targetEntity="Book")
     * @JoinColumn(name="book_id", referencedColumnName="id")
     */
    private $book;

    /** @Column */
    private $name;

    /** @Column(name = "page_number", type="integer") */
    private $pageNumber;

    /** @Column(type="boolean") */
    private $ignored;

    /** @Column(name = "correction_completed", type="boolean") */
    private $correctionCompleted;

}
