
import { NativeModules } from 'react-native';

const { RNWifiAndHotspotWizard } = NativeModules;

class WifiWizard {
    static turnOnWifi = () => RNWifiAndHotspotWizard.turnOnWifi();
    static turnOffWifi = () => RNWifiAndHotspotWizard.turnOffWifi();
    static isWifiEnabled = () => RNWifiAndHotspotWizard.isWifiEnabled();
    static getNearbyNetworks = () => RNWifiAndHotspotWizard.startScan();
    static connectToNetwork = (network,SSID,password) => RNWifiAndHotspotWizard.connectToNetwork(network,SSID,password);
    static disconnectFromNetwork = () => RNWifiAndHotspotWizard.disconnectFromNetwork();
}

class HotspotWizard {
    static turnOnHotspot = (SSID,Password) =>{
        WifiWizard.turnOffWifi();
        return RNWifiAndHotspotWizard.turnOnHotspot(SSID,Password);
    } 
    static getHostAddress = () => RNWifiAndHotspotWizard.getHostAddress();
    static isHotspotEnabled = () => RNWifiAndHotspotWizard.isHotspotEnabled()
    static turnOffHotspot = () => RNWifiAndHotspotWizard.turnOffHotspot();
}
export {
    WifiWizard,
    HotspotWizard,
    RNWifiAndHotspotWizard,
}