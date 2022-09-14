package simbot.example.BootAPIUse.YuanShenAPI.GenShinInfo;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;

/**
 * @Author zeng
 * @Date 2022/9/6 10:28
 * @User 86188
 * @Description:
 */
public class GetData {
    public static JSONObject getEnkeData(String uid) {
        try {
            String url = "https://enka.network/u/" + uid + "/__data.json";
            String jsonStr = HttpUtil.get(url);

            return JSONObject.parseObject(jsonStr);
        } catch (Exception e) {
            String url = "https://enka.microgg.cn/u/" + uid + "/__data.json";
            String jsonStr = HttpUtil.get(url);

            return JSONObject.parseObject(jsonStr);
        }

    }
}
