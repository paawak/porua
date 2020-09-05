--liquibase formatted sql

--changeset paawak:1598596152519-1
CREATE TABLE book (id INT AUTO_INCREMENT NOT NULL, name VARCHAR(200) NOT NULL, language VARCHAR(25) NOT NULL, CONSTRAINT PK_BOOK PRIMARY KEY (id), UNIQUE (name));

--changeset paawak:1598596152519-2
CREATE TABLE ocr_word (book_id INT NOT NULL, page_image_id INT NOT NULL, word_sequence_id INT NOT NULL, raw_text LONGTEXT NOT NULL, corrected_text LONGTEXT NULL, x1 INT NOT NULL, y1 INT NOT NULL, x2 INT NOT NULL, y2 INT NOT NULL, confidence DOUBLE NOT NULL, line_number INT NULL, IGNORED BIT(1) DEFAULT 0 NOT NULL);

--changeset paawak:1598596152519-3
CREATE TABLE page_image (id INT AUTO_INCREMENT NOT NULL, book_id INT NOT NULL, name VARCHAR(200) NOT NULL, page_number INT NOT NULL, correction_completed BIT(1) DEFAULT 0 NOT NULL, ignored BIT(1) DEFAULT 0 NOT NULL, CONSTRAINT PK_PAGE_IMAGE PRIMARY KEY (id), UNIQUE (name));

--changeset paawak:1598596152519-4
CREATE INDEX SYS_IDX_SYS_FK_10123_10130 ON page_image(book_id);

--changeset paawak:1598596152519-5
CREATE INDEX SYS_IDX_SYS_FK_10143_10156 ON ocr_word(book_id);

--changeset paawak:1598596152519-6
CREATE INDEX SYS_IDX_SYS_FK_10144_10158 ON ocr_word(page_image_id);

--changeset paawak:1598596152519-7
ALTER TABLE page_image ADD CONSTRAINT SYS_FK_10123 FOREIGN KEY (book_id) REFERENCES book (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset paawak:1598596152519-8
ALTER TABLE ocr_word ADD CONSTRAINT SYS_FK_10143 FOREIGN KEY (book_id) REFERENCES book (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset paawak:1598596152519-9
ALTER TABLE ocr_word ADD CONSTRAINT SYS_FK_10144 FOREIGN KEY (page_image_id) REFERENCES page_image (id) ON UPDATE NO ACTION ON DELETE NO ACTION;
