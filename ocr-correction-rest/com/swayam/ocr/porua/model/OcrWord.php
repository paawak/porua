<?php

namespace com\swayam\ocr\porua\model;

/**
 * @Entity
 * @Table(name="ocr_word")
 */
class OcrWord {

    /** @Column(name = "book_id", type="bigint") */
    private $bookId;

    /** @Column(name = "page_image_id", type="bigint") */
    private $pageImageId;

    /** @Column(name = "word_sequence_id", type="integer") */
    private $wordSequenceId;

    /** @Column(name = "raw_text") */
    private $rawText;

    /** @Column(name = "corrected_text") */
    private $correctedText;

    /** @Column(type="integer") */
    private $x1;

    /** @Column(type="integer") */
    private $y1;

    /** @Column(type="integer") */
    private $x2;

    /** @Column(type="integer") */
    private $y2;

    /** @Column(type="float") */
    private $confidence;

    /** @Column(name = "line_number", type="integer") */
    private $lineNumber;

    /** @Column(type="boolean") */
    private $ignored;

}
