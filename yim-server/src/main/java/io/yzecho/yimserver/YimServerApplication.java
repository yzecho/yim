package io.yzecho.yimserver;

import io.yzecho.yimserver.config.InitConfiguration;
import io.yzecho.yimserver.zookeeper.ServiceRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.InetAddress;

/**
 * @author yecho
 */
@SpringBootApplication
@Slf4j
public class YimServerApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(YimServerApplication.class, args);
        log.info("启动Service服务成功");
    }

    private InitConfiguration configuration;

    public YimServerApplication(InitConfiguration configuration) {
        this.configuration = configuration;
    }

    /**
     * 启动后将节点注册到Zookeeper
     *
     * @param args
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {
        try {
            int httpPort = configuration.getHttpPort();
            int nettyPort = configuration.getNettyPort();
            String ip = InetAddress.getLocalHost().getHostAddress();

            Thread thread = new Thread(new ServiceRegistry(ip, httpPort, nettyPort));
            thread.setName("yim-server-register-thread");
            thread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
