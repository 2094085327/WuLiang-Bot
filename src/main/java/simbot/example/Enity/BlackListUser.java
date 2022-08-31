package simbot.example.Enity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author zeng
 * @Date 2022/8/30 16:16
 * @User 86188
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("blacklist")
public class BlackListUser implements Serializable {
    private String code;
    private String nickname;
}
