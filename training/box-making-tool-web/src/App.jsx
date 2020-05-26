import React from 'react';
import OcrCorrectionPage from './OcrCorrectionPage'
import ImageUploader from './ImageUploader'
import PageSelectionPanel from './PageSelectionPanel'

export const DisplayMode = {
      PAGE_SELECTION: 'PAGE_SELECTION',
      IMAGE_UPLOADER: 'IMAGE_UPLOADER',
      IMAGE_PROCESSING_IN_PROGRESS: 'IMAGE_PROCESSING_IN_PROGRESS',
      OCR_CORRECTION_PAGE: 'OCR_CORRECTION_PAGE'
    };

class App extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
      displayMode: DisplayMode.PAGE_SELECTION,
      ocrWords: [],
      newPageBookId: null,
      newPageBookName: null
    };
  }

  render() {
    let panelToDisplay;
    const ocrWordsRecievedEvent = (ocrWordListData) => {
        this.setState({
          ocrWords: ocrWordListData,
          displayMode: DisplayMode.OCR_CORRECTION_PAGE
        });
    };
    if (this.state.displayMode === DisplayMode.PAGE_SELECTION) {
      panelToDisplay = <PageSelectionPanel
        ocrWordsRecievedForExistingPage={ocrWordsRecievedEvent}
        showNewPagePanel={(bookId, bookName) => {
            this.setState({
              displayMode: DisplayMode.IMAGE_UPLOADER,
              newPageBookId: bookId,
              newPageBookName: bookName
            });
          }
        }
      />;
    } else if (this.state.displayMode === DisplayMode.IMAGE_UPLOADER) {
      panelToDisplay = <div className="shadow mb-5 bg-white rounded p-2 bd-highlight"><ImageUploader
      bookId={this.state.newPageBookId}
      bookName={this.state.newPageBookName}
      imageSubmittedForAnalysis={() => {
          this.setState({
            displayMode: DisplayMode.IMAGE_PROCESSING_IN_PROGRESS
          });
        }
      }
      ocrWordsRecievedForNewPage={ocrWordsRecievedEvent}/></div>;
    } else if (this.state.displayMode === DisplayMode.IMAGE_PROCESSING_IN_PROGRESS) {
      panelToDisplay =
      <button className="btn btn-primary btn-lg btn-block" type="button" disabled>
        <span className="spinner-border spinner-border-sm float-left" role="status" aria-hidden="true"></span>
        Please wait while we analyse the uploaded image...
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
