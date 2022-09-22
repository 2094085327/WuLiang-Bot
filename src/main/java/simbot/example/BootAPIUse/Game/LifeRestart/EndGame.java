package simbot.example.BootAPIUse.Game.LifeRestart;

import net.coobird.thumbnailator.Thumbnails;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Map;

/**
 * @Description: 游戏结束后的相关操作
 * @Author zeng
 * @Date 2022/9/21 14:48
 * @User 86188
 */
public class EndGame {
    public static void makePicture(String userId, String age, Map<String, Integer> attributes, String urls) {
        int dieAge = Integer.parseInt(age);

        try {
            //读取图片2
            BufferedImage backImg = ImageIO.read(new File("resources/GameRes/image/backImg.png").getAbsoluteFile());
            //用a创建绘画对象
            Graphics2D gd = backImg.createGraphics();
            BufferedImage line = ImageIO.read(new File("resources/GameRes/image/grey.png").getAbsoluteFile());
            int n = 0;
            String msg = "";
            for (Map.Entry<String, Integer> entry : attributes.entrySet()) {
                int value = entry.getValue();

                if (value <= 4) {
                    msg = "     少年你寄了哟~";
                    line = ImageIO.read(new File("resources/GameRes/image/grey.png").getAbsoluteFile());
                }
                if (value > 4 && value <= 6) {
                    msg = "     一般一般";
                    line = ImageIO.read(new File("resources/GameRes/image/green.png").getAbsoluteFile());
                }
                if (value > 6 && value <= 8) {
                    msg = "     还不错";
                    line = ImageIO.read(new File("resources/GameRes/image/blue.png").getAbsoluteFile());
                }
                if (value > 8 && value <= 10) {
                    msg = "     大佬的实力";
                    line = ImageIO.read(new File("resources/GameRes/image/purple.png").getAbsoluteFile());
                }
                if (value > 10) {
                    msg = "     神仙！！！！！";
                    line = ImageIO.read(new File("resources/GameRes/image/gold.png").getAbsoluteFile());
                }
                gd.drawImage(line, 119, 484 + (103 + line.getHeight()) * n, line.getWidth(), line.getHeight(), null);
                // 设置画笔颜色为白色
                gd.setColor(Color.WHITE);
                // 设置画笔字体样式为微软雅黑，斜体，文字大小为50px
                gd.setFont(new Font("黑体", Font.PLAIN, 100));
                gd.drawString(replace(entry.getKey()) + entry.getValue(), 237, 645 + (103 + line.getHeight()) * n);
                gd.drawString(msg, 790, 645 + (103 + line.getHeight()) * n);
                n += 1;
            }

            if (dieAge < 40) {
                msg = "     盛年";
                line = ImageIO.read(new File("resources/GameRes/image/grey.png").getAbsoluteFile());
            }
            if (dieAge >= 40) {
                msg = "     中年";
                line = ImageIO.read(new File("resources/GameRes/image/green.png").getAbsoluteFile());
            }
            if (dieAge >= 60) {
                msg = "     花甲";
                line = ImageIO.read(new File("resources/GameRes/image/blue.png").getAbsoluteFile());
            }
            if (dieAge >= 80) {
                msg = "     耄年";
                line = ImageIO.read(new File("resources/GameRes/image/purple.png").getAbsoluteFile());
            }
            if (dieAge > 100) {
                msg = "     神仙临凡";
                line = ImageIO.read(new File("resources/GameRes/image/gold.png").getAbsoluteFile());
            }

            gd.drawImage(line, 119, 2244, line.getWidth(), line.getHeight(), null);
            gd.drawString("享年:" + dieAge + "岁", 237, 2412);
            gd.drawString(msg, 790, 2412);

            gd.drawImage(roundImage(urls), 156, 135, roundImage(urls).getWidth(), roundImage(urls).getHeight(), null);

            gd.dispose();

            String path = "resources/GameRes/image/" + userId + ".png ";

            //保存新图片
            ImageIO.write(backImg, "png", new File(path).getAbsoluteFile());

            compress(userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String replace(String key) {
        switch (key) {
            case "STR":
                return "体质:";
            case "CHR":
                return "颜值:";
            case "INT":
                return "智力:";
            case "MNY":
                return "家境:";
            case "SPR":
                return "快乐:";
            default:
                return "--";
        }
    }


    public static void compress(String useId) {
        try {
            Thumbnails.of(new File("resources/GameRes/image/" + useId + ".png")).scale(0.25).toFile(new File("resources/GameRes/image/" + useId + ".png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     *
     * @param urls 头像链接
     * @return BufferedImage
     * @throws IOException IO异常
     */
    public static BufferedImage roundImage(String urls) throws IOException {
        BufferedImage bufferedImage = null;
        try {
            URL url = new URL(urls);
            bufferedImage = ImageIO.read(url);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("--人生重开模拟器制作圆形头像错误--");
        }

        BufferedImage outputImage = new BufferedImage(640, 640, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = outputImage.createGraphics();
        g2.setComposite(AlphaComposite.Src);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);
        g2.fill(new RoundRectangle2D.Float(0, 0, 640, 640, 640, 640));
        g2.setComposite(AlphaComposite.SrcAtop);
        g2.drawImage(bufferedImage, 0, 0, null);
        g2.dispose();
        return Thumbnails.of(outputImage).scale(0.5).asBufferedImage();

    }
}
