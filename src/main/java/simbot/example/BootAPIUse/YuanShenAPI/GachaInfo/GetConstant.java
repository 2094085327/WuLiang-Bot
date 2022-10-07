package simbot.example.BootAPIUse.YuanShenAPI.GachaInfo;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * @Description: 获取原神常驻池
 * @Author zeng
 * @Date 2022/10/2 17:08
 * @User 86188
 */
@Service
public class GetConstant {

    public static String constantUrl1 = "https://webstatic.mihoyo.com/hk4e/gacha_info/cn_gf01/e26ce9efdc5b0b3193edf2aa4736c04983a37a4d/zh-cn.json?ts=1664323645";

    public static String constantUrl2 = "https://webstatic.mihoyo.com/hk4e/gacha_info/cn_gf01/f7a8af0d264c59be8820fc27339ba2700d9c42e0/zh-cn.json?ts=1664323722";

    public static ArrayList<String> yuanConstant = new ArrayList<>();
    public static ArrayList<String> fiveLevelUp = new ArrayList<>();

    @Autowired
    public GetConstant() {
        getConstant();
        System.out.println("--常驻信息加载成功--");
    }

    public static void getConstant() {
        // 请求json数据
        String jsonStr = HttpUtil.get(constantUrl1);
        String jsonStr2 = HttpUtil.get(constantUrl2);

        addItem(jsonStr);
        addItem(jsonStr2);
    }

    /**
     * 将常驻物品加入其中
     * @param jsonStr 获取到的字符串
     */
    public static void addItem(String jsonStr) {
        JSONObject jsonObject = JSONObject.parseObject(jsonStr);
        JSONArray jsonArray = jsonObject.getJSONArray("r5_prob_list");

        for (int i = 0; i < jsonArray.size(); i++) {
            if (jsonArray.getJSONObject(i).getInteger("is_up") != 1) {
                yuanConstant.add(jsonArray.getJSONObject(i).getString("item_name"));
            }else {
                fiveLevelUp.add(jsonArray.getJSONObject(i).getString("item_name"));
            }
        }
    }

    public static void main(String[] args) {
        getConstant();
    }
}

