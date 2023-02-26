import React,{useState} from 'react';
import {
  ScrollView,
  View,
  Text,
  ToastAndroid
} from 'react-native';
import Modal from 'react-native-modal'
import Button from 'apsl-react-native-button';
import style from '../assets/styles/style';
import { WifiWizard, HotspotWizard } from 'react-native-wifi-and-hotspot-wizard';
import NearbyDevices from '../components/NearbyDevices';
import ConnectToNetwork from '../components/ConnectToNetwork';
import { Dimensions } from 'react-native';
import TurnOnHotspot from '../components/TurnOnHotspot';
const Home = () =>{
    const win = Dimensions.get('window');

    // * State Variables
    let [GetNearbyNetworksModalState,showGetNearbyNetworksModal] = useState(false);
    let [ConnectToNetworkModalState,showConnectToNetworkModal] = useState(false);
    let [TurnOnHotspotModalState,showTurnOnHotspotModal] = useState(false);

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
        <Text style={{fontSize:20,fontWeight:'bold',marginBottom:5}}> Test Hotspot Wizard : </Text>
        <Button style={{backgroundColor:'#00e676',borderWidth:0,elevation:5}} onPress={()=>{turnOnHotspot()}} >
          <View >
            <Text style={style.buttonText}> TURN ON HOTSPOT</Text>
          </View>
        </Button>
        <Button style={{backgroundColor:'#e57373',borderWidth:0,elevation:5}}  onPress={()=>{turnOffHotspot()}} >
          <View >
            <Text style={style.buttonText}> TURN OFF HOTSPOT</Text>
          </View>
        </Button>
        <Button style={{backgroundColor:'#42a5f5',borderWidth:0,elevation:5}} onPress={()=>{isHotspotEnabled()}}>
          <View >
            <Text style={style.buttonText}> IS HOTSPOT ENABLED?</Text>
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
      <Modal isVisible={TurnOnHotspotModalState} style={{justifyContent: 'flex-end',
    margin: 0,}}>
              <TurnOnHotspot showTurnOnHotspotModal={showTurnOnHotspotModal}/>  
  </Modal>
      <Modal isVisible={ConnectToNetworkModalState} style={{justifyContent: 'flex-end',
    margin: 0,}}>
      <ConnectToNetwork showConnectToNetworkModal={showConnectToNetworkModal}/>
      
      </Modal></ScrollView>
      );

      function turnOnWifi(){
        WifiWizard.turnOnWifi().then(()=>{
          ToastAndroid.show('WiFi is now ACTIVE',ToastAndroid.SHORT)
        });
      }
      function turnOffWifi(){
        WifiWizard.turnOffWifi().then(()=>{
          ToastAndroid.show('WiFi is now INACTIVE',ToastAndroid.SHORT)
        });
      }
      function isWifiEnabled(){
        WifiWizard.isWifiEnabled().then((status)=>{
          if(status){
            ToastAndroid.show('WiFi is ENABLED',ToastAndroid.SHORT)
          }
          else
          {
            ToastAndroid.show('WiFi is DISABLED',ToastAndroid.SHORT)
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
          ToastAndroid.show('Disconnected From Current Network.',ToastAndroid.SHORT)
        }).catch(err=>{
          ToastAndroid.show('Failed To Disconnect.',ToastAndroid.SHORT)

        })
      }
      function isReadyForCommunication(){
        WifiWizard.isReadyForCommunication().then(status=>{
          if(status){
            ToastAndroid.show('WiFi is Ready For Communication',ToastAndroid.SHORT)
          }
          else
          {
            ToastAndroid.show('WiFi is Not Ready For Communication',ToastAndroid.SHORT)
          }
        })
      }
      function turnOnHotspot(){
        showTurnOnHotspotModal(true);
      }
      function turnOffHotspot(){
        HotspotWizard.turnOffHotspot().then(data=>{
          if(data.status=="failed"){
            ToastAndroid.show("Failed to turn off hotspot. Note That, this can only turn off hotspot which is started using this library.",ToastAndroid.LONG)
          }
          if(data.status=="success"){
            ToastAndroid.show("Turned Off Hotspot.",ToastAndroid.SHORT)
          }
        }).catch(err=>{
          ToastAndroid.show("Something went wrong..",ToastAndroid.SHORT)
        })
      }
      function isHotspotEnabled(){
        HotspotWizard.isHotspotEnabled().then(status=>{
          if(status){
            ToastAndroid.show("Hotspot Is Enabled",ToastAndroid.SHORT)
          }
          else
          {
            ToastAndroid.show("Hotspot Is Disabled",ToastAndroid.SHORT)
          }
        })
      }
}

export default Home;