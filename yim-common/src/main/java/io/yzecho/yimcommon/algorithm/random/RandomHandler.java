package io.yzecho.yimcommon.algorithm.random;

import io.yzecho.yimcommon.algorithm.RouteHandler;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author yzecho
 * @desc
 * @date 12/12/2019 16:30
 */
public class RandomHandler implements RouteHandler {
    @Override
    public String routeServer(List<String> values, String key) {
        int size = values.size();
        if (size == 0) {
            throw new RuntimeException("no server start");
        }

        int offset = ThreadLocalRandom.current().nextInt(size);
        return values.get(offset);
    }
}
