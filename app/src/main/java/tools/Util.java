package tools;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Util {
    public static String formatUtcTime(String utcTime){
        Date date = null;
        Date i;
        StringBuffer buffer=new StringBuffer();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        Calendar calendar = Calendar.getInstance();
        try{
            date = sdf.parse(utcTime);
            calendar.setTime(date);
            calendar.set(Calendar.HOUR, calendar.get(Calendar.HOUR) );
        }catch (ParseException e){
            e.printStackTrace();
        }
        buffer.append(calendar.get(Calendar.MONTH)+1);
        buffer.append("月");
        buffer.append(calendar.get(Calendar.DATE));
        buffer.append("日");
        buffer.append(" "+calendar.get(Calendar.HOUR)+":"+calendar.get(Calendar.MINUTE));
        return  buffer.toString();
    }
}
