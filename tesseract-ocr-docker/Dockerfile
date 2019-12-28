FROM debian:bullseye-20191224-slim    

ADD lib-sources /libs/tar-ball/
ADD install-libs.sh /scripts/

RUN apt-get update  &&  \
    apt-get install -y g++  \
    pkg-config autoconf automake libtool \   
    libpng-dev libjpeg62-turbo-dev libtiff5-dev zlib1g-dev \
    libicu-dev libpango1.0-dev libcairo2-dev libleptonica-dev \
    make    &&  \
    chmod u+rx /scripts/install-libs.sh &&  \
    /scripts/install-libs.sh  && \
    ldconfig

RUN mkdir /input-output   

ADD tessdata.tar.gz /tesseract/

CMD tesseract -l $LANG $IMAGE $OUTFILE


