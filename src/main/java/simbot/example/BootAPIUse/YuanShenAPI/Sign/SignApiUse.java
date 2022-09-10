package simbot.example.BootAPIUse.YuanShenAPI.Sign;

import catcode.CatCodeUtil;
import love.forte.simbot.annotation.*;
import love.forte.simbot.api.message.containers.AccountInfo;
import love.forte.simbot.api.message.containers.GroupInfo;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.message.events.MsgGet;
import love.forte.simbot.api.message.events.PrivateMsg;
import love.forte.simbot.api.sender.MsgSender;
import love.forte.simbot.filter.MatchType;
import love.forte.simbot.listener.ContinuousSessionScopeContext;
import love.forte.simbot.listener.ListenerContext;
import love.forte.simbot.listener.SessionCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import simbot.example.Service.BlackListService;
import simbot.example.Service.GenShinService;
import simbot.example.core.common.Constant;

import java.util.Arrays;

/**
 * @Author zeng
 * @Date 2022/8/26 17:21
 * @User 86188
 * @Description: 用于实现原神米游社签到的相关api调用
 */

@Service
public class SignApiUse extends Constant {
    /**
     * 自动装配service
     */
    GenShinService genShinService;
    BlackListService blackListService;
    // GenShinSign sign;

    @Autowired
    public SignApiUse(GenShinService genShinService, BlackListService blackListService  /*, GenShinSign sign*/) {
        this.genShinService = genShinService;
        this.blackListService = blackListService;
        //  this.sign = sign;
    }

    public GenShinSign sign = new GenShinSign();

    /**
     * 信息检查
     *
     * @param uid 待检查的UID
     * @return 返回判断后的结果
     */
    public boolean noInfo(String uid) {
        return genShinService.selectUid(uid) != null && GenShinSign.deletes == 0;
    }

    /**
     * 手动进行原神签到的指令
     *
     * @param groupMsg  群消息
     * @param msgSender 消息发送器
     * @param uid       原神的UID
     */
    @OnGroup
    @Filter(value = "原神签到 *{{uid}}", matchType = MatchType.REGEX_MATCHES, trim = true)
    public void genShinSign(GroupMsg groupMsg, MsgSender msgSender, @FilterValue("uid") String uid) {
        AccountInfo accountInfo = groupMsg.getAccountInfo();
        String nowQqId = accountInfo.getAccountCode();
        if (blackListService.selectCode(nowQqId) == null && BOOTSTATE) {
            if (noInfo(uid)) {
                // 获取QQ号

                // 初始化签到用户
                genShinService.selectUid(uid);

                if (nowQqId.equals(sign.getQqId()) || nowQqId.equals(USERID1)) {
                    // UID存在，进行签到
                    msgSender.SENDER.sendGroupMsg(groupMsg, "正在进行签到的UID:" + uid);

                    // 进行签到操作
                    if (sign.doSign(uid)) {
                        sign.signList(uid);

                        CatCodeUtil util = CatCodeUtil.INSTANCE;
                        msgSender.SENDER.sendGroupMsg(groupMsg, GenShinSign.getItemMsg() + "\n" + util.toCat("image", true, "file=" + GenShinSign.getItemImg()));
                    }
                    msgSender.SENDER.sendGroupMsg(groupMsg, GenShinSign.getMessage());
                }

            } else {

                // UID不存在，要求进行绑定
                msgSender.SENDER.sendGroupMsg(groupMsg, "此UID还未进行绑定，请输入 “绑定 你的cookie” 进行绑定");
                msgSender.SENDER.sendGroupMsg(groupMsg, "需要查询如何绑定请输入 “/原神帮助”");
            }
        }
    }

    /**
     * 群聊/私聊中绑定的相关指令
     *
     * @param msgGet    群聊/私聊
     * @param msgSender 消息发送器
     * @param cookie    待绑定的cookie
     */
    @OnGroup
    @OnPrivate
    @Filter(value = "绑定 *{{cookie}}", matchType = MatchType.REGEX_MATCHES, trim = true)
    public void genShinBindGroup(MsgGet msgGet, MsgSender msgSender, @FilterValue("cookie") String cookie) {

        // 获取发送绑定指令人的QQ号，用于后续推送
        AccountInfo accountInfo = msgGet.getAccountInfo();

        CookieStore cookieStore = new CookieStore();

        if (blackListService.selectCode(accountInfo.getAccountCode()) == null && BOOTSTATE) {
            // 对待绑定的cookie进行检查，错误cookie则要求重新获取
            if (cookieStore.checkCookie(cookie)) {
                CookieStore.setCookie(cookie);

                // 获取此cookie中包含的UID与nickName
                sign.checkUid();

                // 检查次cookie对应的UID是否已经存在
                if (genShinService.selectUid(sign.getUid()) != null) {
                    GenShinSign.qqId = accountInfo.getAccountCode();
                    CookieStore.setCookie(cookie);
                    // uid存在，进行cookie修改
                    genShinService.upDateInfo();
                } else {

                    // uid不存在，进行存入
                    genShinService.insertInfo();
                }
                if (msgGet instanceof GroupMsg) {
                    GroupMsg groupMsg = (GroupMsg) msgGet;
                    msgSender.SENDER.sendGroupMsg(groupMsg, "已为[" + sign.getNickName() + "-" + sign.getUid() + "]绑定cookie\n使用 ”/原神帮助“ 指令来查看能够做到的事情吧~");
                    msgSender.SENDER.sendGroupMsg(groupMsg, "!--你正在通过群聊绑定cookie--!\n" + "!-----建议撤回后通过私聊绑定-----!");
                }else {
                    PrivateMsg privateMsg = (PrivateMsg) msgGet;
                    msgSender.SENDER.sendPrivateMsg(privateMsg, "已为[" + sign.getNickName() + "-" + sign.getUid() + "]绑定cookie\n使用 ”/原神帮助“ 指令来查看能够做到的事情吧~");
                }
            }else {
                if (msgGet instanceof GroupMsg) {
                    GroupMsg groupMsg = (GroupMsg) msgGet;
                    msgSender.SENDER.sendGroupMsg(groupMsg, "cookie有误，请重新获取");
                }else {
                    PrivateMsg privateMsg = (PrivateMsg) msgGet;
                    msgSender.SENDER.sendPrivateMsg(privateMsg, "cookie有误，请重新获取");
                }
            }

        }
    }


    /**
     * 获取原神帮助菜单
     *
     * @param msgSender 消息发送
     * @param msgGet    消息父类
     */
    @OnGroup
    @OnPrivate
    @Filter(value = "/原神帮助", matchType = MatchType.REGEX_MATCHES, trim = true)
    public void genShinHelpGroup(MsgSender msgSender, MsgGet msgGet) {
        AccountInfo accountInfo = msgGet.getAccountInfo();

        // 首先对消息类型进行判断，判断后强转
        if (msgGet instanceof GroupMsg) {

            GroupMsg groupMsg = (GroupMsg) msgGet;
            GroupInfo groupInfo = groupMsg.getGroupInfo();

            int groupBanId = (int) Arrays.stream(groupBanIdList).filter(groupInfo.getGroupCode()::contains).count();

            if (groupBanId != 1 && blackListService.selectCode(accountInfo.getAccountCode()) == null && BOOTSTATE) {
                msgSender.SENDER.sendGroupMsg(groupMsg, SignConstant.GENSHIN_HELP);
            }
        } else {

            PrivateMsg privateMsg = (PrivateMsg) msgGet;

            if (blackListService.selectCode(accountInfo.getAccountCode()) == null && BOOTSTATE) {
                msgSender.SENDER.sendPrivateMsg(privateMsg, SignConstant.GENSHIN_HELP);
            }
        }


    }

    /**
     * 开启/关闭当前QQ号所绑定的UID签到推送
     *
     * @param groupMsg  群聊
     * @param msgSender 发送消息
     */
    @OnGroup
    @Filter(atBot = true, value = "打开推送", matchType = MatchType.REGEX_MATCHES, trim = true)
    @Filter(atBot = true, value = "关闭推送", matchType = MatchType.REGEX_MATCHES, trim = true)
    public void pushOpen(GroupMsg groupMsg, MsgSender msgSender) {

        GroupInfo groupInfo = groupMsg.getGroupInfo();
        AccountInfo accountInfo = groupMsg.getAccountInfo();

        int groupBanId = (int) Arrays.stream(groupBanIdList).filter(groupInfo.getGroupCode()::contains).count();
        if (groupBanId != 1 && blackListService.selectCode(accountInfo.getAccountCode()) == null && BOOTSTATE) {
            // @的人
            String atPeople = "[CAT:at,code=" + accountInfo.getAccountCode() + "]";

            // 去除消息中的空格
            // 将中文空格替换为英文空格
            String msgText = groupMsg.getText();
            msgText = msgText.trim();
            msgText = msgText.replace((char) 12288, ' ');

            // 检查当前QQ号是否绑定过cookie
            if (genShinService.selectQqId(accountInfo.getAccountCode())) {

                switch (msgText) {
                    case "打开推送":
                        GenShinSign.push = 1;

                        msgSender.SENDER.sendGroupMsg(groupMsg, atPeople + "推送打开了！");
                        genShinService.upDatePush(accountInfo.getAccountCode());
                        break;
                    case "关闭推送":
                        GenShinSign.push = 0;
                        genShinService.upDatePush(accountInfo.getAccountCode());
                        msgSender.SENDER.sendGroupMsg(groupMsg, atPeople + "推送关闭了！");
                        break;
                    default:
                }
            } else {

                msgSender.SENDER.sendGroupMsg(groupMsg, atPeople + "你还没有绑定过原神cookie，不需要打开/关闭推送");
            }

        }
    }


    /**
     * 随便给分组取个名字啥的
     */
    private static final String DELETE_GROUP = "==ChoseDeleteOrNot==DELETE==";

    /**
     * 监听群聊，触发启动事件。
     *
     * @param groupMsg 群聊
     * @param context  持续会话域
     * @param sender   发送
     * @param uid      取得的uid
     */
    @OnGroup
    @Filter(value = "删除cookie *{{uid}}", matchType = MatchType.REGEX_MATCHES, trim = true)
    public void start(GroupMsg groupMsg, ListenerContext context, MsgSender sender, @FilterValue("uid") String uid) {

        GroupInfo groupInfo = groupMsg.getGroupInfo();
        AccountInfo accountInfo = groupMsg.getAccountInfo();

        int groupBanId = (int) Arrays.stream(groupBanIdList).filter(groupInfo.getGroupCode()::contains).count();

        if (groupBanId != 1 && blackListService.selectCode(accountInfo.getAccountCode()) == null && BOOTSTATE) {

            // 得到session上下文，并断言它的确不是null
            final ContinuousSessionScopeContext sessionContext = (ContinuousSessionScopeContext) context.getContext(ListenerContext.Scope.CONTINUOUS_SESSION);
            assert sessionContext != null;

            final String groupCode = groupMsg.getGroupInfo().getGroupCode();
            final String code = groupMsg.getAccountInfo().getAccountCode();

            // 通过群号和账号拼接一个此人在此群中的唯一key
            String key = groupCode + ":" + code;

        /*
            注意！你应当考虑一个问题：同一个人同时触发多次同样的会话，比如它同时发了两次 'start'.
            这个问题的解决应当由你自行解决，比如创建会话之前，先去检查是否已经存在。
         */

            // 发送提示信息
            sender.SENDER.sendGroupMsg(groupMsg, "你确定要删除cookie吗？\n删除后无量姬就不能为你服务了o(´^｀)o");
            sender.SENDER.sendGroupMsg(groupMsg, "确认删除请再次输入你的UID ,取消则输入 “取消”");

            // 创建回调函数 SessionCallback 实例。
            // 通过 SessionCallback.builder 进行创建
            final SessionCallback<String> callback = SessionCallback.<String>builder().onResume(phone -> {
                // 得到手机号，进行下一步操作
                if (uid.equals(phone)) {
                    if (genShinService.selectQqId(code) || code.equals(USERID1)) {
                        if (noInfo(uid)) {
                            sender.SENDER.sendGroupMsg(groupMsg, "状态为: " + phone);
                            genShinService.pseudoDeletion(uid);
                            sender.SENDER.sendGroupMsg(groupMsg, "无量姬把你的cookie都删除啦！如果要使用无量姬的功能请重新绑定");
                        } else {
                            sender.SENDER.sendGroupMsg(groupMsg, "UID不存在或未绑定哦，请检查后重新进行删除操作");
                        }
                    }

                } else {
                    sender.SENDER.sendGroupMsg(groupMsg, "状态码:" + phone);
                    sender.SENDER.sendGroupMsg(groupMsg, "无量姬帮你把cookie删除取消啦");
                }

            }).onError(e -> System.out.println("onError: 超时啦: " + e)).onCancel(e -> {
                // 这里是第一个会话，此处通过 onCancel 来处理会话被手动关闭、超时关闭的情况的处理，有些时候会与 orError 同时被触发（例如超时的时候）
                System.out.println("onCancel 关闭啦: " + e);
            }).build();
            // build 构建

            // 这里开始等待第一个会话。
            sessionContext.waiting(DELETE_GROUP, key, callback);
        }
    }

    /**
     * 针对上述第一个会话的监听。
     * 因为这里是监听 获取手机号 的事件，因此此处的 Filter为 \\d 正则。
     * 通过 @OnlySession来限制，当且仅当由 PHONE_GROUP 这个组的会话的时候，此监听函数才会生效。
     */
    @OnGroup
    @OnlySession(group = DELETE_GROUP)
    @Filter(value = "\\d+", matchType = MatchType.REGEX_MATCHES)
    @Filter(value = "取消", matchType = MatchType.REGEX_MATCHES)
    public void phone(GroupMsg m, ListenerContext context) {
        // 得到session上下文。

        final ContinuousSessionScopeContext session = (ContinuousSessionScopeContext) context.getContext(ListenerContext.Scope.CONTINUOUS_SESSION);
        assert session != null;

        final String groupCode = m.getGroupInfo().getGroupCode();
        final String code = m.getAccountInfo().getAccountCode();

        // 拼接出来这个人对应的唯一key
        String key = groupCode + ":" + code;

        final String phone = m.getText();

        // 尝试将这个phone推送给对应的会话。
        session.push(DELETE_GROUP, key, phone);

    }

    /**
     * 对当前QQ号进行uid与昵称查询
     *
     * @param groupMsg  群聊
     * @param msgSender 发送消息
     */
    @OnGroup
    @Filter(value = "账号查询")
    public void showList(GroupMsg groupMsg, MsgSender msgSender) {
        GroupInfo groupInfo = groupMsg.getGroupInfo();
        AccountInfo accountInfo = groupMsg.getAccountInfo();

        int groupBanId = (int) Arrays.stream(groupBanIdList).filter(groupInfo.getGroupCode()::contains).count();

        if (groupBanId != 1 && blackListService.selectCode(accountInfo.getAccountCode()) == null && BOOTSTATE) {
            msgSender.SENDER.sendGroupMsg(groupMsg, genShinService.showMyList(accountInfo.getAccountCode()));
        }
    }

    /**
     * 通过管理员权限对所有列表进行查询
     *
     * @param groupMsg  群聊
     * @param msgSender 发送消息
     */
    @OnGroup
    @Filter(value = "管理查询")
    public void showAllList(GroupMsg groupMsg, MsgSender msgSender) {

        AccountInfo accountInfo = groupMsg.getAccountInfo();

        if (accountInfo.getAccountCode().equals(USERID1)) {
            msgSender.SENDER.sendGroupMsg(groupMsg, genShinService.showAllList());
        }
    }

    /**
     * 判断管理员权限并进行全部重新签到的操作
     *
     * @param groupMsg 群聊消息
     */
    @OnGroup
    @Filter(value = "全部重签")
    public void reSignAll(GroupMsg groupMsg) {

        AccountInfo accountInfo = groupMsg.getAccountInfo();

        if (accountInfo.getAccountCode().equals(USERID1)) {
            genShinService.signAll();
        }
    }

}
