package io.yzecho.yimcommon.algorithm;

import java.util.List;

/**
 * @author yzecho
 * @desc
 * @date 12/12/2019 16:28
 */
public interface RouteHandler {

    /**
     * 服务器负载均衡算法
     *
     * @param values
     * @param key
     * @return
     */
    String routeServer(List<String> values, String key);
}
