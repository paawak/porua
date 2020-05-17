import React from 'react';

class ImageUploader extends React.Component {

  constructor(props) {
    super(props);
    this.state = {};
  }

  render() {
    return (
      	<form>
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
      				<span className="input-group-text" id="inputGroupFileAddon01">Upload</span>
      			</div>
      			<div className="custom-file">
      				<input type="file" className="custom-file-input" id="inputGroupFile01" aria-describedby="inputGroupFileAddon01" />
              <label className="custom-file-label" htmlFor="inputGroupFile01">Choose file</label>
      			</div>
      		</div>
      		<button type="button" class="btn btn-primary">Send To OCR</button>
      	</form>
    );
  }

}

export default ImageUploader;
