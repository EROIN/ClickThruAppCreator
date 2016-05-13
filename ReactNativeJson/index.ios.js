/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */

import React, {
  AppRegistry,
  Component,
  StyleSheet,
  Text,
  View,
  Navigator
} from 'react-native';

var MainScreenComponent=require('./MainComponent.js');
class ReactNativeJson extends Component {
  render() {
    return (
        <Navigator
          initialRoute={{name:'landing'}}
          renderScene={this.renderNavigatorFunction}
        />
    );
  }

  renderNavigatorFunction(route, navigator)
  {
    if(route.name=='landing')
      return <MainScreenComponent {...route.passProps} navigator={navigator}/>
  }
}


AppRegistry.registerComponent('ReactNativeJson', () => ReactNativeJson);
