import React from 'react';
import OcrWord from './OcrWord'

class OcrCorrectionPage extends React.Component {

  render() {

    if (this.props.ocrWords.status !== 200) {
      return (
        <div className="alert alert-danger alert-dismissible fade show" role="alert">
          <strong>Oops!</strong> Could not upload image for analysis.
          <button type="button" className="close" data-dismiss="alert" aria-label="Close">
            <span aria-hidden="true">&times;</span>
          </button>
        </div>
      );
    }

    const ocrWords = this.props.ocrWords.map((ocrWord) =>
      <OcrWord key={ocrWord.ocrWordId.wordSequenceId} bookId={ocrWord.ocrWordId.bookId}
        pageImageId={ocrWord.ocrWordId.pageImageId} wordSequenceId={ocrWord.ocrWordId.wordSequenceId}
        givenText={ocrWord.correctedText == null ? ocrWord.rawText : ocrWord.correctedText}/>
    );

    return (
      <div className="container">
        <h2>Ocr Correction Page</h2>
        <div className="row row-cols-4">
          {ocrWords}
        </div>
      </div>
    );
  }

}

export default OcrCorrectionPage;
