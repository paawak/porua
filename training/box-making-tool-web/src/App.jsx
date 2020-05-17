import React from 'react';
import OcrCorrectionPage from './OcrCorrectionPage'
import ImageUploader from './ImageUploader'

class App extends React.Component {

  render() {
    return (
      <div className="jumbotron jumbotron-fluid">
        <div className="container">
          <h1 className="display-4">OCR Training Workbench</h1>
          <p className="lead">Choose the language and upload an image to OCR</p>
          <div className="d-flex flex-column bd-highlight mb-2">
            <div className="shadow mb-5 bg-white rounded p-2 bd-highlight"><ImageUploader/></div>
            <div className="shadow mb-5 bg-white rounded p-2 bd-highlight"><OcrCorrectionPage/></div>
          </div>
        </div>
      </div>
    );
  }
}

export default App;
