import React, { Component } from "react";

class ImageProcessingInProgress extends Component {
  
  render() {
    return (
      <button className="btn btn-primary btn-lg btn-block" type="button" disabled>
        <span className="spinner-border spinner-border-sm float-left" role="status" aria-hidden="true"></span>
        Please wait while we analyse the uploaded image...
      </button>
    )
  }

}

export default ImageProcessingInProgress;
