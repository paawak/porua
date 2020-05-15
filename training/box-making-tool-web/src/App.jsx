import React from 'react';
import OcrWord from './OcrWord'

class App extends React.Component {

  render() {
    const ocrWords = this.props.ocrWords.map((ocrWord) =>
      <OcrWord wordId={ocrWord.wordId} givenText={ocrWord.givenText}/>
    );

    return (
      <div class="container">
        <div class="row row-cols-5">
          {ocrWords}
        </div>
      </div>
    );
  }
  
}

export default App;
