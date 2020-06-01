import React, { Component } from "react";
const $ = window.$;

class BanglaTextBox extends Component {

  componentDidMount(){
    this.$el = $(this.el);
    $('input[type="text"]').click(function() {
      console.log("aaaaa");      
    }); 
    $('input[type="text"]').bangla({enable: true});
  }
  
  render(){
    return (
      <div>
        <h3>Choose date!</h3>
        <input type='text'  ref={el => this.el = el}  />
      </div>    
    )
  }

}

export default BanglaTextBox;
