import React from 'react';
import OcrCorrectionPage from './OcrCorrectionPage'
import ImageUploader from './ImageUploader'

export const DisplayMode = {
      IMAGE_UPLOADER: 'IMAGE_UPLOADER',
      IMAGE_PROCESSING_IN_PROGRESS: 'IMAGE_PROCESSING_IN_PROGRESS',
      OCR_CORRECTION_PAGE: 'OCR_CORRECTION_PAGE'
    };

class App extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
      displayMode: DisplayMode.IMAGE_UPLOADER,
      ocrWords: []
    };
  }

  render() {
    let panelToDisplay;

    if (this.state.displayMode === DisplayMode.IMAGE_UPLOADER) {
      panelToDisplay = <div className="shadow mb-5 bg-white rounded p-2 bd-highlight"><ImageUploader
      imageSubmittedForAnalysis={() => {
          this.setState({
            displayMode: DisplayMode.IMAGE_PROCESSING_IN_PROGRESS
          });
        }
      }
      ocrWordsRecieved={ocrWordListData => {
          this.setState({
            ocrWords: ocrWordListData,
            displayMode: DisplayMode.OCR_CORRECTION_PAGE
          });
        }
      }/></div>;
    } else if (this.state.displayMode === DisplayMode.IMAGE_PROCESSING_IN_PROGRESS) {
      panelToDisplay =
      <button className="btn btn-primary btn-lg btn-block" type="button" disabled>
        <span className="spinner-border spinner-border-sm float-right" role="status" aria-hidden="true"></span>
        Image is being analysed...
      </button>
    } else if (this.state.displayMode === DisplayMode.OCR_CORRECTION_PAGE) {
      panelToDisplay = <div className="shadow mb-5 bg-white rounded p-2 bd-highlight"><OcrCorrectionPage ocrWords={this.state.ocrWords}/></div>
    } else {
      panelToDisplay = <div/>;
    }

    return (
      <div className="jumbotron jumbotron-fluid">
        <div className="container-xl">
          <h1 className="display-4">OCR Training Workbench</h1>
          {panelToDisplay}
          </div>
      </div>
    );
  }

}

export default App;
