import React from 'react';
import OcrWord from './OcrWord'

class OcrCorrectionPage extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
      markedForDeletion: new Map(),
      markedForCorrection: new Map()
    };
    this.handleSubmitForCorrection = this.handleSubmitForCorrection.bind(this);
  }

  handleSubmitForCorrection() {
    this.state.markedForDeletion.forEach(
      (value, wordSequenceId) => {
        console.log(wordSequenceId + "::" + value);
        //FIXME: work in progress
        fetch("http://localhost:8080/train/word/ignore", {
          method: 'POST',
          headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
          },
          body: JSON.stringify({
            bookId: this.props.page.book.bookId, 
            pageImageId: this.props.page.pageImageId,
            wordSequenceId: wordSequenceId
          })
        })
        .then(rawData => rawData.json())
        .then(data => console.log("Ignored word: " + data.wordSequenceId))
        .catch(() => this.setState({ hasErrors: true }));
      }
    );
  }

  render() {
    const ocrWords = this.props.ocrWords.map((ocrWord) =>
      <OcrWord key={ocrWord.ocrWordId.wordSequenceId} 
        bookId={ocrWord.ocrWordId.bookId}
        confidence={ocrWord.confidence}
        pageImageId={ocrWord.ocrWordId.pageImageId} 
        wordSequenceId={ocrWord.ocrWordId.wordSequenceId}
        givenText={ocrWord.rawText}
        correctedText={ocrWord.correctedText}
        toggleMarkedForDeletion = {
          () => {
           if (this.state.markedForDeletion.has(ocrWord.ocrWordId.wordSequenceId)) {
            this.state.markedForDeletion.delete(ocrWord.ocrWordId.wordSequenceId);
           } else {
            this.state.markedForDeletion.set(ocrWord.ocrWordId.wordSequenceId, true);
           }
          }
        }
        markForCorrection = {
          (correctedText) => {
           this.state.markedForCorrection.set(ocrWord.ocrWordId.wordSequenceId, correctedText);
          }
        }
        />
    );

    return (
      <div className="container">     
        <nav className="navbar sticky-top navbar-expand-lg navbar-light bg-light">
          <div className="navbar-brand">Ocr Correction Page</div>
          <button className="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarMain" aria-controls="navbarMain" aria-expanded="false" aria-label="Toggle navigation">
            <span className="navbar-toggler-icon"></span>
          </button>
          <div className="collapse navbar-collapse" id="navbarMain">
            <ul className="navbar-nav">
              <li className="nav-item dropdown">
                <div className="nav-link dropdown-toggle" id="navbarLanguageMenuLink" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                Language
                </div>
                <div className="dropdown-menu" aria-labelledby="navbarLanguageMenuLink">
                  <div className="dropdown-item">{this.props.page.book.language}</div>
                </div>
              </li>
              <li className="nav-item dropdown">
                <div className="nav-link dropdown-toggle" id="navbarBookMenuLink" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                Book
                </div>
                <div className="dropdown-menu" aria-labelledby="navbarBookMenuLink">
                  <div className="dropdown-item">{this.props.page.book.name}</div>
                </div>
              </li>
              <li className="nav-item dropdown active">
                <div className="nav-link dropdown-toggle" id="navbarPageMenuLink" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                Page <span className="sr-only">(current)</span>
                </div>
                <div className="dropdown-menu" aria-labelledby="navbarPageMenuLink">
                  <div className="dropdown-item">{this.props.page.name}</div>
                </div>
              </li>
            </ul>
          </div>
          <button className="btn btn-outline-success my-2 my-sm-0" type="button" onClick={this.handleSubmitForCorrection}>Submit For Correction</button>
        </nav>

        <div className="row row-cols-4">
          {ocrWords}
        </div>
      </div>
    );
  }

}

export default OcrCorrectionPage;
