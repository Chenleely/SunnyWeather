package tools;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
//封装okhttp
public class HttpUtil {
    private volatile static HttpUtil httpUtil;
    private volatile static OkHttpClient client;
    private HttpUtil(){}
    public static HttpUtil getHttpUtil(){
        synchronized (HttpUtil.class){
            if (httpUtil==null){
                httpUtil=new HttpUtil();
            }
            if (client==null){
                client= new OkHttpClient.Builder()
                        .readTimeout(10000, TimeUnit.MILLISECONDS)//设置读取超时为10秒
                        .connectTimeout(10000, TimeUnit.MILLISECONDS)//设置链接超时为10秒
                        .build();
            }
        }
        return httpUtil;
    }
    public static void doGet(String url,final CallbackListener listener){
        final Request request=new Request.Builder().get().url(url).build();
        Call call=client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                listener.failed(-1);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.code()==200){
                    listener.success(response.body().toString());
                }else{
                    listener.failed(response.code());
                }
            }
        });
    }

    public interface CallbackListener{
        void success(String msg);
        void failed(int code);
    }
}
