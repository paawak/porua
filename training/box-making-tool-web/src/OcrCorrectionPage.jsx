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
        <h2>Ocr Correction Page</h2>                             
        <div id="bookDetailsCarouselControls" className="carousel slide" data-ride="carousel">
          <div className="carousel-inner">
            <div className="carousel-item active">
              {this.getNameValueAsDiv("Book", this.props.page.book.name)}
            </div>
            <div className="carousel-item">
              {this.getNameValueAsDiv("Language", this.props.page.book.language)}
            </div>
            <div className="carousel-item">
              {this.getNameValueAsDiv("Page", this.props.page.name)}
            </div>
          </div>
          <a className="carousel-control-prev" href="#bookDetailsCarouselControls" role="button" data-slide="prev">
            <span className="carousel-control-prev-icon" aria-hidden="true"></span>
            <span className="sr-only">Previous</span>
          </a>
          <a className="carousel-control-next" href="#bookDetailsCarouselControls" role="button" data-slide="next">
            <span className="carousel-control-next-icon" aria-hidden="true"></span>
            <span className="sr-only">Next</span>
          </a>
        </div>
        <div className="row row-cols-4">
          {ocrWords}
        </div>
      </div>
    );
  }

  getNameValueAsDiv(name, value) {
    return <div className="d-block w-100 jumbotron jumbotron-fluid">
            <div className="container">
              <h1 className="display-4">{name}: {value}</h1>
            </div>
          </div>  
  }

}

export default OcrCorrectionPage;
