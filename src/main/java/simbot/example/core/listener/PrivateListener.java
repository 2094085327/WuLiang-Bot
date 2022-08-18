package simbot.example.core.listener;

import catcode.CatCodeUtil;
import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.Listen;
import love.forte.simbot.annotation.OnPrivate;
import love.forte.simbot.api.message.containers.AccountInfo;
import love.forte.simbot.api.message.containers.BotInfo;
import love.forte.simbot.api.message.events.MessageGet;
import love.forte.simbot.api.message.events.PrivateMsg;
import love.forte.simbot.api.sender.MsgSender;
import love.forte.simbot.api.sender.Sender;
import love.forte.simbot.filter.MatchType;
import org.springframework.stereotype.Service;
import simbot.example.BootAPIUse.API;
import simbot.example.Util.CatUtil;
import simbot.example.core.common.Constant;
import simbot.example.core.common.TimeTranslate;
import simbot.example.core.common.Writing;

import java.io.File;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author zeng
 * @date 2022/6/22 11:49
 * @user 86188
 */
@Service
public class PrivateListener extends Constant {

    /**
     * 调用API接口的类
     */
    public API api = new API();

    public static ExecutorService THREAD_POOL;

    /**
     * 解除禁言模块
     * #@Filter() 注解为消息过滤器
     *
     * @param privateMsg 用于获取群聊消息，群成员信息等
     * @param msgSender  用于在群聊中发送消息
     */
    @OnPrivate
    @Filter(value = ".关机", matchType = MatchType.REGEX_MATCHES, trim = true)
    public void setGroupStateClose(PrivateMsg privateMsg, MsgSender msgSender) {
        AccountInfo accountInfo = privateMsg.getAccountInfo();
        String setUser = accountInfo.getAccountCode();
        if (setUser.equals(USERID1)) {
            BOOTSTATE = false;
            System.out.println("已关机");
            msgSender.SENDER.sendPrivateMsg(privateMsg, "姬姬关机了！");
        } else {
            msgSender.SENDER.sendPrivateMsg(privateMsg, "你没有姬姬的权限哦~");
        }
    }

    /**
     * 解除禁言模块
     * #@Filter() 注解为消息过滤器
     *
     * @param privateMsg 用于获取群聊消息，群成员信息等
     * @param msgSender  用于在群聊中发送消息
     */
    @OnPrivate
    @Filter(value = ".开机", matchType = MatchType.REGEX_MATCHES, trim = true)
    public void setGroupStateOpen(PrivateMsg privateMsg, MsgSender msgSender) {
        AccountInfo accountInfo = privateMsg.getAccountInfo();
        String setUser = accountInfo.getAccountCode();
        if (setUser.equals(USERID1)) {
            BOOTSTATE = true;
            System.out.println("已开机");
            msgSender.SENDER.sendPrivateMsg(privateMsg, "姬姬开机了！");
        } else {
            msgSender.SENDER.sendPrivateMsg(privateMsg, "你没有姬姬的权限哦~");
        }
    }


    /**
     * 监听私聊消息并存入日志
     *
     * @param msg       私聊消息获取
     * @param sender    构建发送器
     * @param msgSender 消息SENDER GETTER SETTER
     */
    @Listen(PrivateMsg.class)
    public void fudu(PrivateMsg msg, Sender sender, MsgSender msgSender) {


        // 创建线程池
        THREAD_POOL = new ThreadPoolExecutor(50, 50, 10000,
                TimeUnit.SECONDS, new LinkedBlockingQueue<>(50), r -> {
            Thread thread = new Thread(r);
            thread.setName(String.format("newThread%d", thread.getId()));
            return thread;
        });
        //获取私信人信息
        AccountInfo accountInfo = msg.getAccountInfo();
        //获取机器人信息
        BotInfo botInfo = msg.getBotInfo();

        //获取当前时间
        TimeTranslate time1 = new TimeTranslate();
        String format1 = time1.tt();


        //在控制台输出信息
        String personMsg = "[" + format1 + "]" + "用户[" + accountInfo.getAccountNickname()
                + "/" + accountInfo.getAccountCode() + "]给bot[" + botInfo.getBotName()
                + "]" + "发送了信息：" + msg.getMsg();
        System.out.println(personMsg);

        //将信息存入日志
        Writing writer = new Writing();
        writer.write(personMsg + "\n");
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

                CatUtil.getRecord(file + "\\resources\\voicePack\\audio.wav");

                System.out.println(voice);

                sender.sendPrivateMsg(privateMsg, voice);


            } else {

                THREAD_POOL.execute(() -> {

                    // 获取消息的flag
                    MessageGet.MessageFlag<? extends MessageGet.MessageFlagContent>
                            flag = (MessageGet.MessageFlag<? extends MessageGet.MessageFlagContent>) sender.sendPrivateMsg(privateMsg, api.result(privateMsg.getText())).get();

                    // 通过flag撤回消息
                    try {
                        // 休眠10000ms
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // 撤回操作
                    msgSender.SETTER.setMsgRecall(flag);
                });
            }
        }
    }
}


