package io.yzecho.yimroute.config;

import io.yzecho.yimcommon.algorithm.RouteHandler;
import io.yzecho.yimroute.zookeeper.ZkUtil;
import okhttp3.OkHttpClient;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author yzecho
 * @desc
 * @date 25/11/2019 17:05
 */
@Configuration
public class BeanConfiguration {
    private ZkUtil zkUtil;

    private InitConfiguration configuration;

    public BeanConfiguration(InitConfiguration configuration, ZkUtil zkUtil) {
        this.configuration = configuration;
        this.zkUtil = zkUtil;
    }

    @Bean
    public ZkClient createZkClient() {
        ZkClient zkClient = new ZkClient(configuration.getAddress());

        // 监听根路径下子节点的变化，实时更新server list
        zkClient.unsubscribeChildChanges(configuration.getRoot(), new IZkChildListener() {
            @Override
            public void handleChildChange(String s, List<String> list) throws Exception {
                zkUtil.setAllNode(list);
            }
        });
        return zkClient;
    }

    /**
     * Redis bean
     *
     * @param factory
     * @return
     */
    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory) {
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setKeySerializer(new StringRedisSerializer());
        stringRedisTemplate.setValueSerializer(new StringRedisSerializer());
        stringRedisTemplate.setConnectionFactory(factory);
        return stringRedisTemplate;
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
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();
    }

    @Bean
    public RouteHandler buildRouteHandler() throws Exception {
        String routeWay = configuration.getRouteWay();
        RouteHandler routeHandler = (RouteHandler) Class.forName(routeWay).getDeclaredConstructor().newInstance();
        // 之后会加入 一致性Hash 解决方案
        if (routeWay.contains("ConsistentHash")) {

        }
        return routeHandler;
    }
}
