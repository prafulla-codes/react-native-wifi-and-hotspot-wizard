
import { NativeModules } from 'react-native';

const { RNWifiAndHotspotWizard } = NativeModules;

class WifiWizard {
    static turnOnWifi = () => RNWifiAndHotspotWizard.turnOnWifi();
    static turnOffWifi = () => RNWifiAndHotspotWizard.turnOffWifi();
    static isWifiEnabled = () => RNWifiAndHotspotWizard.isWifiEnabled();
    static getNearbyDevices = () => RNWifiAndHotspotWizard.startScan();
}

class HotspotWizard {
    static turnOnHotspot = (SSID,Password) =>{
        WifiWizard.turnOffWifi();
        return RNWifiAndHotspotWizard.turnOnHotspot(SSID,Password);
    } 
    static turnOffHotspot = () => RNWifiAndHotspotWizard.turnOffHotspot();
}
export {
    WifiWizard,
    HotspotWizard,
    RNWifiAndHotspotWizard,
}