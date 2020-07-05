
import React,{useState, useEffect} from 'react';
import {
  SafeAreaView,
  StyleSheet,
  ScrollView,
  View,
  Text,
  Dimensions,
  Image,
  TextInput,
  StatusBar,
} from 'react-native';
import Modal from 'react-native-modal';
import Icon from 'react-native-vector-icons/FontAwesome5';
import Button from 'apsl-react-native-button';
import style from '../assets/styles/style';
import { WifiWizard } from 'react-native-wifi-and-hotspot-wizard';
import Toast from 'react-native-simple-toast';
import { FlatList } from 'react-native-gesture-handler';


const ConnectToNetwork = (props) =>{
    let win = Dimensions.get("window");
    let WifiSSID,WifiPassword;
    let showConnectToNetworkModal = props.showConnectToNetworkModal;
    let [connected,setConnected] = useState(false);
    return (<View style={{height:win.height/2,backgroundColor:'#fff',padding:15}}>
    {connected?<View style={{alignContent:'center',alignSelf:'center',marginTop:50}}>
      <Icon name="check-circle" color="green"  size={150}></Icon>
      <Text style={{fontSize:35}}> Connected </Text>
    </View>:<><Text style={style.text}>Connect To Network </Text>  
    <Text></Text>
    <Text style={style.text}>SSID</Text>
    <TextInput style={{borderBottomColor:"#212121",borderBottomWidth:2}} placeholder="SSID" onChangeText={(text)=>{
      WifiSSID=text;
    }}></TextInput>
    <Text></Text>
    <Text style={style.text}>Password</Text>
    <TextInput secureTextEntry={true} onChangeText={(text)=>{
      WifiPassword=text;
    }} style={{borderBottomColor:"#212121",borderBottomWidth:2}} placeholder="Password"></TextInput>
    <Button style={{backgroundColor:'#00e676',width: '100%', 
    height: 50, 
    left:12,
    borderWidth:0,
    justifyContent: 'center', 
    alignItems: 'center',
    position: 'absolute',
    bottom:60}} onPress={()=>{
      Toast.show("Connecting... Please Wait");
      connectToNetwork();
    }}> 
    <View><Text style={style.headerText}> Connect </Text></View>
    </Button></>}
    
    
      <Button style={{backgroundColor:'#212121',width: '100%', 
    height: 50, 
    left:12,
    justifyContent: 'center', 
    alignItems: 'center',
    position: 'absolute',
    bottom: 0}} onPress={()=>{
    showConnectToNetworkModal(false)
    }}> 
    <View><Text style={style.headerText}> Close </Text></View>
    </Button>
    </View>)


function connectToNetwork(){
// Search For Nearby Devices
console.log("Scanning Nearby Devices")
WifiWizard.getNearbyNetworks().then(networks=>{
console.log(networks);
console.log(WifiSSID);
let network = networks.filter((network)=>{
return network.SSID==WifiSSID;
})
if(network.length<1){
console.log("network not found")
Toast.show("Network Not Found")
}
else
{
// Connect To Network
WifiWizard.connectToNetwork(network[0],WifiSSID,WifiPassword).then(data=>{
  if(data.status=="connected"){
    setConnected(true);
  }
  else
  {
    Toast.show("Failed To Connect");
  }
})
}
})
}
}

export default ConnectToNetwork;