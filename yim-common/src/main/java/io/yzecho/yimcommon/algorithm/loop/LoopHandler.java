package io.yzecho.yimcommon.algorithm.loop;

import io.yzecho.yimcommon.algorithm.RouteHandler;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author yzecho
 * @desc
 * @date 12/12/2019 16:35
 */
public class LoopHandler implements RouteHandler {
    private AtomicLong index = new AtomicLong();

    @Override
    public String routeServer(List<String> values, String key) {
        int size = values.size();
        if (size == 0) {
            throw new RuntimeException("no server start");
        }
        long pos = index.incrementAndGet() % values.size();
        if (pos < 0) {
            pos = 0L;
        }
        return values.get((int) pos);
    }
}
