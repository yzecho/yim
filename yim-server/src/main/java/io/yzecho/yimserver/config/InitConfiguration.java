package io.yzecho.yimserver.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author yzecho
 * @desc
 * @date 25/11/2019 19:12
 */
@Component
@Data
public class InitConfiguration {

    @Value("${server.port}")
    private int httpPort;

    @Value("${yim.server.port}")
    private int nettyPort;

    @Value("${yim.zookeeper.root}")
    private String root;

    @Value("${yim.zookeeper.address}")
    private String address;

    @Value("${yim.zookeeper.switch}")
    private boolean zkSwitch;
}
