package mode;

import java.util.List;

public class Weather {
    //title
    private String cityName;
    private String updateTime;
    //now
    private String tmp;//气温
    private String weather_info;//天气情况

    private List<Forecast> forecastList;
    //forecast_itemp
    public static class Forecast{
        private String foreDate;
        private String foreInfo;
        private String maxTmp;
        private String minTmp;

        public Forecast(){}
        public Forecast(String foreDate, String foreInfo, String maxTmp, String minTmp) {
            this.foreDate = foreDate;
            this.foreInfo = foreInfo;
            this.maxTmp = maxTmp;
            this.minTmp = minTmp;
        }
        public String getForeDate() {
            return foreDate;
        }
        public void setForeDate(String foreDate) {
            this.foreDate = foreDate;
        }
        public String getForeInfo() {
            return foreInfo;
        }
        public void setForeInfo(String foreInfo) {
            this.foreInfo = foreInfo;
        }
        public String getMaxTmp() {
            return maxTmp;
        }
        public void setMaxTmp(String maxTmp) {
            this.maxTmp = maxTmp;
        }
        public String getMinTmp() {
            return minTmp;
        }
        public void setMinTmp(String minTmp) {
            this.minTmp = minTmp;
        }
    }
    private aqi aqi;
    //aqi
    public static class aqi{
        private String aqiInfo;
        private String pmInfo;

        public aqi(String aqiInfo, String pmInfo) {
            this.aqiInfo = aqiInfo;
            this.pmInfo = pmInfo;
        }

        public aqi() {
        }

        public String getAqiInfo() {
            return aqiInfo;
        }

        public void setAqiInfo(String aqiInfo) {
            this.aqiInfo = aqiInfo;
        }

        public String getPmInfo() {
            return pmInfo;
        }

        public void setPmInfo(String pmInfo) {
            this.pmInfo = pmInfo;
        }
    }

    public Weather.aqi getAqi() {
        return aqi;
    }

    public void setAqi(Weather.aqi aqi) {
        this.aqi = aqi;
    }

    public Weather() {
    }

    public Weather(String cityName, String updateTime, String tmp, String weather_info, List<Forecast> forecastList) {
        this.cityName = cityName;
        this.updateTime = updateTime;
        this.tmp = tmp;
        this.weather_info = weather_info;
        this.forecastList = forecastList;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getTmp() {
        return tmp;
    }

    public void setTmp(String tmp) {
        this.tmp = tmp;
    }

    public String getWeather_info() {
        return weather_info;
    }

    public void setWeather_info(String weather_info) {
        this.weather_info = weather_info;
    }

    public List<Forecast> getForecastList() {
        return forecastList;
    }

    public void setForecastList(List<Forecast> forecastList) {
        this.forecastList = forecastList;
    }
}

