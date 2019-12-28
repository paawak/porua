FROM debian:bullseye-20191224-slim    

ADD lib-sources /libs/tar-ball/
ADD install-libs.sh /scripts/

RUN apt-get update  &&  \
    apt-get install -y g++  \
    pkg-config autoconf automake libtool \   
    make    &&  \
    chmod u+rx /scripts/install-libs.sh &&  \
    /scripts/install-libs.sh  && \
    ldconfig

RUN mkdir /input-output   

ADD tessdata.tar.gz /tesseract/

CMD tesseract -l $LANG $IMAGE $OUTFILE


