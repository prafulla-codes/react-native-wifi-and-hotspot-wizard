import React,{useState, useEffect} from 'react';
import {
  SafeAreaView,
  StyleSheet,
  ScrollView,
  View,
  Text,
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
import NearbyDevices from '../components/NearbyDevices';
import ConnectToNetwork from '../components/ConnectToNetwork';
import { Dimensions } from 'react-native';
const Home = () =>{
    const win = Dimensions.get('window');

    // * State Variables
    let [GetNearbyNetworksModalState,showGetNearbyNetworksModal] = useState(false);
    let [ConnectToNetworkModalState,showConnectToNetworkModal] = useState(false);

    // * Updates The Nearby Devices Whenever Get Nearby Devices Modal is Open
 

    let HotspotSSID,HotspotPassword;
    return (<ScrollView scrollEnabled={true} style={{padding:15}}>
        <Text style={style.text}>Here you can checkout the various functions provided by this library.</Text>
        <Text/>
        <Text style={{fontSize:20,fontWeight:'bold',marginBottom:5}}> Test Wifi Wizard : </Text>
        <Button style={{backgroundColor:'#00e676',borderWidth:0,elevation:5}} onPress={()=>{turnOnWifi()}} >
          <View >
            <Text style={style.buttonText}> TURN ON WIFI</Text>
          </View>
        </Button>
        <Button style={{backgroundColor:'#e57373',borderWidth:0,elevation:5}}  onPress={()=>{turnOffWifi()}} >
          <View >
            <Text style={style.buttonText}> TURN OFF WIFI</Text>
          </View>
        </Button>
        <Button style={{backgroundColor:'#42a5f5',borderWidth:0,elevation:5}} onPress={()=>{isWifiEnabled()}}>
          <View >
            <Text style={style.buttonText}> IS WIFI ENABLED?</Text>
          </View>
        </Button>
        <Button style={{backgroundColor:'#9575cd',borderWidth:0,elevation:5}} onPress={()=>{isReadyForCommunication()}}>
          <View >
            <Text style={style.buttonText}> IS READY FOR COMMUNICATION?</Text>
          </View>
        </Button>
        <Button style={{backgroundColor:'#ffb74d',borderWidth:0,elevation:5}} onPress={()=>{disconnectFromNetwork()}}>
          <View >
            <Text style={style.buttonText}> DISCONNET FROM NETWORK</Text>
          </View>
        </Button>
        <Button style={{backgroundColor:'#0091ea',borderWidth:0,elevation:5}} onPress={()=>{getNearbyNetworks()}}>
          <View >
            <Text style={style.buttonText}> GET NEARBY NETWORKS</Text>
          </View>
        </Button>
        <Button style={{backgroundColor:'#ffeb3b',borderWidth:0,elevation:5}} onPress={()=>{connectToNetwork()}}>
          <View >
            <Text style={style.buttonText}> CONNECT TO NETWORK</Text>
          </View>
        </Button>
      <Modal isVisible={GetNearbyNetworksModalState} style={{justifyContent: 'flex-end',
    margin: 0,}}><View style={{height:win.height/2 ,backgroundColor:'#fff',padding:15}}>
              <NearbyDevices/>
            
              <Button style={{backgroundColor:'#212121',width: '100%', 
      height: 50, 
      left:12,
      justifyContent: 'center', 
      alignItems: 'center',
      position: 'absolute',
      bottom: 0}} onPress={()=>{
        showGetNearbyNetworksModal(false)
      }}> 
      <View><Text style={style.headerText}> Close </Text></View>
      </Button>
      </View></Modal>
      <Modal isVisible={ConnectToNetworkModalState} style={{justifyContent: 'flex-end',
    margin: 0,}}>
      <ConnectToNetwork showConnectToNetworkModal={showConnectToNetworkModal}/>
      
      </Modal></ScrollView>
      );

      function turnOnWifi(){
        WifiWizard.turnOnWifi().then(()=>{
          Toast.show('WiFi is now ACTIVE')
        });
      }
      function turnOffWifi(){
        WifiWizard.turnOffWifi().then(()=>{
          Toast.show('WiFi is now INACTIVE')
        });
      }
      function isWifiEnabled(){
        WifiWizard.isWifiEnabled().then((status)=>{
          if(status){
            Toast.show('WiFi is ENABLED')
          }
          else
          {
            Toast.show('WiFi is DISABLED')
          }
        });
      }
      function getNearbyNetworks(){
        showGetNearbyNetworksModal(true);
      }
      function connectToNetwork(){
        showConnectToNetworkModal(true);
      }
      function disconnectFromNetwork(){
        WifiWizard.disconnectFromNetwork().then(()=>{
          Toast.show('Disconnected From Current Network.')
        }).catch(err=>{
          Toast.show('Failed To Disconnect.')

        })
      }
      function isReadyForCommunication(){
        WifiWizard.isReadyForCommunication().then(status=>{
          if(status){
            Toast.show('WiFi is Ready For Communication')
          }
          else
          {
            Toast.show('WiFi is Not Ready For Communication')
          }
        })
      }
}

export default Home;