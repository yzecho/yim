package io.yzecho.yimroute.service;

import io.yzecho.yimcommon.entity.Chat;
import io.yzecho.yimcommon.entity.User;

import java.io.IOException;
import java.util.Set;

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

    /**
     * 保存和检查用户登录情况
     *
     * @param userId
     * @return
     */
    boolean saveAndCheckUserLoginStatus(Integer userId);

    /**
     * 清除用户的登录状态
     *
     * @param userId
     */
    void removeLoginStatus(Integer userId);

    /**
     * 根据用户id获取用户
     *
     * @param userId
     * @return
     */
    User loadUserByUserId(Integer userId);

    /**
     * 在线用户
     *
     * @return
     */
    Set<User> onlineUser();
}
