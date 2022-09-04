package simbot.example.core.common;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author zeng
 * @date 2022/6/22 15:35
 * @user 86188
 * @Description: 时间戳转换类
 */
public class TimeTranslate {

    public String tt() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date time1 = new Date();

        return simpleDateFormat.format(time1);
    }

}
