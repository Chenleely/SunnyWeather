package tools;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefTools {
    public static final String FILE_NAME="sunny_weather";

    public static void put(Context context,String key,Object object){
        SharedPreferences sp=context.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        if (object instanceof String)
        {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer)
        {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean)
        {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float)
        {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long)
        {
            editor.putLong(key, (Long) object);
        } else
        {
            editor.putString(key, object.toString());
        }
        editor.apply();
    }

    public static Object get(Context context,String key,Object defaultObject){
        if (defaultObject==null){return null;}
        String type = defaultObject.getClass().getSimpleName();
        SharedPreferences prefs=context.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
        if ("String".equals(type)) {
            return prefs.getString(key, (String) defaultObject);
        } else if ("Integer".equals(type)) {
            return prefs.getInt(key, (Integer) defaultObject);
        } else if ("Boolean".equals(type)) {
            return prefs.getBoolean(key, (Boolean) defaultObject);
        } else if ("Float".equals(type)) {
            return prefs.getFloat(key, (Float) defaultObject);
        } else if ("Long".equals(type)) {
            return prefs.getLong(key, (Long) defaultObject);
        }
        return null;
    }
}
