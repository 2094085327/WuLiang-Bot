package simbot.example.core.listener;

import love.forte.simbot.annotation.*;
import love.forte.simbot.api.message.MessageContent;
import love.forte.simbot.api.message.MessageContentBuilder;
import love.forte.simbot.api.message.containers.AccountInfo;
import love.forte.simbot.api.message.containers.GroupAccountInfo;
import love.forte.simbot.api.message.containers.GroupInfo;
import love.forte.simbot.api.message.events.*;
import love.forte.simbot.api.message.results.GroupMemberInfo;
import love.forte.simbot.api.sender.MsgSender;
import love.forte.simbot.api.sender.Sender;
import love.forte.simbot.api.sender.Setter;
import love.forte.simbot.bot.Bot;
import love.forte.simbot.bot.BotManager;
import love.forte.simbot.component.mirai.message.MiraiMessageContentBuilder;
import love.forte.simbot.component.mirai.message.MiraiMessageContentBuilderFactory;
import love.forte.simbot.filter.MatchType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import simbot.example.BootAPIUse.OtherAPI.OtherApi;
import simbot.example.BootAPIUse.YuanShenAPI.Sign.SignConstant;
import simbot.example.Service.BlackListService;
import simbot.example.Util.CatUtil;
import simbot.example.Util.ContextUtil;
import simbot.example.core.common.*;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author zeng
 * @date 2022/6/22 17:23
 * @user 86188
 */
@Service
public class GroupListener extends Constant {

    /**
     * 黑名单
     */
    BlackListService blackListService;
    /**
     * 合并转发
     */
    ApplicationContext applicationContext;

    JudgeBan judgeBan;

    @Autowired
    public GroupListener(BlackListService blackListService, ApplicationContext applicationContext, JudgeBan judgeBan) {
        this.blackListService = blackListService;
        this.applicationContext = applicationContext;
        this.judgeBan = judgeBan;
        try {
            readTxt();
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    ContextUtil contextUtil = new ContextUtil();


    /**
     * 将信息存入日志
     */
    Writing writing = new Writing();

    public static ExecutorService THREAD_POOL;

    /**
     * 获取当前时间
     */
    public TimeTranslate time = new TimeTranslate();

    /**
     * 构建机器人管理器
     */
    @Resource
    public BotManager manager;

    /**
     * 注入得到一个消息构建器工厂
     */
    private MiraiMessageContentBuilderFactory factory;

    @PostConstruct
    public void init() {
        contextUtil.setApplicationContext(applicationContext);
        factory = ContextUtil.getForwardBuilderFactory();
    }

    /**
     * #@全体成员的猫猫码
     */
    public String cat1 = "[CAT:at,all=true]";

    /**
     * 调用API接口的类
     */
    public OtherApi otherApi = new OtherApi();

    /**
     * 日志记录
     *
     * @param groupMsg  群聊
     * @param msgSender 消息发送
     */
    @OnGroup
    public void group(GroupMsg groupMsg, MsgSender msgSender) {

        // 群成员信息
        GroupAccountInfo accountInfo = groupMsg.getAccountInfo();
        // 群信息
        GroupInfo groupInfo = groupMsg.getGroupInfo();

        // 获取时间
        String format1 = time.tt();

        // 在控制台输出信息
        String groupMsgPutOut = "[" + format1 + "]" + "用户[" + accountInfo.getAccountNickname() + "/"
                + accountInfo.getAccountCode() + "]在群[" + groupInfo.getGroupCode()
                + "][" + groupInfo.getGroupName() + "]发送了信息：" + groupMsg.getMsg();

        System.out.println(groupMsgPutOut);

        writing.write(groupMsgPutOut + "\n");
    }


    /**
     * 帮助模块
     * #@Filter() 注解为消息过滤器
     *
     * @param groupMsg  用于获取群聊消息，群成员信息等
     * @param msgSender 用于在群聊中发送消息
     */
    @OnGroup
    @Filter(value = "/help", matchType = MatchType.REGEX_MATCHES, trim = true)
    public void help(GroupMsg groupMsg, MsgSender msgSender) {

        GroupInfo groupInfo = groupMsg.getGroupInfo();
        AccountInfo accountInfo = groupMsg.getAccountInfo();
        Bot bot = manager.getBot(BOOTID1);

        int groupBanId = (int) Arrays.stream(groupBanIdList).filter(groupInfo.getGroupCode()::contains).count();
        if (groupBanId != 1 && blackListService.selectCode(accountInfo.getAccountCode()) == null && BOOTSTATE) {

            MiraiMessageContentBuilder builder = factory.getMessageContentBuilder();
            builder.forwardMessage(fun -> {
                fun.add(bot.getBotInfo(), Constant.HELPS);
                fun.add(bot.getBotInfo(), SignConstant.GENSHIN_HELP);
            });
            msgSender.SENDER.sendGroupMsg(groupMsg, builder.build());
        }
    }


    /**
     * 关机模块
     * #@Filter() 注解为消息过滤器
     *
     * @param msgGet    获取消息类型
     * @param msgSender 用于发送消息
     */
    @OnGroup
    @OnPrivate
    @Filter(value = ".关机", matchType = MatchType.REGEX_MATCHES, trim = true)
    public void setGroupStateClose(MsgSender msgSender, MsgGet msgGet) {

        String setUser = msgGet.getAccountInfo().getAccountCode();

        if (judgeBan.allBan(msgGet)) {
            if (msgGet instanceof GroupMsg) {
                GroupMsg groupMsg = (GroupMsg) msgGet;
                if (setUser.equals(USERID1)) {
                    BOOTSTATE = true;
                    System.out.println("已关机");
                    msgSender.SENDER.sendGroupMsg(groupMsg, "姬姬关机了！");
                } else {
                    msgSender.SENDER.sendGroupMsg(groupMsg, "你没有姬姬的权限哦~");
                }
            } else {
                PrivateMsg privateMsg = (PrivateMsg) msgGet;
                if (setUser.equals(USERID1)) {
                    BOOTSTATE = true;
                    System.out.println("已关机");
                    msgSender.SENDER.sendPrivateMsg(privateMsg, "姬姬关机了！");
                } else {
                    msgSender.SENDER.sendPrivateMsg(privateMsg, "你没有姬姬的权限哦~");
                }
            }
        }
    }


    /**
     * 开机模块
     * #@Filter() 注解为消息过滤器
     *
     * @param msgSender 用于发送消息
     * @param msgGet    用于获取消息类型
     */
    @OnGroup
    @OnPrivate
    @Filter(value = ".开机", matchType = MatchType.REGEX_MATCHES, trim = true)
    public void setGroupStateOpen(MsgSender msgSender, MsgGet msgGet) {

        String setUser = msgGet.getAccountInfo().getAccountCode();

        if (judgeBan.allBan(msgGet)) {
            if (msgGet instanceof GroupMsg) {
                GroupMsg groupMsg = (GroupMsg) msgGet;
                if (setUser.equals(USERID1)) {
                    BOOTSTATE = true;
                    System.out.println("已开机");
                    msgSender.SENDER.sendGroupMsg(groupMsg, "姬姬开机了！");
                } else {
                    msgSender.SENDER.sendGroupMsg(groupMsg, "你没有姬姬的权限哦~");
                }
            } else {
                PrivateMsg privateMsg = (PrivateMsg) msgGet;
                if (setUser.equals(USERID1)) {
                    BOOTSTATE = true;
                    System.out.println("已开机");
                    msgSender.SENDER.sendPrivateMsg(privateMsg, "姬姬开机了！");
                } else {
                    msgSender.SENDER.sendPrivateMsg(privateMsg, "你没有姬姬的权限哦~");
                }
            }
        }
    }


    /**
     * 请求@全体成员模块
     * #@Filter() 注解为消息过滤器
     *
     * @param groupMsg  用于获取群聊消息，群成员信息等
     * @param msgSender 用于在群聊中发送消息
     */
    @OnGroup
    @Filter(value = "请求@全体成员 *{{msg}}", matchType = MatchType.REGEX_MATCHES, trim = true)
    public void atAll(GroupMsg groupMsg, MsgSender msgSender, @FilterValue("msg") String msg) {

        AccountInfo accountInfo = groupMsg.getAccountInfo();
        String groupId = groupMsg.getGroupInfo().getGroupCode();

        // 判断bot是否为管理员
        GroupMemberInfo groupMemberInfo = msgSender.GETTER.getMemberInfo(groupId, BOOTID1);

        // @的人
        String atPeople = "[CAT:at,code=" + accountInfo.getAccountCode() + "]";
        if (judgeBan.allBan(groupMsg)) {
            if (groupMemberInfo.getPermission().isAdmin() || groupMemberInfo.getPermission().isOwner()) {
                if (USERID1.equals(accountInfo.getAccountCode())) {
                    msgSender.SENDER.sendGroupMsg(groupId, cat1 + groupMsg.getMsg());
                } else {
                    msgSender.SENDER.sendGroupMsg(groupMsg, atPeople + "你没有权限@全体成员哦");
                }
            } else {
                msgSender.SENDER.sendGroupMsg(groupMsg, atPeople + "阿姬没有拿到权限！" + face);
            }
        }
    }

    /**
     * #@bot禁言模块
     * 通过正则表达是匹配禁言时间
     *
     * @param groupMsg  用于获取群聊消息，群成员信息等
     * @param msgSender 用于在群聊中发送消息
     */
    @OnGroup
    @Filter(value = "禁言*{{time,\\d+}}分钟", matchType = MatchType.REGEX_MATCHES, trim = true)
    public void atBot(GroupMsg groupMsg, MsgSender msgSender, @FilterValue("time") String time) {
        Setter setter = msgSender.SETTER;
        Sender sender = msgSender.SENDER;

        AccountInfo accountInfo = groupMsg.getAccountInfo();
        String groupId = groupMsg.getGroupInfo().getGroupCode();

        // 判断bot是否为管理员
        GroupMemberInfo groupMemberInfo = msgSender.GETTER.getMemberInfo(groupId, BOOTID1);

        // @的人
        String atPeople = "[CAT:at,code=" + accountInfo.getAccountCode() + "]";

        if (judgeBan.allBan(groupMsg)) {
            if (groupMemberInfo.getPermission().isAdmin() || groupMemberInfo.getPermission().isOwner()) {
                if (USERID1.equals(accountInfo.getAccountCode())) {

                    setter.setGroupBan(groupId, Objects.requireNonNull(CatUtil.getAt(groupMsg.getMsg())), Long.parseLong(time), TimeUnit.MINUTES);
                    // setter.setGroupWholeBan(GROUPID1,true); 群禁言
                } else {
                    sender.sendGroupMsg(groupMsg, atPeople + "你没有权限禁言哦");
                }
            } else {
                sender.sendGroupMsg(groupMsg, atPeople + "阿姬没有拿到权限！" + face);
            }
        }
    }

    /**
     * 解除禁言模块
     * #@Filter() 注解为消息过滤器
     *
     * @param groupMsg  用于获取群聊消息，群成员信息等
     * @param msgSender 用于在群聊中发送消息
     */
    @OnGroup
    @Filter(value = "解除禁言", matchType = MatchType.REGEX_MATCHES, trim = true)
    public void removeBan(GroupMsg groupMsg, MsgSender msgSender) {
        // 获取群信息-群号-个人信息
        AccountInfo accountInfo = groupMsg.getAccountInfo();
        String groupId = groupMsg.getGroupInfo().getGroupCode();


        // 通过猫猫码获取被@的人
        String people = CatUtil.getAt(groupMsg.getMsg());

        // 判断bot是否为管理员
        GroupMemberInfo groupMemberInfo = msgSender.GETTER.getMemberInfo(groupId, BOOTID1);

        // @的人
        String atPeople = "[CAT:at,code=" + accountInfo.getAccountCode() + "]";

        if (judgeBan.allBan(groupMsg)) {
            if (groupMemberInfo.getPermission().isAdmin() || groupMemberInfo.getPermission().isOwner()) {
                if (USERID1.equals(accountInfo.getAccountCode())) {

                    // 将群成员设置为解除禁言的状态
                    assert people != null;
                    msgSender.SETTER.setGroupBan(groupId, people, 0, TimeUnit.MINUTES);
                } else {
                    msgSender.SETTER.setGroupBan(groupId, atPeople, 0, TimeUnit.MINUTES);
                    msgSender.SENDER.sendGroupMsg(groupId, atPeople + "你没有权限解除禁言哦~");
                }
            } else {
                msgSender.SENDER.sendGroupMsg(groupMsg, atPeople + "阿姬没有拿到权限！" + face);
            }
        }
    }


    /**
     * 刷屏模块
     * #@Filter() 注解为消息过滤器
     *
     * @param groupMsg  用于获取群聊消息，群成员信息等
     * @param msgSender 用于在群聊中发送消息
     */
    @OnGroup
    @Filter(atBot = true, value = "/刷屏", matchType = MatchType.REGEX_MATCHES, trim = true)
    public void swipe(GroupMsg groupMsg, MsgSender msgSender) {
        AccountInfo accountInfo = groupMsg.getAccountInfo();
        String setUser = accountInfo.getAccountCode();

        if (judgeBan.allBan(groupMsg)) {
            if (setUser.equals(USERID1)) {
                int times = 20;
                for (int i = 0; i < times; i++) {
                    msgSender.SENDER.sendGroupMsg(groupMsg, "阿姬在！" + face2);
                }

            } else {
                msgSender.SENDER.sendGroupMsg(groupMsg, "刷屏是不好的哦~");
            }
        }
    }


    /**
     * 群加入申请
     *
     * @param groupAddRequest 用于获取群加入申请的请求
     * @param msgSender       用于在群聊中发送消息
     * @param groupMsg        用于获取群聊消息，群成员信息等
     */
    @OnGroupAddRequest
    public void onRequest(GroupAddRequest groupAddRequest, MsgSender msgSender, GroupMsg groupMsg) {

        //入群者信息
        AccountInfo accountInfo = groupAddRequest.getAccountInfo();
        GroupInfo groupInfo = groupMsg.getGroupInfo();

        // 获取时间
        String format1 = time.tt();

        // 将入群信息存入日志
        String msg = "[" + format1 + "][" + accountInfo.getAccountNickname()
                + "--" + accountInfo.getAccountCode() + "]申请加入群聊：[" + groupInfo.getGroupCode()
                + "/" + groupInfo.getGroupName() + "]\n";
        PeopleChangeWrite peopleChangeWrite = new PeopleChangeWrite();
        peopleChangeWrite.writeRequest(msg);

        if (groupInfo.getGroupCode().equals(GROUPID1) || groupInfo.getGroupCode().equals(GROUPID2) ||
                groupInfo.getGroupCode().equals(GROUPID3) || groupInfo.getGroupCode().equals(GROUPID4)) {
            msgSender.SENDER.sendPrivateMsg(USERID1, msg);
        }
    }

    /**
     * 入群欢迎模块
     *
     * @param groupMemberIncrease 群成员增加信息
     * @param msgSender           用于在群聊中发送消息
     */
    @OnGroupMemberIncrease
    public void memberIncrease(GroupMemberIncrease groupMemberIncrease, MsgSender msgSender, MsgGet msgGet) {

        // 得到一个消息构建器。
        MessageContentBuilder builder = factory.getMessageContentBuilder();

        //入群者信息
        AccountInfo accountInfo = groupMemberIncrease.getAccountInfo();
        GroupInfo groupInfo = groupMemberIncrease.getGroupInfo();


        if (judgeBan.allBan(msgGet)) {

            Sender sender = msgSender.SENDER;

            // 获取时间
            String format1 = time.tt();

            // 将入群信息存入日志
            PeopleChangeWrite peopleChangeWrite = new PeopleChangeWrite();
            String msgs = "[" + format1 + "][" + accountInfo.getAccountCode() + "--" + accountInfo.getAccountNickname()
                    + "]加入群聊：[" + groupInfo.getGroupCode() + "/" + groupInfo.getGroupName() + "]\n";
            peopleChangeWrite.writeIncrease(msgs);
            if (groupInfo.getGroupCode().equals(GROUPID1) || groupInfo.getGroupCode().equals(GROUPID2) ||
                    groupInfo.getGroupCode().equals(GROUPID3)) {
                msgSender.SENDER.sendPrivateMsg("2094085327", msgs);
            }

            // 线程池
            if (groupInfo.getGroupCode().equals(GROUPID3)) {
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
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
            }

            if (groupInfo.getGroupCode().equals(GROUPID2)) {

                MessageContent msg1 = builder
                        // at当事人
                        .at(accountInfo)
                        // tips 通过 \n 换行
                        .text(" 欢迎入群！")
                        .build();
                // 通过猫猫码发送表情
                String cat2 = "[CAT:face,id=277]";
                //消息
                String msg2 = " 请看置顶消息！如果没有就当我没说";
                //消息
                String msg3 = "请及时修改昵称!";


                // 发送消息
                sender.sendGroupMsg(groupInfo, msg1);
                sender.sendGroupMsg(groupInfo, msg2 + cat2);
                sender.sendGroupMsg(groupInfo, msg3);
            }

            if (groupInfo.getGroupCode().equals(GROUPID5)) {

                MessageContent msg = builder
                        // at当事人
                        .at(accountInfo)
                        // tips 通过 \n 换行
                        .text(" 欢迎加入!")
                        .build();

                // 发送消息
                String msg2 = "新人爆照！";
                sender.sendGroupMsg(groupInfo, msg);
                sender.sendGroupMsg(groupInfo, msg2);

            }
            System.out.println("--[" + format1 + "][" + accountInfo.getAccountCode() + "--" + accountInfo.getAccountNickname()
                    + "]加入群聊：[" + groupInfo.getGroupCode() + "/" + groupInfo.getGroupName() + "]--" + "\n");

        }
    }

    /**
     * 群成员减少事件监听
     *
     * @param groupMemberReduce 群成员减少信息
     */
    @OnGroupMemberReduce
    public void memberReduce(GroupMemberReduce groupMemberReduce, MsgSender msgSender) {

        //退群者信息
        AccountInfo accountInfo = groupMemberReduce.getAccountInfo();
        GroupInfo groupInfo = groupMemberReduce.getGroupInfo();

        // 判断退群方式和获取操作员
        // 主动退群
        if (groupMemberReduce.getReduceType().equals(GroupMemberReduce.Type.LEAVE)) {
            msgSender.SENDER.sendPrivateMsg(USERID1, "[" + groupInfo.getGroupCode() + "-" + groupInfo.getGroupName() + "]["
                    + accountInfo.getAccountCode() + "-" + accountInfo.getAccountNickname() + "]是自愿退群的呢，走的" +
                    "很安详~");
        }
        // 被踢出群聊
        else {
            msgSender.SENDER.sendPrivateMsg(USERID1, "[" + groupInfo.getGroupCode() + "-" + groupInfo.getGroupName()
                    + "][" + accountInfo.getAccountCode() + "-" + accountInfo.getAccountNickname() + "]是被["
                    + Objects.requireNonNull(groupMemberReduce.getOperatorInfo()).getOperatorCode() + "-"
                    + Objects.requireNonNull(groupMemberReduce.getOperatorInfo()).getOperatorRemarkOrNickname() + "]踢出群聊的呢，走的很不甘心~");
        }

        // 获取时间
        String format1 = time.tt();

        // 将入群信息存入日志
        PeopleChangeWrite peopleChangeWrite = new PeopleChangeWrite();
        String msg = "[" + format1 + "][" + accountInfo.getAccountCode() + "--" + accountInfo.getAccountNickname()
                + "]退出群聊：[" + groupInfo.getGroupCode() + "/" + groupInfo.getGroupName() + "]" + "\n";

        peopleChangeWrite.writeReduce(msg);

        // 在控制台输出退群信息
        System.out.println(msg);
    }

    /**
     * 好友增加事件
     *
     * @param friendIncrease 好友增加信息
     */
    @OnFriendIncrease
    public void friendIncrease(FriendIncrease friendIncrease) {
        AccountInfo accountInfo = friendIncrease.getAccountInfo();

        // 获取时间
        String format1 = time.tt();

        // 将人员信息存入日志
        PeopleChangeWrite peopleChangeWrite = new PeopleChangeWrite();
        peopleChangeWrite.writeReduce("[" + format1 + "]你已同意["
                + accountInfo.getAccountNickname() + "--" + accountInfo.getAccountCode()
                + "]成为好友" + "\n");

        System.out.println("--[" + format1 + "]--你已同意[" + accountInfo.getAccountNickname()
                + "--" + accountInfo.getAccountCode()
                + "]成为好友--" + "\n");
    }

    /**
     * 好友减少事件
     *
     * @param friendReduce 好友减少信息
     */
    @OnFriendReduce
    public void friendReduce(FriendReduce friendReduce) {
        AccountInfo accountInfo = friendReduce.getAccountInfo();

        // 获取时间
        String format1 = time.tt();

        // 将人员信息存入日志
        PeopleChangeWrite peopleChangeWrite = new PeopleChangeWrite();
        peopleChangeWrite.writeRequest("[" + format1 + "][" + accountInfo.getAccountNickname() + "--" + accountInfo.getAccountCode()
                + "]离开了你" + "\n");

        System.out.println("--[" + format1 + "][" + accountInfo.getAccountNickname() + "--" + accountInfo.getAccountCode()
                + "]离开了你--" + "\n");

    }

    /**
     * 好友申请事件
     *
     * @param friendAddRequest 好友增加信息
     */
    @OnFriendAddRequest
    public void friendRequest(FriendAddRequest friendAddRequest) {
        AccountInfo accountInfo = friendAddRequest.getAccountInfo();

        // 获取时间
        String format1 = time.tt();

        // 将人员信息存入日志
        PeopleChangeWrite peopleChangeWrite = new PeopleChangeWrite();
        peopleChangeWrite.friendRequest("[" + format1 + "]["
                + accountInfo.getAccountNickname() + "--" + accountInfo.getAccountCode()
                + "]申请成为你的好友" + "\n");

        System.out.println("--[" + format1 + "][" + accountInfo.getAccountNickname()
                + "--" + accountInfo.getAccountCode()
                + "]申请成为你的好友--" + "\n");

    }

    List<String> msgList = new ArrayList<>();

    /**
     * 读取TXT中的句子并进行初始化存入list中
     *
     * @throws Exception Io流异常
     */
    public void readTxt() throws Exception {
        File idiomsTxt = new File("GameRes/随机句子.txt").getAbsoluteFile();
        if (idiomsTxt.isFile() && idiomsTxt.exists()) {

            InputStreamReader reader = new InputStreamReader(new FileInputStream(idiomsTxt), StandardCharsets.UTF_8);

            BufferedReader bufferedReader = new BufferedReader(reader);

            String linTxt;
            while ((linTxt = bufferedReader.readLine()) != null) {
                msgList.add(linTxt);
            }
            reader.close();
        } else {
            System.out.println("读取出错");
        }
    }

    Map<String, Integer> map = new HashMap<>();

    /**
     * 在群聊中随机发送句子
     *
     * @param groupMsg  群聊消息
     * @param msgSender 发送消息
     */
    @OnGroup
    public void randomMsg(GroupMsg groupMsg, MsgSender msgSender) {

        String groupId = groupMsg.getGroupInfo().getGroupCode();

        if (judgeBan.allBan(groupMsg)) {
            map.merge(groupId, 1, Integer::sum);
            if (map.get(groupId) == 100) {
                msgSender.SENDER.sendGroupMsg(groupMsg, msgList.get((int) (Math.random() * msgList.size())));
                map.put(groupId, 0);
            }
        }
    }
}