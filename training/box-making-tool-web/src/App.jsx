import React from 'react';
import OcrWord from './OcrWord'

class App extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
      hasErrors: false,
      ocrWords: []
    };
  }

  componentDidMount() {
    fetch("http://localhost:3000/mockups/ocr.json")
      .then(rawData => rawData.json())
      .then(ocrWords => this.setState({ ocrWords: ocrWords }))
      .catch(() => this.setState({ hasErrors: true }));
  }

  render() {
    const ocrWords = this.state.ocrWords.map((ocrWord) =>
      <OcrWord wordId={ocrWord.wordId} givenText={ocrWord.givenText}/>
    );

    return (
      <div className="container">
        <div className="row row-cols-5">
          {ocrWords}
        </div>
      </div>
    );
  }

}

export default App;
