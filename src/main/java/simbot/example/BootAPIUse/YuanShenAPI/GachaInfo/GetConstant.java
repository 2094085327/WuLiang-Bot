package simbot.example.BootAPIUse.YuanShenAPI.GachaInfo;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description: 获取原神常驻池
 * @Author zeng
 * @Date 2022/10/2 17:08
 * @User 86188
 */
@Service
public class GetConstant {
    public static String constantUrl = "https://webstatic.mihoyo.com/admin/mi18n/hk4e_cn/m07291724171161/m07291724171161-zh-cn.json";

    public static Pattern pattern = Pattern.compile("novice_item_(\\d+)");

    public static ArrayList<String> yuanConstant = new ArrayList<>();

    @Autowired
    public GetConstant() {
        getConstant();
        System.out.println("--常驻信息加载成功--");
    }

    public static void getConstant() {
        // 请求json数据
        String jsonStr = HttpUtil.get(constantUrl);
        JSONObject jsonObject = JSONObject.parseObject(jsonStr);

        // 遍历获取json的key
        for (String itemKey : jsonObject.keySet()) {
            Matcher matcher = pattern.matcher(itemKey);
            if (matcher.find()) {
                // 通过正则进行匹配
                String value = jsonObject.getString(matcher.group());
                yuanConstant.add(value);
            }
            yuanConstant.add(Arrays.toString(YuanConstant.permanentInfo));
        }
    }

    public static void main(String[] args) {
        getConstant();
        System.out.println(yuanConstant.contains("琴"));
    }
}

