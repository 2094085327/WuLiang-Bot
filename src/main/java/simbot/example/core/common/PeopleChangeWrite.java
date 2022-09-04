package simbot.example.core.common;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author zeng
 * @date 2022/6/28 20:04
 * @user 86188
 */
public class PeopleChangeWrite {

    /**
     * 群成员增加调用方法
     * @param content 变更记录
     */
    public void writeIncrease(String content) {
        try {
            createFile(content);
            System.out.println("--群成员变更-增加-已记录--");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 群成员减少调用方法
     * @param content 变更记录
     */
    public void writeReduce(String content) {
        try {
            createFile(content);
            System.out.println("--群成员变更-减少-已记录--");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 群成员申请调用方法
     * @param content 变更记录
     */
    public void writeRequest(String content) {
        try {
            createFile(content);
            System.out.println("--申请入群-已记录--");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 好友申请调用方法
     * @param content 变更记录
     */
    public void friendRequest(String content) {
        try {
            createFile(content);
            System.out.println("--申请成为好友-已记录--");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 尝试创建文件
     * @param content 变更记录
     * @throws IOException IO流异常
     */
    public void createFile(String content) throws IOException {
        File file = new File("群成员变更记录.md");
        FileWriter fileWriter = new FileWriter(file.getAbsoluteFile(), true);
        BufferedWriter bw = new BufferedWriter(fileWriter);
        bw.write(content);
        bw.close();
    }
}
