package simbot.example.BootAPIUse.YuanShenAPI.GachaInfo;

import cn.hutool.http.HttpUtil;
import net.sf.json.JSONObject;
import org.json.JSONArray;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author zeng
 * @date 2022/8/6 18:17
 * @user 86188
 */
public class YuanApi extends YuanConstant {

    public static double finProbability;
    public static int finFiveStar;
    public static float finCount;
    public static float poolCount = 3;

    /**
     * 获取解析后的真实URL并加入参数
     *
     * @param url 输入的未经过解析的URL
     * @return 返回解析后的URL
     */
    public static String toUrl(String url) {
        // 中文正则，去除链接中的中文
        String regexChinese = "[\u4e00-\u9fa5]";
        // 获取无中文的链接
        String noChineseUrl = url.replaceAll(regexChinese, "");
        // 从"#"处分割链接去除链接中的[#/log]并获取分割后的链接
        String splitUrl1 = noChineseUrl.split("#")[0];
        // 从[?]处分割链接以拼接到接口链接上
        String splitUrl2 = splitUrl1.split("\\?")[1];
        // 含参链接

        return "https://hk4e-api.mihoyo.com/event/gacha_info/api/getGachaLog?" + splitUrl2;
    }

    /**
     * 对关键参数进行拼接
     *
     * @param gachaType 抽卡类型
     * @param url       未拼接的链接
     * @param times     页数
     * @return 返回拼接后的URL
     */
    public static String getUrl(String url, String gachaType, int times, String endId) {

        String urls = toUrl(url) + "&gacha_type=301&page=1&size=20&end_id=0";
        // 通过正则获取关键字
        Pattern pattern2 = Pattern.compile("([\\s\\S]*)" + "gacha_type" + "=([^&]*)" + "([\\s\\S]*)" + "page=" + "([^&]*)" + "([\\s\\S]*)" + "end_id=");
        Matcher matcher2 = pattern2.matcher(urls);

        if (matcher2.find()) {
            String newUrl;
            newUrl = matcher2.group(1) + "gacha_type=" + gachaType + matcher2.group(3) + "page=" + times + matcher2.group(5) + "end_id=" + endId;

            // 返回拼接后的api链接
            return newUrl;
        }
        return "";
    }

    /**
     * 对URL进行检查以判断过期或错误等情况
     *
     * @param url 需要检查的URL
     * @return 返回检查后的状态
     */
    public static String checkApi(String url) {
        String urls = toUrl(url);
        try {
            // 请求json数据
            String jsonStr = HttpUtil.get(urls);
            JSONObject jsonObject = JSONObject.fromObject(jsonStr);
            String message = jsonObject.getString("message");
            if (YuanConstant.APISTATE1.equals(message)) {
                return "链接有错误哦，请重新获取链接给无量姬！";
            }
            if (YuanConstant.APISTATE2.equals(message)) {
                return "链接过期啦，请重新获取链接给无量姬！";
            }
            if (YuanConstant.APISTATE3.equals(message)) {
                return "OK";
            } else {
                return "数据为空，错误代码：" + message;
            }
        } catch (Exception e) {
            System.out.println("API请求解析错误：" + e);
        }
        return null;
    }

    public static void getGachaRoleInfo(String url) throws Exception {
        Robot r = new Robot();

        // 5星个数
        int fiveStar = 0;
        // 总抽数
        int count = 0;

        // 本页最后一条数据的id
        String endId = "0";

        // 大保底次数
        int guaranteedCount = 0;

        // 至五星为止的次数
        int fiveGachaCount = 0;

        int alreadyCost = 0;

        // 五星角色与对应抽数
        ArrayList<String> fivePeopleCount = new ArrayList<>();

        for (int i = 1; i <= 9999; i++) {
            // 接口URL地址
            String urls = YuanApi.getUrl(url, "301", i, endId);

            // 请求json数据
            String jsonStr = HttpUtil.get(urls);
            JSONObject jsonObject = JSONObject.fromObject(jsonStr);
            JSONObject data = jsonObject.getJSONObject("data");

            // 创建list数组
            String list = data.getString("list");
            JSONArray jsonArray;
            jsonArray = new JSONArray(list);
            // 数组长度
            int length = jsonArray.length();
            // 总抽数
            count += length;

            // 当数组长度为0时(即没有抽卡记录时)跳出循环
            if (length == 0) {
                break;
            }
            // 获取当前页最后一个数据的id以进行翻页
            endId = jsonArray.getJSONObject(length - 1).getString("id");

            // 对当前页20个数据进行遍历
            for (int j = 0; j < length; j++) {
                fiveGachaCount += 1;
                // 抽卡等级
                String rankType = jsonArray.getJSONObject(j).getString("rank_type");
                // 抽到的物品名
                String name = jsonArray.getJSONObject(j).getString("name");

                if ("5".equals(rankType)) {

                    // 已经垫池子的抽数
                    if (fiveStar == 0) {
                        alreadyCost = fiveGachaCount - 1;
                    }

                    // 5星
                    fiveStar += 1;
                    if ("刻晴".equals(name) || "迪卢克".equals(name) || "七七".equals(name) || "琴".equals(name) || "莫娜".equals(name)) {
                        guaranteedCount += 1;

                        // 将歪的角色的抽数与姓名加入list
                        fivePeopleCount.add(String.valueOf(fiveGachaCount));
                        fivePeopleCount.add(name + "(歪)");
                    } else {

                        // 没歪的角色姓名与抽数
                        fivePeopleCount.add(String.valueOf(fiveGachaCount));
                        fivePeopleCount.add(name);
                    }
                    // 将五星计数归零
                    fiveGachaCount = 0;
                }

            }
            // 延迟500ms避免被ban
            r.delay(500);
        }
        fivePeopleCount.add(String.valueOf(fiveGachaCount));

        String limited = fiveStar - guaranteedCount + "/" + fiveStar;

        // 小保底概率
        float guaranteedP = (float) (fiveStar - guaranteedCount) / fiveStar;

        // 每个五星平均花费
        float averageFive = (float) (count - alreadyCost - fiveStar) / fiveStar;
        String averageFiveString = String.format("%.1f", averageFive);

        // 欧非判断公式 以(1-平均出金数/90)*50%+(不歪的几率*50%) 作为概率
        double probability = (1 - (averageFive / 80) + guaranteedP * 0.5) * 100;
        if (rangeInDefined(probability)) {
            probability = 0;
            averageFiveString = "--";
            alreadyCost = count;
            poolCount--;
        }


        finCount += count;
        finFiveStar += fiveStar;
        finProbability += probability * (1 / poolCount);

        picture.rolePole(averageFiveString, String.valueOf(count), fivePeopleCount, String.valueOf(alreadyCost), limited, probability);

        System.out.println("——角色池完成——");
    }

    /**
     * 获取武器池信息
     *
     * @param url 待分析的链接
     * @throws Exception 可能存在的异常
     */
    public static void getGachaArmsInfo(String url) throws Exception {
        Robot r = new Robot();

        String endId = "0";
        int count = 0;
        // 大保底次数
        int guaranteedCount = 0;

        // 至五星为止的次数
        int fiveGachaCount = 0;

        int alreadyCost = 0;

        // 5星个数
        int fiveStar = 0;

        // 五星角色与对应抽数
        ArrayList<String> fivePeopleCount = new ArrayList<>();
        int page = 9999;
        for (int i = 1; i < page; i++) {
            String newUrl = getUrl(url, "302", i, endId);
            // 请求json数据
            String jsonStr = HttpUtil.get(newUrl);
            JSONObject jsonObject = JSONObject.fromObject(jsonStr);
            JSONObject data = jsonObject.getJSONObject("data");

            // 创建list数组
            String list = data.getString("list");
            JSONArray jsonArray;
            jsonArray = new JSONArray(list);
            // 数组长度
            int length = jsonArray.length();
            // 总抽数
            count += length;

            // 当数组长度为0时(即没有抽卡记录时)跳出循环
            if (length == 0) {
                break;
            }

            // 获取当前页最后一个数据的id以进行翻页
            endId = jsonArray.getJSONObject(length - 1).getString("id");


            // 对当前页20个数据进行遍历
            for (int j = 0; j < length; j++) {
                fiveGachaCount += 1;
                // 抽卡等级
                String rankType = jsonArray.getJSONObject(j).getString("rank_type");
                // 抽到的物品名
                String name = jsonArray.getJSONObject(j).getString("name");


                if ("5".equals(rankType)) {

                    if (fiveStar == 0) {
                        alreadyCost = fiveGachaCount - 1;
                    }

                    // 5星
                    fiveStar += 1;

                    int banArms = (int) Arrays.stream(gachaArmsInfo).filter(name::contains).count();
                    if (banArms == 1) {
                        guaranteedCount += 1;

                        // 将歪的角色的抽数与姓名加入list
                        fivePeopleCount.add(String.valueOf(fiveGachaCount));
                        fivePeopleCount.add(name + "(歪)");

                    } else {

                        // 没歪的角色姓名与抽数
                        fivePeopleCount.add(String.valueOf(fiveGachaCount));
                        fivePeopleCount.add(name);

                    }
                    // 将五星计数归零
                    fiveGachaCount = 0;
                }
            }
            r.delay(500);
            // 延迟500ms避免被ban
        }
        fivePeopleCount.add(String.valueOf(fiveGachaCount));

        String limited = fiveStar - guaranteedCount + "/" + fiveStar;


        // 小保底概率
        float guaranteedP = (float) (fiveStar - guaranteedCount) / fiveStar;

        // 每个五星平均花费
        float averageFive = (float) (count - alreadyCost - fiveStar) / fiveStar;
        String averageFiveString = String.format("%.1f", averageFive);

        // 欧非判断公式 以(1-平均出金数/90)+(不歪的几率*50%) 作为概率
        double probability = (1 - (averageFive / 80) + guaranteedP * 0.5) * 100;
        if (rangeInDefined(probability)) {
            probability = 0;
            averageFiveString = "--";
            alreadyCost = count;
            poolCount--;
        }

        picture.armsPole(averageFiveString, String.valueOf(count), fivePeopleCount, String.valueOf(alreadyCost), limited, probability);

        finCount += count;
        finFiveStar += fiveStar;
        finProbability += probability * (1 / poolCount);

        System.out.println("——武器池完成——");
    }

    /**
     * 获得常驻池信息
     *
     * @param url 待分析的链接
     * @throws Exception 可能的异常
     */
    public static void getGachaPermanentInfo(String url) throws Exception {
        Robot r = new Robot();

        String endId = "0";
        int count = 0;

        // 至五星为止的次数
        int fiveGachaCount = 0;

        int alreadyCost = 0;

        // 5星个数
        int fiveStar = 0;


        // 五星角色与对应抽数
        ArrayList<String> fivePeopleCount = new ArrayList<>();

        for (int i = 1; i < 9999; i++) {
            String newUrl = getUrl(url, "200", i, endId);

            // 请求json数据
            String jsonStr = HttpUtil.get(newUrl);
            JSONObject jsonObject = JSONObject.fromObject(jsonStr);
            JSONObject data = jsonObject.getJSONObject("data");

            // 创建list数组
            String list = data.getString("list");
            JSONArray jsonArray;
            jsonArray = new JSONArray(list);
            // 数组长度
            int length = jsonArray.length();
            // 总抽数
            count += length;

            // 当数组长度为0时(即没有抽卡记录时)跳出循环
            if (length < 1) {
                break;
            }

            // 获取当前页最后一个数据的id以进行翻页
            endId = jsonArray.getJSONObject(length - 1).getString("id");

            // 对当前页20个数据进行遍历
            for (int j = 0; j < length; j++) {
                fiveGachaCount += 1;
                // 抽卡等级
                String rankType = jsonArray.getJSONObject(j).getString("rank_type");
                // 抽到的物品名
                String name = jsonArray.getJSONObject(j).getString("name");


                if ("5".equals(rankType)) {

                    if (fiveStar == 0) {
                        alreadyCost = fiveGachaCount - 1;
                    }

                    // 5星
                    fiveStar += 1;

                    // 将歪的角色的抽数与姓名加入list
                    fivePeopleCount.add(String.valueOf(fiveGachaCount));
                    fivePeopleCount.add(name + "(歪)");

                    // 将五星计数归零
                    fiveGachaCount = 0;
                }
            }
            r.delay(500);
            // 延迟500ms避免被ban
        }
        fivePeopleCount.add(String.valueOf(fiveGachaCount));

        // 每个五星平均花费
        float averageFive = (float) (count - alreadyCost - fiveStar) / fiveStar;
        String averageFiveString = String.format("%.1f", averageFive);

        // 欧非判断公式
        double probability = (1 - (averageFive / 90)) * 100;
        if (rangeInDefined(probability)) {
            probability = 0;
            averageFiveString = "--";
            alreadyCost = count;
            poolCount--;
        }

        picture.permanentPool(averageFiveString, String.valueOf(count), fivePeopleCount, String.valueOf(alreadyCost), String.valueOf(fiveStar), probability);

        finCount += count;
        finFiveStar += fiveStar;
        finProbability += probability * (1 / poolCount);

        System.out.println("——常驻池完成——");

    }


    public static void allData1() throws IOException {

        String aveFive = String.format("%.1f", (finCount - finFiveStar) / finFiveStar);
        System.out.println(aveFive);
        picture.allDataMake(aveFive, String.valueOf(String.format("%.0f", finCount)), String.valueOf(finFiveStar), finProbability);

    }

    /**
     * 判断概率范围
     *
     * @param current 要判断的值
     * @return 返回判断
     */
    public static boolean rangeInDefined(double current) {
        return Math.max(0, current) != Math.min(current, 150);
    }


}

