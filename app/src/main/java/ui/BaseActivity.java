package ui;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import tools.GPSUtils;
import tools.QWeatherTools;
import tools.UserStateInfo;

public class BaseActivity extends AppCompatActivity {
    //GPS位置信息
    protected LocationManager mLocationManager = null;
    public static final int LOCATION_CODE=8001;
    protected GPSUtils gpsUtils;
    protected Location nowLocation;
    protected QWeatherTools qWeatherTools;
    protected UserStateInfo userStateInfo;
    protected ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        qWeatherTools = QWeatherTools.getqWeatherTools();
        gpsUtils=GPSUtils.getInstance(this);
        userStateInfo=UserStateInfo.getUserStateInfo();
        mProgressDialog=new ProgressDialog(this);
        mProgressDialog.setMessage("加载中，请稍后...");
        mProgressDialog.setCancelable(false);
        //setStatus();
        //showCheckPermissions();
    }

    //是否检查位置权限,6.0以下无需获取权限
    protected void checkPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //请求权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_CODE);
        } else {
            requestGPS();
        }
    }
    protected void showCheckPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission();
        } else {
            requestGPS();
        }
    }
    protected void requestGPS(){
        gpsUtils.getLngAndLat(new GPSUtils.OnLocationResultListener() {
            @Override
            public void onLocationResult(Location location) {
                nowLocation=location;
                Log.d("test","lat: "+location.getLatitude());
            }

            @Override
            public void OnLocationChange(Location location) {
                nowLocation=location;
                Log.d("test","lat: "+location.getLatitude());
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case LOCATION_CODE:
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1]==PackageManager.PERMISSION_GRANTED){
                    //权限被用户同意，搜索位置信息
                    requestGPS();
                }
        }
    }

    //状态栏透明
    protected void setStatus(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            int flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                WindowManager.LayoutParams attributes = window.getAttributes();
                attributes.flags |= flagTranslucentNavigation;
                window.setAttributes(attributes);
                getWindow().setStatusBarColor(Color.TRANSPARENT);
            } else {
                Window window = getWindow();
                WindowManager.LayoutParams attributes = window.getAttributes();
                attributes.flags |= flagTranslucentStatus | flagTranslucentNavigation;
                window.setAttributes(attributes);
            }
        }
    }
}
