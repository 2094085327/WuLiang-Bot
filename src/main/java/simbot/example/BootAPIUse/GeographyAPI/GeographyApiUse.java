package simbot.example.BootAPIUse.GeographyAPI;

import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.FilterValue;
import love.forte.simbot.annotation.OnGroup;
import love.forte.simbot.annotation.OnPrivate;
import love.forte.simbot.api.message.containers.AccountInfo;
import love.forte.simbot.api.message.containers.GroupInfo;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.message.events.PrivateMsg;
import love.forte.simbot.api.sender.MsgSender;
import love.forte.simbot.api.sender.Sender;
import love.forte.simbot.filter.MatchType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import simbot.example.Service.BlackListService;
import simbot.example.core.common.Constant;

import java.util.Arrays;

/**
 * @author zeng
 * @date 2022/7/27 11:49
 * @user 86188
 */
@Service
public class GeographyApiUse extends Constant {

    BlackListService blackListService;
    @Autowired
    public GeographyApiUse(BlackListService blackListService) {
        this.blackListService = blackListService;
    }

    /**
     * 调用GeoAPI中的方法
     */
    geoAPI geoApi = new geoAPI();

    /**
     * 群聊天气查询模块
     * 在检测到关键词和命令后调用天气API来显示天气
     *
     * @param groupMsg  用于获取群聊消息，群成员信息等
     * @param msgSender 用于在群聊中发送消息
     * @param city      将通过正则匹配到的关键词城市传递给天气api
     */
    @OnGroup
    @Filter(value = "{{city}}天气", matchType = MatchType.REGEX_MATCHES, trim = true)
    @Filter(value = "/tq{{city}}", matchType = MatchType.REGEX_MATCHES, trim = true)
    public void weather(GroupMsg groupMsg, MsgSender msgSender, @FilterValue("city") String city) {

        Sender sender = msgSender.SENDER;
        GroupInfo groupInfo = groupMsg.getGroupInfo();
        AccountInfo accountInfo = groupMsg.getAccountInfo();

        int groupBanId = (int) Arrays.stream(groupBanIdList).filter(groupInfo.getGroupCode()::contains).count();
        if (BOOTSTATE && groupBanId != 1 && blackListService.selectCode(accountInfo.getAccountCode()) == null) {

            // 将群号为“637384877”的群排除在人工智能答复模块外.
            if (!groupInfo.getGroupCode().equals(GROUPID3)) {
                if (city == null) {
                    sender.sendGroupMsg(groupMsg, "天气查询失败哦~ 请输入正确的城市~");
                } else {
                    sender.sendGroupMsg(groupMsg, geoApi.weatherInfo(city));
                    geoApi.adm1 = null;
                    geoApi.adm2 = null;
                    geoApi.id = null;

                }
            }
        }
    }

    /**
     * 私聊天气查询模块
     * 在检测到关键词和命令后调用天气API来显示天气
     *
     * @param privateMsg 用于获取私聊消息，群成员信息等
     * @param msgSender  用于在群聊中发送消息
     * @param city       将通过正则匹配到的关键词城市传递给天气api
     */
    @OnPrivate
    @Filter(value = "{{city}}天气", matchType = MatchType.REGEX_MATCHES, trim = true)
    @Filter(value = "/tq{{city}}", matchType = MatchType.REGEX_MATCHES, trim = true)
    public void weather(PrivateMsg privateMsg, MsgSender msgSender, @FilterValue("city") String city) {

        Sender sender = msgSender.SENDER;

        AccountInfo accountInfo = privateMsg.getAccountInfo();


        if (BOOTSTATE && blackListService.selectCode(accountInfo.getAccountCode()) == null) {

            if (city == null) {
                sender.sendPrivateMsg(privateMsg, "天气查询失败哦~ 请输入正确的城市~");
            } else {
                sender.sendPrivateMsg(privateMsg, geoApi.weatherInfo(city));
                geoApi.adm1 = null;
                geoApi.adm2 = null;
                geoApi.id = null;
            }

        }
    }

    /**
     * 群聊城市地理信息查询模块
     * 在检测到关键词和命令后调用地理API来显示天气
     *
     * @param groupMsg  用于获取群聊消息，群成员信息等
     * @param msgSender 用于在群聊中发送消息
     * @param city      将通过正则匹配到的关键词城市传递给地理api
     */
    @OnGroup
    @Filter(value = "{{city}}地理", matchType = MatchType.REGEX_MATCHES, trim = true)
    @Filter(value = "/dl{{city}}", matchType = MatchType.REGEX_MATCHES, trim = true)
    public void geoCity(GroupMsg groupMsg, MsgSender msgSender, @FilterValue("city") String city) {

        Sender sender = msgSender.SENDER;

        GroupInfo groupInfo = groupMsg.getGroupInfo();
        AccountInfo accountInfo = groupMsg.getAccountInfo();

        int groupBanId = (int) Arrays.stream(groupBanIdList).filter(groupInfo.getGroupCode()::contains).count();
        // 将群号为“637384877”的群排除在人工智能答复模块外
        if (BOOTSTATE && groupBanId != 1 && blackListService.selectCode(accountInfo.getAccountCode()) == null) {
            if (city == null) {
                sender.sendGroupMsg(groupMsg, "城市查询失败哦~ 请输入正确的城市~");
            } else {
                sender.sendGroupMsg(groupMsg, geoApi.CityInfo(city));
                geoApi.adm1 = null;
                geoApi.adm2 = null;
                geoApi.id = null;
            }
        }
    }

    /**
     * 私聊城市地理信息查询模块
     * 在检测到关键词和命令后调用地理API来显示天气
     *
     * @param privateMsg 用于获取私聊消息，群成员信息等
     * @param msgSender  用于在私聊中发送消息
     * @param city       将通过正则匹配到的关键词城市传递给地理api
     */
    @OnPrivate
    @Filter(value = "{{city}}地理", matchType = MatchType.REGEX_MATCHES, trim = true)
    @Filter(value = "/dl{{city}}", matchType = MatchType.REGEX_MATCHES, trim = true)
    public void geoCity(PrivateMsg privateMsg, MsgSender msgSender, @FilterValue("city") String city) {

        Sender sender = msgSender.SENDER;

        AccountInfo accountInfo = privateMsg.getAccountInfo();

        // 将群号为“637384877”的群排除在人工智能答复模块外
        if (BOOTSTATE && blackListService.selectCode(accountInfo.getAccountCode()) == null) {

            if (city == null) {
                sender.sendPrivateMsg(privateMsg, "城市查询失败哦~ 请输入正确的城市~");
            } else {
                sender.sendPrivateMsg(privateMsg, geoApi.CityInfo(city));
                geoApi.adm1 = null;
                geoApi.adm2 = null;
                geoApi.id = null;
            }
        }
    }
}
