package simbot.example.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import simbot.example.BootAPIUse.BlackListAPI.BlackList;
import simbot.example.Enity.BlackListUser;
import simbot.example.Mapper.BlackListMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author zeng
 * @Date 2022/8/30 16:14
 * @User 86188
 * @Description: 黑名单service层
 */
@Service
public class BlackListService {

    BlackListMapper blackListMapper;

    @Autowired
    public BlackListService(BlackListMapper blackListMapper) {
        this.blackListMapper = blackListMapper;
    }

    BlackListUser blackListUser = new BlackListUser();
    BlackList blackList = new BlackList();

    /**
     * 插入用户信息
     */
    public void insertInfo(String code, String nickName) {

        blackListUser.setCode(code);
        blackListUser.setNickname(nickName);

        blackListMapper.insert(blackListUser);
    }

    /**
     * 查询QQ号
     *
     * @param code QQ号
     * @return 返回QQ号
     */
    public String selectCode(String code) {
        QueryWrapper<BlackListUser> queryWrapper = Wrappers.query();
        queryWrapper.like("code", code);
        BlackListUser blackListUser = blackListMapper.selectOne(queryWrapper);

        // 当数据存在时进行初始化
        if (blackListUser != null) {
            System.out.println("数据库返回的code:" + blackListUser.getCode());
            return blackListUser.getCode();
        }
        return null;
    }

    public ArrayList<String> blackList() {
        List<BlackListUser> blackListUsers = blackListMapper.selectList(null);
        ArrayList<String> list = new ArrayList<>();
        if (blackListUsers != null) {
            for (BlackListUser listUser : blackListUsers) {
                list.add(listUser.getCode());
            }
        } else {
            list.add("0000000000");
        }
        return list;
    }

    /**
     * 更新黑名单
     */
    public void updateBlackList() {
        blackListUser.setCode(blackList.getCodes());
        blackListUser.setNickname(blackList.getNickName());

        QueryWrapper<BlackListUser> queryWrapper = Wrappers.query();
        queryWrapper.like("code", blackList.getCodes());

        blackListMapper.update(blackListUser, queryWrapper);
    }

    public void deleteBlackList(String code) {
        QueryWrapper<BlackListUser> queryWrapper = Wrappers.query();
        queryWrapper.like("code", code);

        blackListMapper.delete(queryWrapper);
    }

    public void showAllList() {
        List<BlackListUser> blackListUsers = blackListMapper.selectList(null);
        System.out.println(blackListUsers);
        StringBuilder msg = new StringBuilder("黑名单列表:\n");
        int listNumber = blackListUsers.size();
        for (BlackListUser listUser : blackListUsers) {
            msg.append(listUser.getCode()).append(" ").append(listUser.getNickname());
            if (listNumber > 1) {
                msg.append("\n");
                listNumber -= 1;
            }
        }
        BlackList.setAllLists(String.valueOf(msg));
    }
}
