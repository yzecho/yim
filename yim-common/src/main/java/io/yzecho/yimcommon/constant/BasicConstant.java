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
    public static final String ROUTE_PREFIX = "yim-route";

    /**
     * 响应格式
     */
    public static final MediaType MEDIA_TYPE = MediaType.parse("application/json");
}
