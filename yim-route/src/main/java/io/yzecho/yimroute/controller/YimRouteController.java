package io.yzecho.yimroute.controller;

import io.yzecho.yimcommon.constant.BasicConstant;
import io.yzecho.yimcommon.entity.Chat;
import io.yzecho.yimcommon.entity.Server;
import io.yzecho.yimcommon.entity.User;
import io.yzecho.yimcommon.util.StringUtil;
import io.yzecho.yimroute.service.RouteService;
import io.yzecho.yimroute.zookeeper.ZkUtil;
import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.ZkClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author yzecho
 * @desc 服务端处理器
 * @date 25/11/2019 17:44
 */
@RestController
@RequestMapping("/")
@Slf4j
public class YimRouteController {

    private ZkClient zkClient;
    private AtomicLong index = new AtomicLong(0);

    private ZkUtil zkUtil;
    private RedisTemplate<String, String> redisTemplate;
    private RouteService routeService;


    public YimRouteController(ZkClient zkClient, ZkUtil zkUtil, RouteService routeService, RedisTemplate<String, String> redisTemplate) {
        this.zkClient = zkClient;
        this.zkUtil = zkUtil;
        this.redisTemplate = redisTemplate;
        this.routeService = routeService;
    }

    /**
     * 客户端登录，为其分配一个服务器
     * 1.获取zookeeper上的所有子节点
     * 2.轮询法实现服务端负载均衡
     *
     * @param user
     * @return
     */
    @PostMapping("/login")
    public Server login(@RequestBody User user) {
        String result = "";
        try {
            List<String> allNode = zkClient.getChildren("/yimroute");
            if (allNode.size() <= 0) {
                log.info("no server start");
                return null;
            }

            long position = index.incrementAndGet() % allNode.size();
            if (position < 0) {
                position = 0L;
            }

            result = allNode.get(Integer.parseInt(String.valueOf(position)));
            redisTemplate.opsForValue().set(BasicConstant.ROUTE_PREFIX + user.getUserId(), result);
            log.info("get server info:" + result);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        String[] servers = result.split("-");
        return new Server(servers[0], Integer.parseInt(servers[1]), Integer.parseInt(servers[2]));
    }

    /**
     * 分发消息
     *
     * @param chat
     */
    @PostMapping("/chat")
    public void chat(@RequestBody Chat chat) {
        // 判断userId 是否登录---从缓存中获取数据
        String isLogin = redisTemplate.opsForValue().get(BasicConstant.ROUTE_PREFIX + chat.getUserId());
        if (StringUtil.isEmpty(isLogin)) {
            log.info("该用户尚未登录[" + chat.getUserId() + "]");
            return;
        }
        try {
            List<String> allNode = zkClient.getChildren("/yimroute");
            for (String node : allNode) {
                String[] temp = node.split("-");
                String ip = temp[0];
                int httpPort = Integer.parseInt(temp[2]);
                String url = "http://" + ip + ":" + httpPort + "/pushMessage";
                routeService.sendMessage(url, chat);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 客户端下线，从缓存中删除客户端与服务端映射关系
     *
     * @param user
     */
    @PostMapping("/logout")
    public void logout(@RequestBody User user) {
        redisTemplate.opsForValue().getOperations().delete(BasicConstant.ROUTE_PREFIX + user.getUserId());
        log.info("路由端处理了用户下线逻辑:" + user.getUserId());
    }
}

