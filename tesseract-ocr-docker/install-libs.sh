#!/bin/bash
libSrc="lib-sources/"
libDest="libs/"
for file in $(ls $libSrc)
do
	echo "File: $file"
	dir=$(echo $file | sed -e 's,.tar.gz$,,g;s,.tar.xz$,,g')
	echo "Unpacking in: $dir ..."
	mkdir -p $libDest$dir
	tar -xvf $libSrc$file -C $libDest$dir --strip-components=1
done

