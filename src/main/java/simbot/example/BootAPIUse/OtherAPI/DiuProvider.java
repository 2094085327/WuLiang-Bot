package simbot.example.BootAPIUse.OtherAPI;

/**
 * @author zeng
 * @date 2022/7/25 14:44
 * @user 86188
 */

public enum DiuProvider {

    // 1.提供当前枚举类对象
    // 丢
    DIU("https://api.klizi.cn/API/ce/diu.php?qq="),
    // 拍
    PAI("https://api.xingzhige.com/API/paigua/?qq="),
    // 抓
    GRAB("https://api.xingzhige.com/API/grab/?qq="),
    // 抱
    BAO("https://api.xingzhige.com/API/baororo/?qq="),
    // 锤
    POUND("https://api.xingzhige.com/API/pound/?qq=");

    /**
     * 2.声明对象属性:private final修饰
     */
    private final String URL;

    DiuProvider(String url) {
        this.URL = url;
    }

    @Override
    public String toString() {

        return URL;
    }
}

