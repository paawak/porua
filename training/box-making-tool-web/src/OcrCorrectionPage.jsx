import React from 'react';
import OcrWord from './OcrWord'

class OcrCorrectionPage extends React.Component {

  render() {
    const ocrWords = this.props.ocrWords.map((ocrWord) =>
      <OcrWord key={ocrWord.wordSequenceNumber} wordId={ocrWord.wordSequenceNumber} givenText={ocrWord.text}/>
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
