package simbot.example.BootAPIUse.OtherAPI;

import cn.hutool.http.HttpUtil;
import net.sf.json.JSONObject;
import org.json.JSONArray;

import java.util.Objects;

/**
 * @author zeng
 * @date 2022/6/27 15:21
 * @user 86188
 */
public class NewsApi {
    // 新闻API
    public String url = "https://c.m.163.com/nc/article/headline/T1348647853363/0-40.html";
    public String jsonStr = HttpUtil.get(url);
    public JSONObject object = JSONObject.fromObject(jsonStr);
    // 获得名为"T1348647853363"的String
    public String T1348647853363 = object.getString("T1348647853363");

    // 获取新闻标题，时间，内容
    public String news(int index) {
        JSONArray jsonArray;
        jsonArray = new JSONArray(T1348647853363);

        return jsonArray.getJSONObject(index).getString("title") + "\n"
                + jsonArray.getJSONObject(index).getString("lmodify") + "\n"
                + jsonArray.getJSONObject(index).getString("digest")+"...";
    }

    // 获取新闻图片地址
    public String image(int index) {
        JSONArray jsonArray;
        jsonArray = new JSONArray(T1348647853363);

        return jsonArray.getJSONObject(index).getString("imgsrc");
    }

    // 获取新闻数组长度
    public int length() {
        JSONArray jsonArray;
        jsonArray = new JSONArray(T1348647853363);
        return jsonArray.length();
    }

    // 获取新闻网址
    public String URL(int index) {
        JSONArray jsonArray;
        jsonArray = new JSONArray(T1348647853363);

        if (!Objects.equals(jsonArray.getJSONObject(index).getString("url"), "")) {

            return "\n网址：" + jsonArray.getJSONObject(index).getString("url");
        }

        // 当新闻网址为空或不存在url字段时返回
        return "";
    }

}
