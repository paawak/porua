###################         START BUILDER         ###################

FROM debian:bullseye-20191224-slim as tess_installer          

ENV TESS_SRC_ZIP tesseract-4.1.1.tar.gz

ADD $TESS_SRC_ZIP .

RUN apt-get update  &&  \
    apt-get install -y g++  \
    pkg-config autoconf automake libtool \   
    libpng-dev libjpeg62-turbo-dev libtiff5-dev zlib1g-dev \
    libicu-dev libpango1.0-dev libcairo2-dev libleptonica-dev \
    make    &&  \
    cd $(echo $TESS_SRC_ZIP | sed -e 's,.tar.gz$,,g;s,.tar.xz$,,g') && \
    ./autogen.sh && \
    ./configure && \
    make && \
    make install && \
    ldconfig

###################         END BUILDER         ###################

FROM debian:bullseye-20191224-slim
MAINTAINER Palash Ray <paawak@gmail.com>

RUN mkdir /input-output   

ADD tessdata.tar.gz /tesseract/

COPY --from=tess_installer /usr/lib/x86_64-linux-gnu/libwebp* /usr/lib/x86_64-linux-gnu/
COPY --from=tess_installer /usr/lib/x86_64-linux-gnu/libopenjp2* /usr/lib/x86_64-linux-gnu/
COPY --from=tess_installer /usr/lib/x86_64-linux-gnu/libgif* /usr/lib/x86_64-linux-gnu/
COPY --from=tess_installer /usr/lib/x86_64-linux-gnu/libpng* /usr/lib/x86_64-linux-gnu/
COPY --from=tess_installer /usr/lib/x86_64-linux-gnu/libjpeg* /usr/lib/x86_64-linux-gnu/
COPY --from=tess_installer /usr/lib/x86_64-linux-gnu/libtiff* /usr/lib/x86_64-linux-gnu/
COPY --from=tess_installer /usr/lib/x86_64-linux-gnu/libcairo* /usr/lib/x86_64-linux-gnu/
COPY --from=tess_installer /usr/lib/x86_64-linux-gnu/libgomp* /usr/lib/x86_64-linux-gnu/
COPY --from=tess_installer /usr/lib/x86_64-linux-gnu/liblept* /usr/lib/x86_64-linux-gnu/
COPY --from=tess_installer /usr/lib/x86_64-linux-gnu/libjbig* /usr/lib/x86_64-linux-gnu/
COPY --from=tess_installer /usr/local/lib /usr/local/lib
COPY --from=tess_installer /usr/local/bin /usr/local/bin

RUN ldconfig


CMD tesseract -l $LANG $IMAGE $OUTFILE



