package simbot.example.Util;

import love.forte.simbot.bot.BotManager;

/**
 * @author zeng
 * @date 2022/7/25 20:56
 * @user 86188
 */
public class RobotCore {
    /**
     * 全局BotManager
     */
    private static BotManager botManager;
    public static String getDefaultBotCode() {
        return botManager.getDefaultBot().getBotInfo().getAccountCode();
    }
}
