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
        {this.getNameValueAsDiv("Book Name", this.props.page.book.name)}
        {this.getNameValueAsDiv("Language", this.props.page.book.language)}
        {this.getNameValueAsDiv("Page Name", this.props.page.name)}     
        <div className="row row-cols-4">
          {ocrWords}
        </div>
      </div>
    );
  }

  getNameValueAsDiv(name, value) {
    return <div className="jumbotron jumbotron-fluid">
            <div className="container">
              <h1 className="display-4">{name}</h1>
              <p className="lead">{value}</p>
            </div>
          </div>  
  }

}

export default OcrCorrectionPage;
