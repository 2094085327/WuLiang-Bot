package simbot.example.BootAPIUse.OtherAPI;

import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.FilterValue;
import love.forte.simbot.annotation.OnGroup;
import love.forte.simbot.api.message.MessageContentBuilder;
import love.forte.simbot.api.message.MessageContentBuilderFactory;
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

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author zeng
 * @date 2022/7/27 14:15
 * @user 86188
 */
@Service

public class OtherApiUse extends Constant {
    /**
     * 线程池
     */
    public static ExecutorService THREAD_POOL;

    /**
     * 通过自动装配构建消息工厂
     */
    @Autowired
    MessageContentBuilderFactory messageContentBuilderFactory;

    @Autowired
    BlackListService blackListService;

    /**
     * 调用API中的方法
     */
    API api = new API();

    /**
     * 青年大学习模块
     * 在收到@时调用青年大学习Api进行回复
     *
     * @param groupMsg  用于获取群聊消息，群成员信息等
     * @param msgSender 用于在群聊中发送消息
     */
    @OnGroup
    @Filter(value = "青年大学习", matchType = MatchType.REGEX_MATCHES, trim = true)
    public void youthStudy(GroupMsg groupMsg, MsgSender msgSender) {

        AccountInfo accountInfo = groupMsg.getAccountInfo();
        if (blackListService.selectCode(accountInfo.getAccountCode()) == null) {

            Sender sender = msgSender.SENDER;

            GroupInfo groupInfo = groupMsg.getGroupInfo();
            int groupBanId = (int) Arrays.stream(groupBanIdList).filter(groupInfo.getGroupCode()::contains).count();
            // 将群号为“637384877”的群排除在人工智能答复模块外
            if (groupBanId != 1) {
                sender.sendGroupMsg(groupMsg, api.YouthStudy());

            }
        }
    }

    /**
     * 检测直播模块
     * 在检测到关键词和命令后调用天气API来显示天气
     *
     * @param groupMsg  用于获取群聊消息，群成员信息等
     * @param msgSender 用于在群聊中发送消息
     */
    @OnGroup
    @Filter(value = "检测直播", matchType = MatchType.REGEX_MATCHES, trim = true)
    public void bLive(GroupMsg groupMsg, MsgSender msgSender) {

        Sender sender = msgSender.SENDER;
        GroupInfo groupInfo = groupMsg.getGroupInfo();
        AccountInfo accountInfo = groupMsg.getAccountInfo();
        if (blackListService.selectCode(accountInfo.getAccountCode()) == null) {

            int groupBanId = (int) Arrays.stream(groupBanIdList).filter(groupInfo.getGroupCode()::contains).count();
            // 将群号为“637384877”的群排除在人工智能答复模块外
            if (groupBanId != 1) {
                sender.sendGroupMsg(groupMsg, api.bLive(BiUpUid));
            }
        }
    }

    /**
     * 可达鸭模块
     * 在检测到关键词和命令后调用天气API来显示天气
     *
     * @param groupMsg  用于获取群聊消息，群成员信息等
     * @param msgSender 用于在群聊中发送消息
     */
    @OnGroup

    @Filter(value = "可达鸭 {{left}} {{right}}", matchType = MatchType.REGEX_MATCHES, trim = true)
    public void keDaYa(GroupMsg groupMsg, MsgSender msgSender, @FilterValue("left") String left, @FilterValue("right") String right) throws IOException {

        AccountInfo accountInfo = groupMsg.getAccountInfo();
        if (blackListService.selectCode(accountInfo.getAccountCode()) == null) {

            String keMsg = groupMsg.getText();
            String leftMsg = keMsg.split(" ")[1];
            String rightMsg = keMsg.split(" ")[2];

            // 去除消息中的空格
            // 将中文空格替换为英文空格
            left = left.trim();
            left = left.replace((char) 12288, ' ');

            right = right.trim();
            right = right.replace((char) 12288, ' ');

            if (left.length() > 3) {
                left = left.substring(0, 3) + "...";
            }
            if (right.length() > 3) {
                right = right.substring(0, 3) + "...";
            }


            System.out.println(left);
            System.out.println(right);

            Sender sender = msgSender.SENDER;
            Setter setter = msgSender.SETTER;
            GroupInfo groupInfo = groupMsg.getGroupInfo();

            int groupBanId = (int) Arrays.stream(groupBanIdList).filter(groupInfo.getGroupCode()::contains).count();

            // 将群号在groupBanId列表中的群排除在响应外
            if (groupBanId != 1 && !"".equals(left) && !"".equals(right) && !"".equals(leftMsg) && !"".equals(rightMsg)) {
                kedaya ke = new kedaya();

                // 创建消息构建器，用于在服务器上发送图片
                MessageContentBuilder messageContentBuilder = messageContentBuilderFactory.getMessageContentBuilder();

                // 消息标记
                MessageGet.MessageFlag<? extends MessageGet.MessageFlagContent>
                        flag = (MessageGet.MessageFlag<? extends MessageGet.MessageFlagContent>) sender.sendGroupMsg(groupMsg, messageContentBuilder.image(ke.makeImage(left, right)).build()).get();

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
                        // 线程休眠10秒
                        Thread.sleep(10000);
                        setter.setMsgRecall(flag);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
            } else {
                sender.sendGroupMsg(groupMsg, "输入了不正确的内容，阿姬不给你可达鸭！");
            }
        }
    }
}
