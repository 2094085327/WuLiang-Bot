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

//    public boolean checkCookie(String cookie) {
//
//        HashMap<String, String> headers = new HashMap<>(3);
//        headers.put("DS", HeaderParams.getDS());
//        headers.put("Origin", "https://webstatic.mihoyo.com");
//        headers.put("x-rpc-app_version", "2.34.1");
//        headers.put("x-rpc-client_type", "5");
//        headers.put("Referer", "https://webstatic.mihoyo.com/");
//        headers.put("Cookie", cookie);
//
//        try {
//            String signResult = HttpUtil.createGet(SignConstant.HOME_URL).addHeaders(headers).execute().body();
//            System.out.println(signResult);
//
//            //从字符串转成json代码
//            JSONObject jsonObj = JSONObject.fromObject(signResult);
//
//            String retCode = jsonObj.getString("retcode");
//
//            //通过则返回true
//            if (Objects.equals(retCode, SignConstant.RETCODE3)) {
//                return true;
//            } else {
//                GenShinSign.message = "你还没有登陆米游社哦，这个cookie是无效的";
//                return false;
//            }
//
//        } catch (Exception e) {
//            return false;
//        }
//    }

    // GenShinSign genShinSign = new GenShinSign();
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


        // int retcode = result.getJSONObject("data").getJSONArray("list").getJSONObject(0).get("game_uid");

        // String uid = (String) result.getJSONObject("data").getJSONArray("list").getJSONObject(0).get("game_uid");
        // String nickname = (String) result.getJSONObject("data").getJSONArray("list").getJSONObject(0).get("nickname");

        // System.out.println("cookie对应的uid：" + uid);
        // System.out.println("cookie对应的昵称：" + nickname);


    }

    public static void main(String[] args) {
        CookieStore cookieStore = new CookieStore();
        cookieStore.checkCookie("_MHYUUID=39df236e-6d5f-495a-85be-d6ecbc4e8f22; _ga=GA1.2.1612099045.1660810790; UM_distinctid=182b009c4024ab-0cdcbfecb8fde-45647f52-e1000-182b009c4031244; smidV2=20220818162006aa7bc9bb642ed0682890332030e1ba6d00592b55a1d08fef0; ltoken=7rFXfB1hAvRNJPwM6JCo5ZecJEgJTh9zprkxtUtas; ltuid=296624897; CNZZDATA1275023096=1906506578-1660810411-https%253A%252F%252Fbbs.mihoyo.com%252F%7C1661753985; _gid=GA1.2.821339619.1661755632; Hm_lvt_d7c7037093938390bc160fc28becc542=1660811388,1661755639,1661755693,1661756163; Hm_lpvt_d7c7037093938390bc160fc28becc542=1661756163; .thumbcache_a5f2da7236017eb7e922ea0d742741d5=fqfcOFdKR42bXfjJ8tLzYnlpaEgTtRarBU7lg/dNSo0A3B9szkPZXNKPnSCfucDEa5UIqEWpkeHPaa/HFkbjEA%3D%3D; _gat=1");
    }
}
