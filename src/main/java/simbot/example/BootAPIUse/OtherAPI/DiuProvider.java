package simbot.example.BootAPIUse.OtherAPI;

/**
 * @author zeng
 * @date 2022/7/25 14:44
 * @user 86188
 */
//public class DiuProvider {
//
//    private String getUrl(Long code, Mode mode) {
//        String url = "";
//        switch (mode) {
//            case DIU:
//                url = String.valueOf(Mode.DIU) + code;
//                break;
//            case PAI:
//                url = String.valueOf(Mode.PAI) + code;
//                break;
//            default:
//        }
//
//        return url;
//
//    }
//
//    private String when() {
//        return null;
//    }

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

    // 2.声明对象属性:private final修饰
    private final String url;

    DiuProvider(String url) {
        this.url = url;
    }

    @Override
    public String toString() {

        return url;
    }
}
//}
