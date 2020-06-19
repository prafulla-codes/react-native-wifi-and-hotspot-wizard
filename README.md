<p align="center"> <img width="100%" alt="Cover of Abell" src="https://res.cloudinary.com/prafulla98/image/upload/v1592410787/React%20Native%20Wifi%20and%20Hotspot%20Wizard/Frame_1banner_bkhwf3.png?"/> </p>
<p align="center">
<a href=""><img src="https://img.shields.io/badge/Contributions-Welcome-blueviolet?style=for-the-badge&logo=github&logoColor=white&labelColor=black" alt="Contributions"/></a>
<a href=""><img src="https://img.shields.io/badge/SLACK-JOIN%20SLACK-blueviolet?logo=Slack&labelColor=black&style=for-the-badge" alt="Slack"/></a>
<img src="https://img.shields.io/badge/ANDROID-SUPPORTED-brightgreen?style=for-the-badge&logo=android&labelColor=black" alt="android"/>
<a href="https://www.npmjs.com/package/react-native-wifi-and-hotspot-wizard"><img src="https://img.shields.io/npm/v/react-native-wifi-and-hotspot-wizard?color=%23C33F14&logo=npm&style=for-the-badge&labelColor=black" alt="npm"></a>
<a href="https://www.npmjs.com/package/react-native-wifi-and-hotspot-wizard"><img src="https://img.shields.io/npm/dt/react-native-wifi-and-hotspot-wizard?color=brightgreen&logo=npm&style=for-the-badge&labelColor=black" alt="Downloads"></a>
</p>

# React Native Wifi & Hotspot Wizard

 ⚙️ Configure **Wifi and Hotspot** Settings both using single library. 

  This library also **automatically deals with run-time permission management**.

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

Wizards Will help you do all the necessary configurations

##  Wizards
1. **WifiWizard** (for Wifi Related Configurations)
2. **HotspotWizard** (for Hotspot Related Configurations)

## Importing Wizards
```javascript
import {WifiWizard,HotspotWizard} from 'react-native-wifi-and-hotspot-wizard';
```

# WifiWizard Usage

## turnOnWifi()

Enables the Device Wifi 
```javascript
WifiWizard.turnOnWifi();
```


**REQUIRED PERMISSIONS**

Make sure your **AndroidManifest.xml** file has the following permissions.

```xml
   <uses-permission
          android:required="true"
          android:name="android.permission.CHANGE_WIFI_STATE"/>
```
## turnOffWifi()
Disables the Device Wifi 
```javascript
WifiWizard.turnOffWifi();
```

**REQUIRED PERMISSIONS**

Make sure your **AndroidManifest.xml** file has the following permissions.
```xml
   <uses-permission
          android:required="true"
          android:name="android.permission.CHANGE_WIFI_STATE"/>
```

## isWifiEnabled()

Checks the state of the Wifi and Returns the status

```javascript
WifiWizard.isWifiEnabled().then(status=>{
  console.log(status)
});
```

**OUTPUT**

Return type : **Boolean**

```javascript
>>> true 
```



## getNearbyNetworks()

Scans for nearby networks and returns a JSON stringified list of the results.
```javascript
WifiWizard.getNearbyNetworks().then(data=>{
  let devices = JSON.parse(data);
  console.log(devices);
});
```

**REQUIRED PERMISSIONS**

Make sure your **AndroidManifest.xml** file has the following permissions.
```xml
<uses-permission
    android:required="true"
    android:name="android.permission.CHANGE_WIFI_STATE"/>
<uses-permission
    android:required="true"
    android:name="android.permission.ACCESS_COARSE_LOCATION"/>
```

## connectToNetwork()

Connect to a Wifi Network in range

**INPUT**
```
SSID - Name of the network you wish to connect to.

Password - Secret Key.
```
**USAGE**
```javascript
WifiWizard.connectToNetwork(SSID,password).then((status)=>{
  if(status=="connected"){
    // Further Tasks
  }
}).catch(err => console.log(err))

```

## disconnectFromNetwork()

Disconnect from existing network

**USAGE**
```javascript
WifiWizard.disconnectFromNetwork().then(status=>{
  if(status==true){
    // Disconnected succesfully.
  }
  else
  {
    // Failed to disconnect.
  }
})
```

# HotspotWizard Usage

## turnOnHotspot() 

Enables Mobile Hotspot

**Inputs**
```
  SSID - The name of the network you want to set.

  Password - The secret Password you want to set.
```

**Output**

JSON Stringified Object

**For Android Version < 8** 
```
{status : "success"}
OR
{status : "success"}
```

**For Android Version > 8**

In Android > 8, Hotspot SSID and Password cannot be set programatically. Hence the Wizard will return the randomly generated credentials back to the user

```
{
status : "success",
SSID:" <Randmoly Generated SSID> ",
password: " <Randomly Generated Password">
}
```

**USAGE**

```javascript

HotspotWizard.turnOnHotspot("John Doe Network","helloworld").then(data=>{
  let jsonData = JSON.parse(data);
  let status = jsonData.status;
  if(status=="success"){
    // Hotspot Enabled Successfully with custom credentials.
  }
  else if(status=="auth"){
    // Hotspot Enabled Successfully with random credentials.
    console.log(jsonData.SSID);
    console.log(jsonData.status);
  }
}).catch(err=>console.log(err))
```
**PERMISSIONS**

All Runtime Permissions are managed by the library and will be asked when required.

Make sure your **AndroidManifest.xml** file has the following permissions.
```xml
<uses-permission
    android:required="true"
    android:name="android.permission.ACCESS_FINE_LOCATION"/>
<uses-permission
    android:required="true"
    android:name="android.permission.CHANGE_WIFI_STATE"/>
<uses-permission
    android:required="true"
    android:name="android.permission.ACCESS_COARSE_LOCATION"/>
```

## turnOffHotspot() 

Disables the mobile hotspot and restores previous wifi configuration.

**OUTPUT**

JSON Stringified Object

```
{status : "success"}
OR
{status : "success"}
```

**USAGE**

```javascript
HotspotWizard.turnOffHotspot().then(data=>{
  let jsonData = JSON.parse(data);
  let status = jsonData.status;
  if(status=="success"){
    // Hotspot Disabled Successfully
  }
  else
  {
    // Failed to disabled Hotspot.
  }
})
```
**PERMISSIONS**

All Runtime Permissions are managed by the library and will be asked when required.

Make sure your **AndroidManifest.xml** file has the following permissions.
```xml
<uses-permission
    android:required="true"
    android:name="android.permission.ACCESS_FINE_LOCATION"/>
<uses-permission
    android:required="true"
    android:name="android.permission.CHANGE_WIFI_STATE"/>
<uses-permission
    android:required="true"
    android:name="android.permission.ACCESS_COARSE_LOCATION"/>
```
## isHotspotEnabled()

Returns the status of Mobile Hotspot

**OUTPUT**

Boolean Value

```javascript
true / false
```

**USAGE**

```javascript
HotspotWizard.isHotspotEnabled().then(status=>{
  if(status){
    // Hotspot is Enabled.
  }
  else
  {
    // Hotspot is Disabled.
  }
})
```
