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
  @ReactMethod
  public void turnOnWifi() {
    wifi.setWifiEnabled(true);
  }

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
          obj.put("status","success");
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
          obj.put("status","success");
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
          promise.reject("Hotspot Not Active");
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