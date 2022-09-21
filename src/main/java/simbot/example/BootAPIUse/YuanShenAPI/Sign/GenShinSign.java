package simbot.example.BootAPIUse.YuanShenAPI.Sign;


import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import simbot.example.Mapper.GenShinMapper;
import simbot.example.Util.HttpUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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

    GenShinMapper genShinMapper;

    @Autowired
    public GenShinSign(GenShinMapper genShinMapper) {
        this.genShinMapper = genShinMapper;
    }

    public GenShinSign() {
    }

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

            String firstUid = uid.substring(0, 1);


            // UID首位1 2为官服
            if (SignConstant.FIRSTUID1.equals(firstUid) || SignConstant.FIRSTUID2.equals(firstUid)) {
                data.put("region", SignConstant.REGION);
            }
            // 5为B服
            if (SignConstant.FIRSTUID3.equals(firstUid)) {
                data.put("region", SignConstant.REGION2);
            }
            data.put("uid", uid);
            JSONObject signResult = HttpUtils.doPost(SignConstant.SIGN_URL, HeaderBuilder.getHeaders(), data);
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
            } else {
                message = "未知错误，错误码:" + retcode;
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

        String img = jsonArray.getJSONObject(totalSignDay).getString("icon");

        loadImg(img);
        SignPicture.signImg("X" + itemCnt, String.valueOf(totalSignDay + 1));

        setItemMsg("今天获取的奖励是:[" + itemName + "X" + itemCnt + "]");
        setItemImg(img);
    }

    /**
     * 缓存图片
     *
     * @param url 图片路径
     */
    public void loadImg(String url) {
        try {
            // 构造URL
            URL imgUrl = new URL(url);
            // 打开连接
            URLConnection con = imgUrl.openConnection();
            // 输入流
            InputStream is = con.getInputStream();
            // 1K的数据缓冲
            byte[] bs = new byte[1024];
            // 读取到的数据长度

            int len;

            // 输出的文件流
            File file = new File("resources/yuanImage/登录奖励/itemImg.png").getAbsoluteFile();

            FileOutputStream os = new FileOutputStream(file, true);

            // 开始读取
            while ((len = is.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
            // 完毕，关闭所有链接
            os.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("下载签到奖励失败");
        }
    }
}
