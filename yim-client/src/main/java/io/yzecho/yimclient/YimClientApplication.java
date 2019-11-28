package io.yzecho.yimclient;

import io.yzecho.yimclient.scanner.Scan;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author yzecho
 */
@Slf4j
@SpringBootApplication
public class YimClientApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(YimClientApplication.class, args);
        log.info("启动Client成功");
    }

    @Override
    public void run(String... args) throws Exception {
        try {
            Thread thread = new Thread(new Scan());
            thread.setName("yim-client-scanner-thread");
            thread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
