package io.yzecho.yimroute.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author yzecho
 * @desc zookeeper基本信息
 * @date 25/11/2019 17:06
 */
@Component
@Data
public class InitConfiguration {


    @Value("${yim.zookeeper.switch}")
    private boolean zkSwitch;

    @Value("${yim.zookeeper.root}")
    private String root;

    @Value("${yim.zookeeper.address}")
    private String address;
}
