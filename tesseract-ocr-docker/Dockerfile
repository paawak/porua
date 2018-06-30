FROM alpine:3.7
MAINTAINER Palash Ray <paawak@gmail.com>


RUN apk add --no-cache g++  \
    autoconf automake libtool   \   
    make    


ENV tess_dir /tesseract-3.05.02 

RUN mkdir -p ${tess_dir}/src  \
    ${tess_dir}/install  \
    ${tess_dir}/tessdata  \
    /input-output   \
    /leptonica-1.76.0-src   \
    /libpng-1.6.34-src  \
    /zlib-1.2.11-src


ADD ${tess_dir} ${tess_dir}/src/
ADD tessdata ${tess_dir}/tessdata/
ADD leptonica-1.76.0    /leptonica-1.76.0-src/
ADD libpng-1.6.34   /libpng-1.6.34-src/
ADD zlib-1.2.11 /zlib-1.2.11-src/

RUN cd /zlib-1.2.11-src/   &&  \
    ./configure &&  \
    make &&  \
    make install 

RUN cd /libpng-1.6.34-src/   &&  \
    ./configure &&  \
    make &&  \
    make install 

RUN cd /leptonica-1.76.0-src/   &&  \
    ./configure &&  \
    make &&  \
    make install 

RUN cd ${tess_dir}/src/ &&  \
    ./autogen.sh &&  \
    ./configure --prefix=${tess_dir}/install &&  \
    make &&  \
    make install 

RUN export TESSDATA_PREFIX=${tess_dir}/tessdata/
CMD ${tess_dir}/install/bin/tesseract -l $LANG $IMAGE $OUTFILE


