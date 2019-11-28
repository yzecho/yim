package io.yzecho.yimroute.service;

import io.yzecho.yimcommon.entity.Chat;

import java.io.IOException;

/**
 * @author yzecho
 * @desc
 * @date 25/11/2019 17:19
 */
public interface RouteService {
    /**
     * 发送消息
     *
     * @param url
     * @param chat
     * @throws IOException
     */
    void sendMessage(String url, Chat chat) throws IOException;
}
