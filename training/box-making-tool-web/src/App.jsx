import React from 'react';
import OcrCorrectionPage from './OcrCorrectionPage'
import ImageUploader from './ImageUploader'
import PageSelectionPanel from './PageSelectionPanel'
import GoogleSignInComponent from './GoogleSignInComponent';

export const DisplayMode = {
      PAGE_SELECTION: 'PAGE_SELECTION',
      IMAGE_UPLOADER: 'IMAGE_UPLOADER',
      OCR_CORRECTION_PAGE: 'OCR_CORRECTION_PAGE'
    };

class App extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
      isLoggedIn: false,
      googleAccessToken: null,
      displayMode: DisplayMode.PAGE_SELECTION,
      ocrWords: [],
      book: null,
      page: null
    };

    this.loginSuccess = this.loginSuccess.bind(this);
    this.handleLoginFailure = this.handleLoginFailure.bind(this);
  }

  render() {
    if (this.state.isLoggedIn) {
      return this.renderApplicationAfterLogin();
    } else {
      return this.renderLoginPage();
    }    
  }

  renderLoginPage() {
    return (
      <GoogleSignInComponent loginSuccess={this.loginSuccess} handleLoginFailure={this.handleLoginFailure}/>
    );
  }

  loginSuccess(response) {
    if(response.accessToken){
      this.setState(state => ({
        isLoggedIn: true,
        googleAccessToken: response.tokenId
      }));
    }
  }

  handleLoginFailure (response) {
    alert('Failed to log in')
  }

  renderApplicationAfterLogin() {
    let panelToDisplay;
    const ocrWordsRecievedEvent = (ocrWordListData, page) => {
        this.setState({
          ocrWords: ocrWordListData,
          displayMode: DisplayMode.OCR_CORRECTION_PAGE,
          page: page
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
        googleAccessToken={this.state.googleAccessToken}
      />;
    } else if (this.state.displayMode === DisplayMode.IMAGE_UPLOADER) {
      panelToDisplay = <div className="shadow mb-5 bg-white rounded p-2 bd-highlight">
        <ImageUploader book={this.state.book} ocrWordsRecievedForNewPage={ocrWordsRecievedEvent}/>
      </div>;
    } else if (this.state.displayMode === DisplayMode.OCR_CORRECTION_PAGE) {
      panelToDisplay = <div className="shadow mb-5 bg-white rounded p-2 bd-highlight"><OcrCorrectionPage ocrWords={this.state.ocrWords} page={this.state.page}/></div>
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
