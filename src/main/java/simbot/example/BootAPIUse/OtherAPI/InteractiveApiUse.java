package simbot.example.BootAPIUse.OtherAPI;

import catcode.CatCodeUtil;
import love.forte.common.ioc.annotation.Beans;
import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.OnGroup;
import love.forte.simbot.api.message.containers.AccountInfo;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.sender.MsgSender;
import love.forte.simbot.api.sender.Sender;
import love.forte.simbot.filter.MatchType;
import org.springframework.stereotype.Service;
import simbot.example.Util.CatUtil;
import simbot.example.core.common.Constant;


/**
 * @author zeng
 * @date 2022/7/25 17:43
 * @user 86188
 */
@Service
public class InteractiveApiUse extends Constant {

    /**
     * 互动模块-丢
     * sender.sendGroupMsg(groupMsg, Objects.requireNonNull(CatUtil.getAt(groupMsg.getMsg())));
     * 上方代码可以在不封装猫猫码类的时候实现获取id
     *
     * @param groupMsg  群消息
     * @param msgSender 消息发送
     */
    @OnGroup
    @Filter(value = "/丢", matchType = MatchType.REGEX_MATCHES, trim = true)
    public void diu(GroupMsg groupMsg, MsgSender msgSender) {

        DiuProvider diuProvider = DiuProvider.DIU;
        Sender sender = msgSender.SENDER;
        AccountInfo accountInfo = groupMsg.getAccountInfo();

        // 通过猫猫码发送图片
        CatCodeUtil util = CatCodeUtil.INSTANCE;

        try {
            String msg = util.toCat("image", true, "file="
                    + diuProvider + CatUtil.getAt(groupMsg.getMsg()));
            sender.sendGroupMsg(groupMsg, msg);
        } catch (Exception e) {
            String atMe = "[CAT:at,code=2094085327]";
            String face = "[CAT:face,id=5]";
            String atOther = "[CAT:at,code=" + accountInfo.getAccountCode() + "]";
            sender.sendGroupMsg(groupMsg, atOther + "姬姬丢不出来" + face);
            sender.sendGroupMsg(groupMsg, atMe + "快来看看你接口是不是炸了！");
        }
    }

    /**
     * 互动模块-拍
     *
     * @param groupMsg  群消息
     * @param msgSender 消息发送
     */
    @OnGroup
    @Filter(value = "/拍", matchType = MatchType.REGEX_MATCHES, trim = true)
    public void pai(GroupMsg groupMsg, MsgSender msgSender) {

        DiuProvider diuProvider = DiuProvider.PAI;
        Sender sender = msgSender.SENDER;

        CatCodeUtil util = CatCodeUtil.INSTANCE;
        String msg = util.toCat("image", true, "file="
                + diuProvider + CatUtil.getAt(groupMsg.getMsg()));
        /*
         */


        sender.sendGroupMsg(groupMsg, msg);
    }

    /**
     * 互动模块-抓
     *
     * @param groupMsg  群消息
     * @param msgSender 消息发送
     */
    @OnGroup
    @Filter(value = "/抓", matchType = MatchType.REGEX_MATCHES, trim = true)
    public void grab(GroupMsg groupMsg, MsgSender msgSender) {

        DiuProvider diuProvider = DiuProvider.GRAB;
        Sender sender = msgSender.SENDER;
        AccountInfo accountInfo = groupMsg.getAccountInfo();

        CatCodeUtil util = CatCodeUtil.INSTANCE;
        try {
            String msg = util.toCat("image", true, "file="
                    + diuProvider + CatUtil.getAt(groupMsg.getMsg()));
            sender.sendGroupMsg(groupMsg, msg);
        } catch (Exception e) {
            String atMe = "[CAT:at,code=2094085327]";
            String face = "[CAT:face,id=5]";
            String atOther = "[CAT:at,code=" + accountInfo.getAccountCode() + "]";

            sender.sendGroupMsg(groupMsg, atOther + "姬姬抓不到" + face);
            sender.sendGroupMsg(groupMsg, atMe + "快来看看你接口是不是炸了！");

        }


    }

    /**
     * 互动模块-抱
     *
     * @param groupMsg  群消息
     * @param msgSender 消息发送
     */
    @OnGroup
    @Filter(value = "/抱", matchType = MatchType.REGEX_MATCHES, trim = true)
    public void bao(GroupMsg groupMsg, MsgSender msgSender) {

        DiuProvider diuProvider = DiuProvider.BAO;
        Sender sender = msgSender.SENDER;
        AccountInfo accountInfo = groupMsg.getAccountInfo();

        CatCodeUtil util = CatCodeUtil.INSTANCE;

        try {
            String msg = util.toCat("image", true, "file="
                    + diuProvider + CatUtil.getAt(groupMsg.getMsg()));
            sender.sendGroupMsg(groupMsg, msg);
        } catch (Exception e) {
            String atMe = "[CAT:at,code=2094085327]";
            String atOther = "[CAT:at,code=" + accountInfo.getAccountCode() + "]";

            sender.sendGroupMsg(groupMsg, atOther + "姬姬抱不到" + face);
            sender.sendGroupMsg(groupMsg, atMe + "快来看看你接口是不是炸了！");

        }
    }

    /**
     * 互动模块-锤
     *
     * @param groupMsg  群消息
     * @param msgSender 消息发送
     */
    @OnGroup
    @Filter(value = "/锤", matchType = MatchType.REGEX_MATCHES, trim = true)
    public void pound(GroupMsg groupMsg, MsgSender msgSender) {

        DiuProvider diuProvider = DiuProvider.POUND;
        Sender sender = msgSender.SENDER;
        AccountInfo accountInfo = groupMsg.getAccountInfo();

        CatCodeUtil util = CatCodeUtil.INSTANCE;

        try {
            String msg = util.toCat("image", true, "file="
                    + diuProvider + CatUtil.getAt(groupMsg.getMsg()));
            sender.sendGroupMsg(groupMsg, msg);
        } catch (Exception e) {
            String atMe = "[CAT:at,code=2094085327]";
            String face = "[CAT:face,id=5]";
            String atOther = "[CAT:at,code=" + accountInfo.getAccountCode() + "]";

            sender.sendGroupMsg(groupMsg, atOther + "姬姬锤不到" + face);
            sender.sendGroupMsg(groupMsg, atMe + "快来看看你接口是不是炸了！");

        }
    }

}

