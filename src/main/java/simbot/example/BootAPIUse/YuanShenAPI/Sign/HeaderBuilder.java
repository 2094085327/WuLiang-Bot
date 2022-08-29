package simbot.example.BootAPIUse.YuanShenAPI.Sign;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

import java.util.*;

/**
 * @author zeng
 * @date 2022/8/18 13:58
 * @user 86188
 */
public class HeaderBuilder {
    public static class Builder {

        private final Map<String, String> header = new HashMap<>();

        public HeaderBuilder.Builder add(String name, String value) {
            this.header.put(name, value);
            return this;
        }

        public HeaderBuilder.Builder addAll(Header[] headers) {
            for (Header h : headers) {
                this.header.put(h.getName(), h.getValue());
            }
            return this;
        }

        public Header[] build() {
            List<Header> list = new ArrayList<>();
            for (String key : this.header.keySet()) {
                list.add(new BasicHeader(key, this.header.get(key)));
            }
            return list.toArray(new Header[0]);
        }
    }

    public static Header[] getBasicHeaders() {
        return new HeaderBuilder.Builder().add("Cookie", CookieStore.cookie)
                .add("User-Agent", String.format("Mozilla/5.0 (iPhone; CPU iPhone OS 14_0_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) miHoYoBBS/%s", "2.35.2"))
                .add("Referer", SignConstant.REFERER_URL)
                .add("Accept-Encoding", "gzip, deflate, br")
                .add("x-rpc-channel", "appstore")
                .add("accept-language", "zh-CN,zh;q=0.9,ja-JP;q=0.8,ja;q=0.7,en-US;q=0.6,en;q=0.5")
                .add("accept-encoding", "gzip, deflate")
                .add("accept-encoding", "gzip, deflate")
                .add("x-requested-with", "com.mihoyo.hyperion")
                .add("Host", "api-takumi.mihoyo.com").build();
    }

    public static Header[] getHeaders() {
        return new HeaderBuilder.Builder().add("x-rpc-device_id", UUID.randomUUID().toString().replace("-", "").toUpperCase())
                .add("Content-Type", "application/json;charset=UTF-8")
                .add("x-rpc-client_type", "2")
                .add("x-rpc-app_version", "2.34.1")
                .add("DS",HeaderParams.getDS())
                .addAll(getBasicHeaders()).build();
    }

}
