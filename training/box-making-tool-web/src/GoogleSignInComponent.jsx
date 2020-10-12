import React, { Component } from 'react'
import { GoogleLogin } from 'react-google-login';


const CLIENT_ID = '955630342713-55eu6b3k5hmsg8grojjmk8mj1gi47g37.apps.googleusercontent.com';


class GoogleSignInComponent extends Component {
   constructor(props) {
    super(props);    
  }

  render() {
    return (
    <div>
      <GoogleLogin
          clientId={ CLIENT_ID }
          buttonText='Login'
          onSuccess={ this.props.loginSuccess }
          onFailure={ this.props.handleLoginFailure }
          cookiePolicy={ 'single_host_origin' }
          responseType='code,token'
        />
    </div>
    )
  }
}

export default GoogleSignInComponent;
