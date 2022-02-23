package ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.sunnyweather.R;
import com.qweather.sdk.bean.geo.GeoBean;

import java.util.List;

public class CityAdapter extends ArrayAdapter<GeoBean.LocationBean> {
    private onCityClickListener mListener;
    private Context mContext;
    private int resourceId;

    public CityAdapter(@NonNull Context context, int resource, @NonNull List<GeoBean.LocationBean> objects) {
        super(context, resource, objects);
        this.resourceId=resource;
        this.mContext=context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        GeoBean.LocationBean locationBean=getItem(position);
        View view=LayoutInflater.from(mContext).inflate(resourceId,null);
        TextView cityName=view.findViewById(R.id.city_name);
        cityName.setText(locationBean.getCountry()+"    "+locationBean.getAdm1()+"    "+locationBean.getAdm2()+"    "+locationBean.getName());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onCityClick(locationBean.getId(),locationBean.getName());
            }
        });
        return view;
    }

    public void setmListener(onCityClickListener mListener) {
        this.mListener = mListener;
    }

    public interface onCityClickListener{
        void onCityClick(String cityId,String cityName);
    }
}
