
package com.reactnativewifiandhotspotwizard;


import com.facebook.react.bridge.*;

import android.util.Log;
import java.lang.Object;
import android.app.Activity;
import android.content.ComponentName;
import android.net.Uri;

import android.os.Bundle;
import android.provider.Settings;
import java.lang.reflect.Method;
import java.lang.Exception;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.widget.Toast;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.security.auth.callback.Callback;
public class RNWifiAndHotspotWizardModule extends ReactContextBaseJavaModule {

  private final ReactApplicationContext reactContext;

  @ReactMethod
  public void greet(Promise promise){
    String greetings = "Hello I am Prafulla";
    promise.resolve(greetings);
  }

 
  public RNWifiAndHotspotWizardModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
  }

  @Override
  public String getName() {
    return "RNWifiAndHotspotWizard";
  }
}