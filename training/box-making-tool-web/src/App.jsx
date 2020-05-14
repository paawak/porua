import React from 'react';
import OcrWord from './OcrWord'

class App extends React.Component {
  render() {
    return (
      <div class="container">
        <div class="row row-cols-5">
          <OcrWord wordId="1" givenText="aaa"/>
          <OcrWord wordId="2" givenText="bbb"/>
          <OcrWord wordId="3" givenText="ccc"/>
          <OcrWord wordId="4" givenText="ddd"/>
          <OcrWord wordId="5" givenText="eee"/>
          <OcrWord wordId="6" givenText="fff"/>
          <OcrWord wordId="7" givenText="ggg"/>
        </div>
      </div>
    );
  }
}

export default App;
