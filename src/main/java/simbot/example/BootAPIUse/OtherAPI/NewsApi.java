package simbot.example.BootAPIUse.OtherAPI;

import cn.hutool.http.HttpUtil;
import net.sf.json.JSONObject;
import org.json.JSONArray;

/**
 * @author zeng
 * @date 2022/6/27 15:21
 * @user 86188
 */
@SuppressWarnings("")
public class NewsApi {
    private  StringBuilder newsMsg;

    public  StringBuilder getNewsMsg() {
        news();
        return newsMsg;
    }

    /**
     * 新闻API
     */
    public  void news() {
        String newsUrl = "https://v.api.aa1.cn/api/topbaidu/index.php";
        String jsonStr = HttpUtil.get(newsUrl);

        // 去除反爬措施
        jsonStr = jsonStr.substring(0, jsonStr.length() - 4);
        JSONObject object = JSONObject.fromObject(jsonStr);

        try {
            String code = object.getString("code");
            String codeState = "200";
            if (code.equals(codeState)) {
                String newsList = object.getString("newslist");

                JSONArray jsonArray = new JSONArray(newsList);

                newsMsg = new StringBuilder("今日头条:\n");
                int arrayLength = 10;
                for (int length = 0; length < arrayLength; length++) {
                    newsMsg.append(length + 1).append(".").append(jsonArray.getJSONObject(length).getString("title")).append("\n");
                }
                newsMsg.append("--------------------------");
            }
        } catch (Exception e) {
            newsMsg = new StringBuilder("诶呀新闻没了");
        }

    }


//    public static void main(String[] args) {
//        news();
//        System.out.println(getNewsMsg());
//    }
}
