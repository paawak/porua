import React from 'react';

class ImageUploader extends React.Component {

  constructor(props) {
    super(props);
    this.state = {};
  }

  render() {
    return (
      	<form>
      		<fieldset class="form-group">
      			<div class="row">
      				<legend class="col-form-label col-sm-2 pt-0">Language Selection</legend>
      				<div class="col-sm-10">
      					<div class="form-group form-check">
      						<input className="form-check-input" type="radio" name="language"
      							id="languageBengali" value="ben" checked={true} /> <label
      							className="form-check-label" htmlFor="languageBengali">
      							Bengali </label>
      					</div>
      					<div class="form-group form-check">
      						<input className="form-check-input" type="radio" name="language"
      							id="languageEnglish" value="eng" /> <label
      							className="form-check-label" htmlFor="languageEnglish">
      							English </label>
      					</div>
      				</div>
      			</div>
      		</fieldset>
      		<div class="input-group mb-3">
      			<div class="input-group-prepend">
      				<span class="input-group-text" id="inputGroupFileAddon01">Upload</span>
      			</div>
      			<div class="custom-file">
      				<input type="file" class="custom-file-input" id="inputGroupFile01"
      					aria-describedby="inputGroupFileAddon01" /> <label
      					class="custom-file-label" for="inputGroupFile01">Choose file</label>
      			</div>
      		</div>
      		<button type="submit" class="btn btn-primary">Submit</button>
      	</form>
    );
  }

}

export default ImageUploader;
