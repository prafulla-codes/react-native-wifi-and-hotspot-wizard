
# React Native Wifi & Hotspot Wizard

## Introduction

⚙️ Configure Wifi and Hotspot Settings both using single library.

**Note** - This Library is now under development and supports only **Android** as of now.

## Getting started

`$ npm install https://github.com/Pika1998/react-native-wifi-and-hotspot-wizard --save`

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

The Wizard Will help you do all the necessary configurations
```javascript
import {Wizard} from 'react-native-wifi-and-hotspot-wizard';
```

## Turn On Wifi

Enables the Device Wifi 
```javascript
Wizard.turnOnWifi();
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
Wizard.turnOffWifi();
```

Permissions Required

Make sure to add this in your **AndroidManifest.xml** file
```xml
   <uses-permission
          android:required="true"
          android:name="android.permission.CHANGE_WIFI_STATE"/>
```