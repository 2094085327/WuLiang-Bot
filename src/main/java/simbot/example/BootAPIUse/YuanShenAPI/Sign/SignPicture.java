package simbot.example.BootAPIUse.YuanShenAPI.Sign;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * @Author zeng
 * @Date 2022/9/14 15:34
 * @User 86188
 * @Description:
 */
public class SignPicture {

    public static void signImg(String itemNumber, String sighDay) {
        try {
            //读取图片2
            BufferedImage sighBg = ImageIO.read(new File("resources/yuanImage/登录奖励/sighBg.png").getAbsoluteFile());

            BufferedImage item = ImageIO.read(new File("resources/yuanImage/登录奖励/itemImg.png").getAbsoluteFile());

            //用a创建绘画对象
            Graphics2D gd = sighBg.createGraphics();

            gd.drawImage(item, -6, 15, item.getWidth(), item.getHeight(), null);

            // 设置画笔颜色为黑色
            gd.setColor(Color.WHITE);
            // 设置画笔字体样式为微软雅黑，斜体，文字大小为50px
            gd.setFont(new Font("黑体",Font.PLAIN, 50));

            gd.drawString(itemNumber, 122f - (25f * itemNumber.length() / 2f), 285f);
            gd.setColor(new Color(170, 148, 111));
            gd.drawString("第" + sighDay + "天", 122f - (50f + (25f * sighDay.length() / 2f)), 341f);

            gd.dispose();

            //保存新图片
            ImageIO.write(sighBg, "png", new File("resources/yuanImage/登录奖励/all.png").getAbsoluteFile());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("合成签到奖励失败");
        }
    }
}
