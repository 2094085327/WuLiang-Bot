package simbot.example.core.listener;

import love.forte.simbot.annotation.Listen;
import love.forte.simbot.api.message.containers.AccountInfo;
import love.forte.simbot.api.message.containers.BotInfo;
import love.forte.simbot.api.message.events.PrivateMsg;
import love.forte.simbot.api.sender.MsgSender;
import love.forte.simbot.api.sender.Sender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import simbot.example.BootAPIUse.MlyaiAPI.MlyaiApi;
import simbot.example.BootAPIUse.OtherAPI.OtherApi;
import simbot.example.core.common.Constant;
import simbot.example.core.common.TimeTranslate;
import simbot.example.core.common.Writing;

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

    MlyaiApi mlyaiApi;

    @Autowired
    public PrivateListener(MlyaiApi mlyaiApi) {
        this.mlyaiApi = mlyaiApi;
    }

    /**
     * 调用API接口的类
     */
    public OtherApi otherApi = new OtherApi();

    public static ExecutorService THREAD_POOL;

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
        String personMsg = "[" + format1 + "]" + "用户[" + accountInfo.getAccountNickname() + "/" + accountInfo.getAccountCode() + "]给bot[" + botInfo.getBotName() + "]" + "发送了信息：" + msg.getMsg();
        System.out.println(personMsg);

        //将信息存入日志
        Writing writer = new Writing();
        writer.write(personMsg + "\n");
    }
}


