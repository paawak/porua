import React from 'react';
import OcrWord from './OcrWord'

class OcrCorrectionPage extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
      markedForDeletion: new Map(),
      markedForCorrection: new Map(),
      ignoredSuccess: false,
      ignoredFailed: false,
      correctionSuccess: false,
      correctionFailed: false      
    };
    this.handleSubmitForCorrection = this.handleSubmitForCorrection.bind(this);
  }

  handleSubmitForCorrection() {

    const ignoredWords = Array.from(this.state.markedForDeletion).map(entryAsArray => {
      let wordSequenceId = entryAsArray[0];
      let ignoredOcrWord = entryAsArray[1];
      return {
        "bookId": ignoredOcrWord.bookId,
        "pageImageId": ignoredOcrWord.pageImageId,
        "wordSequenceId": wordSequenceId
      }
    });

    if (ignoredWords.length > 0) {
      fetch("http://localhost:8080/train/word/ignore", {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(ignoredWords)
      })
        .then(response => {
          if (response.ok) {
            this.setState({ ignoredSuccess: true });
            this.state.markedForDeletion.clear();
          } else {
            this.setState({ ignoredFailed: true });
          }
        });
    }

    const correctedWords = Array.from(this.state.markedForCorrection).map(entryAsArray => {
      let wordSequenceId = entryAsArray[0];
      let correctedOcrWord = entryAsArray[1];
      return {
        "correctedText": correctedOcrWord.correctedText,
        "ocrWordId": {
          "bookId": correctedOcrWord.bookId,
          "pageImageId": correctedOcrWord.pageImageId,
          "wordSequenceId": wordSequenceId
        }
      };
    });

    if (correctedWords.length > 0) {
      fetch("http://localhost:8080/train/word", {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(correctedWords)
      })
        .then(response => {
          if (response.ok) {
            this.setState({ correctionSuccess: true });
            this.state.markedForCorrection.clear();
          } else {
            this.setState({ correctionFailed: true });
          }
        });
    }

  }

  render() {
    const ocrWords = this.props.ocrWords.map((ocrWord) =>
      <OcrWord key={ocrWord.ocrWordId.wordSequenceId}
        bookId={ocrWord.ocrWordId.bookId}
        confidence={ocrWord.confidence}
        pageImageId={ocrWord.ocrWordId.pageImageId}
        wordSequenceId={ocrWord.ocrWordId.wordSequenceId}
        givenText={ocrWord.rawText}
        correctedText={ocrWord.correctedText}
        toggleMarkedForDeletion={
          () => {
            if (this.state.markedForDeletion.has(ocrWord.ocrWordId.wordSequenceId)) {
              this.state.markedForDeletion.delete(ocrWord.ocrWordId.wordSequenceId);
            } else {
              this.state.markedForDeletion.set(ocrWord.ocrWordId.wordSequenceId, {
                "bookId": ocrWord.ocrWordId.bookId,
                "pageImageId": ocrWord.ocrWordId.pageImageId,
                "wordSequenceId": ocrWord.ocrWordId.wordSequenceId
              });
            }
          }
        }
        markForCorrection={
          (correctedText) => {
            if (correctedText) {
              this.state.markedForCorrection.set(ocrWord.ocrWordId.wordSequenceId, {
                "bookId": ocrWord.ocrWordId.bookId,
                "pageImageId": ocrWord.ocrWordId.pageImageId,
                "wordSequenceId": ocrWord.ocrWordId.wordSequenceId,
                "correctedText": correctedText
              });
            }
          }
        }
      />
    );

    const displayError = (text) => {
      return (
      <div className="alert alert-danger alert-dismissible fade show" role="alert">
        <strong>Error in {text} operation!</strong> Please try again.
          <button type="button" className="close" data-dismiss="alert" aria-label="Close">
            <span aria-hidden="true">&times;</span>
          </button>
      </div>
      )
    };

    const displaySuccess = (text) => {
      return (
      <div className="alert alert-success alert-dismissible fade show" role="alert">
        <strong>Success!</strong> {text} operation was succesful!
          <button type="button" className="close" data-dismiss="alert" aria-label="Close">
            <span aria-hidden="true">&times;</span>
          </button>
      </div>
      )
    };

    return (
      <div className="container">     
        <nav className="navbar sticky-top navbar-expand-lg navbar-light bg-light">
          <div className="navbar-brand">Ocr Correction Page</div>
          <button className="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarMain" aria-controls="navbarMain" aria-expanded="false" aria-label="Toggle navigation">
            <span className="navbar-toggler-icon"></span>
          </button>
          <div className="collapse navbar-collapse" id="navbarMain">
            <ul className="navbar-nav">
              <li className="nav-item dropdown">
                <div className="nav-link dropdown-toggle" id="navbarLanguageMenuLink" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                Language
                </div>
                <div className="dropdown-menu" aria-labelledby="navbarLanguageMenuLink">
                  <div className="dropdown-item">{this.props.page.book.language}</div>
                </div>
              </li>
              <li className="nav-item dropdown">
                <div className="nav-link dropdown-toggle" id="navbarBookMenuLink" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                Book
                </div>
                <div className="dropdown-menu" aria-labelledby="navbarBookMenuLink">
                  <div className="dropdown-item">{this.props.page.book.name}</div>
                </div>
              </li>
              <li className="nav-item dropdown active">
                <div className="nav-link dropdown-toggle" id="navbarPageMenuLink" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                Page <span className="sr-only">(current)</span>
                </div>
                <div className="dropdown-menu" aria-labelledby="navbarPageMenuLink">
                  <div className="dropdown-item">{this.props.page.name}</div>
                </div>
              </li>
            </ul>
          </div>
          <button className="btn btn-outline-success my-2 my-sm-0" type="button" onClick={this.handleSubmitForCorrection}>Submit For Correction</button>
        </nav>
        
        { this.state.ignoredFailed &&
          displayError("ignore")
        }
        
        { this.state.correctionFailed &&
          displayError("word correction")
        }

        { this.state.ignoredSuccess &&
          displaySuccess("Ignore")
        }

        { this.state.correctionSuccess &&
          displaySuccess("Word correction")
        }

        <div className="row row-cols-4">
          {ocrWords}
        </div>
      </div>
    );
  }

}

export default OcrCorrectionPage;
