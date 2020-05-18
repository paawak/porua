import React from 'react';

class ImageUploader extends React.Component {

  constructor(props) {
    super(props);
    this.handleSubmit = this.handleSubmit.bind(this);
    this.imageFileInput = React.createRef();
    this.state = {
      isImageFileSelected: false,
      selectedImageFileName: 'Choose file...'
    };
  }

  handleSubmit(event) {
    event.preventDefault();
    if (this.state.isImageFileSelected) {
      const data = new FormData();
      data.append('language', 'ben');
      data.append('image', this.imageFileInput.current.files[0]);

      fetch('http://localhost:8080/rest/ocr', {
        method: 'POST',
        body: data,
      }).then((response) => {
        response.text().then((body) => {
          console.log("Ocr Text:\n" + body);
        });
      });
    } else {
      alert("Please select an image to upload");
    }
  }

  render() {
    return (
      	<form onSubmit={this.handleSubmit}>
      		<fieldset className="form-group">
      			<div className="row">
      				<legend className="col-form-label col-sm-2 pt-0">Language Selection</legend>
      				<div className="col-sm-10">
      					<div className="form-group form-check">
      						<input className="form-check-input" type="radio" name="language" id="languageBengali" value="ben" defaultChecked={true} />
                    <label	className="form-check-label" htmlFor="languageBengali">Bengali </label>
      					</div>
      					<div className="form-group form-check">
      						<input className="form-check-input" type="radio" name="language" id="languageEnglish" value="eng" />
                  <label className="form-check-label" htmlFor="languageEnglish">English </label>
      					</div>
      				</div>
      			</div>
      		</fieldset>
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
