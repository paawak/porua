###################         START BUILDER         ###################

FROM alpine:3.11.2 as tess_installer      

ADD lib-sources /libs/tar-ball/
ADD install-libs.sh /scripts/

RUN apk add --no-cache g++  \
    autoconf automake libtool \   
    leptonica-dev icu-dev pango-dev cairo-dev \
    make    &&  \
    chmod u+rx /scripts/install-libs.sh &&  \
    /scripts/install-libs.sh 

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


