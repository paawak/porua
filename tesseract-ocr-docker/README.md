# Building the docker image
## Using Docker CLI

    docker build -t paawak/tesseract-ocr-jdk-11:debian_slim_tess-4.1.1_v1.0 -t paawak/tesseract-ocr-jdk-11:latest .

    ## Using Buildah

        buildah bud -t paawak/tesseract-ocr-jdk-11:debian_slim_tess-4.1.1_v1.0 -t paawak/tesseract-ocr-jdk-11:latest .

# Running docker image

    docker run -it     \
        -v /kaaj/source/porua/tesseract-ocr-docker/sample-images:/input-output    \
        -v /kaaj/installs/tesseract/tessdata-4.0.0:/tesseract/tessdata    \
        -e LANG=ben -e IMAGE=/input-output/bangla.tif       \
        -e OUTFILE=/input-output/output     \
        -e TESSDATA_PREFIX=/tesseract/tessdata/     \
      paawak/tesseract-ocr-jdk-11:latest

      docker run -it     \
          -v /kaaj/source/porua/tesseract-ocr-docker/sample-images:/input-output    \
          -v /kaaj/installs/tesseract/tessdata-4.0.0:/tesseract/tessdata    \
          -e LANG=ben -e IMAGE=/input-output/bangla.jpg       \
          -e OUTFILE=/input-output/output     \
          -e TESSDATA_PREFIX=/tesseract/tessdata/     \
        paawak/tesseract-ocr-jdk-11:latest

        docker run -it     \
            -v /kaaj/source/porua/tesseract-ocr-docker/sample-images:/input-output    \
            -v /kaaj/installs/tesseract/tessdata-4.0.0:/tesseract/tessdata    \
            -e LANG=eng -e IMAGE=/input-output/english.png       \
            -e OUTFILE=/input-output/output     \
            -e TESSDATA_PREFIX=/tesseract/tessdata/     \
          paawak/tesseract-ocr-jdk-11:latest

# To get into a docker image

    docker run -it paawak/tesseract-ocr-jdk-11:latest sh


# Cleanup Docker images

    docker ps --filter status=dead --filter status=exited -aq | xargs -r docker rm -v
    docker images --no-trunc | grep '<none>' | awk '{ print $3 }' | xargs -r docker rmi

# Getting latest Tessdata    

The latest Tessdata has been got from <https://github.com/tesseract-ocr/tessdata/releases>. Took the latest 4.x and removed all languages except Bengali and Hindi.

# Running Tesseract REST Docker

    docker run -it -p 8080:8080 -e spring.profiles.active=default,container paawak/tesseract-ocr-rest:latest

# Installing Tesseract 4.x from source

https://github.com/tesseract-ocr/tesseract/wiki/Compiling#linux

Dependencies:
1. G++
2. Leptonica
3. libpng
4. libjpeg
5. libtiff

For available packages in Alpine Linux:
1. http://dl-cdn.alpinelinux.org/alpine/v3.10/main/x86_64/
2. http://dl-cdn.alpinelinux.org/alpine/v3.10/community/x86_64/

The below are the commands:

    ./autogen.sh
    ./configure --prefix=/kaaj/installs/tesseract/4.1.1
    make
    make install
    make training
    make training-install

# Running tesseract

Copy the tessdata directory to an appropriate folder

export TESSDATA_PREFIX=/kaaj/installs/tesseract/tessdata-4.0.0
bin/tesseract -l ben ../images/Bangla-mahabharat-1-page_2.jpg outputbase
