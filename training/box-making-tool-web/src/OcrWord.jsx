import React from 'react';

class OcrWord extends React.Component {

  constructor(props) {
    super(props);
    this.state = {};
  }

  render() {

    const idGenerator = (prefix) => prefix + "_" + this.props.wordId;
    const givenTextInputId = idGenerator("givenText");
    const correctedTextInputId = idGenerator("correctedText");

    return (
      <div className="col">
        <div className="container">
          <div className="row row-cols-1">
            <div className="col">
              <img id={idGenerator("ocrImage")} alt="..." src={"http://localhost:8080/train/word/image?wordId=" + this.props.wordId + "&imagePath=/kaaj/source/porua/training/box-making-tool/src/test/resources/images/bangla-mahabharat-1-page_2.jpg"}/>
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
