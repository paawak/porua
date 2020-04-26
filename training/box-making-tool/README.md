# Training Tesseract (LSTM) For Bengali
## Building Tesseract Training Tools
While building Tesseract from source, we need to build training tools as well.

    make training
    make training-training
    make ScrollView.jar

Then, manually copy the  __java__  folder to the *TESSERACT_HOME*. Define the below environment variable:
    
    export SCROLLVIEW_PATH=$TESSERACT_HOME/java
    
You would also need to copy the contents of <https://github.com/tesseract-ocr/tessconfigs/tree/master/configs> under *$TESSDATA_PREFIX*.   

The best trained LSTM models can be found here: <https://github.com/tesseract-ocr/tessdata_best>  

## Creating Box Files
The coordinate system used in the box file has **(0,0)** at the **bottom-left**.

### Online Documentation
1. <https://tesseract-ocr.github.io/tessdoc/TrainingTesseract-4.00.html#making-box-files>
1. <https://github.com/tesseract-ocr/tesseract/issues/2357#issuecomment-477239316>

### Commands

The below command will generate a box file called image.box:

    tesseract -l ben bangla-mahabharat-1-page_2.jpg image lstmbox

## Miscellaneous
### Tesseract API Guide
<https://tesseract-ocr.github.io/tessdoc/APIExample>
