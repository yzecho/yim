package io.yzecho.yimserver.config;

import okhttp3.OkHttpClient;
import org.I0Itec.zkclient.ZkClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * @author yzecho
 * @desc
 * @date 26/11/2019 20:14
 */
@Configuration
public class BeanConfiguration {

    private InitConfiguration configuration;

    public BeanConfiguration(InitConfiguration configuration) {
        this.configuration = configuration;
    }

    @Bean
    public ZkClient buildZkClient() {
        return new ZkClient(configuration.getAddress(), 5000);
    }

    /**
     * http client
     *
     * @return
     */
    @Bean
    public OkHttpClient okHttpClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();
    }

}
