# ⏱️ CHANGELOG

## v1.0.5 (STABLE)
- Hotspot now turns on with WPA2 security enabled. (Issue : [#4](https://github.com/Pika1998/react-native-wifi-and-hotspot-wizard/issues/4))

## v1.0.4 (STABLE) [Breaking Changes]
- **All methods return JavaScript Object instead of stringified javascript. (IMPORTANT)** 
- Added isReadyForCommunication() method in WifiWizard

Please Check The API  [Documentation](https://react-native-wifi-and-hotspot-wizard.netlify.app/getting-started/]) for latest usage

## v1.0.3 (STABLE) [Major Change]

- connectToNetwork() usage changed
- connectToNetwork() is now only meant to be used in context with getNearbyNetworks(). 
 (<a href="https://react-native-wifi-and-hotspot-wizard.netlify.app/docs/wifi-wizard-api/#connecttonetwork--">https://react-native-wifi-and-hotspot-wizard.netlify.app/docs/wifi-wizard-api/#connecttonetwork--</a>)

## v1.0.2 (STABLE)

- Fixed WifiReceiver Bug.
## v1.0.1 (BROKEN)

- Improved Permission Management
- Added Helper Java Methods

## v1.0.0-release

**Added Functionalities:** 
- connectToNetwork()
- disconnectFromNetwork()

**Renamed getNearbyDevices() to getNearbyNetworks()**
## v1.0.0-beta.0

- Core Functionalites
  - turnOnWifi()
  - turnOffWifi()
  - turnOnHotspot()
  - turnOffHotspot()
  - getNearbyDevices()
  - isWifiEnabled()
