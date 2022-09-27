package ru.nasvyazi.widget;


import android.annotation.SuppressLint;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.Base64;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.security.Key;
import java.util.ArrayList;
import java.util.List;


import static android.content.Context.MODE_PRIVATE;
import static android.os.Looper.getMainLooper;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class WidgetShareDataModule extends ReactContextBaseJavaModule {

  private final ReactApplicationContext reactContext;
  private final String sharedPreferencesName = "TELEOPTI_storage";


  @SuppressLint("RestrictedApi")
  public WidgetShareDataModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;

  }

  @Override
  public String getName() {
    return "WidgetShareData";
  }

  class DataObject {
    public String contractTime;
    public String description;
    public double alpha;
    public double blue;
    public double green;
    public double red;
  }
  @ReactMethod
  public void setDataList(final String dataList, final Promise promise) {

    SharedPreferences sharedPreferences =  getReactApplicationContext().getSharedPreferences(sharedPreferencesName,MODE_PRIVATE);
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putString("teleoptiData", dataList);
    editor.commit();

    Intent intent = new Intent(reactContext, TeleoptiWidget.class);
    intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);

    int[] ids = AppWidgetManager.getInstance(reactContext)
            .getAppWidgetIds(new ComponentName(reactContext, TeleoptiWidget.class));
    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
    reactContext.sendBroadcast(intent);
  }
}