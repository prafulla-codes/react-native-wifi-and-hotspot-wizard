
import { NativeModules } from 'react-native';

const { RNWifiAndHotspotWizard } = NativeModules;

class WifiWizard {
    static turnOnWifi = () => RNWifiAndHotspotWizard.turnOnWifi();
    static turnOffWifi = () => RNWifiAndHotspotWizard.turnOffWifi();
    static isWifiEnabled = () => RNWifiAndHotspotWizard.isWifiEnabled();
    static getNearbyDevices = () => RNWifiAndHotspotWizard.startScan();
}

class HotspotWizard {
    static turnOnHotspot = () =>{
        WifiWizard.turnOffWifi();
        return RNWifiAndHotspotWizard.turnOnHotspot();
    } 
}
export {
    WifiWizard,
    HotspotWizard,
    RNWifiAndHotspotWizard,
}