package io.yzecho.yimserver.zookeeper;

import io.yzecho.yimserver.config.InitConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.ZkClient;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author yzecho
 * @desc
 * @date 25/11/2019 18:14
 */
@Component
@Slf4j
public class ZkUtil {

    private InitConfiguration configuration;

    private final ZkClient zkClient;

    public ZkUtil(InitConfiguration configuration, ZkClient zkClient) {
        this.configuration = configuration;
        this.zkClient = zkClient;
    }


    /**
     * 创建父节点
     */
    public void createRootNode() {
        boolean isExists = zkClient.exists(configuration.getRoot());
        if (isExists) {
            return;
        }

        // 创建root
        zkClient.createPersistent(configuration.getRoot());
    }

    /**
     * 写入指定节点，临时目录
     *
     * @param path
     */
    public void createNode(String path) {
        zkClient.createEphemeral(path);
    }

    /**
     * 获取所有注册节点
     *
     * @return
     */
    public List<String> getAllNode() {
        List<String> children = zkClient.getChildren("/yimroute");
        log.info("查询所有节点成功，节点数:{}", +children.size());
        return children;
    }
}
