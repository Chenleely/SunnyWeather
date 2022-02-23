package tools;

import com.qweather.sdk.view.HeConfig;

public class QWeatherTools {
    private volatile static QWeatherTools qWeatherTools;
    private QWeatherTools(){}
    public static QWeatherTools getqWeatherTools(){
        synchronized (QWeatherTools.class){
            if (qWeatherTools==null){
                HeConfig.init("HE2202222131501920","99f2fe2ef9af4143881305cdc9aeb8bd");
                HeConfig.switchToDevService();
            }
        }
        return qWeatherTools;
    }
}
