package simbot.example.BootAPIUse.YuanShenAPI.Sign;


import com.alibaba.fastjson.JSONObject;
import org.json.JSONArray;
import org.springframework.stereotype.Component;
import simbot.example.Util.HttpUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author zeng
 * @date 2022/8/18 14:19
 * @user 86188
 */
@Component
public class GenShinSign {
    public static String uid;
    public static String qqId;
    public static String nickName;
    public static String message;
    public static Integer push;
    public static Integer deletes;
    public static String itemMsg;
    public static String itemImg;

    public static void setMessage(String message) {
        GenShinSign.message = message;
    }

    public static String getItemImg() {
        return itemImg;
    }

    public static void setItemImg(String itemImg) {
        GenShinSign.itemImg = itemImg;
    }

    public static String getItemMsg() {
        return itemMsg;
    }

    public static void setItemMsg(String itemMsg) {
        GenShinSign.itemMsg = itemMsg;
    }

    public static void setDeletes(Integer deletes) {
        GenShinSign.deletes = deletes;
    }

    public static void setQqId(String qqId) {
        GenShinSign.qqId = qqId;
    }

    public static void setPush(Integer push) {
        GenShinSign.push = push;
    }

    public static Integer getPush() {
        return push;
    }

    public String getUid() {
        return uid;
    }

    public String getQqId() {
        return qqId;
    }

    public String getNickName() {
        return nickName;
    }

    public static String getMessage() {
        return message;
    }

    CookieStore cookieStore = new CookieStore();

    /**
     * 获取cookie对应的Uid
     */
    public void checkUid() {
        JSONObject result = HttpUtils.doGet(String.format("https://api-takumi.mihoyo.com/binding/api/getUserGameRolesByCookie?game_biz=%s", "hk4e_cn"), HeaderBuilder.getBasicHeaders());
        String uid = (String) result.getJSONObject("data").getJSONArray("list").getJSONObject(0).get("game_uid");
        String nickname = (String) result.getJSONObject("data").getJSONArray("list").getJSONObject(0).get("nickname");

        System.out.println("cookie对应的uid：" + uid);
        System.out.println("cookie对应的昵称：" + nickname);

        GenShinSign.uid = uid;
        nickName = nickname;
    }


    /**
     * 签到
     */
    public boolean doSign(String uid) {

        if (cookieStore.checkCookie(CookieStore.getCookie())) {

            Map<String, Object> data = new HashMap<>(3);
            data.put("act_id", SignConstant.ACT_ID);
            data.put("region", SignConstant.REGION);
            data.put("uid", uid);
            JSONObject signResult = HttpUtils.doPost(SignConstant.SIGN_URL, HeaderBuilder.getHeaders(), data);
            System.out.println(signResult);
            int retcode = signResult.getInteger("retcode");
            if (retcode == SignConstant.RETCODE1) {
                message = "cookie错误或者你还没有登陆米游社哦，这个cookie是无效的";
                return false;
            }
            if (retcode == SignConstant.RETCODE2) {
                message = "你今天已经签到过了哦，明天在来吧！";
                return true;
            }
            if (retcode == SignConstant.RETCODE3) {
                message = "签到成功";
                return true;
            }else {
                message = "未知错误，错误码:"+retcode;
                return false;
            }

        }
        return false;
    }

    /**
     * 奖励列表
     *
     * @param uid UID
     */
    public void signList(String uid) {

        Map<String, Object> data = new HashMap<>(3);
        data.put("act_id", SignConstant.ACT_ID);
        data.put("region", SignConstant.REGION);
        data.put("uid", uid);

        JSONObject signInfoResult = HttpUtils.doGet(SignConstant.INFO_URL, HeaderBuilder.getHeaders(), data);

        JSONObject listInfoResult = HttpUtils.doGet(SignConstant.LIST_URL, HeaderBuilder.getHeaders(), data);

        int totalSignDay = signInfoResult.getJSONObject("data").getInteger("total_sign_day") - 1;

        JSONObject data2 = listInfoResult.getJSONObject("data");
        String awards = data2.getString("awards");
        JSONArray jsonArray;
        jsonArray = new JSONArray(awards);

        String itemName = jsonArray.getJSONObject(totalSignDay).getString("name");
        int itemCnt = jsonArray.getJSONObject(totalSignDay).getInt("cnt");


        System.out.println(jsonArray.getJSONObject(totalSignDay).getString("name"));
        System.out.println(jsonArray.getJSONObject(totalSignDay).getInt("cnt"));

        String img = jsonArray.getJSONObject(totalSignDay).getString("icon");

        setItemMsg("今天获取的奖励是:" + itemName + "X" + itemCnt);
        setItemImg(img);
        //return isSign;
    }

}
