package ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.sunnyweather.R;
import com.qweather.sdk.bean.base.Code;
import com.qweather.sdk.bean.geo.GeoBean;
import com.qweather.sdk.view.QWeather;

import java.util.ArrayList;
import java.util.List;

import tools.PrefTools;

public class SearchActivity extends BaseActivity implements CityAdapter.onCityClickListener {
    private SearchView searchView;
    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private CityAdapter adapter;
    private TextView backmain;
    private List<GeoBean.LocationBean> locationBeans;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_city);
        setStatus();
        locationBeans=new ArrayList<>();
        searchView=(SearchView) findViewById(R.id.search_city_view);
        listView=(ListView) findViewById(R.id.city_result);
        backmain=(TextView) findViewById(R.id.back_main);
        backmain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        adapter=new CityAdapter(this,R.layout.city_item,locationBeans);
        listView.setAdapter(adapter);
        adapter.setmListener(this);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                locationBeans.clear();
                adapter.notifyDataSetChanged();
                if(!TextUtils.isEmpty(s)){
                    queryCity(s);
                }
                return false;
            }
        });
    }

    private void queryCity(String city){
        QWeather.getGeoCityLookup(this, city, new QWeather.OnResultGeoListener() {
            @Override
            public void onError(Throwable throwable) {

            }
            @Override
            public void onSuccess(GeoBean geoBean) {
                locationBeans.clear();
                if (Code.OK == geoBean.getCode()) {
                    if(geoBean.getLocationBean()!=null&&geoBean.getLocationBean().size()>0){
                        locationBeans.addAll(geoBean.getLocationBean());
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onCityClick(String cityId,String cityName) {
        Intent intent=new Intent();
        intent.putExtra("city_id",cityId);
        intent.putExtra("city_name",cityName);
        setResult(MainActivity.CHECKOUT_CITY,intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(SearchActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
