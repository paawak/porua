###################         START BUILDER         ###################

FROM alpine:3.7 as tess_installer      

ADD lib-sources /libs/tar-ball/
ADD install-libs.sh /scripts/

RUN apk add --no-cache g++  \
    autoconf automake libtool   \   
    make    &&  \
    chmod u+rx /scripts/install-libs.sh &&  \
    /scripts/install-libs.sh 

###################         END BUILDER         ###################

FROM openjdk:8u171-jre-alpine3.7
MAINTAINER Palash Ray <paawak@gmail.com>

RUN mkdir /input-output   

ADD tessdata.tar.gz /tesseract/

COPY --from=tess_installer /usr/local/lib /usr/local/lib
COPY --from=tess_installer /usr/local/bin /usr/local/bin


CMD tesseract -l $LANG $IMAGE $OUTFILE


