package simbot.example.BootAPIUse.GeographyAPI;

import cn.hutool.http.HttpUtil;
import net.sf.json.JSONObject;
import org.json.JSONArray;

/**
 * @author zeng
 * @date 2022/7/11 9:55
 * @user 86188
 */
public class geoAPI {

    public String adm1;
    public String adm2;
    public String id;

    /**
     * 获取城市地理信息方法
     *
     * @param city 获取外界输入的城市信息
     * @return 返回城市的经纬度，行政区等具体信息
     */
    public String CityInfo(String city) {
        try {
            // 去除消息中的空格
            // 将中文空格替换为英文空格
            city = city.replace((char) 12288, ' ');
            city = city.trim();

            // 接口URL地址
            String URL = "https://geoapi.qweather.com/v2/city/lookup?location=" + city
                    + "&key=1b48b4a69c8a4f7cb17674cbf4cea29b";
            String jsonStr = HttpUtil.get(URL);
            JSONObject object = JSONObject.fromObject(jsonStr);

            // 获得名为"location"的String
            String location = object.getString("location");
            JSONArray jsonArray;
            jsonArray = new JSONArray(location);

            // 赋值，便于其他方法调用
            this.adm1 = jsonArray.getJSONObject(0).getString("adm1");
            this.adm2 = jsonArray.getJSONObject(0).getString("adm2");
            this.id = jsonArray.getJSONObject(0).getString("id");
            // 城市名称
            String cityName = jsonArray.getJSONObject(0).getString("name");
            // 所属国家
            String cityCountry = jsonArray.getJSONObject(0).getString("country");
            // 维度
            float cityLat = Float.parseFloat(jsonArray.getJSONObject(0).getString("lat"));
            // 经度
            float cityLon = Float.parseFloat(jsonArray.getJSONObject(0).getString("lon"));
            // 时区
            String cityTz = jsonArray.getJSONObject(0).getString("tz");
            // 城市地区的属性
            String cityType = jsonArray.getJSONObject(0).getString("type");
            String cityLonChange;
            String cityLatChange;

            // 将获得的经纬度调整为统一格式
            if (cityLon < 0) {
                cityLon = -cityLon;
                cityLonChange = cityLon + "°W";
            } else {
                cityLonChange = cityLon + "°E";
            }

            if (cityLat < 0) {
                cityLat = -cityLat;
                cityLatChange = cityLat + "°S";
            } else {
                cityLatChange = cityLat + "°N";
            }

            return "城市名称:" + cityName + "\n城市ID:" + id + "\n所属国家:" + cityCountry
                    + "\n上级行政区划:" + adm1 + "\n所属一级行政区划:" + adm2
                    + "\n城市经纬度:\n" + cityLatChange + "\n" + cityLonChange + "\n时区:"
                    + cityTz + "\n城市地区属性:" + cityType;
        } catch (Exception e) {
            return "姬姬没有找到" + city + "的地理信息呢，要不换一个试试";

        }
    }

    /**
     * 城市天气信息方法
     *
     * @param city 获取输入的城市信息
     * @return 返回城市具体地理信息
     */
    public String weatherInfo(String city) {
        // 去除消息中的空格
        // 将中文空格替换为英文空格
        city = city.replace((char) 12288, ' ');
        city = city.trim();

        // 调用城市地理信息方法以获取城市id
        CityInfo(city);

        String URL = "https://devapi.qweather.com/v7/weather/now?location=" + id
                + "&key=1b48b4a69c8a4f7cb17674cbf4cea29b";

        // 获取城市温度信息
        try {
            String jsonStr = HttpUtil.get(URL);
            JSONObject jsonObject = JSONObject.fromObject(jsonStr);
            // 更新时间
            String updateTime = jsonObject.getString("updateTime");
            JSONObject now = jsonObject.getJSONObject("now");
            // 温度
            String temp = now.getString("temp");
            // 体感温度
            String feelsLike = now.getString("feelsLike");
            //  文字描述
            String text = now.getString("text");
            // 360°风向
            String wind360 = now.getString("wind360");
            // 风向
            String windDir = now.getString("windDir");
            // 风力等级
            String windScale = now.getString("windScale");
            // 相对湿度
            String humidity = now.getString("humidity");
            // 大气压强
            String pressure = now.getString("pressure");
            // 能见度
            String vis = now.getString("vis");
            // 能见度
            String cloud = now.getString("cloud");

            String weatherInfo = "无量姬已经获取到了 " + adm1 + adm2 + "市 的天气呢\n更新时间：" + updateTime + "\n温度："
                    + temp + "°C\n体感温度：" + feelsLike + "°C\n天气状况：" + text + "\n风向："
                    + windDir + "-" + wind360 + "°\n风力等级：" + windScale + " 级\n相对湿度:"
                    + humidity + "\n大气压强：" + pressure + " 百帕\n能见度："
                    + vis + " 公里";

            // 云量信息有可能为空
            if (cloud != null) {
                weatherInfo += "\n云量：" + cloud + "%";
            }
            return weatherInfo + "\n" + dailyWeather() + "\n" + WeatherDay();

        } catch (Exception e) {
            return "姬姬没有找到" + city + "的天气呢，换一个试试吧~";
        }
    }

    /**
     * 获取第二天的天气状况
     *
     * @return 具体信息
     */
    public String WeatherDay() {

        String URL = "https://devapi.qweather.com/v7/weather/3d?location=" + id
                + "&key=1b48b4a69c8a4f7cb17674cbf4cea29b";
        String jsonStr = HttpUtil.get(URL);
        JSONObject object = JSONObject.fromObject(jsonStr);

        // 获得名为"daily"的String
        String daily = object.getString("daily");
        JSONArray jsonArray;
        jsonArray = new JSONArray(daily);

        // 日期
        String nextDay = jsonArray.getJSONObject(1).getString("fxDate");
        // 日出时间
        String sunrise = jsonArray.getJSONObject(1).getString("sunrise");
        // 日落时间
        String sunset = jsonArray.getJSONObject(1).getString("sunset");
        // 最高温度
        String tempMax = jsonArray.getJSONObject(1).getString("tempMax");
        // 最低温度
        String tempMin = jsonArray.getJSONObject(1).getString("tempMin");
        // 日间天气
        String textDay = jsonArray.getJSONObject(1).getString("textDay");
        // 日间天气
        String textNight = jsonArray.getJSONObject(1).getString("textNight");
        return "--------" + nextDay + "-------\n日出时间:" + sunrise + "  日落时间:" + sunset
                + "\n最高气温:" + tempMax + " °C   最低气温:" + tempMin + " °C\n日间天气:" + textDay
                + "\n夜间天气:" + textNight;
    }

    /**
     * 获取每日天气建议
     *
     * @return 返回温馨提示
     */
    public String dailyWeather() {
        try {


            String URL = "https://devapi.qweather.com/v7/indices/1d?type=1,2&location=" + id
                    + "&key=1b48b4a69c8a4f7cb17674cbf4cea29b";
            String jsonStr = HttpUtil.get(URL);
            JSONObject object = JSONObject.fromObject(jsonStr);

            // 获得名为"daily"的String
            String daily = object.getString("daily");
            JSONArray jsonArray;
            jsonArray = new JSONArray(daily);

            // 日期
            String text = jsonArray.getJSONObject(0).getString("text");
            return "姬姬温馨提示：" + text;
        } catch (Exception e) {
            return "姬姬没有温馨提示哦";
        }
    }

}

