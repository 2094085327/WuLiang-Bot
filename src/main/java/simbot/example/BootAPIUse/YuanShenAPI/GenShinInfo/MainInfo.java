package simbot.example.BootAPIUse.YuanShenAPI.GenShinInfo;


import catcode.CatCodeUtil;
import com.alibaba.fastjson.JSONObject;
import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.FilterValue;
import love.forte.simbot.annotation.OnGroup;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.sender.MsgSender;
import love.forte.simbot.filter.MatchType;
import org.springframework.stereotype.Service;
import simbot.example.Util.CommandUtil;
import simbot.example.Util.GenShinUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author zeng
 * @Date 2022/9/6 10:35
 * @User 86188
 * @Description: 获取原神基本信息
 */
@Service
public class MainInfo {

    @OnGroup
    @Filter(value = "更新信息 *{{getUid}}", matchType = MatchType.REGEX_MATCHES, trim = true)
    public void allUpdate(GroupMsg groupMsg, MsgSender msgSender, @FilterValue("getUid") String getUid) {
        String uid = GenShinUtil.transformUid(getUid);

        if (uid != null) {
            System.out.println("更新");
            msgSender.SENDER.sendGroupMsg(groupMsg, "无量姬开始更新信息~请稍等哦~");
            JSONObject enkaData = GetData.getEnkeData(uid);
            if (enkaData == null) {
                msgSender.SENDER.sendGroupMsg(groupMsg, "无量姬没有获取到该uid的信息哦,可能是enka接口服务出现问题,稍候再试吧~");
            } else {
                String listKey = "avatarInfoList";
                if (!enkaData.containsKey(listKey)) {
                    msgSender.SENDER.sendGroupMsg(groupMsg, "该UID未在游戏中打开角色展柜,请打开5分钟后再试~");
                } else {

                    String pyFile = String.valueOf(new File("1.py").getAbsoluteFile());

                    List<String> uidList = new ArrayList<>();
                    uidList.add(getUid);
                    CommandUtil.exec("python", uidList, pyFile);

                    System.out.println("done");
                    msgSender.SENDER.sendGroupMsg(groupMsg, "更新完毕");
                }
            }
        } else {
            msgSender.SENDER.sendGroupMsg(groupMsg, "这个uid不正确哦~，请检查一下");
        }
    }
}
