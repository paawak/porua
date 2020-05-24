package com.swayam.ocr.porua.tesseract;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OcrWordId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "book_id")
    private long bookId;

    @Column(name = "raw_image_id")
    private long rawImageId;

    @Column(name = "word_sequence_id")
    private int wordSequenceId;

}
