import React from 'react';

class OcrWord extends React.Component {

  constructor(props) {
    super(props);
    this.state = {};
  }

  render() {

    const idGenerator = (prefix) => prefix + "_" + this.props.wordSequenceId;
    const givenTextInputId = idGenerator("givenText");
    const correctedTextInputId = idGenerator("correctedText");

    return (
      <div className="col">
        <div className="container">
          <div className="row row-cols-1">
            <div className="col">
              <img id={idGenerator("ocrImage")} alt="..." src={"http://localhost:8080/train/word/image?bookId=" + this.props.bookId + "&pageImageId=" + this.props.pageImageId + "&wordSequenceId=" + this.props.wordSequenceId}/>
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
