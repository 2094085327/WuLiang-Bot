package simbot.example.BootAPIUse.OtherAPI;

import cn.hutool.core.img.gif.AnimatedGifEncoder;
import cn.hutool.core.img.gif.GifDecoder;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * @author zeng
 * @date 2022/7/27 18:45
 * @user 86188
 */
public class kedaya {

    public byte[] makeImage(String left, String right) throws IOException {
        AnimatedGifEncoder encoder = new AnimatedGifEncoder();

        File file = new File("image/kedaya.gif");

        InputStream inputStream2 = new FileInputStream(file.getAbsoluteFile());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        encoder.start(outputStream);
        encoder.setRepeat(0);
        encoder.setDelay(200);

        GifDecoder decoder = new GifDecoder();
        decoder.read(inputStream2);
        int count = decoder.getFrameCount();
        for (int i = 0; i < count; i++) {
            BufferedImage frame = decoder.getFrame(i);
            Graphics2D graphics = frame.createGraphics();

            graphics.setFont(new Font("微软雅黑", Font.BOLD, 30));
            graphics.setColor(new Color(0));

            switch (i) {
                case 0:
                    rotate(left, graphics, 56, 100, -5.1);
                    break;
                case 1:
                    rotate(left, graphics, 40, 104, -5.1);
                    break;
                case 2:
                    rotate(left, graphics, 40, 90, -5.1);
                    break;
                case 4:
                    rotate(right, graphics, 320, 71, 8.1);
                    break;
                case 5:
                    rotate(right, graphics, 320, 85, 11.1);
                    break;
                case 6:
                    rotate(right, graphics, 320, 81, 10.1);
                    break;
                case 7:
                    rotate(right, graphics, 320, 84, 11.1);
                    break;
                case 8:
                    rotate(right, graphics, 325, 71, 11.1);
                    break;
                case 9:
                    rotate(right, graphics, 320, 75, 8.8);
                    break;
                case 10:
                    rotate(right, graphics, 320, 72, 8.8);
                    break;
                case 11:
                    rotate(right, graphics, 320, 75, 6);
                    break;
                case 13:
                    rotate(left, graphics, 56, 105, -5.1);
                    break;
                case 14:
                    rotate(left, graphics, 55, 100, -5.1);
                    break;
                case 15:
                    rotate(left, graphics, 66, 105, -3.1);
                    break;
                case 16:
                    rotate(left, graphics, 60, 100, -5.1);
                    break;
                case 17:
                    rotate(left, graphics, 56, 100, -5.1);
                    break;
                default:
                    break;
            }
            encoder.addFrame(frame);

        }
        encoder.finish();
        return outputStream.toByteArray();


    }

    private void rotate(String text, Graphics2D graphics, int x, int y, double angle) {
        graphics.translate(x, y);
        graphics.rotate((Math.PI / 180) * angle);
        graphics.drawString(text, 0, 0);

    }
}
