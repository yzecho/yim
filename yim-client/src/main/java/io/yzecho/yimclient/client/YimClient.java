package io.yzecho.yimclient.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.yzecho.yimclient.config.InitConfiguration;
import io.yzecho.yimclient.pojo.ClientInfo;
import io.yzecho.yimcommon.constant.BasicConstant;
import io.yzecho.yimcommon.constant.MessageConstant;
import io.yzecho.yimcommon.entity.Chat;
import io.yzecho.yimcommon.entity.Server;
import io.yzecho.yimcommon.protobuf.MessageProto;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Objects;

/**
 * @author yzecho
 * @desc 客户端启动初始化: 1.与服务端建立连接。 2.处理客户端输入。
 * @date 23/11/2019 15:22
 */
@Component
@Slf4j
public class YimClient {
    private Server server;
    private Channel channel;

    private EventLoopGroup group = new NioEventLoopGroup(0, new DefaultThreadFactory("yim-client-work"));

    private InitConfiguration configuration;

    private OkHttpClient okHttpClient;

    @Value("${yim.user.id}")
    private Integer userId;

    @Value("${yim.user.username}")
    private String username;

    @Autowired
    private ClientInfo clientInfo;

    public YimClient(OkHttpClient okHttpClient, InitConfiguration configuration) {
        this.okHttpClient = okHttpClient;
        this.configuration = configuration;
    }

    @PostConstruct
    public void start() {
        if (server != null) {
            log.info("客户当前已登录");
            return;
        }
        // 1.获取服务ip+port
        getServer();
        // 2.启动客户端
        startClient();
        // 3.登录到服务端
        registerToServer();
    }

    /**
     * 与服务端通信
     */
    private void registerToServer() {
        MessageProto.ChatMessage login = MessageProto.ChatMessage.newBuilder()
                .setCommand(MessageConstant.LOGIN)
                .setUserId(configuration.getUserId())
                .setContent(configuration.getUsername())
                .setTime(System.currentTimeMillis())
                .build();
        channel.writeAndFlush(login).addListener(future -> {
            log.info("注册服务端成功");
        });
    }

    /**
     * 向路由服务器获取服务端ip和port
     */
    private void getServer() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userId", configuration.getUserId());
            jsonObject.put("username", configuration.getUsername());
            RequestBody requestBody = RequestBody.create(BasicConstant.MEDIA_TYPE, jsonObject.toString());

            Request request = new Request.Builder()
                    .url(configuration.getRouteLoginUrl())
                    .post(requestBody)
                    .build();

            Response response = okHttpClient.newCall(request).execute();

            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            try (ResponseBody body = response.body()) {
                String json = Objects.requireNonNull(body).string();
                server = JSON.parseObject(json, Server.class);
                log.info("获取服务端ip+port成功" + server.getIp() + ":" + server.getNettyPort());
                // 保存信息
                clientInfo.saveServiceInfo(server.getIp() + ":" + server.getNettyPort())
                        .saveUserInfo(userId, username);
            }
        } catch (IOException e) {
            log.error("连接失败");
        }
    }

    /**
     * 启动客户端，建立连接
     */
    private void startClient() {
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new YimClientInitializer());
            ChannelFuture channelFuture = bootstrap.connect(server.getIp(), server.getNettyPort()).sync();
            if (channelFuture.isSuccess()) {
                log.info("客户端启动成功");
            }
            channel = channelFuture.channel();
        } catch (InterruptedException e) {
            log.info("建立连接失败", e);
        }
    }

    /**
     * 客户端发送消息
     */
    public void sendMessage(Chat chat) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("command", chat.getCommand());
            jsonObject.put("userId", chat.getUserId());
            jsonObject.put("content", chat.getContent());
            jsonObject.put("time", chat.getTime());

            RequestBody requestBody = RequestBody.create(BasicConstant.MEDIA_TYPE, jsonObject.toString());

            Request request = new Request.Builder()
                    .url(configuration.getRouteChatUrl())
                    .post(requestBody)
                    .build();
            Response response = okHttpClient.newCall(request).execute();

            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 清理客户端登录
     */
    public void clear() {
        logoutRoute();
        logoutServer();
        server = null;
    }

    /**
     * 客户端退出命令-路由端处理
     */
    private void logoutRoute() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userId", configuration.getUserId());
            RequestBody requestBody = RequestBody.create(BasicConstant.MEDIA_TYPE, jsonObject.toString());

            Request request = new Request.Builder()
                    .url(configuration.getRouteLogoutUrl())
                    .post(requestBody)
                    .build();

            Response response = okHttpClient.newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 客户端退出命令-服务端操作
     */
    private void logoutServer() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userId", configuration.getUserId());
            RequestBody requestBody = RequestBody.create(BasicConstant.MEDIA_TYPE, jsonObject.toString());

            Request request = new Request.Builder()
                    .url("http://" + server.getIp() + ":" + server.getHttpPort() + "/clientLogout")
                    .post(requestBody)
                    .build();

            Response response = okHttpClient.newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 重新启动
     */
    public void restart() {
        // 1.清理客户端信息(路由)
        logoutRoute();
        server = null;
        // 2.start
        start();
    }

}
