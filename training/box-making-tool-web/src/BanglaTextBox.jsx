import React, { Component } from "react";

const $ = window.$;

class BanglaTextBox extends Component {

  constructor(props) {
    super(props);
    this.state = {};
  }

  componentDidMount(){
    const jqueryIdSelector = "#" + this.props.id;
    $(jqueryIdSelector).bangla({enable: true});
  }
  
  render() {
    return (
      <input type='text' className="banglaText" name={this.props.name} id={this.props.id} 
        placeholder={this.props.placeholder} 
        disabled={this.props.disabled}
        onBlur={this.props.onBlur}        
        /> 
    )
  }

}

export default BanglaTextBox;
