import React from 'react';
import OcrCorrectionPage from './OcrCorrectionPage'
import ImageUploader from './ImageUploader'
import PageSelectionPanel from './PageSelectionPanel'

export const DisplayMode = {
      PAGE_SELECTION: 'PAGE_SELECTION',
      IMAGE_UPLOADER: 'IMAGE_UPLOADER',
      OCR_CORRECTION_PAGE: 'OCR_CORRECTION_PAGE'
    };

class App extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
      displayMode: DisplayMode.PAGE_SELECTION,
      ocrWords: [],
      book: null
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
        showNewPagePanel={(book) => {
            this.setState({
              displayMode: DisplayMode.IMAGE_UPLOADER,
              book: book
            });
          }
        }
      />;
    } else if (this.state.displayMode === DisplayMode.IMAGE_UPLOADER) {
      panelToDisplay = <div className="shadow mb-5 bg-white rounded p-2 bd-highlight">
        <ImageUploader book={this.state.book} ocrWordsRecievedForNewPage={ocrWordsRecievedEvent}/>
      </div>;
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
