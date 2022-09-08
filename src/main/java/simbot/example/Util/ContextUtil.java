package simbot.example.Util;

import love.forte.simbot.component.mirai.message.MiraiMessageContentBuilderFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;


/**
 * @Description: 消息转发相关工具
 * @author 86188
 */
@Service
@SuppressWarnings("unused")
public class ContextUtil {
    /**
     * 无法Autowired
     */
    private static ApplicationContext applicationContext;

    public void setApplicationContext(ApplicationContext applicationContext) {
        ContextUtil.applicationContext = applicationContext;
    }

    public static <T> T getBeanByType(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

    public static MiraiMessageContentBuilderFactory getForwardBuilderFactory() {
        return ((MiraiMessageContentBuilderFactory) applicationContext.getBean("simbotMessageContentBuilderFactory"));
    }


}
