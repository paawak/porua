import React from 'react';

class ImageUploader extends React.Component {

  constructor(props) {
    super(props);
    this.handleSubmit = this.handleSubmit.bind(this);
    this.imageFileInput = React.createRef();
    this.state = {
      isImageFileSelected: false,
      selectedImageFileName: 'Choose file...',
      pageNumberEntered: null
    };
  }

  handleSubmit(event) {
    event.preventDefault();
    if (this.state.isImageFileSelected) {
      const data = new FormData();
      data.append('bookId', this.props.book.id);
      data.append('pageNumber', this.state.pageNumberEntered);
      data.append('image', this.imageFileInput.current.files[0]);

      fetch('http://localhost:8080/train/word', {
        method: 'POST',
        body: data,
      }).then(response => response.json())
        .then(data => {
          this.props.ocrWordsRecievedForNewPage(data);
        });

        this.props.imageSubmittedForAnalysis();
      } else {
        alert("Please select an image to upload");
    }
  }

  render() {
    return (
      	<form onSubmit={this.handleSubmit}>
          <p className="lead">Upload a new image to OCR</p>
      		<fieldset className="form-group" disabled>
            <label htmlFor="bookName">Selected Book</label>
            <input type="text" id="bookName" className="form-control" placeholder={this.props.book.name}/>
      		</fieldset>
          <fieldset className="form-group" disabled>
            <label htmlFor="languageName">Selected Language</label>
            <input type="text" id="languageName" className="form-control" placeholder={this.props.book.language}/>
      		</fieldset>
          <div className="form-group">
            <label htmlFor="pageNumber">Page Number</label>
            <input type="number" className="form-control" id="pageNumber" onChange={
              e => {this.setState({pageNumberEntered: e.target.value})}
            }/>
          </div>
      		<div className="input-group mb-3">
      			<div className="input-group-prepend">
      				<span className="input-group-text" id="imageAddon">Upload Image</span>
      			</div>
      			<div className="custom-file">
      				<input type="file" ref={this.imageFileInput} className="custom-file-input" name="image" id="image" aria-describedby="imageAddon"
              onChange={e => {
                  this.setState({
                    isImageFileSelected: true,
                    selectedImageFileName: this.imageFileInput.current.files[0].name
                  });
                }
              }
              />
              <label className="custom-file-label" htmlFor="image">{this.state.selectedImageFileName}</label>
      			</div>
      		</div>
      		<button type="submit" className="btn btn-primary">Send To OCR</button>
      	</form>
    );
  }

}

export default ImageUploader;
