# Introduction
This is a tool for creating box files for Tesseract OCR from Bangla images. You would be able to edit images which show incorrect text and feed it to the backend system for correction. The box file is generated with corrected text. This is still work in progress.

# Demo

## Upload a new image
This demo will show you how you can upload a new image containing Bangla to the backend system, and how it detects words using OCR and then sends it to the frontend so that you can manually correct the words, or remove mis-detected words.

<figure class="video_container">
  <video controls="true" allowfullscreen="true" poster="assets/tesseract-box-making-tool-uploading-new-image-file.png">
    <source src="assets/tesseract-box-making-tool-uploading-new-image-file.m4v" type="video/mp4">
  </video>
</figure>

## Correct an existing image
This demo will show you how you can retrieve an image that you had uploaded in the past and correct it.

  <video controls="true" allowfullscreen="true" poster="assets/assets/tesseract-box-making-tool-correcting-existing-image-file.png">
    <source src="assets/tesseract-box-making-tool-correcting-existing-image-file.m4v" type="video/mp4">
    Your browser does not support the video tag.
  </video>

# Developer Stuff

## Running the project

This project is written in React. You can run it by:

    npm start

Open [http://localhost:3000](http://localhost:3000) to view it in the browser.

## Bangla typing
1. <https://github.com/dipu-bd/jquery.bangla>    
1. <https://medium.com/@dipu.sudipta/jquery-bangla-an-input-tool-for-bangla-language-cc3bd781b2d8>

## Fix for common problems
### Error: ENOSPC: System limit for number of file watchers reached, watch 'box-making-tool-web/public'
This happens when you have the Visual Studio Open. Solution:

    export CHOKIDAR_USEPOLLING=1
