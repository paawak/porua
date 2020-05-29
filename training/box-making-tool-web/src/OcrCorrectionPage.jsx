import React from 'react';
import OcrWord from './OcrWord'

class OcrCorrectionPage extends React.Component {

  render() {
    const ocrWords = this.props.ocrWords.map((ocrWord) =>
      <OcrWord key={ocrWord.ocrWordId.wordSequenceId} bookId={ocrWord.ocrWordId.bookId}
        pageImageId={ocrWord.ocrWordId.pageImageId} wordSequenceId={ocrWord.ocrWordId.wordSequenceId}
        givenText={ocrWord.correctedText == null ? ocrWord.rawText : ocrWord.correctedText}/>
    );

    return (
      <div className="container">
        <h2>Ocr Correction Page</h2>                             
        <div id="carouselExampleControls" className="carousel slide" data-ride="carousel">
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
          <a className="carousel-control-prev" href="#carouselExampleControls" role="button" data-slide="prev">
            <span className="carousel-control-prev-icon" aria-hidden="true"></span>
            <span className="sr-only">Previous</span>
          </a>
          <a className="carousel-control-next" href="#carouselExampleControls" role="button" data-slide="next">
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
    return <div className="jumbotron jumbotron-fluid">
            <div className="container">
              <h1 className="display-4">{name}: {value}</h1>
            </div>
          </div>  
  }

}

export default OcrCorrectionPage;
