CREATE MEMORY TABLE IF NOT EXISTS ocr_word ( 
	id INTEGER GENERATED ALWAYS AS IDENTITY(START WITH 1) PRIMARY KEY, 
	raw_ocr_word OTHER NOT NULL,
	corrected_text CLOB
);
