package ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.example.sunnyweather.R;
import com.google.gson.Gson;
import com.qweather.sdk.bean.base.Code;
import com.qweather.sdk.bean.base.Lang;
import com.qweather.sdk.bean.base.Unit;
import com.qweather.sdk.bean.geo.GeoBean;
import com.qweather.sdk.bean.weather.WeatherNowBean;
import com.qweather.sdk.view.QWeather;

import mode.UserInfo;
import okhttp3.MediaType;
import tools.HttpUtil;
import tools.NetRepo;

public class MainActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}