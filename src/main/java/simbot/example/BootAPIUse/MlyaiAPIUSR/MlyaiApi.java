package simbot.example.BootAPIUse.MlyaiAPIUSR;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import lombok.Data;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;

/**
 * @author zeng
 * @date 2022/8/21 20:54
 * @user 86188
 */
@Data
@Component
@ConfigurationProperties(prefix = "mlyaiuse")
public class MlyaiApi extends MlyaiConstant {

    public String apiKey;

    public String apisecret;

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public void setApisecret(String apisecret) {
        this.apisecret = apisecret;
    }

    public String chat(String content, String id, String fromName, Integer type, String groupId, String replyMember) throws IOException {
        // 去除消息中的空格
        // 将中文空格替换为英文空格
        content = content.trim();
        content = content.replace((char) 12288, ' ');

        JSONObject body = new JSONObject();

        body.put("content", content);
        // 消息类型，1：私聊，2：群聊
        body.put("type", type);
        body.put("from", id);
        body.put("fromName", fromName);
        body.put("to", groupId);
        body.put("toName", replyMember);
        System.out.println(body);

        HashMap<String, String> headers = new HashMap<>(3);
        headers.put("Content-Type", "application/json;charset=UTF-8");
        headers.put("Api-Key", apiKey);
        headers.put("Api-Secret", apisecret);
        String jsonStr = HttpUtil.createPost("https://api.mlyai.com/reply").addHeaders(headers).body(JSON.toJSONString(body)).execute().body();

        //JSONObject从String中得到数据，提取数据，并且输出数据
        //从字符串转成java代码
        net.sf.json.JSONObject jsonObj = net.sf.json.JSONObject.fromObject(jsonStr);
        String code = jsonObj.getString("code");

        System.out.println(jsonObj);

        System.out.println(code);

        if (codeOk.equals(code)) {
            String data = jsonObj.getString("data");
            JSONArray jsonArray;
            jsonArray = new JSONArray(data);

            System.out.println(jsonArray.getJSONObject(0).getString("content"));

            return jsonArray.getJSONObject(0).getString("content");
        } else {
            return face2;
        }
    }
}
