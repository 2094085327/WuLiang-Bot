package simbot.example.BootAPIUse.PictureAPI;

import catcode.CatCodeUtil;
import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.OnGroup;
import love.forte.simbot.api.message.containers.AccountInfo;
import love.forte.simbot.api.message.containers.GroupInfo;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.message.events.MessageGet;
import love.forte.simbot.api.sender.MsgSender;
import love.forte.simbot.api.sender.Sender;
import love.forte.simbot.api.sender.Setter;
import love.forte.simbot.filter.MatchType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import simbot.example.BootAPIUse.API;
import simbot.example.Service.BlackListService;
import simbot.example.core.common.Constant;
import simbot.example.core.common.TimeTranslate;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author zeng
 * @date 2022/7/27 11:38
 * @user 86188
 */

@Service
public class PictureApiUse extends Constant {

    @Autowired
    BlackListService blackListService;

    /**
     * 线程池
     */
    public static ExecutorService THREAD_POOL;

    /**
     * 获取当前时间
     */
    public TimeTranslate time = new TimeTranslate();

    /**
     * 调用API接口的类
     */
    public API api = new API();


    /**
     * 二刺螈模块
     * 在收到@时调用P站Api进行链接发送
     *
     * @param groupMsg  用于获取群聊消息，群成员信息等
     * @param msgSender 用于在群聊中发送消息
     */
    @OnGroup
    @Filter(value = "来点好康的", matchType = MatchType.REGEX_MATCHES, trim = true)
    public void picture(GroupMsg groupMsg, MsgSender msgSender) {
        MessageGet.MessageFlag<? extends MessageGet.MessageFlagContent> flag1;
        MessageGet.MessageFlag<? extends MessageGet.MessageFlagContent> flag2;

        Setter setter = msgSender.SETTER;

        GroupInfo groupInfo = groupMsg.getGroupInfo();
        AccountInfo accountInfo = groupMsg.getAccountInfo();

        int groupBanId = (int) Arrays.stream(groupBanIdList).filter(groupInfo.getGroupCode()::contains).count();

        CatCodeUtil util = CatCodeUtil.INSTANCE;
        String imgMsg = api.twoDimensional();
        String url = api.url;

        // 线程池
        THREAD_POOL = new ThreadPoolExecutor(50, 50, 3,
                TimeUnit.SECONDS, new LinkedBlockingQueue<>(50), r -> {
            Thread thread = new Thread(r);
            thread.setName(String.format("newThread%d", thread.getId()));
            return thread;
        });


        // 将群号为“637384877”的群排除在人工智能答复模块外
        if (groupBanId != 1 && blackListService.selectCode(accountInfo.getAccountCode()) == null) {
            try {

                String img = util.toCat("image", true, "file=" + url);
                String error = util.toCat("image", true, "file=" + "https://gchat.qpic.cn/gchatpic_new/2094085327/2083469072-2232305563-72311C09F00D0DBEF47CF5B070311E46/0?term&#61;2");

                // 创建消息标记
                flag1 = (MessageGet.MessageFlag<? extends MessageGet.MessageFlagContent>) msgSender.SENDER.sendGroupMsg(groupMsg, imgMsg).get();

                // 创建消息标记
                flag2 = (MessageGet.MessageFlag<? extends MessageGet.MessageFlagContent>) msgSender.SENDER.sendGroupMsg(groupMsg, img).get();

                MessageGet.MessageFlag<? extends MessageGet.MessageFlagContent> finalFlag = flag2;
                THREAD_POOL.execute(() -> {
                    try {
                        // 线程休眠45秒
                        Thread.sleep(45000);
                        setter.setMsgRecall(flag1);
                        setter.setMsgRecall(finalFlag);
                    } catch (Exception e) {
                        // 没有图片时发送图片不见了并准备撤回
                        // 创建消息标记
                        MessageGet.MessageFlag<? extends MessageGet.MessageFlagContent>
                                flag3 = (MessageGet.MessageFlag<? extends MessageGet.MessageFlagContent>) msgSender.SENDER.sendGroupMsg(groupMsg, "啊哦~图片不见了" + face + error).get();
                        try {
                            // 线程休眠45秒
                            Thread.sleep(10000);
                            setter.setMsgRecall(flag3);
                        } catch (Exception e2) {
                            System.out.println("报错");
                        }
                    }
                });

            } catch (Exception e) {
                String img = util.toCat("image", true, "file=" + "https://gchat.qpic.cn/gchatpic_new/2094085327/2083469072-2232305563-72311C09F00D0DBEF47CF5B070311E46/0?term&#61;2");

                // 创建消息标记
                flag2 = (MessageGet.MessageFlag<? extends MessageGet.MessageFlagContent>) msgSender.SENDER.sendGroupMsg(groupMsg, "啊哦~图片不见了" + face + img).get();
                MessageGet.MessageFlag<? extends MessageGet.MessageFlagContent> finalFlag1 = flag2;
                THREAD_POOL.execute(() -> {
                    try {
                        // 线程休眠45秒
                        Thread.sleep(45000);
                        setter.setMsgRecall(finalFlag1);
                    } catch (InterruptedException e2) {
                        msgSender.SENDER.sendGroupMsg(groupMsg, "啊哦~图片不见了" + face + img);
                    }
                });

            }
        }
    }

    /**
     * 二刺螈模块2
     * 在收到@时调用动漫网址来发送图片
     *
     * @param groupMsg  用于获取群聊消息，群成员信息等
     * @param msgSender 用于在群聊中发送消息
     */
    @OnGroup
    @Filter(value = "看看动漫", matchType = MatchType.REGEX_MATCHES, trim = true)
    public void picture2(GroupMsg groupMsg, MsgSender msgSender) {

        Setter setter = msgSender.SETTER;

        CatCodeUtil util = CatCodeUtil.INSTANCE;
        String msg = util.toCat("image", true, "file="
                + "https://www.dmoe.cc/random.php");

        Sender sender = msgSender.SENDER;

        GroupInfo groupInfo = groupMsg.getGroupInfo();
        AccountInfo accountInfo = groupMsg.getAccountInfo();

        int groupBanId = (int) Arrays.stream(groupBanIdList).filter(groupInfo.getGroupCode()::contains).count();
        try {
            // 将群号为“637384877”的群排除在人工智能答复模块外
            if (groupBanId != 1 && blackListService.selectCode(accountInfo.getAccountCode()) == null) {

                // 消息标记
                MessageGet.MessageFlag<? extends MessageGet.MessageFlagContent>
                        flag = (MessageGet.MessageFlag<? extends MessageGet.MessageFlagContent>) msgSender.SENDER.sendGroupMsg(groupMsg, msg).get();

                System.out.println(flag);

                // 线程池
                THREAD_POOL = new ThreadPoolExecutor(50, 50, 3,
                        TimeUnit.SECONDS, new LinkedBlockingQueue<>(50), r -> {
                    Thread thread = new Thread(r);
                    thread.setName(String.format("newThread%d", thread.getId()));
                    return thread;
                });

                // 创建延时消息
                THREAD_POOL.execute(() -> {
                    try {
                        // 线程休眠45秒
                        Thread.sleep(45000);
                        setter.setMsgRecall(flag);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
            }

        } catch (Exception e) {
            sender.sendGroupMsg(groupMsg, "超时了哦~");
        }
    }

    /**
     * 原神图片api
     * 在收到@时调用原神图片api来发送图片
     *
     * @param groupMsg  用于获取群聊消息，群成员信息等
     * @param msgSender 用于在群聊中发送消息
     */
    @OnGroup
    @Filter(value = "来点原神", matchType = MatchType.REGEX_MATCHES, trim = true)
    public void yuanShen(GroupMsg groupMsg, MsgSender msgSender) {

        CatCodeUtil util = CatCodeUtil.INSTANCE;
        String msg = util.toCat("image", true, "file="
                + "https://api.dujin.org/pic/yuanshen/");

        Sender sender = msgSender.SENDER;
        Setter setter = msgSender.SETTER;

        GroupInfo groupInfo = groupMsg.getGroupInfo();
        AccountInfo accountInfo = groupMsg.getAccountInfo();

        int groupBanId = (int) Arrays.stream(groupBanIdList).filter(groupInfo.getGroupCode()::contains).count();

        try {
            // 将群号为“637384877”的群排除在人工智能答复模块外
            if (groupBanId != 1 && blackListService.selectCode(accountInfo.getAccountCode()) == null) {

                // 消息标记
                MessageGet.MessageFlag<? extends MessageGet.MessageFlagContent>
                        flag = (MessageGet.MessageFlag<? extends MessageGet.MessageFlagContent>) msgSender.SENDER.sendGroupMsg(groupMsg, msg).get();

                System.out.println(flag);

                // 线程池
                THREAD_POOL = new ThreadPoolExecutor(50, 50, 3,
                        TimeUnit.SECONDS, new LinkedBlockingQueue<>(50), r -> {
                    Thread thread = new Thread(r);
                    thread.setName(String.format("newThread%d", thread.getId()));
                    return thread;
                });

                // 创建延时消息
                THREAD_POOL.execute(() -> {
                    try {
                        // 线程休眠45秒
                        Thread.sleep(45000);
                        setter.setMsgRecall(flag);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
            }

        } catch (Exception e) {
            sender.sendGroupMsg(groupMsg, "超时了哦~");
        }
    }

}
