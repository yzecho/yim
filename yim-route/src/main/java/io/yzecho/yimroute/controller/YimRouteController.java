package io.yzecho.yimroute.controller;

import io.yzecho.yimcommon.algorithm.RouteHandler;
import io.yzecho.yimcommon.constant.BasicConstant;
import io.yzecho.yimcommon.constant.MessageConstant;
import io.yzecho.yimcommon.entity.BaseResponse;
import io.yzecho.yimcommon.entity.Chat;
import io.yzecho.yimcommon.entity.Server;
import io.yzecho.yimcommon.entity.User;
import io.yzecho.yimcommon.enums.StatusEnum;
import io.yzecho.yimcommon.exception.YimException;
import io.yzecho.yimcommon.util.StringUtil;
import io.yzecho.yimroute.service.RouteService;
import io.yzecho.yimroute.vo.PrivateVO;
import io.yzecho.yimroute.zookeeper.ZkUtil;
import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.ZkClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Set;
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

    @Autowired
    private RouteHandler routeHandler;

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
     * 2.服务端负载均衡
     *
     * @param user
     * @return
     */
    @PostMapping("/login")
    public Server login(@RequestBody User user) {

        boolean isLogin = routeService.saveAndCheckUserLoginStatus(user.getUserId());
        if (!isLogin) {
            log.error("该用户已登录");
            return null;
        }

        String result = "";
        try {
            List<String> allNode = zkClient.getChildren("/yimroute");
//            if (allNode.size() <= 0) {
//                log.info("no server start");
//                return null;
//            }
//
//            long position = index.incrementAndGet() % allNode.size();
//            if (position < 0) {
//                position = 0L;
//            }
            result = routeHandler.routeServer(allNode, String.valueOf(user.getUserId()));

            //result = allNode.get(Integer.parseInt(String.valueOf(position)));
            redisTemplate.opsForValue().set(BasicConstant.ROUTE_PREFIX + user.getUserId(), result);
            redisTemplate.opsForValue().set(BasicConstant.ACCOUNT_PREFIX + user.getUserId(), user.getUsername());
            log.info("get server info:" + result);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        String[] servers = result.split("-");
        return new Server(servers[0], Integer.parseInt(servers[1]), Integer.parseInt(servers[2]));
    }

    /**
     * 私聊api
     *
     * @param privateVO
     */
    @PostMapping("/privateRoute")
    public void privateRoute(@RequestBody PrivateVO privateVO) throws IOException {
        log.info("PrivateVO:{}", privateVO);
        String value = redisTemplate.opsForValue().get(BasicConstant.ROUTE_PREFIX + privateVO.getReceivedUserId());
        if (value == null) {
            throw new YimException(StatusEnum.OFF_LINE);
        }
        String[] server = value.split("-");
        Server result = new Server(server[0], Integer.valueOf(server[1]), Integer.valueOf(server[2]));

        String url = "http://" + result.getIp() + ":" + result.getHttpPort() + "/pushMessage";
        Chat chat = new Chat(MessageConstant.PRIVATE, System.currentTimeMillis(), privateVO.getReceivedUserId(), privateVO.getContent());
        routeService.sendMessage(url, chat);
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
        redisTemplate.opsForValue().getOperations().delete(BasicConstant.ACCOUNT_PREFIX + user.getUserId());
        redisTemplate.opsForSet().remove(BasicConstant.LOGIN_STATUS_PREFIX, user.getUserId().toString());
        log.info("路由端处理了用户下线逻辑:" + user.getUserId());
    }

    /**
     * 在线用户
     *
     * @return
     */
    @PostMapping("/onlineUser")
    public BaseResponse<Set<User>> onlineUser() {
        BaseResponse<Set<User>> response = new BaseResponse<>();
        Set<User> users = routeService.onlineUser();
        response.setDataBody(users);
        response.setCode(StatusEnum.SUCCESS.getCode());
        response.setMessage(StatusEnum.SUCCESS.getMessage());
        return response;
    }
}

