package simbot.example.BootAPIUse.YuanShenAPI.Sign;


import com.alibaba.fastjson.JSONObject;
import simbot.example.Util.HttpUtils;

/**
 * @author zeng
 * @date 2022/8/18 15:48
 * @user 86188
 */
public class CookieStore {

    public static String cookie;

    public static void setCookie(String cookie) {
        CookieStore.cookie = cookie;
    }

    public static String getCookie() {
        return cookie;
    }

    /**
     * 检查传入的cookie
     *
     * @param cookie cookie值
     * @return 返回判断结果
     */
    public boolean checkCookie(String cookie) {
        CookieStore.setCookie(cookie);
        try {
            JSONObject result = HttpUtils.doGet(String.format("https://api-takumi.mihoyo.com/binding/api/getUserGameRolesByCookie?game_biz=%s", "hk4e_cn"), HeaderBuilder.getBasicHeaders());

            int retcode = result.getInteger("retcode");
            if (retcode == SignConstant.RETCODE3) {
                return true;
            } else {
                GenShinSign.message = "你还没有登陆米游社哦，这个cookie是无效的";
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }
}