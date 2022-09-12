package simbot.example.BootAPIUse.PictureAPI;

import catcode.CatCodeUtil;
import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.FilterValue;
import love.forte.simbot.annotation.OnGroup;
import love.forte.simbot.api.message.MessageContentBuilder;
import love.forte.simbot.api.message.MessageContentBuilderFactory;
import love.forte.simbot.api.message.containers.AccountInfo;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.message.events.MessageGet;
import love.forte.simbot.api.sender.MsgSender;
import love.forte.simbot.component.mirai.message.MiraiMessageContentBuilder;
import love.forte.simbot.component.mirai.message.MiraiMessageContentBuilderFactory;
import love.forte.simbot.filter.MatchType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import simbot.example.BootAPIUse.OtherAPI.OtherApi;
import simbot.example.Service.BlackListService;
import simbot.example.Util.ContextUtil;
import simbot.example.core.common.Constant;
import simbot.example.core.common.JudgeBan;
import simbot.example.core.common.TimeTranslate;

import javax.annotation.PostConstruct;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
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

    /**
     * 注入得到一个消息构建器工厂
     */
    private MiraiMessageContentBuilderFactory factory;
    /**
     * 合并转发
     */
    ApplicationContext applicationContext;
    /**
     * 黑名单
     */
    BlackListService blackListService;

    ContextUtil contextUtil = new ContextUtil();
    JudgeBan judgeBan = new JudgeBan();
    CatCodeUtil util = CatCodeUtil.INSTANCE;

    /**
     * 通过自动装配构建消息工厂
     */
    MessageContentBuilderFactory messageContentBuilderFactory;

    @Autowired
    public PictureApiUse(BlackListService blackListService, ApplicationContext applicationContext, MessageContentBuilderFactory messageContentBuilderFactory) {
        this.blackListService = blackListService;
        this.applicationContext = applicationContext;
        this.messageContentBuilderFactory = messageContentBuilderFactory;
    }


    @PostConstruct
    public void init() {
        contextUtil.setApplicationContext(applicationContext);
        factory = ContextUtil.getForwardBuilderFactory();
    }

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
    public OtherApi otherApi = new OtherApi();

    /**
     * 二刺螈模块(自定义标签)
     * 在收到@时调用P站Api进行图片发送
     *
     * @param groupMsg  用于获取群聊消息，群成员信息等
     * @param msgSender 用于在群聊中发送消息
     */
    @OnGroup
    @Filter(value = "来点*{{tag}}涩图", matchType = MatchType.REGEX_MATCHES, trim = true)
    @Filter(value = "来点好康的*{{tag}}", matchType = MatchType.REGEX_MATCHES, trim = true)
    public void customizePicture(GroupMsg groupMsg, MsgSender msgSender, @FilterValue("tag") String tag) {

        AccountInfo accountInfo = groupMsg.getAccountInfo();

        String imgMsg, url, msg = "来点好康的", nullTag = "点";

        if (nullTag.equals(tag) || msg.equals(groupMsg.getText())) {
            imgMsg = otherApi.twoDimensional(tag, "random");
        } else {
            imgMsg = otherApi.twoDimensional(tag, "chose");
        }
        url = otherApi.url;

        // 线程池
        THREAD_POOL = new ThreadPoolExecutor(50, 50, 3,
                TimeUnit.SECONDS, new LinkedBlockingQueue<>(50), r -> {
            Thread thread = new Thread(r);
            thread.setName(String.format("newThread%d", thread.getId()));
            return thread;
        });

        //通过UUID构造文件名
        String imgName = UUID.randomUUID().toString();

        if (judgeBan.allBan(groupMsg)) {
            try {
                loadImg(url, imgName);

                InputStream inputStream = new FileInputStream(new File("image/" + imgName + ".jpg").getAbsoluteFile());

                // 创建消息构建器，用于在服务器上发送图片
                MessageContentBuilder messageContentBuilder = messageContentBuilderFactory.getMessageContentBuilder();

                MiraiMessageContentBuilder builder = factory.getMessageContentBuilder();
                builder.forwardMessage(fun -> {
                    fun.add(accountInfo, imgMsg);
                    fun.add(accountInfo, messageContentBuilder.image(inputStream).build());
                });

                // 创建消息标记
                MessageGet.MessageFlag<? extends MessageGet.MessageFlagContent> flag1 = (MessageGet.MessageFlag<? extends MessageGet.MessageFlagContent>) msgSender.SENDER.sendGroupMsg(groupMsg, builder.build()).get();
                inputStream.close();
                THREAD_POOL.execute(() -> {
                    try {
                        // 线程休眠45秒
                        Thread.sleep(30000);
                        msgSender.SETTER.setMsgRecall(flag1);
                    } catch (Exception e) {
                        e.printStackTrace();
                        try {
                            String error = util.toCat("image", true, "file=" + "https://gchat.qpic.cn/gchatpic_new/2094085327/2083469072-2232305563-72311C09F00D0DBEF47CF5B070311E46/0?term&#61;2");

                            // 创建消息标记,没有图片时发送图片不见了并准备撤回
                            MiraiMessageContentBuilder builder2 = factory.getMessageContentBuilder();
                            builder2.forwardMessage(fun -> {
                                fun.add(accountInfo, imgMsg);
                                fun.add(accountInfo, "啊哦~图片不见了" + face);
                                fun.add(accountInfo, error);
                            });

                            MessageGet.MessageFlag<? extends MessageGet.MessageFlagContent>
                                    flag2 = (MessageGet.MessageFlag<? extends MessageGet.MessageFlagContent>) msgSender.SENDER.sendGroupMsg(groupMsg, builder2.build()).get();

                            Thread.sleep(30000);
                            msgSender.SETTER.setMsgRecall(flag2);
                        } catch (Exception exception) {
                            e.printStackTrace();
                        }
                    }
                    Path path = Paths.get(new File("image/" + imgName + ".jpg").getAbsolutePath());
                    try {
                        Files.delete(path);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 缓存图片
     *
     * @param url  图片路径
     * @param name 图片名字
     * @throws Exception 异常
     */
    public void loadImg(String url, String name) throws Exception {
        // 构造URL
        URL imgUrl = new URL(url);
        // 打开连接
        URLConnection con = imgUrl.openConnection();
        // 输入流
        InputStream is = con.getInputStream();
        // 1K的数据缓冲
        byte[] bs = new byte[1024];
        // 读取到的数据长度

        int len;

        // 输出的文件流
        File file = new File("image/" + name + ".jpg").getAbsoluteFile();

        FileOutputStream os = new FileOutputStream(file, true);

        // 开始读取
        while ((len = is.read(bs)) != -1) {
            os.write(bs, 0, len);
        }
        // 完毕，关闭所有链接
        os.close();
        is.close();
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

        String msg = util.toCat("image", true, "file="
                + "https://www.dmoe.cc/random.php");

        try {
            // 将群号为“637384877”的群排除在人工智能答复模块外
            if (judgeBan.allBan(groupMsg)) {

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
                        msgSender.SETTER.setMsgRecall(flag);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
            }

        } catch (Exception e) {
            msgSender.SENDER.sendGroupMsg(groupMsg, "超时了哦~");
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

        String msg = util.toCat("image", true, "file="
                + "https://api.dujin.org/pic/yuanshen/");

        try {
            // 将群号为“637384877”的群排除在人工智能答复模块外
            if (judgeBan.allBan(groupMsg)) {

                // 消息标记
                MessageGet.MessageFlag<? extends MessageGet.MessageFlagContent>
                        flag = (MessageGet.MessageFlag<? extends MessageGet.MessageFlagContent>) msgSender.SENDER.sendGroupMsg(groupMsg, msg).get();

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
                        msgSender.SETTER.setMsgRecall(flag);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
            }

        } catch (Exception e) {
            msgSender.SENDER.sendGroupMsg(groupMsg, "超时了哦~");
        }
    }

}
