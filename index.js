
import { NativeModules } from 'react-native';

const { RNWifiAndHotspotWizard } = NativeModules;

class Wizard {
    static greet = () => RNWifiAndHotspotWizard.greet();
    static turnOnWifi = () => RNWifiAndHotspotWizard.turnOnWifi();
    static turnOffWifi = () => RNWifiAndHotspotWizard.turnOffWifi();
}
export {
    Wizard,
    RNWifiAndHotspotWizard,
}