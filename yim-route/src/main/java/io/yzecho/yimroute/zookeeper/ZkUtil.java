package io.yzecho.yimroute.zookeeper;

import io.yzecho.yimroute.config.InitConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.ZkClient;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yzecho
 * @desc
 * @date 25/11/2019 17:05
 */
@Component
@Slf4j
public class ZkUtil {
    private ZkClient zkClient;

    private InitConfiguration configuration;

    public void setZkClient(ZkClient zkClient) {
        this.zkClient = zkClient;
    }

    public ZkUtil(InitConfiguration configuration) {
        this.configuration = configuration;
    }

    /**
     * 创建zookeeper根节点
     */
    public void createRootNode() {
        boolean isExists = zkClient.exists(configuration.getRoot());
        if (isExists) {
            return;
        }
        // 创建 root
        zkClient.createPersistent(configuration.getRoot());
    }

    /**
     * 写入指定路径 临时目录
     *
     * @param path
     */
    public void createNode(String path) {
        zkClient.createPersistent(path);
    }

    /**
     * 获取所有注册节点
     *
     * @return
     */
    public List<String> getAllNode() {
        List<String> allNode = zkClient.getChildren("/yimroute");
        log.info("查询所有节点信息，节点数:{}", allNode.size());
        return allNode;
    }

    /**
     * 更新server list
     *
     * @param allNode
     */
    public void setAllNode(List<String> allNode) {
        log.info("server节点更新，节点数:{}", allNode.size());
        List<String> children = zkClient.getChildren("/yimroute");
        children = allNode;
    }
}
