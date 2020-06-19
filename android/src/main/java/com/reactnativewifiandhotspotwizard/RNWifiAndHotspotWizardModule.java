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
import java.lang.Exception;
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

public class RNWifiAndHotspotWizardModule extends ReactContextBaseJavaModule {
  WifiManager wifi;
  private final ReactApplicationContext reactContext;
  final int PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION = 1;
  final int PERMISSIONS_REQUEST_HOTSPOT = 23;
  final int PERMISSIONS_WRITE_SETTINGS=25;
  private WifiConfiguration mWifiConfiguration;
  private Object mReservation;

  // TURN ON WIFI
  @ReactMethod
  public void turnOnWifi() {
    wifi.setWifiEnabled(true);
  }

  // TURN OFF WIFI
  @ReactMethod
  public void turnOffWifi() {
      wifi.setWifiEnabled(false);

  }

  @ReactMethod
  public void isWifiEnabled(Promise promise) {
    boolean isWifiEnabled = wifi.isWifiEnabled();
    promise.resolve(isWifiEnabled);
  }

  public void writePermission() {
		Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS,
				Uri.parse("package:" + getReactApplicationContext().getPackageName()));
		getReactApplicationContext().startActivityForResult(intent, PERMISSIONS_WRITE_SETTINGS, null);
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
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat
    .checkSelfPermission(reactContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
    || ContextCompat.checkSelfPermission(reactContext, Manifest.permission.CHANGE_WIFI_STATE) != PackageManager.PERMISSION_GRANTED
    || ContextCompat.checkSelfPermission(reactContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) { 
      ActivityCompat.requestPermissions(getCurrentActivity(),new String[] { Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CHANGE_WIFI_STATE,Manifest.permission.ACCESS_COARSE_LOCATION},
      PERMISSIONS_REQUEST_HOTSPOT);
      turnOnHotspot(SSID,Password,promise);
    }
    else if(!Settings.System.canWrite(getReactApplicationContext()))
    {
      writePermission();
    }
    else
    {
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
  }
  @ReactMethod
  public void turnOffHotspot(Promise promise){
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat
    .checkSelfPermission(reactContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
    || ContextCompat.checkSelfPermission(reactContext, Manifest.permission.CHANGE_WIFI_STATE) != PackageManager.PERMISSION_GRANTED
    || ContextCompat.checkSelfPermission(reactContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) { 
      ActivityCompat.requestPermissions(getCurrentActivity(),new String[] { Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CHANGE_WIFI_STATE,Manifest.permission.ACCESS_COARSE_LOCATION},
      PERMISSIONS_REQUEST_HOTSPOT);
      turnOffHotspot(promise);
    }
    else if(!Settings.System.canWrite(getReactApplicationContext()))
    {
      writePermission();
    }
    else
    {
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
      promise.resolve("failed to start hotspot");
    }
  }
  @ReactMethod
  public void disconnectFromNetwork(Promise promise){
    boolean disconnect = wifi.disconnect();
    promise.resolve(disconnect);
  }
  // Connects to an SSID.
  @ReactMethod
	public void connectToNetwork(String ssid, String password,Promise promise) {
    // Start Scanning Networks
    wifi.startScan();
    WifiReceiver receiverWifi = new WifiReceiver(wifi, promise);
    getCurrentActivity().registerReceiver(receiverWifi, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
    // Get Scan Results
    List < ScanResult > results = wifi.getScanResults();
    ScanResult result=null;
    for (ScanResult scanresult: results) {
			String resultString = "" + scanresult.SSID;
			if (ssid.equals(resultString)) {
      result = scanresult;
			}
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

		String capabilities = result.capabilities;
		
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
		    promise.reject("Failed");
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
		  promise.reject("Failed to add new network configurations");
		}

    	// disconnect current network
		boolean disconnect = wifi.disconnect();
		if ( !disconnect ) {
			promise.reject("Failed to disconnect from existing network");
		}

   		// enable new network
		boolean enableNetwork = wifi.enableNetwork(updateNetwork, true);
		if ( !enableNetwork ) {
			promise.reject("Failed to connect to network");
		}

		promise.resolve("connected");
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
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat
        .checkSelfPermission(reactContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(getCurrentActivity(),new String[] { Manifest.permission.ACCESS_COARSE_LOCATION },
          PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION);
      // After this point you wait for callback in onRequestPermissionsResult(int,
      // String[], int[]) overriden method
      wifi.startScan();
      WifiReceiver receiverWifi = new WifiReceiver(wifi, promise);
      getCurrentActivity().registerReceiver(receiverWifi, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
    } else {
      wifi.startScan();
      WifiReceiver receiverWifi = new WifiReceiver(wifi, promise);
      getCurrentActivity().registerReceiver(receiverWifi, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
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