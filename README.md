
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


## Usage
```javascript
import RNWifiAndHotspotWizard from 'react-native-wifi-and-hotspot-wizard';

// TODO: What to do with the module?
RNWifiAndHotspotWizard;
```
  