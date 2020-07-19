import React from 'react';
import OcrWord from './OcrWord'

class OcrCorrectionPage extends React.Component {

  render() {
    const ocrWords = this.props.ocrWords.map((ocrWord) =>
      <OcrWord key={ocrWord.ocrWordId.wordSequenceId} 
        bookId={ocrWord.ocrWordId.bookId}
        confidence={ocrWord.confidence}
        pageImageId={ocrWord.ocrWordId.pageImageId} wordSequenceId={ocrWord.ocrWordId.wordSequenceId}
        givenText={ocrWord.rawText}
        correctedText={ocrWord.correctedText}
        />
    );

    return (
      <div className="container">
        <nav className="navbar sticky-top navbar-expand-lg navbar-light bg-light">
          <div className="navbar-brand">Ocr Correction Page</div>
          <button className="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNavAltMarkup" aria-controls="navbarNavAltMarkup" aria-expanded="false" aria-label="Toggle navigation">
            <span className="navbar-toggler-icon"></span>
          </button>
          <div className="collapse navbar-collapse" id="navbarNavAltMarkup">
            <div className="navbar-nav">
              <div className="nav-item nav-link">Language: {this.props.page.book.language}</div>
              <div className="nav-item nav-link">Book: {this.props.page.book.name}</div>              
              <div className="nav-item nav-link active">Page: {this.props.page.name} <span className="sr-only">(current)</span></div>
            </div>
          </div>
          <button className="btn btn-outline-success my-2 my-sm-0" type="button">Submit For Correction</button>
        </nav>             
        <div className="row row-cols-4">
          {ocrWords}
        </div>
      </div>
    );
  }

}

export default OcrCorrectionPage;
