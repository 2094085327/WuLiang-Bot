package simbot.example.core.listener;

import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.Listen;
import love.forte.simbot.annotation.OnPrivate;
import love.forte.simbot.api.message.containers.AccountInfo;
import love.forte.simbot.api.message.containers.BotInfo;
import love.forte.simbot.api.message.events.PrivateMsg;
import love.forte.simbot.api.sender.MsgSender;
import love.forte.simbot.api.sender.Sender;
import love.forte.simbot.filter.MatchType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import simbot.example.BootAPIUse.MlyaiAPI.MlyaiApi;
import simbot.example.core.common.Constant;
import simbot.example.core.common.TimeTranslate;
import simbot.example.core.common.Writing;
import simbot.example.BootAPIUse.API;

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

    @Autowired
    MlyaiApi mlyaiApi = new MlyaiApi();


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
     * @param msgSender 用于在私聊中发送消息
     */
    @OnPrivate
    @Filter(value = "发消息", matchType = MatchType.REGEX_MATCHES, trim = true)
    public void sendmsg(MsgSender msgSender, PrivateMsg privateMsg) {
        msgSender.SENDER.sendPrivateMsg(2094085327, "早，这是测试");
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

}


