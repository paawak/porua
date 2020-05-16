import React from 'react';

class OcrWord extends React.Component {

  constructor(props) {
    super(props);
    this.state = {};
  }

  render() {
    const givenTextInputId = `givenText_${this.props.wordId}`;
    const correctedTextInputId = `correctedText_${this.props.wordId}`;
    return (
      <div className="col">
        <div className="container" id="word_1">
          <div className="row row-cols-1">
            <div className="col">
              <img id="word-1" alt="..." src={"mockups/images/" + this.props.wordId + ".png"}/>
            </div>
            <div className="col">
              <label htmlFor={givenTextInputId}>OCR:</label>
            </div>
            <div className="col">
              <input type="text" name={givenTextInputId} id={givenTextInputId} value={this.props.givenText} disabled={true}/>
            </div>
            <div className="col">
              <label htmlFor={correctedTextInputId}>Correction: </label>
            </div>
            <div className="col">
              <input type="text" name={correctedTextInputId} id={correctedTextInputId}/>
            </div>
          </div>
        </div>
      </div>
    );
  }

}

export default OcrWord;
