import React, { Component } from "react";

const $ = window.$;

class BanglaTextBox extends Component {

  componentDidMount(){
    this.$el = $(this.el);
    $('input[class="banglaText"]').bangla({enable: true});
  }
  
  render() {
    return (
      <input type='text' className="banglaText"  ref={el => this.el = el}  /> 
    )
  }

}

export default BanglaTextBox;
