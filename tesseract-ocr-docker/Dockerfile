FROM alpine:3.7 as builder
MAINTAINER Palash Ray <paawak@gmail.com>


RUN apk add --no-cache g++  \
    autoconf automake libtool   \   
    make    


ENV tess_dir /tesseract-3.05.02 

RUN mkdir -p ${tess_dir}/src  \
    ${tess_dir}/install  \
    /leptonica-1.76.0-src   \
    /libpng-1.6.34-src  \
    /zlib-1.2.11-src    \
    /jpeg-6b-src    \
    /usr/local/man/man1/cjpeg.1 \
    /tiff-4.0.9-src


ADD ${tess_dir} ${tess_dir}/src/
ADD leptonica-1.76.0    /leptonica-1.76.0-src/
ADD libpng-1.6.34   /libpng-1.6.34-src/
ADD zlib-1.2.11 /zlib-1.2.11-src/
ADD jpeg-6b /jpeg-6b-src/
ADD tiff-4.0.9 /tiff-4.0.9-src/

RUN cd /zlib-1.2.11-src/   &&  \
    ./configure &&  \
    make &&  \
    make install 

RUN cd /jpeg-6b-src/   &&  \
    ./configure &&  \
    make &&  \
    make install 

RUN cd /tiff-4.0.9-src/   &&  \
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


FROM alpine:3.7
MAINTAINER Palash Ray <paawak@gmail.com>

ENV tess_dir /tesseract-3.05.02 

RUN mkdir -p ${tess_dir}/src  \
    ${tess_dir}/install  \
    ${tess_dir}/tessdata  \
    /input-output   

ADD tessdata ${tess_dir}/tessdata/

COPY --from=builder ${tess_dir}/install ${tess_dir}/install

COPY --from=builder /usr /usr


CMD ${tess_dir}/install/bin/tesseract -l $LANG $IMAGE $OUTFILE


