package io.yzecho.yimclient.service;

import io.yzecho.yimclient.vo.PrivateVO;

import java.io.IOException;

/**
 * @author yzecho
 * @desc 消息处理器
 * @date 09/12/2019 21:40
 */
public interface MessageHandlerService {

    /**
     * 统一的消息处理器
     *
     * @param msg
     */
    void sendMsg(String msg) throws IOException;

    /**
     * 私聊
     *
     * @param privateVO
     */
    void privateChat(PrivateVO privateVO) throws IOException;

    /**
     * 执行内部命令
     *
     * @param msg
     * @return
     */
    boolean innerCommand(String msg) throws Exception;

    /**
     * 字符串转emoji
     *
     * @param msg
     */
    void emojiCommand(String msg);

    /**
     * 获取客户端信息
     *
     * @param msg
     */
    void clientInfoCommand(String msg);

}
