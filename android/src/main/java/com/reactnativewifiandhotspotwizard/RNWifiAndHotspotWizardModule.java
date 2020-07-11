package com.reactnativewifiandhotspotwizard;

import com.facebook.react.bridge.*;
import java.lang.Object;
import androidx.core.content.ContextCompat;
import android.util.Log;
import org.json.*;
import android.app.Activity;
import android.content.ComponentName;
import android.net.Uri;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.Settings;
import java.lang.reflect.Method;
import 	android.net.NetworkInfo;
import java.lang.Exception;
import android.net.ConnectivityManager;
import android.content.Context;
import android.content.Intent;
import android.Manifest;
import android.content.pm.ActivityInfo;
import android.net.wifi.WifiConfiguration;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.net.wifi.ScanResult;
import android.widget.Toast;
import sun.security.ec.point.ProjectivePoint.Mutable;
import android.os.Build;
import java.net.Inet4Address;
import android.net.wifi.WifiManager.LocalOnlyHotspotReservation;
import androidx.core.app.ActivityCompat;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import javax.security.auth.callback.Callback;
import android.net.wifi.WifiInfo;
import android.net.wifi.SupplicantState;
public class RNWifiAndHotspotWizardModule extends ReactContextBaseJavaModule {
  WifiManager wifi;
  private final ReactApplicationContext reactContext;
  public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
  final int PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION = 1;
  final int PERMISSIONS_REQUEST_HOTSPOT = 23;
  final int PERMISSIONS_WRITE_SETTINGS=25;
  private WifiConfiguration mWifiConfiguration;
  private Object mReservation;

  // * TURN ON WIFI [Permissions - CHANGE_WIFI_STATE]
  @ReactMethod
  public void turnOnWifi(Promise promise) {
    try{
      wifi.setWifiEnabled(true);
      promise.resolve(true);
    }catch(Exception e){
      promise.reject(e.getMessage());
    }

  }

  // * TURN OFF WIFI [Permissions - CHANGE_WIFI_STATE]
  @ReactMethod
  public void turnOffWifi(Promise promise) {
    try{
      wifi.setWifiEnabled(false);
      promise.resolve(true);
    }catch(Exception e){
      promise.reject(e.getMessage());
    }
  }

  // * CHECK IF WIFI IS ENABLED [Permissions - CHANGE_WIFI_STATE]
  @ReactMethod
  public void isWifiEnabled(Promise promise) {
    boolean isWifiEnabled = wifi.isWifiEnabled();
    promise.resolve(isWifiEnabled);
  }

  // * CHECKS IF WIFI IS READY TO COMMUNICATE [Permissions - ACCESS_NETWORK_STATE]
  @ReactMethod
  public void isReadyForCommunication(Promise promise){
    ConnectivityManager connManager = (ConnectivityManager) reactContext.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

    if (mWifi.isConnected()) {
        promise.resolve(true);
    }
    else
    {
        promise.resolve(false);
    }
  }
  // * [Needs Permission WRITE_SETTINGS]
  public void writePermission() {
		Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS,
				Uri.parse("package:" + getReactApplicationContext().getPackageName()));
		getReactApplicationContext().startActivityForResult(intent, PERMISSIONS_WRITE_SETTINGS, null);
  }
  
  public Boolean isHotspotEnabled(){
    if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.O) {
      try{
        Method method = wifi.getClass().getDeclaredMethod("getWifiApState");
        method.setAccessible(true);
        int actualState = (Integer) method.invoke(wifi,null);
        if(actualState==13){
          return true;
        }
        else{
          return false;
        }
      }catch(Exception e){
        return false;
      }
    }
    else {
      if(mReservation!=null){
        return true;
      }
      else
      {
        return false;
      }
    }
  }
  @ReactMethod 
  public void isHotspotEnabled(Promise promise){
    if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.O) {
      try{
        Method method = wifi.getClass().getDeclaredMethod("getWifiApState");
        method.setAccessible(true);
        int actualState = (Integer) method.invoke(wifi,null);
        if(actualState==13){
          promise.resolve(true);
        }
        else{
          promise.resolve(false);
        }
      }catch(Exception e){
        promise.reject(e.getMessage());
      }
    }
    else {
      if(mReservation!=null){
        promise.resolve(true);
      }
      else
      {
        promise.resolve(false);
      }
    }
  }

  @ReactMethod
  public void turnOnHotspot(String SSID,String Password,Promise promise){
    if(isHotspotEnabled()){
      turnOffHotspot();
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
      int permissionAccessFineLocation = ContextCompat.checkSelfPermission(getReactApplicationContext(),Manifest.permission.ACCESS_FINE_LOCATION);
      int permissionAccessCoarseLocation = ContextCompat.checkSelfPermission(getReactApplicationContext(),Manifest.permission.ACCESS_COARSE_LOCATION);
      int permissionChangeWifiState = ContextCompat.checkSelfPermission(getReactApplicationContext(),Manifest.permission.CHANGE_WIFI_STATE);
      List<String> listPermissionsNeeded = new ArrayList<>();
      if(permissionAccessFineLocation != PackageManager.PERMISSION_GRANTED){
        listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
      }
      if(permissionAccessCoarseLocation != PackageManager.PERMISSION_GRANTED){
        listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
      }
      if(permissionChangeWifiState != PackageManager.PERMISSION_GRANTED){
        listPermissionsNeeded.add(Manifest.permission.CHANGE_WIFI_STATE);
      }
      if(!listPermissionsNeeded.isEmpty()){
        ActivityCompat.requestPermissions(getCurrentActivity(),listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
      }
    }
    if(!Settings.System.canWrite(getReactApplicationContext()))
    {
      writePermission();
    }
    if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.O) {
        try{
          Method getWifiApConfiguration = wifi.getClass().getDeclaredMethod("getWifiApConfiguration");
          Object result = getWifiApConfiguration.invoke(wifi);
          if(result != null){
            mWifiConfiguration = (WifiConfiguration) result;
          }
        }catch(Exception e){
          promise.reject(e.getMessage());
        }
        WifiConfiguration  myConfig =  new WifiConfiguration();
        myConfig.SSID=SSID;
        myConfig.preSharedKey=Password;
        myConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
        myConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
        myConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
        myConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
        Boolean bool = true;
        Object result;
        try{
          Method setWifiApEnabledMethod = WifiManager.class.getMethod("setWifiApEnabled",
          WifiConfiguration.class, boolean.class);
          result = (Boolean) setWifiApEnabledMethod.invoke(wifi,myConfig,bool);
          JSONObject obj = new JSONObject();
          if((Boolean) result){
            obj.put("status","success");
          }
          else{
            obj.put("status","failed");
          }
          promise.resolve(obj.toString());
        }catch(Exception e){
          promise.reject(e.toString());
        }
      }
      else
      {
        wifi.startLocalOnlyHotspot(new LocalOnlyHotspotCallback(promise,SSID,Password),null);
      }
  
  }

  public void turnOffHotspot(){
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
      int permissionAccessFineLocation = ContextCompat.checkSelfPermission(getReactApplicationContext(),Manifest.permission.ACCESS_FINE_LOCATION);
      int permissionAccessCoarseLocation = ContextCompat.checkSelfPermission(getReactApplicationContext(),Manifest.permission.ACCESS_COARSE_LOCATION);
      int permissionChangeWifiState = ContextCompat.checkSelfPermission(getReactApplicationContext(),Manifest.permission.CHANGE_WIFI_STATE);
      List<String> listPermissionsNeeded = new ArrayList<>();
      if(permissionAccessFineLocation != PackageManager.PERMISSION_GRANTED){
        listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
      }
      if(permissionAccessCoarseLocation != PackageManager.PERMISSION_GRANTED){
        listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
      }
      if(permissionChangeWifiState != PackageManager.PERMISSION_GRANTED){
        listPermissionsNeeded.add(Manifest.permission.CHANGE_WIFI_STATE);
      }
      if(!listPermissionsNeeded.isEmpty()){
        ActivityCompat.requestPermissions(getCurrentActivity(),listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
      }
    }
    if(!Settings.System.canWrite(getReactApplicationContext()))
    {
      writePermission();
    }
    if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.O) {
        WifiConfiguration  myConfig =  mWifiConfiguration;
        Boolean bool = false;
        Object restoreConfigResult,result;
        try{
          Method setWifiApEnabledMethod = WifiManager.class.getMethod("setWifiApEnabled",
          WifiConfiguration.class, boolean.class);
          Method setWifiApConfiguration = wifi.getClass().getMethod("setWifiApConfiguration",WifiConfiguration.class);
          restoreConfigResult = setWifiApConfiguration.invoke(wifi,myConfig);
          result = (Boolean) setWifiApEnabledMethod.invoke(wifi,myConfig,bool);
          JSONObject obj = new JSONObject();
          if((Boolean) result){
            obj.put("status","success");
          }
          else
          {
            obj.put("status","failed");
          }
        }catch(Exception e){
        }
      }
      else
      {
        if(mReservation!=null){
          WifiManager.LocalOnlyHotspotReservation mReserve = (WifiManager.LocalOnlyHotspotReservation) mReservation;
          mReserve.close();
          try{
            JSONObject obj = new JSONObject();
            obj.put("status","success");
          }catch(Exception e){
          }
        }
        else
        {
          try{
            JSONObject obj = new JSONObject();
          }catch(Exception e){  
          }
        }
      }
  }
  @ReactMethod
  public void turnOffHotspot(Promise promise){
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
      int permissionAccessFineLocation = ContextCompat.checkSelfPermission(getReactApplicationContext(),Manifest.permission.ACCESS_FINE_LOCATION);
      int permissionAccessCoarseLocation = ContextCompat.checkSelfPermission(getReactApplicationContext(),Manifest.permission.ACCESS_COARSE_LOCATION);
      int permissionChangeWifiState = ContextCompat.checkSelfPermission(getReactApplicationContext(),Manifest.permission.CHANGE_WIFI_STATE);
      List<String> listPermissionsNeeded = new ArrayList<>();
      if(permissionAccessFineLocation != PackageManager.PERMISSION_GRANTED){
        listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
      }
      if(permissionAccessCoarseLocation != PackageManager.PERMISSION_GRANTED){
        listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
      }
      if(permissionChangeWifiState != PackageManager.PERMISSION_GRANTED){
        listPermissionsNeeded.add(Manifest.permission.CHANGE_WIFI_STATE);
      }
      if(!listPermissionsNeeded.isEmpty()){
        ActivityCompat.requestPermissions(getCurrentActivity(),listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
      }
    }
    if(!Settings.System.canWrite(getReactApplicationContext()))
    {
      writePermission();
    }
    if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.O) {
        WifiConfiguration  myConfig =  mWifiConfiguration;
        Boolean bool = false;
        Object restoreConfigResult,result;
        try{
          Method setWifiApEnabledMethod = WifiManager.class.getMethod("setWifiApEnabled",
          WifiConfiguration.class, boolean.class);
          Method setWifiApConfiguration = wifi.getClass().getMethod("setWifiApConfiguration",WifiConfiguration.class);
          restoreConfigResult = setWifiApConfiguration.invoke(wifi,myConfig);
          result = (Boolean) setWifiApEnabledMethod.invoke(wifi,myConfig,bool);
          JSONObject obj = new JSONObject();
          if((Boolean) result){
            obj.put("status","success");
          }
          else
          {
            obj.put("status","failed");
          }
          promise.resolve(obj.toString());
        }catch(Exception e){
          promise.reject(e.toString());
        }
      }
      else
      {
        if(mReservation!=null){
          WifiManager.LocalOnlyHotspotReservation mReserve = (WifiManager.LocalOnlyHotspotReservation) mReservation;
          mReserve.close();
          try{
            JSONObject obj = new JSONObject();
            obj.put("status","success");
            promise.resolve(obj.toString());
          }catch(Exception e){
            promise.reject(e.getMessage());
          }
        }
        else
        {
          try{
            JSONObject obj = new JSONObject();
            obj.put("status","failed");
            promise.resolve(obj.toString());
          }catch(Exception e){
            promise.reject(e.getMessage());
          }
        }
      }
  }
 
  public class LocalOnlyHotspotCallback extends WifiManager.LocalOnlyHotspotCallback{
    Promise promise;    
    String SSID,Password;
    WifiConfiguration config;
    LocalOnlyHotspotCallback(Promise promise,String SSID,String Password){
      this.promise = promise;
      this.SSID = SSID;
      this.Password = Password;
    }
    @Override
    public void onStarted(WifiManager.LocalOnlyHotspotReservation reservation)
    {
      mReservation = (WifiManager.LocalOnlyHotspotReservation) reservation;
      config = reservation.getWifiConfiguration();
      try{
        JSONObject obj  = new JSONObject();
        obj.put("status","auth");
        obj.put("SSID",config.SSID);
        obj.put("password",config.preSharedKey);
        promise.resolve(obj.toString());
      }catch(Exception e)
      {
        promise.reject(e.getMessage());
      }

    }
    @Override
    public void onFailed(int reason){
      JSONObject obj = new JSONObject();
      try{
        obj.put("status","failed");
        promise.resolve(obj.toString());
      }catch(Exception e){
        promise.reject(e.getMessage());
      }
    }
  }
  @ReactMethod
  public void disconnectFromNetwork(Promise promise){
    try{
      boolean disconnect = wifi.disconnect();
      promise.resolve(disconnect);
    }catch(Exception e){
      promise.reject(e.getMessage());
    }
   
  }
  // Connects to an SSID.
  @ReactMethod
	public void connectToNetwork(String network,String ssid, String password,Promise promise) {

    JSONObject networkObj=null;
    JSONObject message= new JSONObject();
    try{
      networkObj = new JSONObject(network);
    }catch(Exception e){
      promise.reject(e.getMessage());
    }

    if(networkObj==null){
      try{
        message.put("status","not found");
        promise.resolve(message.toString());
      }catch(Exception e){
        promise.reject(e.getMessage());
      }
      return;
    }
		//Make new configuration
		WifiConfiguration conf = new WifiConfiguration();

    	//clear alloweds
		conf.allowedAuthAlgorithms.clear();
		conf.allowedGroupCiphers.clear();
		conf.allowedKeyManagement.clear();
		conf.allowedPairwiseCiphers.clear();
		conf.allowedProtocols.clear();

    // Quote ssid and password
		conf.SSID = String.format("\"%s\"", ssid);
	
    WifiConfiguration tempConfig = this.IsExist(conf.SSID);
		if (tempConfig != null) {
			wifi.removeNetwork(tempConfig.networkId);
    }
    String capabilities="";
    try{
      capabilities = (String) networkObj.get("capabilities");
    }catch(Exception e){
      promise.reject(e.getMessage());
    }
		
    	// appropriate ciper is need to set according to security type used
		if (capabilities.contains("WPA") || capabilities.contains("WPA2") || capabilities.contains("WPA/WPA2 PSK")) {

			// This is needed for WPA/WPA2 
			// Reference - https://android.googlesource.com/platform/frameworks/base/+/refs/heads/master/wifi/java/android/net/wifi/WifiConfiguration.java#149
			conf.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);

			conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
			conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);

			conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);

			conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
			conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);

			conf.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
			conf.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
			conf.status = WifiConfiguration.Status.ENABLED;
			conf.preSharedKey = String.format("\"%s\"", password);
      
		} else if (capabilities.contains("WEP")) {
			// This is needed for WEP
			// Reference - https://android.googlesource.com/platform/frameworks/base/+/refs/heads/master/wifi/java/android/net/wifi/WifiConfiguration.java#149
			conf.wepKeys[0] = "\"" + password + "\"";
			conf.wepTxKeyIndex = 0;
			conf.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
			conf.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
			conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
			conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
    	} else {
			conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
		}

		List<WifiConfiguration> mWifiConfigList = wifi.getConfiguredNetworks();
		if (mWifiConfigList == null) {
        try{
          message.put("status","failed");
          promise.resolve(message.toString());
        }catch(Exception e){
          promise.reject(e.getMessage());
        }
        }

		int updateNetwork = -1;

		// Use the existing network config if exists
		for (WifiConfiguration wifiConfig : mWifiConfigList) {
			if (wifiConfig.SSID.equals(conf.SSID)) {
        		conf=wifiConfig;
				updateNetwork=conf.networkId;
			}
		}

		// If network not already in configured networks add new network
		if ( updateNetwork == -1 ) {
	      updateNetwork = wifi.addNetwork(conf);
	      wifi.saveConfiguration();
		}

    	// if network not added return false
		if ( updateNetwork == -1 ) {
      try{
        message.put("status","failed");
        promise.resolve(message.toString());
      }catch(Exception e){
        promise.reject(e.getMessage());
      }
		  
		}

    	// disconnect current network
		boolean disconnect = wifi.disconnect();
		if ( !disconnect ) {
      try{
        message.put("status","failed");
        promise.resolve(message.toString());
      }catch(Exception e){
        promise.reject(e.getMessage());
      }
		}

   		// enable new network
		boolean enableNetwork = wifi.enableNetwork(updateNetwork, true);
		if ( !enableNetwork ) {
      try{
        message.put("status","failed");
        promise.resolve(message.toString());
      }catch(Exception e){
        promise.reject(e.getMessage());
      }
		}
    try {
      Thread.sleep(500);
    } catch (InterruptedException ie) {
      try{
        message.put("status","failed");
        promise.resolve(message.toString());
      }catch(Exception e){
        promise.reject(e.getMessage());
      }
    }
    try{
      message.put("status","connected");
      promise.resolve(message.toString());
    }catch(Exception e){
      promise.reject(e.getMessage());
    }
  }

  // Check if the SSID is already configured previously
  private WifiConfiguration IsExist(String SSID) {
		List<WifiConfiguration> existingConfigs = wifi.getConfiguredNetworks();
		if (existingConfigs == null) {
			return null;
		}

		for (WifiConfiguration existingConfig : existingConfigs) {
			if (existingConfig.SSID.equals("\"" + SSID + "\"")) {
				return existingConfig;
			}
		}
		return null;
  }
  



  @ReactMethod
  public void startScan(Promise promise) {
    try{
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
        int permissionAccessCoarseLocation = ContextCompat.checkSelfPermission(getReactApplicationContext(),Manifest.permission.ACCESS_COARSE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if(permissionAccessCoarseLocation != PackageManager.PERMISSION_GRANTED){
          listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if(!listPermissionsNeeded.isEmpty()){
          ActivityCompat.requestPermissions(getCurrentActivity(),listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
        }
      }
      wifi.startScan();
      WifiReceiver receiverWifi = new WifiReceiver(wifi, promise);
      getCurrentActivity().registerReceiver(receiverWifi, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
    }catch(Exception e){
      promise.reject(e.getMessage());
    }
 

  }


  public RNWifiAndHotspotWizardModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
    wifi = (WifiManager) reactContext.getSystemService(Context.WIFI_SERVICE);

  }


  class WifiReceiver extends BroadcastReceiver {

    private Promise promise;
    private WifiManager wifi;

    public WifiReceiver(final WifiManager wifi, Promise promise) {
      super();
      this.promise = promise;
      this.wifi = wifi;
    }


    // This method call when number of wifi connections changed
    public void onReceive(Context c, Intent intent) {

      c.unregisterReceiver(this);

      try {
        List<ScanResult> results = this.wifi.getScanResults();
        JSONArray wifiArray = new JSONArray();

        for (ScanResult result : results) {
          JSONObject wifiObject = new JSONObject();
          if (!result.SSID.equals("")) {
            try {
              wifiObject.put("SSID", result.SSID);
              wifiObject.put("BSSID", result.BSSID);
              wifiObject.put("capabilities", result.capabilities);
              wifiObject.put("frequency", result.frequency);
              wifiObject.put("level", result.level);
              wifiObject.put("timestamp", result.timestamp);
            } catch (Exception e) {
              this.promise.reject(e.getMessage());
              return;
            }
            wifiArray.put(wifiObject);
          }
        }
        this.promise.resolve(wifiArray.toString());
        return;
      } catch (Exception e) {
        this.promise.reject(e.getMessage());
        return;
      }
    }
  }

  @Override
  public String getName() {
    return "RNWifiAndHotspotWizard";
  }
}