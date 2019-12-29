###################         START BUILDER         ###################

FROM debian:buster-20191224-slim as tess_installer          

ENV TESS_SRC_ZIP tesseract-4.1.1.tar.gz

ADD $TESS_SRC_ZIP .

# Needed for installing jre
RUN mkdir -p /usr/share/man/man1

RUN apt-get update  &&  \
    apt-get install -y \
    openjdk-11-jre-headless \
    g++  \
    pkg-config autoconf automake libtool \   
    libpng-dev libjpeg62-turbo-dev libtiff5-dev zlib1g-dev \
    libicu-dev libpango1.0-dev libcairo2-dev libleptonica-dev \
    make    &&  \
    cd $(echo $TESS_SRC_ZIP | sed -e 's,.tar.gz$,,g;s,.tar.xz$,,g') && \
    ./autogen.sh && \
    ./configure && \
    make && \
    make install && \
    make training && \
    make training-install

###################         END BUILDER         ###################

FROM debian:buster-20191224-slim
MAINTAINER Palash Ray <paawak@gmail.com>

ADD tessdata.tar.gz /tesseract/

# Copy tesseract and its dependencies

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

# Copy jre and certs
RUN mkdir -p /etc/ssl/certs/java && \
    mkdir -p /etc/java-11-openjdk && \
    mkdir -p /usr/lib/jvm/java-11-openjdk-amd64

COPY --from=tess_installer /etc/ssl/certs/java/ /etc/ssl/certs/java/
COPY --from=tess_installer /etc/java-11-openjdk /etc/java-11-openjdk/
COPY --from=tess_installer /usr/lib/jvm/java-11-openjdk-amd64 /usr/lib/jvm/java-11-openjdk-amd64/

RUN ldconfig && \
    rm -rfv /usr/lib/jvm/java-11-openjdk-amd64/man && \
    rm -rfv /usr/lib/jvm/java-11-openjdk-amd64/docs

ENV JAVA_HOME /usr/lib/jvm/java-11-openjdk-amd64
ENV PATH $JAVA_HOME/bin:$PATH

CMD tesseract -l $LANG $IMAGE $OUTFILE

