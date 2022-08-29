package simbot.example.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import simbot.example.Enity.GenShinUser;

/**
 * @Author zeng
 * @Date 2022/8/23 8:22
 * @User 86188
 * @Description:
 */

@Mapper
public interface GenShinMapper extends BaseMapper<GenShinUser> {
}
