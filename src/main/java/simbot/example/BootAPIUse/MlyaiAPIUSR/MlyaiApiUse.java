package simbot.example.BootAPIUse.MlyaiAPIUSR;

import catcode.CatCodeUtil;
import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.OnGroup;
import love.forte.simbot.annotation.OnPrivate;
import love.forte.simbot.api.message.containers.AccountInfo;
import love.forte.simbot.api.message.containers.GroupInfo;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.message.events.MessageGet;
import love.forte.simbot.api.message.events.PrivateMsg;
import love.forte.simbot.api.sender.MsgSender;
import love.forte.simbot.api.sender.Sender;
import org.springframework.stereotype.Service;
import simbot.example.BootAPIUse.API;
import simbot.example.core.common.Constant;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author zeng
 * @date 2022/8/21 22:03
 * @user 86188
 */
@Service
public class MlyaiApiUse extends Constant {

    final MlyaiApi mlyaiApi;

    public MlyaiApiUse(MlyaiApi mlyaiApi) {
        this.mlyaiApi = mlyaiApi;
    }

    public static ExecutorService THREAD_POOL;

    /**
     * 调用API接口的类
     */
    public API api = new API();

    /**
     * 人工智能回复模块
     * 在收到@时调用人工智能Api进行回复
     *
     * @param groupMsg  用于获取群聊消息，群成员信息等
     * @param msgSender 用于在群聊中发送消息
     */
    @OnGroup
    @Filter(atBot = true)
    public void mlChat(GroupMsg groupMsg, MsgSender msgSender) throws IOException {
        GroupInfo groupInfo = groupMsg.getGroupInfo();
        AccountInfo accountInfo = groupMsg.getAccountInfo();

        String id = groupMsg.getId();
        String fromName = accountInfo.getAccountNickname();
        String groupId = groupInfo.getGroupCode();
        String groupName = groupInfo.getGroupName();


        String content = groupMsg.getText();
        // 将数组通过流的形式遍历并计数有效的指令个数
        int listSize = (int) Arrays.stream(list).filter(content::contains).count();
        int groupBanId = (int) Arrays.stream(groupBanIdList).filter(groupInfo.getGroupCode()::contains).count();

        // 当输入的msg通过contains匹配到相应指令时listSize值为1，无相应指令时为0，在无指令时调用API
        if (listSize != 1) {
            // 将在BAN列表中的群排除在人工智能答复模块外
            if (groupBanId != 1 && BOOTSTATE) {
                msgSender.SENDER.sendGroupMsg(groupMsg, mlyaiApi.chat(content, id, fromName, 2, groupId, groupName));
                System.out.println(content);
                /*  CatCodeUtil util = CatCodeUtil.INSTANCE;

                 String voice = util.toCat("voice", true, "file="
                 + api.record(reMsg));
                 sender.sendGroupMsg(groupMsg, voice);*/
            }

            // 当被Bot在被屏蔽的群组中被@时将消息转发至User
            if (groupBanId == 1) {

                msgSender.SENDER.sendPrivateMsg(USERID1, "[" + accountInfo.getAccountCode()
                        + "-" + accountInfo.getAccountNickname() + "]正在屏蔽群组["
                        + groupInfo.getGroupCode() + "-" + groupInfo.getGroupName() + "]@Bot");
                msgSender.SENDER.sendPrivateMsg(USERID1, "消息内容为:" + groupMsg.getMsg());
            }

        }
    }

    /**
     * 私聊消息回复与撤回
     *
     * @param privateMsg 私聊消息获取
     * @param sender     构建发送器
     * @param msgSender  消息SENDER GETTER SETTER
     */
    @OnPrivate
    public void replay(PrivateMsg privateMsg, Sender sender, MsgSender msgSender) {

        String msgs = privateMsg.getMsg();
        AccountInfo accountInfo = privateMsg.getAccountInfo();

        // 创建线程池
        THREAD_POOL = new ThreadPoolExecutor(50, 50, 10000,
                TimeUnit.SECONDS, new LinkedBlockingQueue<>(50), r -> {
            Thread thread = new Thread(r);
            thread.setName(String.format("newThread%d", thread.getId()));
            return thread;
        });

        // 将数组通过流的形式遍历并计数有效的指令个数
        int listSize = (int) Arrays.stream(list).filter(msgs::contains).count();

        if (BOOTSTATE && listSize != 1) {

            //检测到特定私信内容进行特定回复
            if ("hi".equals(privateMsg.getMsg()) || "你好".equals(privateMsg.getMsg())) {

                CatCodeUtil util = CatCodeUtil.INSTANCE;
                sender.sendPrivateMsg(privateMsg, "嗨！");

                // 项目路径
                File file = new File(System.getProperty("user.dir"));
                System.out.println(file);
                String voice = util.toCat("voice", true, "file=" + api.record(msgs));

                System.out.println(voice);

                sender.sendPrivateMsg(privateMsg, voice);


            } else {

                THREAD_POOL.execute(() -> {

                    // 获取消息的flag
                    MessageGet.MessageFlag<? extends MessageGet.MessageFlagContent>
                            flag = null;
                    try {
                        // 为私聊消息增加延迟更加拟真
                        Thread.sleep(10000);

                        // 创立消息标记用于撤回
                        flag = (MessageGet.MessageFlag<? extends MessageGet.MessageFlagContent>) sender.sendPrivateMsg(privateMsg, mlyaiApi.chat(privateMsg.getText(), accountInfo.getAccountCode(), accountInfo.getAccountNickname(), 1, null, null)).get();

                        // 休眠10000ms后撤回消息
                        Thread.sleep(10000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    // 撤回操作
                    if (flag != null) {
                        msgSender.SETTER.setMsgRecall(flag);
                    } else {
                        System.out.println("没有撤回的消息");
                    }

                });
            }
        }
    }

}
