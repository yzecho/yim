package io.yzecho.yimcommon.constant;

import okhttp3.MediaType;

/**
 * @author yzecho
 * @desc
 * @date 25/11/2019 17:01
 */
public class BasicConstant {

    /**
     * redis中客户端服务端映射
     */
    public static final String ROUTE_PREFIX = "yim-route:";

    /**
     * 用户前缀
     */
    public static final String ACCOUNT_PREFIX = "yim-account:";

    /**
     * 登录状态前缀
     */
    public static final String LOGIN_STATUS_PREFIX = "login-status";

    /**
     * http 响应格式
     */
    public static final MediaType MEDIA_TYPE = MediaType.parse("application/json");
}
