package simbot.example.BootAPIUse.YuanShenAPI;

import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.OnGroup;
import love.forte.simbot.api.message.MessageContentBuilder;
import love.forte.simbot.api.message.MessageContentBuilderFactory;
import love.forte.simbot.api.message.containers.GroupInfo;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.sender.MsgSender;
import love.forte.simbot.filter.MatchType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import simbot.example.BootAPIUse.YuanShenAPI.GachaInfo.YuanApi;
import simbot.example.BootAPIUse.YuanShenAPI.GachaInfo.picture;
import simbot.example.BootAPIUse.YuanShenAPI.Sign.GenShinSign;
import simbot.example.core.common.Constant;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;

/**
 * @author zeng
 * @date 2022/8/2 9:13
 * @user 86188
 */
@Service
public class YuanShenApiUse extends Constant {

    /**
     * 通过自动装配构建消息工厂
     */
    @Autowired
    MessageContentBuilderFactory messageContentBuilderFactory;


    @OnGroup
    @Filter(value = "原神抽卡分析", matchType = MatchType.CONTAINS, trim = true)
    public void gaChaLog(GroupMsg groupMsg, MsgSender msgSender) throws Exception {

        GroupInfo groupInfo = groupMsg.getGroupInfo();
        int groupBanId = (int) Arrays.stream(groupBanIdList).filter(groupInfo.getGroupCode()::contains).count();
        if (groupBanId != 1) {

            String url = YuanApi.toUrl(groupMsg.getMsg());
            String urlCheckType = YuanApi.checkApi(url);
            if (!"OK".equals(urlCheckType)) {
                assert urlCheckType != null;
                msgSender.SENDER.sendGroupMsg(groupMsg, urlCheckType);
            } else {
                msgSender.SENDER.sendGroupMsg(groupMsg, "请耐心等待，正在分析中,大致需要30秒至一分钟(视抽卡数决定)");


                YuanApi.getGachaRoleInfo(url);
                msgSender.SENDER.sendGroupMsg(groupMsg, "--角色池分析完成--");

                YuanApi.getGachaArmsInfo(url);
                msgSender.SENDER.sendGroupMsg(groupMsg, "--武器池分析完成--");

                YuanApi.getGachaPermanentInfo(url);
                msgSender.SENDER.sendGroupMsg(groupMsg, "--常驻池分析完成--");

                YuanApi.allData1();

                YuanApi.fincount = 0;
                YuanApi.finFiveStar = 0;
                YuanApi.finProbability = 0;

                picture.allPictureMake();

                InputStream inputStream = new FileInputStream(new File("yuanImage/finally.png").getAbsoluteFile());

                // 创建消息构建器，用于在服务器上发送图片
                MessageContentBuilder messageContentBuilder = messageContentBuilderFactory.getMessageContentBuilder();

                msgSender.SENDER.sendGroupMsg(groupMsg, messageContentBuilder.image(inputStream).build());

            }
        }
    }


    @OnGroup
    @Filter(value = "原神签到", matchType = MatchType.CONTAINS, trim = true)
    public void genShinSign(GroupMsg groupMsg, MsgSender msgSender) {
        GenShinSign sign = new GenShinSign();
        sign.doSign();

    }

}