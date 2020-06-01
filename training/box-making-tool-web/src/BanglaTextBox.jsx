import React, { Component } from "react";
import $ from "jquery";


class BanglaTextBox extends Component {

  componentDidMount(){
    this.$el = $(this.el);
    //$('input[type="text"]').bangla(); 
    $('input[type="text"]').click(function() {
      console.log("aaaaa");      
    }); 
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
