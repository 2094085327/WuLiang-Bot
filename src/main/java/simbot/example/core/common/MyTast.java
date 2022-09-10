package simbot.example.core.common;

import catcode.CatCodeUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import love.forte.simbot.api.message.results.GroupMemberInfo;
import love.forte.simbot.api.sender.BotSender;
import love.forte.simbot.bot.Bot;
import love.forte.simbot.bot.BotManager;
import love.forte.simbot.timer.Cron;
import love.forte.simbot.timer.EnableTimeTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import simbot.example.BootAPIUse.OtherAPI.NewsApi;
import simbot.example.BootAPIUse.OtherAPI.OtherApi;
import simbot.example.BootAPIUse.YuanShenAPI.Sign.GenShinSign;
import simbot.example.Enity.GenShinUser;
import simbot.example.Mapper.GenShinMapper;
import simbot.example.Service.GenShinService;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author zeng
 * @date 2022/6/25 0:03
 * @user 86188
 */
@Service
@EnableTimeTask
@SuppressWarnings("unused")
public class MyTast extends Constant {

    GenShinService genShinService;
    GenShinMapper genShinMapper;
   // GenShinSign genShinSign;

    @Autowired
    public MyTast(GenShinService genShinService, GenShinMapper genShinMapper  /*,GenShinSign genShinSign*/) {
        this.genShinService = genShinService;
        this.genShinMapper = genShinMapper;
      //  this.genShinSign = genShinSign;
    }


    /**
     * 构建机器人管理器
     */
    @Resource
    public BotManager manager;

    private void sendGroupMsg(String g, String msg) {
        Bot bot = manager.getBot(BOOTID1);
        BotSender bs = bot.getSender();
        bs.SENDER.sendGroupMsg(g, msg);
    }

    private void sendPrivateMsg(String g, String msg) {
        Bot bot = manager.getBot("341677404");
        BotSender bs = bot.getSender();
        // 判断bot是否为管理员
        GroupMemberInfo groupMemberInfo = bs.GETTER.getMemberInfo("1019170385", g);
        //groupMsg.
        bs.SENDER.sendPrivateMsg(groupMemberInfo.getAccountCode(), msg);
        //bs.SENDER.sendPrivateMsg(g, msg);
    }

    /**
     * 调用每日一言APi
     */
    public OtherApi otherApi = new OtherApi();

    TimeTranslate time2 = new TimeTranslate();


    /**
     * 每日一言构建定时器
     * #@Fixed(value = 60, timeUnit = TimeUnit.MINUTES)
     */
    public void task() {

        Bot bot = manager.getBot("341677404");
        BotSender botSender = bot.getSender();


        botSender.SENDER.sendGroupMsg("1043409458", otherApi.everydaySentences());

    }

    @Cron(value = "0 32 20 * * ? *")
    public void img() {
        Bot bot = manager.getBot("341677404");
        BotSender botSender = bot.getSender();

        CatCodeUtil util = CatCodeUtil.INSTANCE;
        String img = util.toCat("image", true, "file="
                + "https://c2cpicdw.qpic.cn/offpic_new/2094085327//2094085327-1184240239-588C5FFE182E2972466B8B2403F76CBC/0?term&#61;2");

        botSender.SENDER.sendGroupMsg("1043409458", img);
    }

    @Cron(value = "0 00 07 * * ? *")
    public void sendNews() {
        NewsApi newsApi = new NewsApi();
        sendGroupMsg("1043409458", String.valueOf(newsApi.getNewsMsg()));
    }

    /**
     * #@Fixed(value = 30, timeUnit = TimeUnit. SECONDS)
     */
    public void bLive() {
        Bot bot = manager.getBot("341677404");
        BotSender botSender = bot.getSender();
        String state1 = "false";
        String state2 = "true";
        if (state1.equals(BLIVESTATE)) {
            otherApi.bLiveHelp(BiUpUid);
        }
        if (state2.equals(BLIVESTATE)) {
            botSender.SENDER.sendGroupMsg("140469072", otherApi.bLive(BiUpUid));
        }
    }

    /**
     * 原神定时签到
     */
    @Cron(value = "00 01 00 * * ? *")
    public void genShinSign() {
        genShinService.signAll();
    }
}