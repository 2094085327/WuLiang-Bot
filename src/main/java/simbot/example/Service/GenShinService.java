package simbot.example.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import simbot.example.BootAPIUse.YuanShenAPI.Sign.CookieStore;
import simbot.example.BootAPIUse.YuanShenAPI.Sign.GenShinSign;
import simbot.example.Enity.GenShinUser;
import simbot.example.Mapper.GenShinMapper;

import java.util.List;

/**
 * @Author zeng
 * @Date 2022/8/26 11:01
 * @User 86188
 * @Description:
 */
@Service
public class GenShinService {

    @Autowired
    GenShinMapper genShinMapper;

    GenShinSign genShinSign = new GenShinSign();

    GenShinUser genShinUser = new GenShinUser();

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

    public String showMyList(String qqid) {
        QueryWrapper<GenShinUser> queryWrapper = Wrappers.query();
        queryWrapper.like("qqid", qqid);

        List<GenShinUser> blackListUsers = genShinMapper.selectList(queryWrapper);
        System.out.println(blackListUsers);
        StringBuilder msg = new StringBuilder("当前QQ号绑定的账户:\n");
        for (GenShinUser genShinUser : blackListUsers) {
            msg.append(genShinUser.getUid()).append(" ").append(genShinUser.getNickname()).append("\n");
        }
        return String.valueOf(msg);
    }
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
}
