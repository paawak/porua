import React, { Component } from "react";

const $ = window.$;

class BanglaTextBox extends Component {

  constructor(props) {
    super(props);
    this.state = {};
  }

  componentDidMount(){
    $('input[class="banglaText"]').bangla({enable: true});
  }
  
  render() {
    return (
      <input type='text' className="banglaText" name={this.props.name} id={this.props.id} placeholder={this.props.placeholder}/> 
    )
  }

}

export default BanglaTextBox;
