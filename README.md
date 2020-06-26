<p align="center"> <img width="100%" alt="Cover of Abell" src="https://res.cloudinary.com/prafulla98/image/upload/v1592410787/React%20Native%20Wifi%20and%20Hotspot%20Wizard/Frame_1banner_bkhwf3.png?"/> </p>
<p align="center">
<a href="CONTRIBUTING.md"><img src="https://img.shields.io/badge/Contributions-Welcome-blueviolet?style=for-the-badge&logo=github&logoColor=white&labelColor=black" alt="Contributions"/></a>
<a href="https://join.slack.com/t/wifihotspotwizard/shared_invite/zt-f2mmp8p3-EIlxb~FOlNd3FaCgP6UZkQ"><img src="https://img.shields.io/badge/SLACK-JOIN%20SLACK-blueviolet?logo=Slack&labelColor=black&style=for-the-badge" alt="Slack"/></a>
<img src="https://img.shields.io/badge/ANDROID-SUPPORTED-brightgreen?style=for-the-badge&logo=android&labelColor=black" alt="android"/>
<a href="https://www.npmjs.com/package/react-native-wifi-and-hotspot-wizard"><img src="https://img.shields.io/npm/v/react-native-wifi-and-hotspot-wizard?color=%23C33F14&logo=npm&style=for-the-badge&labelColor=black" alt="npm"></a>
<a href="https://www.npmjs.com/package/react-native-wifi-and-hotspot-wizard"><img src="https://img.shields.io/npm/dt/react-native-wifi-and-hotspot-wizard?color=brightgreen&logo=npm&style=for-the-badge&labelColor=black" alt="Downloads"></a>
</p>

# React Native Wifi & Hotspot Wizard

 ⚙️ Configure both **Wifi and Hotspot** settings using a single library. 

  This library also **automatically deals with run-time permission management**.


<a href="https://react-native-wifi-and-hotspot-wizard.netlify.app" target="_blank">
<img 
height="100"     src="https://res.cloudinary.com/prafulla98/image/upload/v1592631858/React%20Native%20Wifi%20and%20Hotspot%20Wizard/Frame_4get_started_button_fvscwx.png"></a>

<a href="CONTRIBUTING.md">

<img 
height="100"  src="https://res.cloudinary.com/prafulla98/image/upload/v1592633367/React%20Native%20Wifi%20and%20Hotspot%20Wizard/Frame_6contribute_fhvbah.png">
</p>
</a>

# CHANGELOG

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
