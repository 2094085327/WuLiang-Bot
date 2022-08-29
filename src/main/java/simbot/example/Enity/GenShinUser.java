package simbot.example.Enity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author zeng
 * @Date 2022/8/23 8:21
 * @User 86188
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("genshininfo")
public class GenShinUser implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private String uid;
    private String qqid;
    private String nickname;
    private String cookie;
    private Integer push;
    private Integer deletes;
}
