import React from 'react';

class BanglaTextBox extends React.Component {

  constructor(props) {
    super(props);
    this.handleKeyEvent = this.handleKeyEvent.bind(this);
    this.state = {};
  }

  handleKeyEvent(keyEvent) {
    if (keyEvent.metaKey) {
      console.log("Metachar");
      return;
    }
    console.log("char: " + keyEvent.keyCode );

    //keyEvent.key = '\u0995';
    
  }

  render() {
    return (
      <input type="text" onKeyUp={this.handleKeyEvent}/>
    );
  }

}

export default BanglaTextBox;
