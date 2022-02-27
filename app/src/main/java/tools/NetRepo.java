package tools;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.example.sunnyweather.R;

import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

public class NetRepo {
    public static String token="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJFbWFpbCI6InpoYW96aGl3ZWlAc2luYS5jbiIsIk5hbWUiOiJsb3VpczI5NiIsImV4cCI6MTY1MDA2Mjg4NiwiaXNzIjoic3Vubnlfd2VhdGhlcl9sb3VpczI5NiIsInN1YiI6InN1bm55X3dlYXRoZXIifQ.9URN7OBRyvEUxzdIrXWfysBK2piH2iQSLjOZJcz7b-Q";
    public static String base_url="http://louis296.top:8081/v1?Version=20220101&Action=";
    public static String Authorization="Authorization";
    public static MediaType JSON = MediaType.parse("application/json;charset=utf-8");
    public static class GetRequest{
        String url;
        Param param;//参数
        Request request;
        public GetRequest(String url){
            this.url=url;
        }
        public Request getRequest() {
            if(param==null || TextUtils.isEmpty(param.key)||TextUtils.isEmpty(param.value)){
                request=new Request.Builder().url(url).get().build();
            }else{
                request=new Request.Builder().url(url).addHeader(param.getKey(), param.getValue()).build();
            }
            return request;
        }

        public void setUrl(String url) {
            this.url = url;
        }
        public String getUrl() {
            return url;
        }
        public Param getParam() {
            return param;
        }
        public void setParam(Param param) {
            this.param = param;
        }
    }

    public static class PostRequest{
        String url;
        Request request;
        MediaType type;
        String jsonString;
        Param param;//参数
        public PostRequest(String url){
            this.url=url;
        }
        public Request getRequest() {
            RequestBody body=RequestBody.create(type,jsonString);
            if(param==null || TextUtils.isEmpty(param.key)||TextUtils.isEmpty(param.value)){
                request=new Request.Builder().url(url).post(body).build();
            }else{
                request=new Request.Builder().url(url)
                        .addHeader(param.getKey(), param.getValue()).post(body).build();
            }
            return request;
        }

        public Param getParam() {
            return param;
        }

        public void setParam(Param param) {
            this.param = param;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public void setType(MediaType type) {
            this.type = type;
        }

        public MediaType getType() {
            return type;
        }

        public String getJsonString() {
            return jsonString;
        }

        public void setJsonString(String jsonString) {
            this.jsonString = jsonString;
        }
    }

    //参数
    public static class Param{
        String key;
        String value;
        public Param(String key, String val){
            this.key=key;
            this.value=val;
        }

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }
    }
}
