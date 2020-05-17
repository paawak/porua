import React from 'react';
import OcrWord from './OcrWord'

class OcrCorrectionPage extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
      hasErrors: false,
      ocrWords: []
    };
  }

  componentDidMount() {
    fetch("http://localhost:8080/train/word?imagePath=/kaaj/source/porua/training/box-making-tool/src/test/resources/images/bangla-mahabharat-1-page_2.jpg&language=ben")
      .then(rawData => rawData.json())
      .then(ocrWords => this.setState({ ocrWords: ocrWords }))
      .catch(() => this.setState({ hasErrors: true }));
  }

  render() {
    const ocrWords = this.state.ocrWords.map((ocrWord) =>
      <OcrWord key={ocrWord.wordSequenceNumber} wordId={ocrWord.wordSequenceNumber} givenText={ocrWord.text}/>
    );

    return (
      <div className="container">
        <div className="row row-cols-4">
          {ocrWords}
        </div>
      </div>
    );
  }

}

export default OcrCorrectionPage;
