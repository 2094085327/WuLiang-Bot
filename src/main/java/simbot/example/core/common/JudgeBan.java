package simbot.example.core.common;

import love.forte.simbot.api.message.containers.AccountInfo;
import love.forte.simbot.api.message.containers.GroupInfo;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.message.events.MsgGet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import simbot.example.BootAPIUse.BlackListAPI.BlackList;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @Author zeng
 * @Date 2022/9/12 12:04
 * @User 86188
 * @Description: 用来判断是否符合发送信息的条件
 */
@Service
public class JudgeBan extends Constant {
    public ArrayList<String> worldList = new ArrayList<>();

    @Autowired
    public JudgeBan() {
        banWorld();
    }

    public void banWorld() {
        try {
            File world = new File("resources/GameRes/ban_word.txt").getAbsoluteFile();
            if (world.isFile() && world.exists()) {

                InputStreamReader reader = new InputStreamReader(new FileInputStream(world), StandardCharsets.UTF_8);

                BufferedReader bufferedReader = new BufferedReader(reader);

                String linTxt;
                while ((linTxt = bufferedReader.readLine()) != null) {
                    worldList.add(linTxt);
                }
                reader.close();
            } else {
                System.out.println("资源缺失");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public boolean allBan(MsgGet msgGet) {
        AccountInfo accountInfo = msgGet.getAccountInfo();

        // 群聊
        if (msgGet instanceof GroupMsg) {
            GroupMsg groupMsg = (GroupMsg) msgGet;
            GroupInfo groupInfo = groupMsg.getGroupInfo();
            int groupBanId = (int) Arrays.stream(groupBanIdList).filter(groupInfo.getGroupCode()::contains).count();

            String msg = msgGet.getText();
            // 去除消息中的空格
            // 将中文空格替换为英文空格
            assert msg != null;
            msg = msg.trim();
            msg = msg.replace((char) 12288, ' ');

            boolean checkMsg = true;
            for (String list : worldList) {

                if (msg.contains(list)) {
                    System.out.println("违禁词：" + list);
                    checkMsg = false;
                    break;
                }
            }

            return groupBanId != 1 && !BlackList.blackList.contains(groupInfo.getGroupCode()) && BOOTSTATE && checkMsg;
        } else {
            // 私聊
            return !BlackList.blackList.contains(accountInfo.getAccountCode()) && BOOTSTATE;
        }
    }
}
