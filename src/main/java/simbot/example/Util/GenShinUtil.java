package simbot.example.Util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author zeng
 * @date 2022/8/18 16:04
 * @user 86188
 */
@Component
//@ConfigurationProperties(prefix = "spring")
public class GenShinUtil {

    /**
     * Spring Boot 默认提供了数据源，默认提供了
     org.springframework.jdbc.core.JdbcTemplate
     * JdbcTemplate 中会自己注入数据源，用于简化 JDBC操作
     * 还能避免一些常见的错误,使用起来也不用再自己来关闭数据库连接
     */
    @Autowired
    JdbcTemplate jdbcTemplate;

    //查询employee表中所有数据
    //List 中的1个 Map 对应数据库的 1行数据
    //Map 中的 key 对应数据库的字段名，value 对应数据库的字段值

    //查询employee表中所有数据
    //List 中的1个 Map 对应数据库的 1行数据
    //Map 中的 key 对应数据库的字段名，value 对应数据库的字段值



    public static void tests() throws SQLException {

    }


    public static void find(Connection connection) throws SQLException {
        String sql = "select * from db_user";
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            System.out.print(resultSet.getInt(1) + "  ");
            System.out.print(resultSet.getString(2) + "  ");
            System.out.println(resultSet.getString(3));
        }
       // DButil.close(resultSet, statement, connection);
    }
}
