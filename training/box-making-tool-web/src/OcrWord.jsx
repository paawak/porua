import React from 'react';

class OcrWord extends React.Component {
  render() {
    return (
      <div class="col">
        <div class="container" id="word_1">
          <div class="row row-cols-1">
            <div class="col">
              <img id="word-1" src="mockups/images/1.png"/>
            </div>
            <div class="col">
              <label for="ocrWord1">OCR:</label>
            </div>
            <div class="col">
              <input type="text" name="ocrWord1" id="ocrWord1" value="aaa" disabled="true"/>
            </div>
            <div class="col">
              <label for="correction1">Correction: </label>
            </div>
            <div class="col">
              <input type="text" name="correction1" id="correction1"/>
            </div>
          </div>
        </div>
      </div>
    );
  }
}

export default OcrWord;
