FROM alpine:3.7 as builder

RUN apk add --no-cache g++  \
    autoconf automake libtool   \   
    make    

RUN mkdir -p /libs/tar-ball  \    
    /libs/src  
    #/usr/local/man/man1/cjpeg.1 

ADD lib-sources /libs/tar-ball/
ADD install-libs.sh /scripts/
ADD tessdata.tar.gz /tesseract/


RUN chmod u+rx /scripts/install-libs.sh &&  \
    /scripts/install-libs.sh 

FROM openjdk:8u171-jre-alpine3.7
MAINTAINER Palash Ray <paawak@gmail.com>

RUN mkdir /input-output   


COPY --from=builder /tesseract/tessdata /tesseract/tessdata

COPY --from=builder /usr/local/lib /usr/local/lib
COPY --from=builder /usr/local/bin /usr/local/bin


CMD tesseract -l $LANG $IMAGE $OUTFILE


