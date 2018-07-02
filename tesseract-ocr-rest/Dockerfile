FROM paawak/tesseract-ocr-3.05.02:0.6
MAINTAINER Palash Ray <paawak@gmail.com>
RUN mkdir /tesseract-temp-images
ADD target/tesseract-ocr-rest.jar //
ENTRYPOINT ["java", "-jar", "/tesseract-ocr-rest.jar"]
