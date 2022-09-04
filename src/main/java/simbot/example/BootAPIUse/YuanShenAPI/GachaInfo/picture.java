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

/**
 * @author zeng
 * @date 2022/8/7 18:09
 * @user 86188
 */
@SuppressWarnings("unused")
public class picture {

    /**
     * 图片缩放的方法
     *
     * @param width  要缩放到的宽度
     * @param height 要缩放到的长度
     */
    public static void scaleImage(int width, int height) {

        try {

            ImageIcon withe = new ImageIcon(new File("yuanImage/withe.png").getAbsolutePath());

            BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

            Graphics2D g2d = bi.createGraphics();

            // 设置图片品质
            g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING,

                    RenderingHints.VALUE_RENDER_QUALITY));
            g2d.drawImage(withe.getImage(), 0, 0, width, height, null);

            ImageIO.write(bi, "png", new File("yuanImage/withe2.png").getAbsoluteFile());

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    /**
     * 运气判断
     *
     * @param probability 概率
     * @return 返回概率相应图片
     * @throws IOException Io流异常
     */
    public static BufferedImage choseLuck(double probability) throws IOException {

        if (probability > 80) {
            return ImageIO.read(new File("yuanImage/运气/欧.png").getAbsoluteFile());
        }
        if (probability > 60) {
            return ImageIO.read(new File("yuanImage/运气/吉.png").getAbsoluteFile());
        }
        if (probability > 40) {
            return ImageIO.read(new File("yuanImage/运气/平.png").getAbsoluteFile());
        }
        if (probability > 20) {
            return ImageIO.read(new File("yuanImage/运气/惨.png").getAbsoluteFile());
        }
        if (probability >= 0) {
            return ImageIO.read(new File("yuanImage/运气/寄.png").getAbsoluteFile());
        }
        return null;
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

        //读取图片2
        BufferedImage withe = ImageIO.read(new File("yuanImage/withe.png").getAbsoluteFile());

        //读取图片4
        BufferedImage string2 = ImageIO.read(new File("yuanImage/string2.png").getAbsoluteFile());
        //读取图片5
        BufferedImage king = choseLuck(probability);
        //读取图片5
        BufferedImage countWithe = ImageIO.read(new File("yuanImage/计数块.png").getAbsoluteFile());

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
            File file1 = new File("yuanImage/角色图片/" + picName + ".png");
            if (file1.exists()) {
                //读取图片10
                BufferedImage diLuKe = ImageIO.read(new File("yuanImage/角色图片/" + picName + ".png").getAbsoluteFile());
                picArray[picTimes] = diLuKe;
            } else {
                //读取图片10
                BufferedImage diLuKe = ImageIO.read(new File("yuanImage/default1.png").getAbsoluteFile());
                picArray[picTimes] = diLuKe;
            }
            picTimes++;
        }


        int fivePeopleCunt = fivePeople.size();

        int lines = (int) Math.ceil((float) fivePeopleCunt / 7);

        scaleImage(withe.getWidth(), withe.getHeight() + 506 * lines + 60 * lines);

        //读取图片2
        BufferedImage withe2 = ImageIO.read(new File("yuanImage/withe2.png").getAbsoluteFile());

        //用a创建绘画对象
        Graphics2D gd = withe2.createGraphics();
        //把图片e画上去
        assert king != null;
        gd.drawImage(king, 58, 58, king.getWidth(), king.getHeight(), null);
        //把图片d画上去
        gd.drawImage(string2, 1112, 100, string2.getWidth(), string2.getHeight(), null);

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
        ImageIO.write(withe2, "png", new File("yuanImage/role pool.png").getAbsoluteFile());

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

        //读取图片背景
        BufferedImage withe = ImageIO.read(new File("yuanImage/withe.png"));

        //读取图片常驻池文字
        BufferedImage string2 = ImageIO.read(new File("yuanImage/string3.png").getAbsoluteFile());

        //读取图片读取欧非判断图片
        BufferedImage king = choseLuck(probability);

        //读取图片读取计数块用于判断长度(可能可以优化)
        BufferedImage countWithe = ImageIO.read(new File("yuanImage/计数块.png").getAbsoluteFile());

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

        // 读取武器图片
        for (String picName : fivePeople) {
            File file1 = new File("yuanImage/武器图片/" + picName + ".png");
            // 判断此武器是否存在
            if (file1.exists()) {
                //当存在时读取武器
                BufferedImage armsImg = ImageIO.read(new File("yuanImage/武器图片/" + picName + ".png").getAbsoluteFile());
                picArray[picTimes] = armsImg;
            } else {
                //不存在时调用默认图片
                BufferedImage diLuKe = ImageIO.read(new File("yuanImage/default1.png").getAbsoluteFile());
                picArray[picTimes] = diLuKe;
            }
            picTimes++;
        }


        int fivePeopleCunt = fivePeople.size();

        int lines = (int) Math.ceil((float) fivePeopleCunt / 7);

        scaleImage(withe.getWidth(), withe.getHeight() + 506 * lines + 60 * lines);

        //读取调整大小后的背景
        BufferedImage withe2 = ImageIO.read(new File("yuanImage/withe2.png").getAbsoluteFile());

        //创建绘画画笔
        Graphics2D gd = withe2.createGraphics();

        //将欧非图片画上去
        assert king != null;
        gd.drawImage(king, 58, 58, king.getWidth(), king.getHeight(), null);

        //将文字图片画上去
        gd.drawImage(string2, 1112, 100, string2.getWidth(), string2.getHeight(), null);

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
        ImageIO.write(withe2, "png", new File("yuanImage/arms pool.png").getAbsoluteFile());

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

        //读取图片2
        BufferedImage withe = ImageIO.read(new File("yuanImage/pwithe.png").getAbsoluteFile());

        //读取图片4
        BufferedImage string2 = ImageIO.read(new File("yuanImage/string4.png").getAbsoluteFile());
        //读取图片5
        BufferedImage king = choseLuck(probability);
        //读取图片5
        BufferedImage countWithe = ImageIO.read(new File("yuanImage/计数块2.png"));

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
            File file1 = new File("yuanImage/常驻图片/" + picName + ".png");
            if (file1.exists()) {
                //读取图片10
                BufferedImage armsImg = ImageIO.read(new File("yuanImage/常驻图片/" + picName + ".png").getAbsoluteFile());
                picArray[picTimes] = armsImg;
            } else {
                //读取图片10
                BufferedImage diLuKe = ImageIO.read(new File("yuanImage/default1.png").getAbsoluteFile());
                picArray[picTimes] = diLuKe;
            }
            picTimes++;
        }

        int fivePeopleCunt = fivePeople.size();

        int lines = (int) Math.ceil((float) fivePeopleCunt / 7);

        scaleImage(withe.getWidth(), withe.getHeight() + 506 * lines + 60 * lines);

        //读取图片2
        BufferedImage withe2 = ImageIO.read(new File("yuanImage/withe2.png").getAbsoluteFile());


        //用a创建绘画对象
        Graphics2D gd = withe2.createGraphics();
        //把图片e画上去
        assert king != null;
        gd.drawImage(king, 58, 58, king.getWidth(), king.getHeight(), null);
        //把图片d画上去
        gd.drawImage(string2, 1112, 100, string2.getWidth(), string2.getHeight(), null);

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
        ImageIO.write(withe2, "png", new File("yuanImage/permanent pool.png").getAbsoluteFile());

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

        //读取图片2
        BufferedImage withe = ImageIO.read(new File("yuanImage/withe.png").getAbsoluteFile());

        //读取图片5
        BufferedImage king = choseLuck(probability);

        //读取图片4
        BufferedImage string1 = ImageIO.read(new File("yuanImage/string1.png").getAbsoluteFile());

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

        gd.dispose();

        //保存新图片
        ImageIO.write(withe, "png", new File("yuanImage/allData.png").getAbsoluteFile());
    }

    /**
     * 最终将所有生成的图片合成一张
     *
     * @throws IOException 抛出IO流异常
     */
    public static void allPictureMake() throws Exception {

        //读取总数据图片
        BufferedImage allData = ImageIO.read(new File("yuanImage/allData.png").getAbsoluteFile());

        //读取角色池图片
        BufferedImage rolePool = ImageIO.read(new File("yuanImage/role pool.png").getAbsoluteFile());

        //读取武器池图片
        BufferedImage armsPool = ImageIO.read(new File("yuanImage/arms pool.png").getAbsoluteFile());

        //读取常驻池图片
        BufferedImage permanentPool = ImageIO.read(new File("yuanImage/permanent pool.png").getAbsoluteFile());


        ImageIcon sec = new ImageIcon(new File("yuanImage/sec.png").getAbsolutePath());

        BufferedImage bi = new BufferedImage(3305, 500 + allData.getHeight() + rolePool.getHeight() + armsPool.getHeight() + permanentPool.getHeight(), BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = bi.createGraphics();

        // 设置图片品质
        g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));

        g2d.drawImage(sec.getImage(), 0, 0, 3305, 500 + allData.getHeight() + rolePool.getHeight() + armsPool.getHeight() + permanentPool.getHeight(), null);

        //把总数据画上去
        g2d.drawImage(allData, 100, 100, allData.getWidth(), allData.getHeight(), null);
        //把角色池画上去
        g2d.drawImage(rolePool, 100, 1242, rolePool.getWidth(), rolePool.getHeight(), null);

        //把武器池画上去
        g2d.drawImage(armsPool, 100, 1342 + rolePool.getHeight(), armsPool.getWidth(), armsPool.getHeight(), null);

        //把常驻池画上去
        g2d.drawImage(permanentPool, 100, 1442 + rolePool.getHeight() + armsPool.getHeight(), permanentPool.getWidth(), permanentPool.getHeight(), null);

        g2d.dispose();

        //保存新图片.getAbsoluteFile()
        ImageIO.write(bi, "png", new File("yuanImage/finally.png").getAbsoluteFile());
    }




    /**
     * @param srcFile      源图片路径
     * @param targetFile   截好后图片全名
     * @param startAcross  开始截取位置横坐标
     * @param startEndLong 开始截图位置纵坐标
     * @param width        截取的长
     * @param height       截取的高
     * @throws Exception 异常
     */
    public static void cutImage(String srcFile, String targetFile, int startAcross, int startEndLong, int width, int height) throws Exception {
        // 取得图片读入器
        Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName("jpg");
        ImageReader reader = readers.next();
        // 取得图片读入流
        InputStream source = new FileInputStream(srcFile);
        ImageInputStream iis = ImageIO.createImageInputStream(source);
        reader.setInput(iis, true);
        // 图片参数对象
        ImageReadParam param = reader.getDefaultReadParam();
        Rectangle rect = new Rectangle(startAcross, startEndLong, width, height);
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

        BufferedImage[] images = new BufferedImage[len];
        int[][] ImageArrays = new int[len][];

        for (int i = 0; i < len; i++) {
            try {

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
            BufferedImage imageNew = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
            int height_i = 0;
            int width_i = 0;
            for (int i = 0; i < images.length; i++) {

                if (type == 1) {
                    imageNew.setRGB(width_i, 0, images[i].getWidth(), newHeight, ImageArrays[i], 0, images[i].getWidth());
                    width_i += images[i].getWidth();
                } else if (type == 2) {
                    imageNew.setRGB(0, height_i, newWidth, images[i].getHeight(), ImageArrays[i], 0, newWidth);
                    height_i += images[i].getHeight();
                }
            }
            // 输出想要的图片
            ImageIO.write(imageNew, targetFile.split("\\.")[1], new File(targetFile));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


}
