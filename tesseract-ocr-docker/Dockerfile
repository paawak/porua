FROM alpine:3.7 as builder
MAINTAINER Palash Ray <paawak@gmail.com>


RUN apk add --no-cache g++  \
    autoconf automake libtool   \   
    make    


ENV tess_dir tesseract-3.05.02 
ENV sources /lib-sources 

ADD ${sources} ${sources}/

RUN mkdir -p /${tess_dir}/install  \    
    /usr/local/man/man1/cjpeg.1 


RUN cd ${sources}/  && \
    tar -xvf zlib-1.2.11.tar.gz

RUN cd ${sources}/zlib-1.2.11/   &&  \
    ./configure &&  \
    make &&  \
    make install 

RUN cd ${sources}/  && \
    tar -xvf jpegsrc.v6b.tar.gz

RUN cd ${sources}/jpeg-6b/   &&  \
    ./configure &&  \
    make &&  \
    make install 

RUN cd ${sources}/  && \
    tar -xvf tiff-4.0.9.tar.gz

RUN cd ${sources}/tiff-4.0.9/   &&  \
    ./configure &&  \
    make &&  \
    make install 

RUN cd ${sources}/  && \
    tar -xvf libpng-1.6.34.tar.xz

RUN cd ${sources}/libpng-1.6.34/   &&  \
    ./configure &&  \
    make &&  \
    make install 

RUN cd ${sources}/  && \
    tar -xvf leptonica-1.76.0.tar.gz

RUN cd ${sources}/leptonica-1.76.0/   &&  \
    ./configure &&  \
    make &&  \
    make install 

RUN cd ${sources}/  && \
    tar -xvf ${tess_dir}.tar.gz

RUN cd ${sources}/${tess_dir}/ &&  \
    ./autogen.sh &&  \
    ./configure --prefix=/${tess_dir}/install &&  \
    make &&  \
    make install 

RUN cd ${sources}/  && \
    tar -xvf tessdata.tar.gz


FROM alpine:3.7
MAINTAINER Palash Ray <paawak@gmail.com>

ENV tess_dir tesseract-3.05.02 

RUN mkdir -p /${tess_dir}/install  \
    /${tess_dir}/tessdata  \
    /input-output   


COPY --from=builder /lib-sources/tessdata /${tess_dir}/tessdata

COPY --from=builder /${tess_dir}/install /${tess_dir}/install

COPY --from=builder /usr /usr


CMD /${tess_dir}/install/bin/tesseract -l $LANG $IMAGE $OUTFILE


