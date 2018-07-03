FROM paawak/tesseract-ocr-barebones:alpine_jre-8_tess-3.05.02_v0.1
MAINTAINER Palash Ray <paawak@gmail.com>
RUN mkdir /tesseract-temp-images
ADD target/tesseract-ocr-rest.jar //
ENTRYPOINT ["java", "-jar", "/tesseract-ocr-rest.jar"]
