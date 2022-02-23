package ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.example.sunnyweather.R;
import com.google.gson.Gson;
import com.qweather.sdk.bean.air.AirNowBean;
import com.qweather.sdk.bean.base.Code;
import com.qweather.sdk.bean.base.Lang;
import com.qweather.sdk.bean.base.Unit;
import com.qweather.sdk.bean.geo.GeoBean;
import com.qweather.sdk.bean.weather.WeatherDailyBean;
import com.qweather.sdk.bean.weather.WeatherNowBean;
import com.qweather.sdk.view.QWeather;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicInteger;

import mode.UserInfo;
import mode.Weather;
import okhttp3.MediaType;
import tools.HttpUtil;
import tools.NetRepo;
import tools.PrefTools;

public class MainActivity extends BaseActivity {
    private String TAG="MainActivity";
    private Weather mWeather;
    private String cityName=" ";
    private AtomicInteger count=new AtomicInteger(0);
    private String mLocation;
    //天气页UI组件
    //title
    private TextView titleCity;
    private TextView titleUpdateTime;
    //now
    private TextView nowDegree;
    private TextView nowWeatherInfo;
    //forecast
    private LinearLayout forecastLayout;
    //aqi
    private TextView pm25Text;
    private TextView aqiText;
    private ImageView bingImage;
    private SwipeRefreshLayout swipeRefreshLayout;

    private Button momentsBtn;
    private Button checkoutBtn;

    public static final int CHECKOUT_CITY=8002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setStatus();
        mLocation= (String) PrefTools.get(this,"location","");
        cityName=(String)PrefTools.get(this,"city_name","");
        bindViewComponent();
        queryWeather(mLocation);
    }

    private void bindViewComponent(){
        momentsBtn=(Button) findViewById(R.id.checkout_moments_btn);
        momentsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        checkoutBtn=(Button)findViewById(R.id.checkout_city_btn);
        checkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,SearchActivity.class);
                startActivityForResult(intent,CHECKOUT_CITY);
            }
        });
        titleCity=(TextView) findViewById(R.id.title_city);
        titleUpdateTime=(TextView) findViewById(R.id.title_update_time);
        nowDegree=(TextView) findViewById(R.id.now_degree_text);
        nowWeatherInfo=(TextView) findViewById(R.id.now_weather_info_text);
        forecastLayout=(LinearLayout) findViewById(R.id.forecast_layout);
        aqiText=(TextView) findViewById(R.id.aqi_text);
        pm25Text=(TextView) findViewById(R.id.pm25_text);
        bingImage=(ImageView) findViewById(R.id.bing_pic_img);
        swipeRefreshLayout=(SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.design_default_color_primary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                queryWeather(mLocation);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        String bingPic=(String) PrefTools.get(this,"bing_pic",null);
        if (bingPic==null){
            Glide.with(this).load(bingPic).into(bingImage);
        }else{
            lodaBingPic();
        }
    }
    private void  lodaBingPic(){
        String requestBingPic="http://guolin.tech/api/bing_pic";
        NetRepo.GetRequest request=new NetRepo.GetRequest(requestBingPic);
        HttpUtil.doGet(request, new HttpUtil.CallbackListener() {
            @Override
            public void success(String msg) {
                final String bingPic=msg;
                PrefTools.put(MainActivity.this,"bing_pic",bingPic);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(MainActivity.this).load(bingPic).into(bingImage);
                    }
                });
            }

            @Override
            public void failed(int code) {

            }
        });

    }
    private  void queryWeather(String location){
        mProgressDialog.show();
        mWeather=new Weather();
        //更新实时天气
        QWeather.getWeatherNow(MainActivity.this, location, Lang.ZH_HANS, Unit.METRIC, new QWeather.OnResultWeatherNowListener() {
            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "getWeather onError: " + e);
                mProgressDialog.dismiss();
            }
            @Override
            public void onSuccess(WeatherNowBean weatherBean) {
                Log.i(TAG, "getWeather onSuccess: " + new Gson().toJson(weatherBean));
                //先判断返回的status是否正确，当status正确时获取数据，若status不正确，可查看status对应的Code值找到原因
                if (Code.OK == weatherBean.getCode()) {
                    String time=weatherBean.getNow().getObsTime();
                    mWeather.setCityName(cityName);
                    mWeather.setUpdateTime(weatherBean.getNow().getObsTime());
                    mWeather.setTmp(weatherBean.getNow().getTemp());
                    mWeather.setWeather_info(weatherBean.getNow().getText());
                    count.incrementAndGet();
                    if(count.get()==3){
                        mProgressDialog.dismiss();
                        updateUI(MainActivity.this);
                    }
                } else {
                    //在此查看返回数据失败的原因
                    Code code = weatherBean.getCode();
                    Log.i(TAG, "failed code: " + code);
                    mProgressDialog.dismiss();
                }
            }
        });
        //天气质量
        QWeather.getAirNow(this, location, Lang.ZH_HANS, new QWeather.OnResultAirNowListener() {
            @Override
            public void onError(Throwable throwable) {
                Log.i(TAG, "getWeather onError: " + throwable);
                mProgressDialog.dismiss();
            }

            @Override
            public void onSuccess(AirNowBean airNowBean) {
                Log.i(TAG, "getWeather onSuccess: " + new Gson().toJson(airNowBean));
                //先判断返回的status是否正确，当status正确时获取数据，若status不正确，可查看status对应的Code值找到原因
                if (Code.OK == airNowBean.getCode()) {
                    Weather.aqi aqi=new Weather.aqi();
                    aqi.setAqiInfo(airNowBean.getNow().getAqi());
                    aqi.setPmInfo(airNowBean.getNow().getPm2p5());
                    mWeather.setAqi(aqi);
                    count.incrementAndGet();
                    if(count.get()==3){
                        mProgressDialog.dismiss();
                        updateUI(MainActivity.this);
                    }
                } else {
                    //在此查看返回数据失败的原因
                    Code code = airNowBean.getCode();
                    Log.i(TAG, "failed code: " + code);
                    mProgressDialog.dismiss();
                }
            }
        });
        //未来七天天气预报
        QWeather.getWeather3D(this, location, Lang.ZH_HANS, Unit.METRIC, new QWeather.OnResultWeatherDailyListener() {
            @Override
            public void onError(Throwable throwable) {
                Log.i(TAG, "getWeather onError: " + throwable);
                mProgressDialog.dismiss();
            }

            @Override
            public void onSuccess(WeatherDailyBean weatherDailyBean) {
                Log.i(TAG, "getWeather onSuccess: " + new Gson().toJson(weatherDailyBean));
                //先判断返回的status是否正确，当status正确时获取数据，若status不正确，可查看status对应的Code值找到原因
                if (Code.OK == weatherDailyBean.getCode()) {
                    //mWeather.getForecastList().clear();
                    List<Weather.Forecast> forecastList=new ArrayList<>();
                    if (mWeather.getForecastList()!=null){
                        mWeather.getForecastList().clear();
                    }
                    for(WeatherDailyBean.DailyBean dailyBean:weatherDailyBean.getDaily()){
                        Weather.Forecast forecast=new Weather.Forecast();
                        forecast.setForeDate(dailyBean.getFxDate());
                        forecast.setForeInfo(dailyBean.getTextDay());
                        forecast.setMinTmp(dailyBean.getTempMin());
                        forecast.setMaxTmp(dailyBean.getTempMax());
                        forecastList.add(forecast);
                    }
                    mWeather.setForecastList(forecastList);
                    mProgressDialog.dismiss();
                    count.incrementAndGet();
                    if(count.get()==3){
                        mProgressDialog.dismiss();
                        updateUI(MainActivity.this);
                    }
                } else {
                    //在此查看返回数据失败的原因
                    Code code = weatherDailyBean.getCode();
                    Log.i(TAG, "failed code: " + code);
                    mProgressDialog.dismiss();
                }
            }
        });
        lodaBingPic();
    }
    private void updateUI(Context context){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                count.set(0);
                titleCity.setText(mWeather.getCityName());
                titleUpdateTime.setText(mWeather.getUpdateTime());
                nowDegree.setText(mWeather.getTmp()+"℃");
                nowWeatherInfo.setText(mWeather.getWeather_info());
                aqiText.setText(mWeather.getAqi().getAqiInfo());
                pm25Text.setText(mWeather.getAqi().getPmInfo());
                forecastLayout.removeAllViews();
                for(Weather.Forecast forecast:mWeather.getForecastList()){
                    View view= LayoutInflater.from(context).inflate(R.layout.forecast_item,forecastLayout,false);
                    TextView dateText=(TextView) view.findViewById(R.id.forecast_item_date_text);
                    TextView infoText=(TextView) view.findViewById(R.id.forecast_info_text);
                    TextView maxText=(TextView) view.findViewById(R.id.forecast_max_text);
                    TextView minText=(TextView) view.findViewById(R.id.forecast_min_text);
                    dateText.setText(forecast.getForeDate());
                    infoText.setText(forecast.getForeInfo());
                    maxText.setText(forecast.getMaxTmp()+"℃");
                    minText.setText(forecast.getMinTmp()+"℃");
                    forecastLayout.addView(view);
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CHECKOUT_CITY:
                String cityId=data.getStringExtra("city_id");
                String cityName=data.getStringExtra("city_name");
                this.cityName=cityName;
                this.mLocation=cityId;
                queryWeather(cityId);
                break;
            default:
        }
    }
}