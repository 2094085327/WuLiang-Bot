package simbot.example.BootAPIUse.OtherAPI;

import cn.hutool.http.HttpUtil;
import net.sf.json.JSONObject;
import org.json.JSONArray;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import simbot.example.core.common.Constant;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author zeng
 * @date 2022/6/24 16:12
 * @user 86188
 */
@Component
@ConfigurationProperties(prefix = "artificial")
public class OtherApi extends Constant {

    public String key;

    public String url;

    /**
     * 每日一言APi-通过正则匹配获取HTML格式的信息
     *
     * @return 返回从api获得的每日一言
     */
    public String everydaySentences() {

        // 因为不需要推送参数，所以直接使用get方法
        String jsonStr = HttpUtil.get("https://v.api.aa1.cn/api/yiyan/index.php");

        // 匹配标签<p>
        String reg = ".*?(<(p>)(.*)</\\2).*";
        Pattern pattern = Pattern.compile(reg);
        // 指定要匹配的字符串
        Matcher matcher = pattern.matcher(jsonStr);
        if (matcher.find()) {
            System.out.println(matcher.group(3));
            return matcher.group(3);
        } else {

            return "没什么想说的呢~";
        }
    }

    /**
     * 青年大学习API
     *
     * @return 返回当期青年大学习的题目与答案
     */
    public String youthStudy() {
        return HttpUtil.createGet("https://api.klizi.cn/API/other/youth.php").execute().body();
    }

    /**
     * 二刺螈图片(P站图片，随机选择是否发送涩图)
     *
     * @return 返回获取到的P站图片url
     */

    public String twoDimensional() {
        int random = (int) (Math.random() * 3);
        String url = "";
        switch (random) {
            case 0:
                url = "https://api.lolicon.app/setu/v2?tag=萝莉|少女&tag=白丝|黑丝";
                break;
            case 1:
            case 2:
                url = "https://api.lolicon.app/setu/v2?tag=原神&tag=白丝|黑丝";
                break;
            default:
        }

        try {
            String jsonStr = HttpUtil.get(url);
            JSONObject object = JSONObject.fromObject(jsonStr);
            String data = object.getString("data");
            JSONArray jsonArray;
            jsonArray = new JSONArray(data);
            System.out.println(jsonArray.getJSONObject(0).getJSONObject("urls"));

            String title = jsonArray.getJSONObject(0).getString("title");
            String author = jsonArray.getJSONObject(0).getString("author");
            boolean r18 = jsonArray.getJSONObject(0).getBoolean("r18");
            JSONArray tags = jsonArray.getJSONObject(0).getJSONArray("tags");

            String urls = jsonArray.getJSONObject(0).getJSONObject("urls").getString("original");
            this.url = urls;

            return "Title:" + title + "\n\nAuthor:" + author + "\n\nR18:" + r18 + "\n\nTags:" + tags + "\n\nURL:" + urls;
        } catch (Exception e) {
            this.url = "https://gchat.qpic.cn/gchatpic_new/2094085327/2083469072-2232305563-72311C09F00D0DBEF47CF5B070311E46/0?term&#61;2";
            return "啊哦~涩图不见了呢";
        }

    }

    /**
     * B站直播检测
     *
     * @param uid 输入up主的uid来检测齐直播间状态
     * @return 返回直播状态，直播信息等
     */
    public String bLive(String uid) {
        try {
            String url = "https://api.live.bilibili.com/xlive/web-room/v1/index/getRoomBaseInfo?uids=" +
                    uid + "&req_biz=video";
            String jsonStr = HttpUtil.get(url);
            JSONObject object = JSONObject.fromObject(jsonStr);
            JSONObject data = object.getJSONObject("data");
            JSONObject byUids = data.getJSONObject("by_uids");
            JSONObject id = byUids.getJSONObject(uid);
            String liveStatus = id.getString("live_status");
            String roomId = id.getString("room_id");
            String title = id.getString("title");
            String liveTime = id.getString("live_time");
            String msg = "";
            String state = "1";
            if (state.equals(liveStatus)) {
                msg += "直播间正在直播中\n";
                BLIVESTATE = "false";
                SendTwice = "false";
                System.out.println(data);
                msg += "房间号:" + roomId + "\n房间名:" + title + "\n直播开始时间:" + liveTime;
                return msg;
            } else {
                BLIVESTATE = "false";
                return "";
            }
        } catch (Exception e) {
            BLIVESTATE = "false";
            return "没有在直播";
        }

    }

    /**
     * B站直播检测
     *
     * @param uid 输入up主的uid来检测齐直播间状态
     */
    public void bLiveHelp(String uid) {
        try {
            String url = "https://api.live.bilibili.com/xlive/web-room/v1/index/getRoomBaseInfo?uids="
                    + uid + "&req_biz=video";
            String jsonStr = HttpUtil.get(url);
            JSONObject object = JSONObject.fromObject(jsonStr);
            JSONObject data = object.getJSONObject("data");
            JSONObject byUids = data.getJSONObject("by_uids");
            JSONObject id = byUids.getJSONObject(uid);
            String liveStatus = id.getString("live_status");
            String state = "0";
            if (state.equals(liveStatus)) {
                BLIVESTATE = "true";
            } else {
                BLIVESTATE = "false";
            }
        } catch (Exception e) {

            BLIVESTATE = "false";

        }
    }

    /**
     * 人工智能语音APi
     *
     * @param message 接收传递来的消息
     * @return 当能够正常处理时返回AI的自动回复，否则当catch到异常时返回猫猫码狗头
     */
    public String record(String message) {
        // 去除消息中的空格
        // 将中文空格替换为英文空格
        message = message.trim();
        message = message.replace((char) 12288, ' ');

        // 接口地址

        return "https://fanyi.sogou.com/reventondc/synthesis?text=" + message + "&speed=1&lang=zh-CHS&from=translateweb&speaker=2";

    }

}