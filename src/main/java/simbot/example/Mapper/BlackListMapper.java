package simbot.example.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import simbot.example.Enity.BlackListUser;

/**
 * @Author zeng
 * @Date 2022/8/30 16:14
 * @User 86188
 * @Description:
 */
@Mapper
public interface BlackListMapper extends BaseMapper<BlackListUser> {
}
