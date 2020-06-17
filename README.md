
# React Native Wifi & Hotspot Wizard

## Introduction

⚙️ Configure **Wifi and Hotspot** Settings both using single library. 

This library also **automatically deals with run-time permission management**.

**Note** - This Library is now under development and supports only **Android** as of now.

## Getting started

`$ npm i react-native-wifi-and-hotspot-wizard`

### Mostly automatic installation

`$ react-native link react-native-wifi-and-hotspot-wizard`


## Manual Installation ( If Automatic not working )

#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import com.reactlibrary.RNWifiAndHotspotWizardPackage;` to the imports at the top of the file
  - Add `new RNWifiAndHotspotWizardPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-wifi-and-hotspot-wizard'
  	project(':react-native-wifi-and-hotspot-wizard').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-wifi-and-hotspot-wizard/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-wifi-and-hotspot-wizard')
  	```


# Usage

Wizards Will help you do all the necessary configurations

##  Wizards
1. ###  **WifiWizard** (for Wifi Related Configurations)
2. ### **HotspotWizard** (for Hotspot Related Configurations)

## Importing Wizards
```javascript
import {WifiWizard,HotspotWizard} from 'react-native-wifi-and-hotspot-wizard';
```

# WifiWizard Usage

## Turn On Wifi

Enables the Device Wifi 
```javascript
WifiWizard.turnOnWifi();
```

Permissions Required

Make sure to add this in your **AndroidManifest.xml** file
```xml
   <uses-permission
          android:required="true"
          android:name="android.permission.CHANGE_WIFI_STATE"/>
```
## Turn Off Wifi
Disables the Device Wifi 
```javascript
WifiWizard.turnOffWifi();
```

Permissions Required

Make sure to add this in your **AndroidManifest.xml** file
```xml
   <uses-permission
          android:required="true"
          android:name="android.permission.CHANGE_WIFI_STATE"/>
```

## Check Wifi State

checks the state of wifi and returns status (boolean)
Disables the Device Wifi 

```javascript
WifiWizard.isWifiEnabled();
```



## Get Nearby Devices

Scans for nearby devices and returns the list as JSON String
```javascript
WifiWizard.getNearbyDevices().then(data=>{
  let devices = JSON.parse(devices);
  console.log(devices);
});
```

Permissions Required


Make sure to add this in your **AndroidManifest.xml** file
```xml
   <uses-permission
          android:required="true"
          android:name="android.permission.ACCESS_COARSE_LOCATION"/>
```