import React,{useState} from 'react';
import {
  View,
  Text,
} from 'react-native';
import Icon from 'react-native-vector-icons/FontAwesome5';
import style from '../assets/styles/style';
import { WifiWizard } from 'react-native-wifi-and-hotspot-wizard';
import { FlatList } from 'react-native-gesture-handler';

let NearbyDevices = () =>{
    let [nearbyNetworksList,setNearbyNetworks] = useState([]);
   

        WifiWizard.getNearbyNetworks().then(devices=>{
            // List Of Devices [Containing Only SSID]
            let devices_list = devices.map(device=>{
                return device.SSID;
            })
            setNearbyNetworks(devices_list);
            }).catch(err=>{
            console.log(err)
            })
   
    return (
        <View>
        <Text style={style.text}>Nearby Networks </Text>
        {nearbyNetworksList.length>=1?
        <FlatList 
        data={nearbyNetworksList}
        renderItem={({item,index})=>(
          <View key={index} style={{flexDirection:'row',marginTop:20}}>
            <Icon name="wifi" solid={true} color="#212121" size={14}></Icon><Text style={{color:'#5A4C98',fontWeight:'bold',fontSize:16,textAlignVertical:'bottom'}}> {item} </Text>
          </View>
        )}
        ></FlatList>:<Text style={{marginBottom:15}}>Scanning for networks...</Text>}
        </View>
    )
}

export default NearbyDevices;