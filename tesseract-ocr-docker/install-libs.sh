#!/bin/bash
libSrc="/libs/tar-ball/"
libDest="/libs/src/"
for file in $(ls $libSrc)
do
	echo "File: $file"
	dir=$(echo $file | sed -e 's,.tar.gz$,,g;s,.tar.xz$,,g')
	echo "Unpacking in: $dir ..."
	mkdir -p $libDest$dir
	tar -xvf $libSrc$file -C $libDest$dir --strip-components=1
    cd $libDest$dir
    if [[ $file = *"tesseract"* ]]; then
        ./autogen.sh
    fi
    ./configure
    make
    make install
done

