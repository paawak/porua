import React from 'react';
import OcrWord from './OcrWord'

class App extends React.Component {
  render() {
    return (
      <div class="container">
        <div class="row row-cols-5">
          <div class="col">
          <OcrWord/>
          <OcrWord/>
          <OcrWord/>
          <OcrWord/>
          <OcrWord/>
          </div>
        </div>
      </div>
    );
  }
}

export default App;
