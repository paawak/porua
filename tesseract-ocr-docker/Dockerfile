FROM alpine:3.7
MAINTAINER Palash Ray <paawak@gmail.com>
RUN apk add --no-cache leptonica
RUN apk add --no-cache libpng
RUN apk add --no-cache libjpeg
#RUN apk add --no-cache libtiff
ENV tess_dir /tesseract-3.05.02 
ENV tess_cmd tesseract
RUN mkdir ${tess_dir}
RUN mkdir ${tess_dir}/tessdata
RUN mkdir /input-output
ADD tesseract-3.05.02-bin/bin/${tess_cmd} ${tess_dir}/
ADD tessdata ${tess_dir}/tessdata/
RUN export TESSDATA_PREFIX=${tess_dir}/tessdata/
CMD ${tess_dir}/${tess_cmd} -l $LANG $IMAGE $OUTFILE


