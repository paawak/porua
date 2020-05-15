# About

Exposes Tesseract OCR (https://github.com/tesseract-ocr/tesseract/) calls over http as REST, so that it can be used with any programming language.

This image is built on top of Alpine 3.7 and has the *paawak/tesseract-ocr-barebones*(https://hub.docker.com/r/paawak/tesseract-ocr-barebones/) as its base image. Every attempt has been made to keep its size to a minimum. 

This application is written in Java/Spring Boot. JavaCPP-Presets(https://github.com/bytedeco/javacpp-presets) is used to talk to the C++ API of Tesseract from Java. Then, Spring Boot uses REST semantices to further expose these calls over HTTP. 

# How build docker image

mvn clean package

# How upload docker image to docker hub

mvn clean install

# How to run

docker run -it -p 8080:8080 -e spring.profiles.active=default,container paawak/tesseract-ocr-rest:latest

# Accessing the API

The REST API is at /rest/ocr. It takes in the below form data:
1. image: multipart file
2. language: for now, supports only English (value *eng*) and Bengali (value *ben*) 

## CURL

	curl -v -X POST -H "content-type:multipart/form-data" -F image=@english.png -F language=eng http://localhost:8080/rest/ocr  

## UI

	There is also a simple HTML front-end at: http://localhost:8080/
	
# Training

Getting a list of words and their bounds:

    curl -X GET "http://localhost:8080/train/word?imagePath=/kaaj/source/porua/training/box-making-tool/src/test/resources/images/bangla-mahabharat-1-page_2.jpg&language=ben"	
	
# Sources
		
		https://github.com/paawak/porua/tree/master/tesseract-ocr-rest
	
	
