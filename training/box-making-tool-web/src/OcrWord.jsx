import React from 'react';
import BanglaTextBox from './BanglaTextBox'

class OcrWord extends React.Component {

  constructor(props) {
    super(props);
    this.state = {};
  }

  render() {

    const idGenerator = (prefix) => prefix + "_" + this.props.wordSequenceId;
    const givenTextInputId = idGenerator("givenText");
    const correctedTextInputId = idGenerator("correctedText");
    const closeButtonId = idGenerator("closeButton");

    const borderColorClassMap = {
      0: 'success',
      1: 'danger',
      2: 'warning'
    };

    const borderColorClass = "border-" + borderColorClassMap[parseInt(this.props.wordSequenceId, 10) % 3];

    return (
      <div className="col">                  
          <div className="container">                                               
              <div className={"row row-cols-1 overflow-auto border " + borderColorClass}>              
                <div className="col">
                  <button type="button" id={closeButtonId} className="close" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                  </button> 
                </div>                       
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
                  <BanglaTextBox name={correctedTextInputId} id={correctedTextInputId}/>
                </div>
              </div>            
          </div>        
      </div>
    );
  }

}

export default OcrWord;
