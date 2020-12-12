package com.example.sunnyweather.network;

import android.util.Log;

import com.example.sunnyweather.model.PlaceResponse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class Utility {
    public static PlaceResponse handlePlaceReponse(String response){
        try {
            Gson gson=new Gson();
            PlaceResponse placeResponse=gson.fromJson(response,PlaceResponse.class);
            List<PlaceResponse.Place> places=placeResponse.getPlaces();
            for (PlaceResponse.Place place:places){
                Log.d("test",place.getAddress());
                Log.d("test",place.getName());
                Log.d("test",place.getLocation().getLat());
                Log.d("test",place.getLocation().getLng());
            }
            return placeResponse;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
