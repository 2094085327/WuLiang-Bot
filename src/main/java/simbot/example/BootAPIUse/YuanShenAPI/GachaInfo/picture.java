package simbot.example.BootAPIUse.YuanShenAPI.GachaInfo;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author zeng
 * @date 2022/8/7 18:09
 * @user 86188
 */

public class picture {

    /**
     * 最终将所有生成的图片合成一张
     *
     * @throws IOException 抛出IO流异常
     */
    public static void allPictureMake() throws IOException {

        // 项目路径
        File file = new File(System.getProperty("user.dir"));

        //读取总数据图片
        BufferedImage allData = ImageIO.read(new File(file + "\\src\\main\\resources\\yuanImage\\allData.png"));

        //读取角色池图片
        BufferedImage rolePool = ImageIO.read(new File(file + "\\src\\main\\resources\\yuanImage\\role pool.png"));

        //读取武器池图片
        BufferedImage armsPool = ImageIO.read(new File(file + "\\src\\main\\resources\\yuanImage\\arms pool.png"));

        //读取常驻池图片
        BufferedImage PermanentPool = ImageIO.read(new File(file + "\\src\\main\\resources\\yuanImage\\permanent pool.png"));


        ImageIcon sec = new ImageIcon(file + "\\src\\main\\resources\\yuanImage\\sec3.png");

        BufferedImage bi = new BufferedImage(3305, 500 + allData.getHeight() + rolePool.getHeight() + armsPool.getHeight() + PermanentPool.getHeight(), BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = bi.createGraphics();

        // 设置图片品质
        g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));

        System.out.println(allData.getHeight());
        System.out.println(rolePool.getHeight());
        System.out.println(armsPool.getHeight());
        System.out.println(PermanentPool.getHeight());

        g2d.drawImage(sec.getImage(), 0, 0, 3305, 500 + allData.getHeight() + rolePool.getHeight() + armsPool.getHeight() + PermanentPool.getHeight(), null);
        g2d.dispose();
        ImageIO.write(bi, "png", new File(file + "\\src\\main\\resources\\yuanImage\\sec1.png"));


        //读取总数据图片
        BufferedImage backImg = ImageIO.read(new File(file + "\\src\\main\\resources\\yuanImage\\sec1.png"));

        //用a创建绘画对象
        Graphics2D gd = backImg.createGraphics();

        //把总数据画上去
        gd.drawImage(allData, 100, 100, allData.getWidth(), allData.getHeight(), null);
        //把角色池画上去
        gd.drawImage(rolePool, 100, 1242, rolePool.getWidth(), rolePool.getHeight(), null);

        //把武器池画上去
        gd.drawImage(armsPool, 100, 1342 + rolePool.getHeight(), armsPool.getWidth(), armsPool.getHeight(), null);

        //把常驻池画上去
        gd.drawImage(PermanentPool, 100, 1442 + rolePool.getHeight() + armsPool.getHeight(), PermanentPool.getWidth(), PermanentPool.getHeight(), null);

        gd.dispose();

        //保存新图片
        ImageIO.write(backImg, "png", new File(file + "\\src\\main\\resources\\yuanImage\\finally.png"));
    }

    /**
     * 生成最终统计数据的图片
     *
     * @param averageFive 每个五星平均花费的抽数
     * @param all         总抽数
     * @param five        五星个数
     * @throws IOException IO流异常
     */
    public static void allDataMake(String averageFive, String all, String five, double probability) throws IOException {
        // 项目路径
        File file = new File(System.getProperty("user.dir"));

        //读取图片2
        BufferedImage withe = ImageIO.read(new File(file + "\\src\\main\\resources\\yuanImage\\withe.png"));


        //读取图片5
        BufferedImage king = choseLuck(probability);

        //读取图片4
        BufferedImage string1 = ImageIO.read(new File(file + "\\src\\main\\resources\\yuanImage\\string1.png"));

        //用a创建绘画对象
        Graphics2D gd = withe.createGraphics();

        //把图片c画上去
        assert king != null;
        gd.drawImage(king, 58, 58, king.getWidth(), king.getHeight(), null);
        //把图片d画上去
        gd.drawImage(string1, 1112, 100, string1.getWidth(), string1.getHeight(), null);

        // 设置画笔颜色为黑色
        gd.setColor(Color.BLACK);
        // 设置画笔字体样式为微软雅黑，斜体，文字大小为20px
        gd.setFont(new Font("微软雅黑", Font.ITALIC, 206));
        gd.drawString(averageFive, 1190, 770);
        gd.drawString(all, 1864, 770);
        gd.drawString(five, 2591, 770);


        //读取图片2
        // BufferedImage withe2 = ImageIO.read(new File(file + "\\src\\main\\resources\\yuanImage\\withe2.png"));

        // withe2.getScaledInstance(100, 100, Image.SCALE_DEFAULT);


        gd.dispose();


        //保存新图片
        ImageIO.write(withe, "png", new File(file + "\\src\\main\\resources\\yuanImage\\allData.png"));


    }

    /**
     * 图片缩放的方法
     *
     * @param width  要缩放到的宽度
     * @param height 要缩放到的长度
     */
    public static void scaleImage(int width, int height) {

        // 项目路径
        File file = new File(System.getProperty("user.dir"));

        try {

            ImageIcon withe = new ImageIcon(file + "\\src\\main\\resources\\yuanImage\\withe.png");

            BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

            Graphics2D g2d = bi.createGraphics();

            // 设置图片品质
            g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING,

                    RenderingHints.VALUE_RENDER_QUALITY));
            g2d.drawImage(withe.getImage(), 0, 0, width, height, null);

            ImageIO.write(bi, "png", new File(file + "\\src\\main\\resources\\yuanImage\\withe2.png"));

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    /**
     * 生成角色池图片
     *
     * @param averageFive 每个五星平均花费的抽数
     * @param all         总抽数
     * @param fivePeoples 五星角色数
     * @param alreadyCost 目前在池子中垫的抽数
     * @param limited     限定在总五星中的比例(限定/总)
     * @throws IOException IO流异常
     */
    public static void rolePole(String averageFive, String all, ArrayList<String> fivePeoples, String alreadyCost, String limited, double probability) throws IOException {
        // 项目路径
        File file = new File(System.getProperty("user.dir"));


        //读取图片2
        BufferedImage withe = ImageIO.read(new File(file + "\\src\\main\\resources\\yuanImage\\withe.png"));

        //读取图片4
        BufferedImage string2 = ImageIO.read(new File(file + "\\src\\main\\resources\\yuanImage\\string2.png"));
        //读取图片5
        BufferedImage king = choseLuck(probability);
        //读取图片5
        BufferedImage countWithe = ImageIO.read(new File(file + "\\src\\main\\resources\\yuanImage\\计数块.png"));

        //读取图片1
        ArrayList<String> fivePeople = new ArrayList<>();
        ArrayList<String> fivePeopleCounts = new ArrayList<>();
        // 取出ArrayList中存储的数据进行拼接
        for (int i = 0; i < fivePeoples.size() - 2; i++) {
            // 跳过奇数个防止重复
            if (i % 2 != 0) {
                fivePeople.add(fivePeoples.get(i));
                if (i <= 17) {
                    fivePeopleCounts.add(fivePeoples.get(i + 1));
                }
            }
        }
        // 当五星个数为偶数时需要额外补上最后一组数据
        if ((fivePeoples.size()) % 2 != 0) {
            fivePeopleCounts.add(fivePeoples.get(fivePeoples.size() - 1));
        }
        fivePeopleCounts.add("");
        // 当五星个数为偶数时需要额外补上最后一组数据
        if ((fivePeoples.size()) % 2 != 0) {
            fivePeople.add(fivePeoples.get(fivePeoples.size() - 2));
        }
        int picTimes = 0;
        BufferedImage[] picArray;
        picArray = new BufferedImage[fivePeople.size()];

        for (String picName : fivePeople) {
            File file1 = new File(file + "\\src\\main\\resources\\yuanImage\\角色图片\\" + picName + ".png");
            if (file1.exists()) {
                //读取图片10
                BufferedImage diLuKe = ImageIO.read(new File(file + "\\src\\main\\resources\\yuanImage\\角色图片\\" + picName + ".png"));
                picArray[picTimes] = diLuKe;
            } else {
                //读取图片10
                BufferedImage diLuKe = ImageIO.read(new File(file + "\\src\\main\\resources\\yuanImage\\default1.png"));
                picArray[picTimes] = diLuKe;
            }
            picTimes++;
        }


        int fivePeopleCunt = fivePeople.size();

        int lines = (int) Math.ceil((float) fivePeopleCunt / 7);

        scaleImage(withe.getWidth(), withe.getHeight() + 506 * lines + 60 * lines);

        //读取图片2
        BufferedImage withe2 = ImageIO.read(new File(file + "\\src\\main\\resources\\yuanImage\\withe2.png"));

        //用a创建绘画对象
        Graphics2D gd = withe2.createGraphics();
        //把图片e画上去
        assert king != null;
        gd.drawImage(king, 58, 58, king.getWidth(), king.getHeight(), null);
        //把图片d画上去
        gd.drawImage(string2, 1112, 100, string2.getWidth(), string2.getHeight(), null);


        //for (int n = 1; n <= lines; n++) {
        int roleX = 58;
        int n = 1;
        int counts = 0;
        int lineX = 1;
        for (int i = 1; i < fivePeople.size() + 1; i++) {

            //把角色画上去
            gd.drawImage(picArray[i - 1], roleX + picArray[i - 1].getWidth() * (lineX - 1), king.getHeight() + 106 + (picArray[i - 1].getHeight() + 50) * (n - 1), picArray[i - 1].getWidth(), picArray[i - 1].getHeight(), null);

            gd.drawImage(countWithe, roleX + picArray[i - 1].getWidth() * (lineX - 1), king.getHeight() + 482 + (picArray[i - 1].getHeight() + 50) * (n - 1), countWithe.getWidth(), countWithe.getHeight(), null);

            // 设置画笔颜色为黑色
            gd.setColor(new Color(89, 87, 87));
            // 设置画笔字体样式为微软雅黑，斜体，文字大小为20px
            gd.setFont(new Font("汉仪青云简", Font.ITALIC, 100));

            gd.drawString(fivePeopleCounts.get(i - 1), roleX + 147 + picArray[i - 1].getWidth() * (lineX - 1), king.getHeight() + 575 + (picArray[i - 1].getHeight() + 50) * (n - 1));

            roleX += 52;
            counts++;
            lineX++;
            if ((float) counts / 7 == 1) {
                n++;
                counts = 0;
                lineX = 1;
                roleX = 58;
            }

        }

        // 设置画笔颜色为黑色
        gd.setColor(Color.BLACK);
        // 设置画笔字体样式为微软雅黑，斜体，文字大小为20px
        gd.setFont(new Font("微软雅黑", Font.ITALIC, 206));
        gd.drawString(averageFive, 1218, 770);
        gd.drawString(all, 1910, 770);
        gd.drawString(limited, 2575, 770);

        gd.setColor(new Color(255, 192, 0));
        gd.setFont(new Font("微软雅黑", Font.ITALIC, 101));
        gd.drawString(alreadyCost, 1336, 412);

        gd.dispose();

        //保存新图片
        ImageIO.write(withe2, "png", new File(file + "\\src\\main\\resources\\yuanImage\\role pool.png"));

    }

    /**
     * 生成武器池图片
     *
     * @param averageFive 每个五星平均花费的抽数
     * @param all         总抽数
     * @param fivePeoples 五星角色数
     * @param alreadyCost 目前在池子中垫的抽数
     * @param limited     限定在总五星中的比例(限定/总)
     * @throws IOException IO流异常
     */
    public static void armsPole(String averageFive, String all, ArrayList<String> fivePeoples, String alreadyCost, String limited, double probability) throws IOException {
        // 项目路径
        File file = new File(System.getProperty("user.dir"));

        //读取图片2
        BufferedImage withe = ImageIO.read(new File(file + "\\src\\main\\resources\\yuanImage\\withe.png"));

        //读取图片4
        BufferedImage string2 = ImageIO.read(new File(file + "\\src\\main\\resources\\yuanImage\\string3.png"));
        //读取图片5
        BufferedImage king = choseLuck(probability);
        //读取图片5
        BufferedImage countWithe = ImageIO.read(new File(file + "\\src\\main\\resources\\yuanImage\\计数块.png"));

        //读取图片1
        ArrayList<String> fivePeople = new ArrayList<>();
        ArrayList<String> fivePeopleCounts = new ArrayList<>();
        // 取出ArrayList中存储的数据进行拼接
        for (int i = 1; i < fivePeoples.size() - 1; i++) {
            // 跳过奇数个防止重复
            if (i % 2 != 0) {
                fivePeople.add(fivePeoples.get(i));
                if (i <= 17) {
                    fivePeopleCounts.add(fivePeoples.get(i + 1));
                }
            }
        }
        // 当五星个数为偶数时需要额外补上最后一组数据
        if ((fivePeoples.size()) % 2 != 0) {
            fivePeopleCounts.add(fivePeoples.get(fivePeoples.size() - 1));
        }
        fivePeopleCounts.add("");
//        // 当五星个数为偶数时需要额外补上最后一组数据
//        if ((fivePeoples.size()) % 2 != 0) {
//            fivePeople.add(fivePeoples.get(fivePeoples.size() - 2));
//        }
        int picTimes = 0;
        BufferedImage[] picArray;
        picArray = new BufferedImage[fivePeople.size()];

        for (String picName : fivePeople) {
            File file1 = new File(file + "\\src\\main\\resources\\yuanImage\\武器图片\\" + picName + ".png");
            if (file1.exists()) {
                //读取图片10
                BufferedImage armsImg = ImageIO.read(new File(file + "\\src\\main\\resources\\yuanImage\\武器图片\\" + picName + ".png"));
                picArray[picTimes] = armsImg;
            } else {
                //读取图片10
                BufferedImage diLuKe = ImageIO.read(new File(file + "\\src\\main\\resources\\yuanImage\\default1.png"));
                picArray[picTimes] = diLuKe;
            }
            picTimes++;
        }


        int fivePeopleCunt = fivePeople.size();

        int lines = (int) Math.ceil((float) fivePeopleCunt / 7);

        scaleImage(withe.getWidth(), withe.getHeight() + 506 * lines + 60 * lines);

        //读取图片2
        BufferedImage withe2 = ImageIO.read(new File(file + "\\src\\main\\resources\\yuanImage\\withe2.png"));

        //用a创建绘画对象
        Graphics2D gd = withe2.createGraphics();
        //把图片e画上去
        assert king != null;
        gd.drawImage(king, 58, 58, king.getWidth(), king.getHeight(), null);
        //把图片d画上去
        gd.drawImage(string2, 1112, 100, string2.getWidth(), string2.getHeight(), null);


        //for (int n = 1; n <= lines; n++) {
        int roleX = 58;
        int n = 1;
        int counts = 0;
        int lineX = 1;
        for (int i = 1; i < fivePeople.size() + 1; i++) {

            //把角色画上去
            gd.drawImage(picArray[i - 1], roleX + picArray[i - 1].getWidth() * (lineX - 1), king.getHeight() + 106 + (picArray[i - 1].getHeight() + 50) * (n - 1), picArray[i - 1].getWidth(), picArray[i - 1].getHeight(), null);

            gd.drawImage(countWithe, roleX + picArray[i - 1].getWidth() * (lineX - 1), king.getHeight() + 482 + (picArray[i - 1].getHeight() + 50) * (n - 1), countWithe.getWidth(), countWithe.getHeight(), null);

            // 设置画笔颜色为黑色
            gd.setColor(new Color(89, 87, 87));
            // 设置画笔字体样式为微软雅黑，斜体，文字大小为20px
            gd.setFont(new Font("汉仪青云简", Font.ITALIC, 100));

            gd.drawString(fivePeopleCounts.get(i - 1), roleX + 147 + picArray[i - 1].getWidth() * (lineX - 1), king.getHeight() + 575 + (picArray[i - 1].getHeight() + 50) * (n - 1));

            roleX += 52;
            counts++;
            lineX++;
            if ((float) counts / 7 == 1) {
                n++;
                counts = 0;
                lineX = 1;
                roleX = 58;
            }

        }

        // 设置画笔颜色为黑色
        gd.setColor(Color.BLACK);
        // 设置画笔字体样式为微软雅黑，斜体，文字大小为20px
        gd.setFont(new Font("微软雅黑", Font.ITALIC, 206));
        gd.drawString(averageFive, 1218, 770);
        gd.drawString(all, 1910, 770);
        gd.drawString(limited, 2575, 770);

        gd.setColor(new Color(255, 192, 0));
        gd.setFont(new Font("微软雅黑", Font.ITALIC, 101));
        gd.drawString(alreadyCost, 1336, 412);

        gd.dispose();

        //保存新图片
        ImageIO.write(withe2, "png", new File(file + "\\src\\main\\resources\\yuanImage\\arms pool.png"));

    }


    /**
     * 生成常驻池图片
     *
     * @param averageFive 每个五星平均花费的抽数
     * @param all         总抽数
     * @param fivePeoples 五星角色数
     * @param alreadyCost 目前在池子中垫的抽数
     * @param limited     限定在总五星中的比例(限定/总)
     * @throws IOException IO流异常
     */
    public static void permanentPool(String averageFive, String all, ArrayList<String> fivePeoples, String alreadyCost, String limited, double probability) throws IOException {
        // 项目路径
        File file = new File(System.getProperty("user.dir"));

        //读取图片2
        BufferedImage withe = ImageIO.read(new File(file + "\\src\\main\\resources\\yuanImage\\pwithe.png"));

        //读取图片4
        BufferedImage string2 = ImageIO.read(new File(file + "\\src\\main\\resources\\yuanImage\\string4.png"));
        //读取图片5
        BufferedImage king = choseLuck(probability);
        //读取图片5
        BufferedImage countWithe = ImageIO.read(new File(file + "\\src\\main\\resources\\yuanImage\\计数块2.png"));

        //读取图片1
        ArrayList<String> fivePeople = new ArrayList<>();
        ArrayList<String> fivePeopleCounts = new ArrayList<>();
        // 取出ArrayList中存储的数据进行拼接
        for (int i = 1; i < fivePeoples.size() - 1; i++) {
            // 跳过奇数个防止重复
            if (i % 2 != 0) {
                fivePeople.add(fivePeoples.get(i));
                if (i <= 17) {
                    fivePeopleCounts.add(fivePeoples.get(i + 1));
                }
            }
        }
        // 当五星个数为偶数时需要额外补上最后一组数据
        if ((fivePeoples.size()) % 2 != 0) {
            fivePeopleCounts.add(fivePeoples.get(fivePeoples.size() - 1));
        }
        fivePeopleCounts.add("");

        int picTimes = 0;
        BufferedImage[] picArray;
        picArray = new BufferedImage[fivePeople.size()];

        for (String picName : fivePeople) {
            File file1 = new File(file + "\\src\\main\\resources\\yuanImage\\常驻图片\\" + picName + ".png");
            if (file1.exists()) {
                //读取图片10
                BufferedImage armsImg = ImageIO.read(new File(file + "\\src\\main\\resources\\yuanImage\\常驻图片\\" + picName + ".png"));
                picArray[picTimes] = armsImg;
            } else {
                //读取图片10
                BufferedImage diLuKe = ImageIO.read(new File(file + "\\src\\main\\resources\\yuanImage\\default1.png"));
                picArray[picTimes] = diLuKe;
            }
            picTimes++;
        }

        int fivePeopleCunt = fivePeople.size();

        int lines = (int) Math.ceil((float) fivePeopleCunt / 7);

        scaleImage(withe.getWidth(), withe.getHeight() + 506 * lines + 60 * lines);

        //读取图片2
        BufferedImage withe2 = ImageIO.read(new File(file + "\\src\\main\\resources\\yuanImage\\withe2.png"));


        //用a创建绘画对象
        Graphics2D gd = withe2.createGraphics();
        //把图片e画上去
        assert king != null;
        gd.drawImage(king, 58, 58, king.getWidth(), king.getHeight(), null);
        //把图片d画上去
        gd.drawImage(string2, 1112, 100, string2.getWidth(), string2.getHeight(), null);


        //for (int n = 1; n <= lines; n++) {
        int roleX = 58;
        int n = 1;
        int counts = 0;
        int lineX = 1;
        for (int i = 1; i < fivePeople.size() + 1; i++) {

            //把角色画上去
            gd.drawImage(picArray[i - 1], roleX + picArray[i - 1].getWidth() * (lineX - 1), king.getHeight() + 106 + (picArray[i - 1].getHeight() + 50) * (n - 1), picArray[i - 1].getWidth(), picArray[i - 1].getHeight(), null);

            gd.drawImage(countWithe, roleX + picArray[i - 1].getWidth() * (lineX - 1), king.getHeight() + 482 + (picArray[i - 1].getHeight() + 50) * (n - 1), countWithe.getWidth(), countWithe.getHeight(), null);

            // 设置画笔颜色为黑色
            gd.setColor(new Color(89, 87, 87));
            // 设置画笔字体样式为微软雅黑，斜体，文字大小为20px
            gd.setFont(new Font("汉仪青云简", Font.ITALIC, 100));

            gd.drawString(fivePeopleCounts.get(i - 1), roleX + 147 + picArray[i - 1].getWidth() * (lineX - 1), king.getHeight() + 575 + (picArray[i - 1].getHeight() + 50) * (n - 1));

            roleX += 52;
            counts++;
            lineX++;
            if ((float) counts / 7 == 1) {
                n++;
                counts = 0;
                lineX = 1;
                roleX = 58;
            }

        }

        // 设置画笔颜色为黑色
        gd.setColor(Color.BLACK);
        // 设置画笔字体样式为微软雅黑，斜体，文字大小为20px
        gd.setFont(new Font("微软雅黑", Font.ITALIC, 206));
        gd.drawString(averageFive, 1218, 770);
        gd.drawString(all, 1910, 770);
        gd.drawString(limited, 2575, 770);

        gd.setColor(new Color(255, 192, 0));
        gd.setFont(new Font("微软雅黑", Font.ITALIC, 101));
        gd.drawString(alreadyCost, 1336, 412);

        gd.dispose();

        //保存新图片
        ImageIO.write(withe2, "png", new File(file + "\\src\\main\\resources\\yuanImage\\permanent pool.png"));

    }


    public static BufferedImage choseLuck(double probability) throws IOException {
        // 项目路径
        File file = new File(System.getProperty("user.dir"));
        if (probability > 80) {
            return ImageIO.read(new File(file + "\\src\\main\\resources\\yuanImage\\运气\\欧.png"));
        }
        if (probability > 60) {
            return ImageIO.read(new File(file + "\\src\\main\\resources\\yuanImage\\运气\\吉.png"));
        }
        if (probability > 40) {
            return ImageIO.read(new File(file + "\\src\\main\\resources\\yuanImage\\运气\\平.png"));
        }
        if (probability > 20) {
            return ImageIO.read(new File(file + "\\src\\main\\resources\\yuanImage\\运气\\惨.png"));
        }
        if (probability >= 0) {
            return ImageIO.read(new File(file + "\\src\\main\\resources\\yuanImage\\运气\\寄.png"));
        }
        return null;
    }

    /**
     * @param srcFile      源图片路径
     * @param targetFile   截好后图片全名
     * @param startAcross  开始截取位置横坐标
     * @param StartEndlong 开始截图位置纵坐标
     * @param width        截取的长
     * @param hight        截取的高
     * @throws Exception 异常
     */
    public static void cutImage(String srcFile, String targetFile, int startAcross, int StartEndlong, int width, int hight) throws Exception {
        // 取得图片读入器
        Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName("jpg");
        ImageReader reader = readers.next();
        // 取得图片读入流
        InputStream source = new FileInputStream(srcFile);
        ImageInputStream iis = ImageIO.createImageInputStream(source);
        reader.setInput(iis, true);
        // 图片参数对象
        ImageReadParam param = reader.getDefaultReadParam();
        Rectangle rect = new Rectangle(startAcross, StartEndlong, width, hight);
        param.setSourceRegion(rect);
        BufferedImage bi = reader.read(0, param);
        ImageIO.write(bi, targetFile.split("\\.")[1], new File(targetFile));
    }

    /**
     * 图片拼接图片必须长宽一致
     *
     * @param files      需要拼接的图片类型
     * @param type       1-横向拼接，2-纵向拼接
     * @param targetFile 目标文件名称
     */
    public static boolean mergeImage2(File[] files, int type, String targetFile) {
        int len = files.length;
        if (len < 1) {
            throw new RuntimeException("图片数量为0，不可以执行拼接");
        }
        // File[] src = new File[len];
        BufferedImage[] images = new BufferedImage[len];
        int[][] ImageArrays = new int[len][];

        for (int i = 0; i < len; i++) {
            try {
                // src[i] = new File(files[i]);
                images[i] = ImageIO.read(files[i]);
            } catch (Exception e) {
                e.printStackTrace();
            }

            int width = images[i].getWidth();
            int height = images[i].getHeight();
            ImageArrays[i] = new int[width * height];
            ImageArrays[i] = images[i].getRGB(0, 0, width, height, ImageArrays[i], 0, width);
        }

        int newHeight = 0;
        int newWidth = 0;
        for (BufferedImage image : images) {
            // 横向
            if (type == 1) {
                newHeight = newHeight > image.getHeight() ? newWidth : image.getHeight();
                // 错误代码，原先没有添加+ 号
                newWidth += image.getWidth();
            } else if (type == 2) {
                // 纵向
                newWidth = newHeight > image.getWidth() ? newWidth : image.getWidth();
                newHeight += image.getHeight();
            }
        }

        if (type == 1 && newWidth < 1) {
            return false;
        }

        if (type == 2 && newHeight < 1) {
            return false;
        }


        try {
            BufferedImage ImageNew = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
            int height_i = 0;
            int width_i = 0;
            for (int i = 0; i < images.length; i++) {

                if (type == 1) {
                    ImageNew.setRGB(width_i, 0, images[i].getWidth(), newHeight, ImageArrays[i], 0, images[i].getWidth());
                    width_i += images[i].getWidth();
                } else if (type == 2) {
                    ImageNew.setRGB(0, height_i, newWidth, images[i].getHeight(), ImageArrays[i], 0, newWidth);
                    height_i += images[i].getHeight();
                }
            }
            // 输出想要的图片
            ImageIO.write(ImageNew, targetFile.split("\\.")[1], new File(targetFile));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @param gachaType 抽奖类型
     * @param times     页数
     * @return 返回抽卡信息
     */
    public static String getUrl(String gachaType, int times) {

        String urls = "https://hk4e-api.mihoyo.com/event/gacha_info/api/getGachaLog?authkey_ver=1&sign_type=2&auth_appid=webview_gacha&init_type=301&gacha_id=d7d9d26fd678245ee04bec46b4bab7a8f5359c90&timestamp=1657669968&lang=zh-cn&device_type=pc&ext=%7b%22loc%22%3a%7b%22x%22%3a-3771.129638671875%2c%22y%22%3a250.9356231689453%2c%22z%22%3a-2365.77490234375%7d%2c%22platform%22%3a%22WinST%22%7d&game_version=CNRELWin2.8.0_R9182063_S9401797_D9464149&plat_type=pc&region=cn_gf01&authkey=6NQAMEZnyHZ%2bquGwCGVuf7qWCbVt4TmJFNZOZ9RMxfirpEOBaWEs%2fT44oYQuEqC4urJYtDv64aMZUm7Pd7%2f0XpgH5UCeyXV9tKFkUvIYLj8z%2bnstf%2fBZWoyaH%2fLMQTedVStVKtIuMQzPNLCs%2fW4MhsfM7q%2bkYYNtnvxbLKZ8PUJHQpYl6I%2faEc87p252KQLeNtdPcpwohMnIn6e92dnIOcflc%2bLGmSf3fpgrRhwGvqltRIi7JjdFDEp7yd9tA8ivyUTEnvYEyefxyzxmPv8vKyc69hbjBLxFeNOibO7xV27QNxjojpQ%2bBAVTq3GzQGxyNPjbwDeo4G22ODP5AW3%2bIHNaHAQXxhBk66nEGBL9A9onfghjgCf5mcRVZ%2fP5%2bo4yoM%2b4yd8NNwOfJc58Gj%2fvRAE4c6fEekNs0wLwp54Kj9HiZecSNnhCPrTac0Zi4Bkus98x%2fh83vgMjAfAGki1SYsDN18%2b6pGN05n8h9oMYKad%2fJqxnBOT0rgN2aLtl6%2bUWuJWBnjUr6IXZAqx0IiRnClefmMPIxMYRLl08lYTYFOYuP3I9aLMvDInIGddp4GzTuk31WIcCvJFr62kYz47OOR1VIAfu4iXSUoHhrogXzs5eudso1JAnHlhsVzy1437GFcFcI1pB5%2f4XxAn8FhOLbiX12iNzetG14ms0CY51Gv%2bZEfJR3RGGh0tib4WhGPRsfXs2%2bsPReMQ9Qd0X2jw0OZCj%2bVfZja%2fr1QYS9D%2bzXJTV5nAUyQBAAZCX%2bMNf81hPjNf5X5s3J785tXO%2fK%2fr%2f1HE%2bfNQr2DvpyLbbFXVSMUOIPn%2fh%2fhB42HeTpXaXobsLpOtUNASqBYdT%2bbF2JG2aRcU5kOdYV8WjPGb2YBc0wJDHzr1w4nej990ZdAhWUecOaZn0re%2fRol8TbSjlnam10ZpHLkXVlcUu9aE8TBYdViqbau1ACIrIJR1aExDW2D1UxdhmR2YqGd1OrT6EyhOFbzXl%2btpSh670ahgXgPnn4KBghGmnjzqfZyaBibdeFKK11nL8G6wpcrNNUOQTTyTS2LY4YqX9ujmM13XUSnXT3LQ6cGaj7RJoPVjKEnlWriMeeHExTiVCTL6SC1P5wHr0jfwquRK0ncBBwkzxGVUWol0aL8fu50Ch62C4TAug3cvnbYR1aW4nMqOUdqiNkh%2bBwdv6Smb8zUs70Ux%2blmukHJnMRzeABlSxa3XHwOWNgIMu1uExd0tm8yvCRgWaHDu302brf3vfcWj89tzmnVp94nwC2OGml8%2b3ChxBxpxe76Mkm2rLFr4QPEcPgynsuSPO%2fAw%2fD3nnhyiAuEuqekYw0SxincJS1qnMu6Sp%2f%2bqxmLdKeDKRVB6dK10hA6U7myo5jg%3d%3d&game_biz=hk4e_cn" + "&gacha_type=301&page=1&size=20&end_id=0";
        // 通过正则获取关键字
        Pattern pattern2 = Pattern.compile("([\\s\\S]*)" + "gacha_type" + "=([^&]*)" + "([\\s\\S]*)" + "page=" + "([^&]*)" + "([\\s\\S]*)");
        Matcher matcher2 = pattern2.matcher(urls);


        if (matcher2.find()) {
            String newUrl;
            newUrl = matcher2.group(1) + "gacha_type=" + gachaType + matcher2.group(3) + "page=" + times + matcher2.group(5);

            // 返回拼接后的api链接
            System.out.println(newUrl);
            return newUrl;
        }

        return null;
    }

    public static void main(String[] args) throws Exception {
        // cutImage("C:\\Users\\86188\\Desktop\\simbot-mirai-demo1\\src\\main\\resources\\yuanImage\\sec.jpg","C:\\Users\\86188\\Desktop\\simbot-mirai-demo1\\src\\main\\resources\\yuanImage\\sec.jpg",0,0,1151,1000);

        // pictureMake("62.8", "502", "8");

//        File[] files = new File[]{new File("C:\\Users\\86188\\Desktop\\simbot-mirai-demo1\\src\\main\\resources\\yuanImage\\pic 1.jpg"), new File("C:\\Users\\86188\\Desktop\\simbot-mirai-demo1\\src\\main\\resources\\yuanImage\\pic2.jpg")};
//
//
//        mergeImage2(files, 2, "C:\\Users\\86188\\Desktop\\simbot-mirai-demo1\\src\\main\\resources\\yuanImage\\sec3.jpg");
        getUrl("302", 1);
    }

}
