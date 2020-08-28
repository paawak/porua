--
-- PostgreSQL database dump
--

-- Dumped from database version 9.6.8
-- Dumped by pg_dump version 10.12 (Ubuntu 10.12-0ubuntu0.18.04.1)

-- Started on 2020-08-25 10:22:42 IST

--
-- TOC entry 188 (class 1259 OID 17400)
-- Name: book; Type: TABLE; Schema:  Owner: postgres
--

CREATE TABLE book (
    id integer NOT NULL,
    name character varying(200) NOT NULL,
    language character varying(25) NOT NULL
);


ALTER TABLE book OWNER TO postgres;

--
-- TOC entry 187 (class 1259 OID 17398)
-- Name: book_id_seq; Type: SEQUENCE; Schema:  Owner: postgres
--

CREATE SEQUENCE book_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE book_id_seq OWNER TO postgres;

--
-- TOC entry 2205 (class 0 OID 0)
-- Dependencies: 187
-- Name: book_id_seq; Type: SEQUENCE OWNED BY; Schema:  Owner: postgres
--

ALTER SEQUENCE book_id_seq OWNED BY book.id;


--
-- TOC entry 186 (class 1259 OID 17392)
-- Name: databasechangelog; Type: TABLE; Schema:  Owner: postgres
--

CREATE TABLE databasechangelog (
    id character varying(255) NOT NULL,
    author character varying(255) NOT NULL,
    filename character varying(255) NOT NULL,
    dateexecuted timestamp without time zone NOT NULL,
    orderexecuted integer NOT NULL,
    exectype character varying(10) NOT NULL,
    md5sum character varying(35),
    description character varying(255),
    comments character varying(255),
    tag character varying(255),
    liquibase character varying(20),
    contexts character varying(255),
    labels character varying(255),
    deployment_id character varying(10)
);


ALTER TABLE databasechangelog OWNER TO postgres;

--
-- TOC entry 185 (class 1259 OID 17387)
-- Name: databasechangeloglock; Type: TABLE; Schema:  Owner: postgres
--

CREATE TABLE databasechangeloglock (
    id integer NOT NULL,
    locked boolean NOT NULL,
    lockgranted timestamp without time zone,
    lockedby character varying(255)
);


ALTER TABLE databasechangeloglock OWNER TO postgres;

--
-- TOC entry 189 (class 1259 OID 17406)
-- Name: ocr_word; Type: TABLE; Schema:  Owner: postgres
--

CREATE TABLE ocr_word (
    book_id integer NOT NULL,
    page_image_id integer NOT NULL,
    word_sequence_id integer NOT NULL,
    raw_text text NOT NULL,
    corrected_text text,
    x1 integer NOT NULL,
    y1 integer NOT NULL,
    x2 integer NOT NULL,
    y2 integer NOT NULL,
    confidence double precision NOT NULL,
    line_number integer,
    ignored boolean DEFAULT false NOT NULL
);


ALTER TABLE ocr_word OWNER TO postgres;

--
-- TOC entry 191 (class 1259 OID 17415)
-- Name: page_image; Type: TABLE; Schema:  Owner: postgres
--

CREATE TABLE page_image (
    id integer NOT NULL,
    book_id integer NOT NULL,
    name character varying(200) NOT NULL,
    page_number integer NOT NULL,
    correction_completed boolean DEFAULT false NOT NULL,
    ignored boolean DEFAULT false NOT NULL
);


ALTER TABLE page_image OWNER TO postgres;

--
-- TOC entry 190 (class 1259 OID 17413)
-- Name: page_image_id_seq; Type: SEQUENCE; Schema:  Owner: postgres
--

CREATE SEQUENCE page_image_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE page_image_id_seq OWNER TO postgres;

--
-- TOC entry 2206 (class 0 OID 0)
-- Dependencies: 190
-- Name: page_image_id_seq; Type: SEQUENCE OWNED BY; Schema:  Owner: postgres
--

ALTER SEQUENCE page_image_id_seq OWNED BY page_image.id;


--
-- TOC entry 2059 (class 2604 OID 17403)
-- Name: book id; Type: DEFAULT; Schema:  Owner: postgres
--

ALTER TABLE ONLY book ALTER COLUMN id SET DEFAULT nextval('book_id_seq'::regclass);


--
-- TOC entry 2061 (class 2604 OID 17418)
-- Name: page_image id; Type: DEFAULT; Schema:  Owner: postgres
--

ALTER TABLE ONLY page_image ALTER COLUMN id SET DEFAULT nextval('page_image_id_seq'::regclass);


--
-- TOC entry 2065 (class 2606 OID 17391)
-- Name: databasechangeloglock databasechangeloglock_pkey; Type: CONSTRAINT; Schema:  Owner: postgres
--

ALTER TABLE ONLY databasechangeloglock
    ADD CONSTRAINT databasechangeloglock_pkey PRIMARY KEY (id);


--
-- TOC entry 2067 (class 2606 OID 17422)
-- Name: book sys_ct_10112; Type: CONSTRAINT; Schema:  Owner: postgres
--

ALTER TABLE ONLY book
    ADD CONSTRAINT sys_ct_10112 UNIQUE (name);


--
-- TOC entry 2073 (class 2606 OID 17424)
-- Name: page_image sys_ct_10124; Type: CONSTRAINT; Schema:  Owner: postgres
--

ALTER TABLE ONLY page_image
    ADD CONSTRAINT sys_ct_10124 UNIQUE (name);


--
-- TOC entry 2069 (class 2606 OID 17405)
-- Name: book sys_pk_10109; Type: CONSTRAINT; Schema:  Owner: postgres
--

ALTER TABLE ONLY book
    ADD CONSTRAINT sys_pk_10109 PRIMARY KEY (id);


--
-- TOC entry 2076 (class 2606 OID 17420)
-- Name: page_image sys_pk_10119; Type: CONSTRAINT; Schema:  Owner: postgres
--

ALTER TABLE ONLY page_image
    ADD CONSTRAINT sys_pk_10119 PRIMARY KEY (id);


--
-- TOC entry 2074 (class 1259 OID 17425)
-- Name: sys_idx_sys_fk_10123_10130; Type: INDEX; Schema:  Owner: postgres
--

CREATE INDEX sys_idx_sys_fk_10123_10130 ON page_image USING btree (book_id);


--
-- TOC entry 2070 (class 1259 OID 17426)
-- Name: sys_idx_sys_fk_10143_10156; Type: INDEX; Schema:  Owner: postgres
--

CREATE INDEX sys_idx_sys_fk_10143_10156 ON ocr_word USING btree (book_id);


--
-- TOC entry 2071 (class 1259 OID 17427)
-- Name: sys_idx_sys_fk_10144_10158; Type: INDEX; Schema:  Owner: postgres
--

CREATE INDEX sys_idx_sys_fk_10144_10158 ON ocr_word USING btree (page_image_id);


--
-- TOC entry 2079 (class 2606 OID 17428)
-- Name: page_image sys_fk_10123; Type: FK CONSTRAINT; Schema:  Owner: postgres
--

ALTER TABLE ONLY page_image
    ADD CONSTRAINT sys_fk_10123 FOREIGN KEY (book_id) REFERENCES book(id);


--
-- TOC entry 2077 (class 2606 OID 17433)
-- Name: ocr_word sys_fk_10143; Type: FK CONSTRAINT; Schema:  Owner: postgres
--

ALTER TABLE ONLY ocr_word
    ADD CONSTRAINT sys_fk_10143 FOREIGN KEY (book_id) REFERENCES book(id);


--
-- TOC entry 2078 (class 2606 OID 17438)
-- Name: ocr_word sys_fk_10144; Type: FK CONSTRAINT; Schema:  Owner: postgres
--

ALTER TABLE ONLY ocr_word
    ADD CONSTRAINT sys_fk_10144 FOREIGN KEY (page_image_id) REFERENCES page_image(id);


-- Completed on 2020-08-25 10:22:42 IST

--
-- PostgreSQL database dump complete
--

