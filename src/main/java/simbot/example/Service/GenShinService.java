package simbot.example.Service;

import catcode.CatCodeUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import love.forte.simbot.api.sender.BotSender;
import love.forte.simbot.bot.Bot;
import love.forte.simbot.bot.BotManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import simbot.example.BootAPIUse.YuanShenAPI.Sign.CookieStore;
import simbot.example.BootAPIUse.YuanShenAPI.Sign.GenShinSign;
import simbot.example.Enity.GenShinUser;
import simbot.example.Mapper.GenShinMapper;
import simbot.example.core.common.Constant;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author zeng
 * @Date 2022/8/26 11:01
 * @User 86188
 * @Description: 原神签到相关服务
 */
@Service
public class GenShinService extends Constant {


    GenShinMapper genShinMapper;

    GenShinSign genShinSign ;

    GenShinUser genShinUser = new GenShinUser();
    @Autowired
    public GenShinService(GenShinMapper genShinMapper, GenShinSign genShinSign) {
        this.genShinMapper = genShinMapper;
        this.genShinSign = genShinSign;
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


    /**
     * 根据uid搜索数据
     *
     * @param uid 用户UID
     * @return 当有值时返回取得的uid
     */
    public String selectUid(String uid) {
        QueryWrapper<GenShinUser> queryWrapper = Wrappers.query();
        queryWrapper.like("uid", uid);
        GenShinUser genShinUser = genShinMapper.selectOne(queryWrapper);

        // 当数据存在时进行初始化
        if (genShinUser != null) {
            System.out.println("数据库返回的Uid:" + genShinUser.getUid());
            GenShinSign.setDeletes(genShinUser.getDeletes());
            GenShinSign.setQqId(genShinUser.getQqid());
            GenShinSign.setPush(genShinUser.getPush());
            CookieStore.setCookie(genShinUser.getCookie());
            return genShinUser.getUid();
        }
        return null;
    }

    /**
     * 搜索QQ号，可能待优化
     *
     * @param qqId QQ号
     * @return 返回获取的QQ号是否存在
     */
    public boolean selectQqId(String qqId) {
        // TODO 支持代替签到，多qq号
        try {
            QueryWrapper<GenShinUser> queryWrapper = Wrappers.query();
            queryWrapper.like("qqid", qqId);
            GenShinUser genShinUser1 = genShinMapper.selectOne(queryWrapper);
            GenShinSign.qqId = genShinUser1.getQqid();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 插入用户信息
     */
    public void insertInfo() {

        genShinUser.setUid(genShinSign.getUid());
        genShinUser.setQqid(genShinSign.getQqId());
        genShinUser.setNickname(genShinSign.getNickName());
        genShinUser.setCookie(CookieStore.getCookie());
        genShinUser.setPush(0);
        genShinUser.setDeletes(0);

        genShinMapper.insert(genShinUser);
    }

    /**
     * 绑定时更新用户信息
     */
    public void upDateInfo() {
        genShinUser.setQqid(genShinSign.getQqId());
        genShinUser.setNickname(genShinSign.getNickName());
        genShinUser.setCookie(CookieStore.getCookie());
        genShinUser.setDeletes(0);

        QueryWrapper<GenShinUser> queryWrapper = Wrappers.query();
        queryWrapper.like("uid", genShinSign.getUid());

        genShinMapper.update(genShinUser, queryWrapper);
    }

    /**
     * 改变推送状态
     *
     * @param qqId 对应的QQ号
     */
    public void upDatePush(String qqId) {
        QueryWrapper<GenShinUser> queryWrapper = Wrappers.query();
        queryWrapper.like("qqid", qqId);
        GenShinUser genShinUser = genShinMapper.selectOne(queryWrapper);
        genShinUser.setPush(GenShinSign.getPush());
        genShinMapper.update(genShinUser, queryWrapper);

    }

    /**
     * 删除cookie
     *
     * @param uid 待删除的cookie对应的uid
     */
    public void pseudoDeletion(String uid) {
        genShinUser.setDeletes(1);

        QueryWrapper<GenShinUser> queryWrapper = Wrappers.query();
        queryWrapper.like("uid", uid);

        genShinMapper.update(genShinUser, queryWrapper);

    }

    /**
     * 展示当前QQ账号所绑定的UID
     * @param qqid QQ号
     * @return 返回账号列表
     */
    public String showMyList(String qqid) {
        QueryWrapper<GenShinUser> queryWrapper = Wrappers.query();
        queryWrapper.like("qqid", qqid);

        List<GenShinUser> blackListUsers = genShinMapper.selectList(queryWrapper);
        int userNumber = blackListUsers.size();
        if (blackListUsers.size() != 0) {
            System.out.println(blackListUsers);
            StringBuilder msg = new StringBuilder("当前QQ号绑定的账户:\n");
            for (GenShinUser genShinUser : blackListUsers) {
                msg.append(genShinUser.getUid()).append(" ").append(genShinUser.getNickname());
                if (userNumber > 1) {
                    msg.append("\n");
                    userNumber -= 1;
                }
            }

            return String.valueOf(msg);
        }
        return "[CAT:at,code=" + qqid + "]你还没有绑定账户哦";
    }

    /**
     * 查看所有绑定的用户UID和昵称
     * @return 返回要发送的信息
     */
    public String showAllList() {

        QueryWrapper<GenShinUser> queryWrapper = Wrappers.query();
        queryWrapper.like("deletes", 0);

        List<GenShinUser> blackListUsers = genShinMapper.selectList(queryWrapper);
        System.out.println(blackListUsers);
        StringBuilder msg = new StringBuilder("全部绑定的账户:\n");
        for (GenShinUser genShinUser : blackListUsers) {
            msg.append(genShinUser.getUid()).append(" ").append(genShinUser.getNickname()).append("\n");
        }
        return String.valueOf(msg);
    }

    /**
     * 通过循环遍历数据库对未删除cookie的用户执行签到操作
     */
    public void signAll() {
        // 构造搜索条件，搜索未删除的cookie
        QueryWrapper<GenShinUser> queryWrapper = Wrappers.query();
        queryWrapper.like("deletes", 0);

        List<GenShinUser> genShinUsers1 = genShinMapper.selectList(queryWrapper);

        CatCodeUtil util = CatCodeUtil.INSTANCE;
        // 取出list中的数据依次进行签到
        for (GenShinUser genShinUser : genShinUsers1) {

            // 执行签到,同时对当前数据进行初始化
            if (genShinSign. doSign(selectUid(genShinUser.getUid()))) {

                if (GenShinSign.push == 1) {
                    // @的人
                    String atPeople = "[CAT:at,code=" + genShinUser.getQqid() + "]";
                     sendGroupMsg("1019170385", atPeople + GenShinSign.getMessage());

                    genShinSign. signList(genShinUser.getUid());
                    System.out.println(GenShinSign.getItemImg());

                      sendGroupMsg("1019170385", GenShinSign.getItemMsg() + "\n" + util.toCat("image", true, "file=" + GenShinSign.getItemImg()));
                }
                genShinSign.  signList(genShinUser.getUid());

                sendGroupMsg("140469072", GenShinSign.getItemMsg() + "\n" + util.toCat("image", true, "file=" + GenShinSign.getItemImg()));
            }
              sendGroupMsg("140469072", genShinUser.getNickname() + ":" + GenShinSign.getMessage());
        }
    }
}
