###################         START BUILDER         ###################

FROM alpine:3.11.2 as tess_installer      

ENV TESS_SRC_ZIP tesseract-4.1.1.tar.gz

ADD $TESS_SRC_ZIP .

RUN apk add --no-cache g++  \
    autoconf automake libtool \   
    leptonica-dev icu-dev pango-dev cairo-dev \
    make    &&  \
    cd $(echo $TESS_SRC_ZIP | sed -e 's,.tar.gz$,,g;s,.tar.xz$,,g') && \
    ./autogen.sh && \
    ./configure && \
    make && \
    make install

###################         END BUILDER         ###################

FROM alpine:3.11.2
MAINTAINER Palash Ray <paawak@gmail.com>

RUN mkdir /input-output   

ADD tessdata.tar.gz /tesseract/

COPY --from=tess_installer /usr/lib/libwebp.so* /usr/lib/
COPY --from=tess_installer /usr/lib/libgif.so* /usr/lib/
COPY --from=tess_installer /usr/lib/libpng.so* /usr/lib/
COPY --from=tess_installer /usr/lib/libjpeg.so* /usr/lib/
COPY --from=tess_installer /usr/lib/libtiff.so* /usr/lib/
COPY --from=tess_installer /usr/lib/libcairo.so* /usr/lib/
COPY --from=tess_installer /usr/lib/libgomp.so* /usr/lib/
COPY --from=tess_installer /usr/lib/liblept.so* /usr/lib/
COPY --from=tess_installer /usr/local/lib /usr/local/lib
COPY --from=tess_installer /usr/local/bin /usr/local/bin


CMD tesseract -l $LANG $IMAGE $OUTFILE


