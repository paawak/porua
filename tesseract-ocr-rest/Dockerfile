FROM paawak/tesseract-ocr-jdk-11:debian_slim_tess-4.1.1_v1.0
MAINTAINER Palash Ray <paawak@gmail.com>
RUN mkdir /tesseract-temp-images
ADD target/tesseract-ocr-rest.jar //
ENTRYPOINT ["java", "-jar", "/tesseract-ocr-rest.jar"]
