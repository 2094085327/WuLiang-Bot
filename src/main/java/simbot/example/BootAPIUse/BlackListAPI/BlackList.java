package simbot.example.BootAPIUse.BlackListAPI;

import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.FilterValue;
import love.forte.simbot.annotation.OnGroup;
import love.forte.simbot.api.message.containers.AccountInfo;
import love.forte.simbot.api.message.containers.GroupInfo;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.sender.MsgSender;
import love.forte.simbot.filter.MatchType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import simbot.example.Service.BlackListService;
import simbot.example.core.common.Constant;

import java.util.Arrays;

/**
 * @Author zeng
 * @Date 2022/8/30 16:22
 * @User 86188
 * @Description:
 */
@Service
public class BlackList extends Constant {

    private static String codes;
    private static String nickName;
    private static String allLists;

    public static String getAllLists() {
        return allLists;
    }

    public static void setAllLists(String allLists) {
        BlackList.allLists = allLists;
    }

    public String getCodes() {
        return codes;
    }

    public void setCodes(String code) {
        codes = code;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        BlackList.nickName = nickName;
    }

    /**
     * 自动装配service
     */
    @Autowired
    BlackListService blackListService;

    /**
     * 加入黑名单，禁用一切功能
     *
     * @param groupMsg  群消息
     * @param msgSender 消息发送
     * @param code      QQ号
     * @param nickName  昵称
     */
    @OnGroup
    @Filter(atBot = true, value = "/加入黑名单 *{{code}} {{nickname}}", matchType = MatchType.REGEX_MATCHES, trim = true)
    public void getIn(GroupMsg groupMsg, MsgSender msgSender, @FilterValue("code") String code, @FilterValue("nickname") String nickName) {

        GroupInfo groupInfo = groupMsg.getGroupInfo();
        AccountInfo accountInfo = groupMsg.getAccountInfo();

        setCodes(code);
        setNickName(nickName);

        int groupBanId = (int) Arrays.stream(groupBanIdList).filter(groupInfo.getGroupCode()::contains).count();

        if (groupBanId != 1 && BOOTSTATE) {
            if (accountInfo.getAccountCode().equals(USERID1)) {

                // 检查次cookie对应的UID是否已经存在
                if (blackListService.selectCode(code) != null) {

                    // uid存在，进行cookie修改

                    blackListService.updateBlackList();
                    msgSender.SENDER.sendGroupMsg(groupMsg, "已将" + code + "加入黑名单");
                    System.out.println("存在");
                } else {

                    // uid不存在，进行存入
                    blackListService.insertInfo(code, nickName);
                    msgSender.SENDER.sendGroupMsg(groupMsg, "已将" + code + "加入黑名单");
                    System.out.println("不存在");
                }
            }
        }
    }

    @OnGroup
    @Filter(value = "/移出黑名单 *{{code}}", matchType = MatchType.REGEX_MATCHES, trim = true)
    public void deleteList(GroupMsg groupMsg, MsgSender msgSender, @FilterValue("code") String code) {
        GroupInfo groupInfo = groupMsg.getGroupInfo();
        AccountInfo accountInfo = groupMsg.getAccountInfo();

        setCodes(code);

        int groupBanId = (int) Arrays.stream(groupBanIdList).filter(groupInfo.getGroupCode()::contains).count();

        if (groupBanId != 1 && BOOTSTATE && accountInfo.getAccountCode().equals(USERID1)) {


            // 检查次cookie对应的UID是否已经存在
            if (blackListService.selectCode(code) != null) {

                // uid存在，进行cookie修改

                blackListService.deleteBlackList(code);
                msgSender.SENDER.sendGroupMsg(groupMsg, "已将" + code + "移出黑名单");
                System.out.println("存在");
            } else {
                msgSender.SENDER.sendGroupMsg(groupMsg, code + "不在黑名单中");
                System.out.println("不存在");
            }
        }

    }

    @OnGroup
    @Filter(value = "黑名单列表")
    public void showList(GroupMsg groupMsg, MsgSender msgSender) {
        GroupInfo groupInfo = groupMsg.getGroupInfo();
        AccountInfo accountInfo = groupMsg.getAccountInfo();

        int groupBanId = (int) Arrays.stream(groupBanIdList).filter(groupInfo.getGroupCode()::contains).count();

        if (groupBanId != 1 && BOOTSTATE && accountInfo.getAccountCode().equals(USERID1)) {
            blackListService.showAllList();
            msgSender.SENDER.sendGroupMsg(groupMsg, getAllLists());
        }
    }
}
