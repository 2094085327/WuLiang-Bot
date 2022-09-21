package simbot.example.BootAPIUse.YuanShenAPI.GachaInfo;

import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.OnGroup;
import love.forte.simbot.api.message.MessageContentBuilder;
import love.forte.simbot.api.message.MessageContentBuilderFactory;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.sender.MsgSender;
import love.forte.simbot.filter.MatchType;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import simbot.example.Service.BlackListService;
import simbot.example.core.common.Constant;
import simbot.example.core.common.JudgeBan;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

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
    MessageContentBuilderFactory messageContentBuilderFactory;
    /**
     * 黑名单
     */
    BlackListService blackListService;

    @Autowired
    public YuanShenApiUse(MessageContentBuilderFactory messageContentBuilderFactory, BlackListService blackListService) {
        this.messageContentBuilderFactory = messageContentBuilderFactory;
        this.blackListService = blackListService;
    }

    JudgeBan judgeBan = new JudgeBan();

    @OnGroup
    @Filter(value = "原神抽卡分析", matchType = MatchType.CONTAINS, trim = true)
    public void gaChaLog(GroupMsg groupMsg, MsgSender msgSender) throws Exception {

        if (judgeBan.allBan(groupMsg)) {

            String url = YuanApi.toUrl(groupMsg.getMsg());
            String urlCheckType = YuanApi.checkApi(url);
            if (!YuanConstant.APISTATE3.equals(urlCheckType)) {
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

                YuanApi.finCount = 0;
                YuanApi.finFiveStar = 0;
                YuanApi.finProbability = 0;

                picture.allPictureMake();

                InputStream inputStream = new FileInputStream(new File("resources/yuanImage/finally.png").getAbsoluteFile());

                // 创建消息构建器，用于在服务器上发送图片
                MessageContentBuilder messageContentBuilder = messageContentBuilderFactory.getMessageContentBuilder();


                msgSender.SENDER.sendGroupMsg(groupMsg, messageContentBuilder.image(inputStream).build());

                inputStream.close();

                // 删除生成的图片减少内存占用
                try {
                    Files.delete(Paths.get(new File("resources/yuanImage/allData.png").getAbsolutePath()));
                    Files.delete(Paths.get(new File("resources/yuanImage/arms pool.png").getAbsolutePath()));
                    Files.delete(Paths.get(new File("resources/yuanImage/permanent pool.png").getAbsolutePath()));
                    Files.delete(Paths.get(new File("resources/yuanImage/role pool.png").getAbsolutePath()));
                    Files.delete(Paths.get(new File("resources/yuanImage/finally.png").getAbsolutePath()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}