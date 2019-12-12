package io.yzecho.yimclient.service;

import io.yzecho.yimclient.vo.OnlineUsersVO;
import io.yzecho.yimclient.vo.PrivateVO;

import java.io.IOException;
import java.util.List;

/**
 * @author yzecho
 * @desc
 * @date 09/12/2019 22:07
 */
public interface RouteRequestService {

    /**
     * 获取全部在线用户
     *
     * @return
     */
    List<OnlineUsersVO.DataBodyBean> onlineUsers() throws IOException, Exception;

    /**
     * 私聊
     *
     * @param privateVO
     */
    void sendPrivateMsg(PrivateVO privateVO) throws IOException;
}
