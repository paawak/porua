import React from 'react';
import BanglaTextBox from './BanglaTextBox'

class OcrWord extends React.Component {

  constructor(props) {
    super(props);
    this.state = {};
    this.handleCloseButton = this.handleCloseButton.bind(this);
  }

  handleCloseButton(event) {
    fetch("http://localhost:8080/train/word/ignore", {
      method: 'POST',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        bookId: this.props.bookId, 
        pageImageId: this.props.pageImageId,
        wordSequenceId: this.props.wordSequenceId
      })
    })
    .then(rawData => rawData.json())
    .then(data => console.log("Ignored word: " + data))
    .catch(() => this.setState({ hasErrors: true }));
  }

  render() {

    const idGenerator = (prefix) => prefix + "_" + this.props.wordSequenceId;
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
          <div className="container p-2">                                               
              <div className={"row row-cols-1 overflow-auto border " + borderColorClass}>              
                <div className="col">
                  <button type="button" id={closeButtonId} className="close" aria-label="Close" onClick={this.handleCloseButton}>
                    <span aria-hidden="true">&times;</span>
                  </button> 
                </div>                       
                <div className="col pb-3">
                  <img id={idGenerator("ocrImage")} alt="..." src={"http://localhost:8080/train/word/image?bookId=" + this.props.bookId + "&pageImageId=" + this.props.pageImageId + "&wordSequenceId=" + this.props.wordSequenceId}/>
                </div>
                <div className="col p-3 alert alert-dark jumbotron jumbotron-fluid" role="alert">
                  <p className="lead">OCR Text</p>
                  <hr className="my-4"/>
                  <h1 className="display-4">{this.props.givenText}</h1>
                </div>
                <div className="col p-3 form-group">
                  <BanglaTextBox name={correctedTextInputId} id={correctedTextInputId} placeholder="Correct Text"/>
                </div>
              </div>            
          </div>        
      </div>
    );
  }

}

export default OcrWord;
