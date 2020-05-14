import React from 'react';

class OcrWord extends React.Component {
  render() {
    const givenTextInputId = `givenText_${this.props.wordId}`;
    const correctedTextInputId = `correctedText_${this.props.wordId}`;
    return (
      <div class="col">
        <div class="container" id="word_1">
          <div class="row row-cols-1">
            <div class="col">
              <img id="word-1" src={"mockups/images/" + this.props.wordId + ".png"}/>
            </div>
            <div class="col">
              <label for={givenTextInputId}>OCR:</label>
            </div>
            <div class="col">
              <input type="text" name={givenTextInputId} id={givenTextInputId} value={this.props.givenText} disabled="true"/>
            </div>
            <div class="col">
              <label for={correctedTextInputId}>Correction: </label>
            </div>
            <div class="col">
              <input type="text" name={correctedTextInputId} id={correctedTextInputId}/>
            </div>
          </div>
        </div>
      </div>
    );
  }
}

export default OcrWord;
