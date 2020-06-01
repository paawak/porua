import React, { Component } from "react";
import { render } from "react-dom";
import $ from "jquery";

class BanglaTextBox extends Component {
  componentDidMount() {
    $('input[type="text"]').click(function() {
      console.log("aaaaa");      
    }); 
    //$('input[type="text"]').bangla();//not working
  }
  render() {
    return (
      <input type="text" id="banglaText"/>
    );
  }
}

render(<BanglaTextBox />, document.getElementById("root"));

export default BanglaTextBox;
