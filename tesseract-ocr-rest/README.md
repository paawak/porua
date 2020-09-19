# About

Exposes Tesseract OCR (https://github.com/tesseract-ocr/tesseract/) calls over http as REST, so that it can be used with any programming language.

This image is built on top of Alpine 3.7 and has the *paawak/tesseract-ocr-barebones*(https://hub.docker.com/r/paawak/tesseract-ocr-barebones/) as its base image. Every attempt has been made to keep its size to a minimum. 

This application is written in Java/Spring Boot. JavaCPP-Presets(https://github.com/bytedeco/javacpp-presets) is used to talk to the C++ API of Tesseract from Java. Then, Spring Boot uses REST semantices to further expose these calls over HTTP. 

# How build docker image

mvn clean package

# How upload docker image to docker hub

mvn clean install

# How to run

## Running in Local

    java -Dspring.profiles.active=local -jar target/tesseract-ocr-rest.jar

## Running in Docker

    docker run -it -p 8080:8080    \
    -v /kaaj/installs/tesseract/tessdata_best-4.0.0:/tesseract/tessdata    \
    -v /kaaj/source/porua/tesseract-ocr-rest/images:/tesseract-temp-images   \
    -e spring.profiles.active=container     \
    paawak/tesseract-ocr-rest:latest

# Accessing the API

The REST API is at /rest/ocr. It takes in the below form data:
1. image: multipart file
2. language: for now, supports only English (value *eng*) and Bengali (value *ben*) 

## CURL

	curl -v -X POST -H "content-type:multipart/form-data" -F image=@english.png -F language=eng http://localhost:8080/rest/ocr  

## UI

	There is also a simple HTML front-end at: http://localhost:8080/
	
# Training

## Uploading a PDF eBook
This functionality lets you upload a PDF. It extracts all images into pages, sends it to OCR.

The frontend is:

    http://localhost:8080/ 
    
The REST API is:
    
    http://localhost:8080/train/pdf 
    
## Getting a list of words and their bounds:

    curl -X GET "http://localhost:8080/train/word?imagePath=/kaaj/source/porua/training/box-making-tool/src/test/resources/images/bangla-mahabharat-1-page_2.jpg&language=ben"	

## Getting a word image

The below GET request does the trick:
<http://localhost:8080/train/word/image?wordId=3&imagePath=/kaaj/source/porua/training/box-making-tool/src/test/resources/images/bangla-mahabharat-1-page_2.jpg>	

## Retrieving corrected text from Hsql DB

Start the *sqltool.jar* bundled with the Hsql distribution
    
    java -jar sqltool.jar 
    
Then connect to the Hsql DB using
    
    \j SA jdbc:hsqldb:file:/kaaj/source/porua/training/box-making-tool/hsql-db/ocrdb;shutdown=true
    
Select the corrected words:    

    select id, corrected_text from ocr_word where corrected_text IS NOT NULL;
    
Exporting the corrected words to a CSV file:   
    
    * *DSV_TARGET_FILE=correctedtexts.csv
    \xq select id, corrected_text from ocr_word where corrected_text IS NOT NULL
    
# DB Migration Utils with Liquibase

Reference: <https://docs.liquibase.com/tools-integrations/maven/commands/home.html>

## Generate Mysql SQL Schema from existing DB

In the *pom.xml* change the *outputChangeLogFile* property in the format *schema.<target_db>.sql*. For mysql, it is *schema.mysql.sql*.

    mvn -P mysql liquibase:generateChangeLog   

## Applying liquibase changelog to MySQL DB

    mvn -P mysql liquibase:update    
				
# Sources
		
<https://github.com/paawak/porua/tree/master/tesseract-ocr-rest>
