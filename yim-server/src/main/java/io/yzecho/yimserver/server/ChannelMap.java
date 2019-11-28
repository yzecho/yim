package io.yzecho.yimserver.server;

import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yzecho
 * @desc
 * @date 25/11/2019 20:06
 */
public class ChannelMap {
    private static ChannelMap instance = null;

    private final Map<Integer, Channel> CHANNEL_MAP = new ConcurrentHashMap<>();

    private ChannelMap() {

    }

    public static ChannelMap newInstance() {
        if (instance == null) {
            instance = new ChannelMap();
        }
        return instance;
    }

    public Map<Integer, Channel> getCHANNEL_MAP() {
        return CHANNEL_MAP;
    }

    public void putClient(Integer userId, Channel channel) {
        CHANNEL_MAP.put(userId, channel);
    }

    public Channel getClient(Integer userId) {
        return CHANNEL_MAP.get(userId);
    }
}
