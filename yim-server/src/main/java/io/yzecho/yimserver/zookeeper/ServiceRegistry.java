package io.yzecho.yimserver.zookeeper;

import io.yzecho.yimserver.config.InitConfiguration;
import io.yzecho.yimserver.config.SpringBeanFactory;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author yzecho
 * @desc
 * @date 25/11/2019 19:17
 */
@Slf4j
public class ServiceRegistry implements Runnable {

    private InitConfiguration configuration;
    private ZkUtil zkUtil;
    private String ip;
    private int httpPort;
    private int nettyPort;

    public ServiceRegistry(String ip, int httpPort, int nettyPort) {
        this.ip = ip;
        this.httpPort = httpPort;
        this.nettyPort = nettyPort;
        zkUtil = SpringBeanFactory.getBean(ZkUtil.class);
        configuration = SpringBeanFactory.getBean(InitConfiguration.class);
    }

    @Override
    public void run() {
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
            httpPort = configuration.getHttpPort();
            nettyPort = configuration.getNettyPort();

            // 创建父节点
            zkUtil.createRootNode();
            // 判断是否需要注册到zookeeper
            if (configuration.isZkSwitch()) {
                String path = configuration.getRoot() + "/" + ip + "-" + nettyPort + "-" + httpPort;
                zkUtil.createNode(path);
                log.info("服务端节点注册到zookeeper成功，path:" + path);

            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
