FROM openjdk:11.0.8-jre
MAINTAINER Palash Ray <paawak@gmail.com>
RUN mkdir /tesseract-temp-images
ADD target/tesseract-ocr-rest.jar //
ENTRYPOINT ["java", "-jar", "/tesseract-ocr-rest.jar"]
