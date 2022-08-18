package simbot.example.BootAPIUse.YuanShenAPI.GachaInfo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author zeng
 * @date 2022/8/2 19:12
 * @user 86188
 */
public class Param {

    String urls = "https://webstatic.mihoyo.com/hk4e/event/e20190909gacha/index.html?authkey_ver=1&sign_type=2&auth_appid=webview_gacha&init_type=301&gacha_id=d7d9d26fd678245ee04bec46b4bab7a8f5359c90&timestamp=1657669968&lang=zh-cn&device_type=mobile&ext=%7b%22loc%22%3a%7b%22x%22%3a1870.073974609375%2c%22y%22%3a254.1224365234375%2c%22z%22%3a-562.7354736328125%7d%2c%22platform%22%3a%22Android%22%7d&game_version=CNRELAndroid2.8.0_R9182063_S9401797_D9464149&plat_type=android&region=cn_gf01&authkey=fXfbN602ZA3b8mIB4hN4lS%2fXeymurHnpDYZxVP9mHfP1lfHKQbpypjH4qeFs2b6arlsg1%2bURMHoOReEcqQJZD8rVYeBSgZv4RQJ8DGHm%2fKKFeIaYd3bnWNfXII%2faavGYGrM%2b%2fp6yTEhly6qV%2fYwhzcqpUcYh8I%2bPomU9MizNSPm5B9SNMQ9JVfgCh3KZUo%2bs%2b5u9FrUUSiBsTO%2b%2bxjyntv6gPjew8cW%2fZLBC2l0SskYEDlqSZ%2fURu9d9XowELc2AcFLxYMzJA0pKqJijKWtIWNP1D1d2zHYcxRWBl2JsGT%2bGeJxgmUfUpDUd7fm%2b8hC3CV4BtWcHWNmr3KWFnDhGZZuMb18rzgALe3CPENRAmtOidwqp0rbGnFZqG%2fY9omTh6EEVZmIQpsF0RHahLSI73HyqOCoUOnM0HKU%2bRHzrFKqicLn2KvURlrDPC3Am3wN8qIzHn3sMzTn%2fnr%2bMCzH6idXvfeo7dSvSntJBkYESY5LEaEoKrszd8LBWGVDK6uJdtNJ9KidrZGoRPBbXTwZY3AaX%2bSyju7bKcx5%2faVGxiPHzI0HUZKRieg42%2fnMIndTukBlrOCueoa%2fIw9r2VAx%2bRqCWg2sAtk4e4mbdGqAG1uIbaWaPEkQvNi031VIjqdSaXXfnKyEVlKSH35XE6azROqAzbvbLn%2bxTMzEFsp1hExZ2IXm76L7C6yGARx0EITY0vOVKcuMmqvXW94jIxzVrxRx9STNkm47ibdkR%2fYxxLF3XBvR%2fVW5%2fTD%2fdRkqzAiM740p8GlwsCQ1B2EMIYvnIJDpjccYbpX%2b6RsgwWQ31M1iigZKbf83e9Btc0bmcxRM0ZX0cXoq4nmp3SHMtQZe%2bkLVLxBPtRMGmPJs%2f4oG4izqoRWrabgI2q3s%2fQeqbOigXciZW4M%2b1yikuZQMQOPZhkBygTjiIgHZBYJrRWo%2fa1WCBZPD5%2fwubzdO1wx6QXdJCKx3IAddnuFWEA23F9L45V52qZw3hTIQ%2fXD4VwFRe%2fdaWaMAd7KkT36S3Ix%2fQRJe8LiDDbpNrSUKHfhtWLJpVy7hqfQ2qio8pCpuNvinklTWC6RJmdezQASStqZGw1UengbbZCidP035InNKvZlmZXjUcDgxrevNB676Knu%2b4U0z%2bIl4ud4UyckVPlymPGkAs6jGGU%2fJyb9PhDWRywNCZrJP%2f%2btbvtGkQZEr9ng8PC%2fW89o1dUw87unIuaO6v3L12hy1a5CAfPsiIMFCaLCtLx%2bwu4oI2bf%2f%2bkYO2PkMeUmGxHEKs3OzZ9U4U%2fwGl%2f%2f6Tg983oGdccbmpb1eHr8JQS6Bm6JOW4PLcksRX19LSxVx1lWR7%2biCEWHIzS1X%2bHWPFYWcOYr4BuzDNUhxxx530Bw%3d%3d&game_biz=hk4e_cn#/log";


    public static String getParam(String key, String url, int times, String end_id) {

        Pattern pattern2 = Pattern.compile("([\\s\\S]*)" + key + "=([^&]*)" + "([\\s\\S]*)" + "end_id=");
        // Matcher matcher = pattern.matcher(url);
        Matcher matcher2 = pattern2.matcher(url);
        if (matcher2.find()) {
            // System.out.println(matcher.group(2));
            // System.out.println(matcher2.group(1));

            return matcher2.group(1) + key + "=" + times + matcher2.group(3) + "end_id=" + end_id;

        }
        return "";
    }
}