package simbot.example.BootAPIUse.Game.LifeRestart;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.FilterValue;
import love.forte.simbot.annotation.OnGroup;
import love.forte.simbot.api.message.MessageContentBuilder;
import love.forte.simbot.api.message.MessageContentBuilderFactory;
import love.forte.simbot.api.message.containers.AccountInfo;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.sender.MsgSender;
import love.forte.simbot.component.mirai.message.MiraiMessageContentBuilder;
import love.forte.simbot.component.mirai.message.MiraiMessageContentBuilderFactory;
import love.forte.simbot.filter.MatchType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import simbot.example.Util.ContextUtil;
import simbot.example.core.common.JudgeBan;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author zeng
 * @Date 2022/9/18 22:34
 * @User 86188
 * @Description: 人生重开模拟器
 */
@Service
public class LifeRestart {
    static JSONObject age = new JSONObject();
    static JSONObject event = new JSONObject();

    /**
     * 正在使用的账户和账户年龄
     */
    private final Map<String, Integer> user = new HashMap<>();
    /**
     * 防刷屏名单
     */
    private final Map<String, Integer> unUser = new HashMap<>();
    /**
     * 属性点
     */
    private final Map<String, Map<String, Integer>> attributes = new HashMap<>();
    /**
     * 事件列表
     */
    private final Map<String, ArrayList<String>> myEventMap = new HashMap<>();
    private final Map<String, Integer> endMap = new HashMap<>();

    JudgeBan judgeBan = new JudgeBan();
    ContextUtil contextUtil = new ContextUtil();

    /**
     * 通过自动装配构建消息工厂
     */
    MessageContentBuilderFactory messageContentBuilderFactory;
    /**
     * 合并转发
     */
    ApplicationContext applicationContext;
    /**
     * 注入得到一个消息构建器工厂
     */
    private MiraiMessageContentBuilderFactory factory;

    @Autowired
    public LifeRestart(MessageContentBuilderFactory messageContentBuilderFactory, ApplicationContext applicationContext) {
        try {
            age = JSONObject.parseObject(new FileInputStream(new File("resources/liftReStartJson/age.json").getAbsoluteFile()), JSONObject.class);

            event = JSONObject.parseObject(new FileInputStream(new File("resources/liftReStartJson/events.json").getAbsoluteFile()), JSONObject.class);
            this.messageContentBuilderFactory = messageContentBuilderFactory;
            this.applicationContext = applicationContext;
            System.out.println("--人生重开加载成功--");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @PostConstruct
    public void init() {
        contextUtil.setApplicationContext(applicationContext);
        factory = ContextUtil.getForwardBuilderFactory();
    }

    /**
     * 开始一局新游戏
     *
     * @param groupMsg  群聊
     * @param msgSender 发送消息
     */
    @OnGroup
    @Filter(value = "人生重开")
    public void startRestart(GroupMsg groupMsg, MsgSender msgSender) {
        if (judgeBan.allBan(groupMsg)) {
            ArrayList<String> myList = new ArrayList<>();
            Map<String, Integer> mine = new HashMap<>(5);

            String userId = groupMsg.getAccountInfo().getAccountCode();
            myEventMap.remove(userId);

            user.put(userId, 0);
            if (unUser.get(userId) != null) {
                unUser.remove(userId);
            }
            JSONArray ages = age.getJSONArray(String.valueOf(user.get(userId)));

            // 随机获取事件
            int index = (int) (Math.random() * ages.size());

            // 分割字符串
            String ageStr = ages.get(index).toString().split("\\*")[0];

            myList.add(ageStr);

            // 将已经触发的事件存入list
            myEventMap.put(userId, myList);

            JSONObject events = event.getJSONObject(ageStr);

            int chr = (int) (Math.random() * 11);
            int intInt = (int) (Math.random() * (21 - chr));
            int str = (int) (Math.random() * (21 - chr - intInt));
            int mny = (int) (Math.random() * (21 - chr - intInt - str));
            int spr = (int) (Math.random() * (21 - chr - intInt - str - mny));

            mine.put("CHR", chr);
            mine.put("INT", intInt);
            mine.put("STR", str);
            mine.put("MNY", mny);
            mine.put("SPR", spr);

            attributes.put(userId, mine);

            msgSender.SENDER.sendGroupMsg(groupMsg, "[CAT:at,code=" + userId + "]你获得的初始属性是:\n   颜值:" + mine.get("CHR") + "\n   智力:" + mine.get("INT") + "\n   体质:" + mine.get("STR") + "\n   家境:" + mine.get("MNY") + "\n   快乐:" + mine.get("SPR"));

            msgSender.SENDER.sendGroupMsg(groupMsg, "[CAT:at,code=" + userId + "]0岁   " + events.getString("事件内容"));

            boolean judgeEvent = events.getString("优先分支") != null && events.getString("优先分支").contains("10000") || events.getString("分支") != null && events.getString("分支").contains("10000");

            if (judgeEvent) {
                msgSender.SENDER.sendGroupMsg(groupMsg, user.get(userId) + "岁    你死了");
                msgSender.SENDER.sendGroupMsg(groupMsg, "输入[人生重开]重新开始游戏");
                user.remove(userId);
                myEventMap.remove(userId);
            }
        }
    }

    /**
     * 收到继续指令后发送下一条人生事件
     *
     * @param groupMsg  群聊
     * @param msgSender 消息发送
     */
    @OnGroup
    @Filter(value = "继续")
    public void randomEvent(GroupMsg groupMsg, MsgSender msgSender) {

        if (judgeBan.allBan(groupMsg)) {

            String userId = groupMsg.getAccountInfo().getAccountCode();

            if (user.get(userId) != null) {

                ArrayList<String> eventsList = getEvent(userId, 1,groupMsg.getAccountInfo().getAccountAvatar());
                for (String events : eventsList) {
                    msgSender.SENDER.sendGroupMsg(groupMsg, "[CAT:at,code=" + userId + "]" + events);
                }

            } else {
                if (unUser.get(userId) == null) {
                    msgSender.SENDER.sendGroupMsg(groupMsg, "[CAT:at,code=" + userId + "]你还没有创建账户,输入[人生重开]开始游戏");
                    unUser.put(userId, 1);
                } else {
                    unUser.put(userId, unUser.get(userId) + 1);
                }
            }
        }
    }

    /**
     * 继续的加强版，能够自定义事件数，但限制上限为20
     *
     * @param groupMsg  群聊
     * @param msgSender 消息发送
     * @param times     运行次数
     */
    @OnGroup
    @Filter(value = "继续 *{{times}}", matchType = MatchType.REGEX_MATCHES, trim = true)
    public void randomEventAll(GroupMsg groupMsg, MsgSender msgSender, @FilterValue("times") int times) {

        AccountInfo accountInfo = groupMsg.getAccountInfo();

        String avatar = accountInfo.getAccountAvatar();
        if (judgeBan.allBan(groupMsg)) {

            String userId = groupMsg.getAccountInfo().getAccountCode();

            // 账户存在时进行循环
            if (user.get(userId) != null) {
                int size = 20;
                if (times <= size && times > 0) {
                    ArrayList<String> eventsList = getEvent(userId, times, avatar);
                    MiraiMessageContentBuilder builder = factory.getMessageContentBuilder();
                    //构建合并转发消息
                    builder.forwardMessage(fun -> {
                        for (String s : eventsList) {
                            fun.add(accountInfo, s);
                        }
                    });

                    msgSender.SENDER.sendGroupMsg(groupMsg, builder.build());
                    if (endMap.get(userId) == 1) {
                        try {
                            InputStream inputStream = new FileInputStream(new File("resources/GameRes/image/" + userId + ".png").getAbsoluteFile());

                            // 创建消息构建器，用于在服务器上发送图片
                            MessageContentBuilder messageContentBuilder = messageContentBuilderFactory.getMessageContentBuilder();

                            msgSender.SENDER.sendGroupMsg(groupMsg, messageContentBuilder.image(inputStream).build());

                            inputStream.close();
                            endMap.remove(userId);
                            Files.delete(Paths.get(new File("resources/GameRes/image/" + userId + ".png").getAbsolutePath()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    // 输入的循环次数超过20时进行提示并加入防刷屏名单
                    msgSender.SENDER.sendGroupMsg(groupMsg, "[CAT:at,code=" + userId + "]只能输入到0~20哦~");
                    unUser.put(userId, 1);
                }
            } else {
                // 当防刷屏名单中不存在此账户时进行提示
                if (unUser.get(userId) == null) {
                    msgSender.SENDER.sendGroupMsg(groupMsg, "[CAT:at,code=" + userId + "]你还没有创建账户,输入[人生重开]开始游戏");
                    unUser.put(userId, 1);
                } else {
                    // 存在时+1
                    unUser.put(userId, unUser.get(userId) + 1);
                }
            }
        }
    }

    @OnGroup
    @Filter("查看属性")
    public void show(GroupMsg groupMsg, MsgSender msgSender) {
        String userId = groupMsg.getAccountInfo().getAccountCode();
        Map<String, Integer> showAttributes = attributes.get(userId);

        msgSender.SENDER.sendGroupMsg(groupMsg, "[CAT:at,code=" + userId + "]你现在的属性是:\n   颜值:" + showAttributes.get("CHR") + "\n   智力:" + showAttributes.get("INT") + "\n   体质:" + showAttributes.get("STR") + "\n   家境:" + showAttributes.get("MNY") + "\n   快乐:" + showAttributes.get("SPR"));
    }

    @OnGroup
    @Filter(value = "结束人生")
    public void exitRestart(GroupMsg groupMsg, MsgSender msgSender) {
        if (judgeBan.allBan(groupMsg)) {
            String userId = groupMsg.getAccountInfo().getAccountCode();
            if (user.get(userId) != null) {
                user.remove(userId);
                msgSender.SENDER.sendGroupMsg(groupMsg, "[CAT:at,code=" + userId + "]您已退出[人生重开模拟器]\n期待您的下次游玩");
            }
        }
    }

    /**
     * 获取事件
     *
     * @param userId 账户
     * @param times  继续的次数
     * @return 返回存储了信息的ArrayList
     */
    public ArrayList<String> getEvent(String userId, int times, String urls) {
        ArrayList<String> eventList = new ArrayList<>();
        ArrayList<String> myEventList;

        // 循环获取事件
        for (int i = 0; i < times; i++) {
            Map<String, Integer> mine = attributes.get(userId);
            user.put(userId, user.get(userId) + 1);
            JSONArray ages = age.getJSONArray(String.valueOf(user.get(userId)));
            // 随机获取事件
            int index = (int) (Math.random() * ages.size());
            // 分割字符串
            String ageStr = ages.get(index).toString().split("\\*")[0];

            JSONObject events = event.getJSONObject(ageStr);
            JSONArray impArray = events.getJSONArray("有某事件时一定随机不到");
            myEventList = myEventMap.get(userId);

            impArray.retainAll(myEventList);
            while (!impArray.isEmpty()) {
                System.out.println("年龄:" + user.get(userId) + "旧事件" + ageStr);
                // 存在不可能事件，进入循环重新获取不冲突的事件
                ageStr = ages.get((int) (Math.random() * ages.size())).toString().split("\\*")[0];
                events = event.getJSONObject(ageStr);
                impArray = events.getJSONArray("有某事件时一定随机不到");
                impArray.retainAll(myEventList);

                System.out.println("新事件" + ageStr);
            }
            myEventList.add(ageStr);
            myEventMap.put(userId, myEventList);

            eventList.add(user.get(userId) + "岁   " + events.getString("事件内容"));
            if (events.getInteger("颜值变化") != null) {

                mine.put("CHR", mine.get("CHR") + events.getInteger("颜值变化"));
                attributes.put(userId, mine);

                eventList.add("你的颜值变化:" + events.getInteger("颜值变化"));
            }
            if (events.getInteger("智力变化") != null) {

                mine.put("INT", mine.get("INT") + events.getInteger("智力变化"));
                attributes.put(userId, mine);

                eventList.add("你的智力变化:" + events.getInteger("智力变化"));
            }
            if (events.getInteger("体质变化") != null) {

                mine.put("STR", mine.get("STR") + events.getInteger("体质变化"));
                attributes.put(userId, mine);

                eventList.add("你的体质变化:" + events.getInteger("体质变化"));
            }
            if (events.getInteger("家境变化") != null) {

                mine.put("MNY", mine.get("MNY") + events.getInteger("家境变化"));
                attributes.put(userId, mine);

                eventList.add("你的家境变化:" + events.getInteger("家境变化"));
            }
            if (events.getInteger("快乐变化") != null) {

                mine.put("SPR", mine.get("SPR") + events.getInteger("快乐变化"));
                attributes.put(userId, mine);

                eventList.add("你的快乐变化:" + events.getInteger("快乐变化"));
            }


            boolean judgeEvent = events.getString("优先分支") != null && events.getString("优先分支").contains("10000") || events.getString("分支") != null && events.getString("分支").contains("10000");

            if (judgeEvent) {
                eventList.add(user.get(userId) + "岁    你死了");
                System.out.println(attributes.get(userId));
                eventList.add("输入[人生重开]重新开始游戏");
                EndGame.makePicture(userId, user.get(userId).toString(), attributes.get(userId),urls);
                endMap.put(userId, 1);
                user.remove(userId);
                break;
            }
        }
        return eventList;
    }

}
