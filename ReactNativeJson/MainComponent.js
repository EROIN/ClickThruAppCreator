import React, {
  AppRegistry,
  Component,
  StyleSheet,
  Text,
  View,
  Navigator,
  Image,
  TouchableOpacity
} from 'react-native';
var styles=require('./mainComponent.styles');

module.exports=React.createClass({
  jsonData:require('./test.json'),

  getDefaultProps:function()
  {
    var jsonData=require('./test.json');
    return({
      initialKey:jsonData.startImageKey
    });
  },

  getInitialState:function()
  {
    var objectData;
    console.log("sdsdsdsdsd",this.jsonData[this.props.initialKey]);
    if(this.jsonData[this.props.initialKey]==undefined)
    objectData={image:this.props.initialKey,hotspots:[]};
    else
    objectData=this.jsonData[this.props.initialKey];
    return({
      object:objectData
    });
  },

  render:function()
  {
    // console.log(this.state.image.slice(0,-4));
    return(
      <View style={styles.container}>
        <Image source={{uri:this.state.object.image}} style={styles.image}>
          {this.generateHotSpots()}
        </Image>
      </View>
    );
  },

  generateHotSpots:function()
  {
    var hotSpots=this.state.object.hotspots;
    // console.log(hotSpots);
    var clickables=hotSpots.map((item,index)=>{
      var style=item.style;
      style.backgroundColor='rgba(0,0,0,0.5)';
      // console.log(style);
      return(
        <TouchableOpacity style={style} onPress={()=>this.changeScreen(item.click)}>
        </TouchableOpacity>
      );
    });
    return clickables;
  },

  changeScreen:function(key)
  {
    // this.setState({object:this.jsonData[key]});
    console.log(key)
    //delete navigator push for removing navigator animations. and uncomment the first line that sets the state.
    this.props.navigator.push({
      name:'landing',
      passProps:{
        initialKey:key
      }
    });
  }

});
