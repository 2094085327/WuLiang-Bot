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
import simbot.example.BootAPIUse.API;
import simbot.example.BootAPIUse.OtherAPI.NewsApi;
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
public class MyTast extends Constant {

    @Autowired
    GenShinService genShinService;

    @Autowired
    GenShinMapper genShinMapper;


    public NewsApi news = new NewsApi();
    static int i = 1;

    /**
     * 构建机器人管理器
     */
    @Resource
    public BotManager manager;

    private void sendGroupMsg(String g, String msg) {
        Bot bot = manager.getBot("341677404");
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
    public API api = new API();

    TimeTranslate time2 = new TimeTranslate();


    /**
     * 每日一言构建定时器
     * #@Fixed(value = 60, timeUnit = TimeUnit.MINUTES)
     */
    public void task() {

        Bot bot = manager.getBot("341677404");
        BotSender botSender = bot.getSender();


        botSender.SENDER.sendGroupMsg("1043409458", api.EverydaySentences());

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

    //@Fixed(value = 30, timeUnit = TimeUnit.SECONDS)
    public void bLive() {
        Bot bot = manager.getBot("341677404");
        BotSender botSender = bot.getSender();
        if ("false".equals(BLIVESTATE)) {
            api.bLiveHelp(BiUpUid);
        }
        if ("true".equals(BLIVESTATE)) {
            botSender.SENDER.sendGroupMsg("140469072", api.bLive(BiUpUid));
        }
    }

    /**
     * 原神定时签到
     */
    @Cron(value = "00 01 00 * * ? *")
    public void genShinSign() {
        // 构造搜索条件，搜索未删除的cookie
        QueryWrapper<GenShinUser> queryWrapper = Wrappers.query();
        queryWrapper.like("deletes", 0);

        List<GenShinUser> genShinUsers1 = genShinMapper.selectList(queryWrapper);

        // 签到类
        GenShinSign genShinSign = new GenShinSign();

        CatCodeUtil util = CatCodeUtil.INSTANCE;
        // 取出list中的数据依次进行签到
        for (GenShinUser genShinUser : genShinUsers1) {

            // 执行签到,同时对当前数据进行初始化
            if (genShinSign.doSign(genShinService.selectUid(genShinUser.getUid()))) {

                if (GenShinSign.push == 1) {
                    // @的人
                    String atPeople = "[CAT:at,code=" + genShinUser.getQqid() + "]";
                    sendGroupMsg("1019170385", atPeople + GenShinSign.getMessage());

                    sendGroupMsg("140469072", GenShinSign.getItemMsg() + "\n" + util.toCat("image", true, "file=" + GenShinSign.getItemImg()));
                }
                genShinSign.signList(genShinUser.getUid());

                sendGroupMsg("140469072", GenShinSign.getItemMsg() + "\n" + util.toCat("image", true, "file=" + GenShinSign.getItemImg()));
            }
            sendGroupMsg("140469072", genShinUser.getNickname() + ":" + GenShinSign.getMessage());
        }
    }
}