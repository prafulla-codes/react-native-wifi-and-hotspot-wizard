
import { NativeModules,PermissionsAndroid } from 'react-native';

const { RNWifiAndHotspotWizard } = NativeModules;

class WifiWizard {
    static turnOnWifi = () => RNWifiAndHotspotWizard.turnOnWifi();
    static turnOffWifi = () => RNWifiAndHotspotWizard.turnOffWifi();
    static isWifiEnabled = () => RNWifiAndHotspotWizard.isWifiEnabled();
    static getNearbyNetworks = async () =>{
       let devices =  await RNWifiAndHotspotWizard.startScan();
       return JSON.parse(devices);
    } 
    static connectToNetwork = async (network,SSID,password) =>{
       let message = await RNWifiAndHotspotWizard.connectToNetwork(JSON.stringify(network),SSID,password);
       return JSON.parse(message);
    } 
    static disconnectFromNetwork = () => RNWifiAndHotspotWizard.disconnectFromNetwork();
    static isReadyForCommunication = () => RNWifiAndHotspotWizard.isReadyForCommunication();
}

class HotspotWizard {
    static turnOnHotspot = async (SSID,Password) =>{
        WifiWizard.turnOffWifi();
        let status = await  RNWifiAndHotspotWizard.turnOnHotspot(SSID,Password);
        return JSON.parse(status);
    } 
    static isHotspotEnabled = () => RNWifiAndHotspotWizard.isHotspotEnabled()
    static turnOffHotspot = async () =>{
        let message = await RNWifiAndHotspotWizard.turnOffHotspot();
        return JSON.parse(message);
    } 
}
export {
    WifiWizard,
    HotspotWizard,
    RNWifiAndHotspotWizard,
}