import React, { Component } from "react";

const $ = window.$;

class BanglaTextBox extends Component {

  componentDidMount(){
    $('input[class="banglaText"]').bangla({enable: true});
  }
  
  render() {
    return (
      <input type='text' className="banglaText" /> 
    )
  }

}

export default BanglaTextBox;
