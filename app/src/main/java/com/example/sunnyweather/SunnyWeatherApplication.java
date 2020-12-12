package com.example.sunnyweather;

import android.app.Application;
import android.content.Context;

public class SunnyWeatherApplication extends Application {
        //全局获取Context
        //系统启动时选择初始化SunnyWeatherApplication而不是默认的Application
        private static Context context;
        public static String TOKEN="me40XvJodPHx83t0";
    @Override
    public void onCreate() {

        super.onCreate();
        context=getApplicationContext();
    }
    public static Context getContext(){
        return context;
    }
}
