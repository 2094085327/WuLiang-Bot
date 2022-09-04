package simbot.example.core.common;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author zeng
 * @date 2022/6/22 13:24
 * @user 86188
 */
public class Writing {

    /**
     * 将群聊和私聊消息记入日志
     *
     * @param content 变更记录
     */
    public void write(String content) {
        try {
            File file = new File("监听记录.md");
            FileWriter fileWriter = new FileWriter(file.getAbsoluteFile(), true);
            BufferedWriter bw = new BufferedWriter(fileWriter);
            bw.write(content);
            bw.close();
            System.out.println("--监听完毕--");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
