package simbot.example.BootAPIUse.GeographyAPI;

import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.FilterValue;
import love.forte.simbot.annotation.OnGroup;
import love.forte.simbot.annotation.OnPrivate;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.message.events.MsgGet;
import love.forte.simbot.api.message.events.PrivateMsg;
import love.forte.simbot.api.sender.MsgSender;
import love.forte.simbot.filter.MatchType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import simbot.example.Service.BlackListService;
import simbot.example.core.common.Constant;
import simbot.example.core.common.JudgeBan;

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

    JudgeBan judgeBan = new JudgeBan();

    /**
     * 群聊天气查询模块
     * 在检测到关键词和命令后调用天气API来显示天气
     *
     * @param msgGet    用于获取消息类型
     * @param msgSender 用于发送消息
     * @param city      将通过正则匹配到的关键词城市传递给天气api
     */
    @OnGroup
    @OnPrivate
    @Filter(value = "{{city}}天气", matchType = MatchType.REGEX_MATCHES, trim = true)
    @Filter(value = "/tq{{city}}", matchType = MatchType.REGEX_MATCHES, trim = true)
    public void weather(MsgGet msgGet, MsgSender msgSender, @FilterValue("city") String city) {

        if (judgeBan.allBan(msgGet)) {
            if (msgGet instanceof GroupMsg) {
                GroupMsg groupMsg = (GroupMsg) msgGet;

                if (city == null) {
                    msgSender.SENDER.sendGroupMsg(groupMsg, "天气查询失败哦~ 请输入正确的城市~");
                } else {
                    msgSender.SENDER.sendGroupMsg(groupMsg, geoApi.weatherInfo(city));
                }
            } else {
                PrivateMsg privateMsg = (PrivateMsg) msgGet;
                if (city == null) {
                    msgSender.SENDER.sendPrivateMsg(privateMsg, "天气查询失败哦~ 请输入正确的城市~");
                } else {
                    msgSender.SENDER.sendPrivateMsg(privateMsg, geoApi.weatherInfo(city));
                }
            }
            geoApi.adm1 = null;
            geoApi.adm2 = null;
            geoApi.id = null;
        }
    }

    /**
     * 群聊城市地理信息查询模块
     * 在检测到关键词和命令后调用地理API来显示天气
     *
     * @param msgGet    用于获取消息类型
     * @param msgSender 用于发送消息
     * @param city      将通过正则匹配到的关键词城市传递给地理api
     */
    @OnGroup
    @OnPrivate
    @Filter(value = "{{city}}地理", matchType = MatchType.REGEX_MATCHES, trim = true)
    @Filter(value = "/dl{{city}}", matchType = MatchType.REGEX_MATCHES, trim = true)
    public void geoCity(MsgGet msgGet, MsgSender msgSender, @FilterValue("city") String city) {

        // 将群号为“637384877”的群排除在人工智能答复模块外
        if (judgeBan.allBan(msgGet)) {
            if (msgGet instanceof GroupMsg) {
                GroupMsg groupMsg = (GroupMsg) msgGet;
                if (city == null) {
                    msgSender.SENDER.sendGroupMsg(groupMsg, "城市查询失败哦~ 请输入正确的城市~");
                } else {
                    msgSender.SENDER.sendGroupMsg(groupMsg, geoApi.CityInfo(city));
                }
            } else {
                PrivateMsg privateMsg = (PrivateMsg) msgGet;
                if (city == null) {
                    msgSender.SENDER.sendPrivateMsg(privateMsg, "城市查询失败哦~ 请输入正确的城市~");
                } else {
                    msgSender.SENDER.sendPrivateMsg(privateMsg, geoApi.CityInfo(city));
                }
            }
            geoApi.adm1 = null;
            geoApi.adm2 = null;
            geoApi.id = null;
        }
    }
}
