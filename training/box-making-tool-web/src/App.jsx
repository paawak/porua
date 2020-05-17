import React from 'react';
import OcrCorrectionPage from './OcrCorrectionPage'
import ImageUploader from './ImageUploader'

class App extends React.Component {

  constructor(props) {
    super(props);
  }

  render() {
    return (
      <div class="jumbotron jumbotron-fluid">
        <div class="container">
          <h1 class="display-4">OCR Training Workbench</h1>
          <p class="lead">Choose the language and upload an image to OCR</p>
          <div class="d-flex flex-column bd-highlight mb-2">
            <div class="shadow mb-5 bg-white rounded p-2 bd-highlight"><ImageUploader/></div>
            <div class="shadow mb-5 bg-white rounded p-2 bd-highlight"><OcrCorrectionPage/></div>
          </div>
        </div>
      </div>
    );
  }
}

export default App;
