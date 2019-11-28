package io.yzecho.yimserver.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.yzecho.yimserver.config.InitConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author yzecho
 * @desc
 * @date 19/11/2019 20:52
 */
@Component
@Slf4j
public class YimServer {
    private EventLoopGroup bossGroup = new NioEventLoopGroup();
    private EventLoopGroup workerGroup = new NioEventLoopGroup();

    private InitConfiguration configuration;

    public YimServer(InitConfiguration configuration) {
        this.configuration = configuration;
    }

    /**
     * 启动server
     *
     * @throws InterruptedException
     */
    @PostConstruct
    public void start() throws InterruptedException {
        // Netty用于启动Nio服务器的辅助启动类
        ServerBootstrap bootstrap = new ServerBootstrap();
        // 将两个Nio线程组传入辅助启动类中
        bootstrap.group(bossGroup, workerGroup)
                // 设置创建的Channel为NioServerSocket类型
                .channel(NioServerSocketChannel.class)
                // 保持长连接
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                // 设定绑定IO时间的处理类
                .childHandler(new YimServerInitializer());

        bootstrap.bind(configuration.getNettyPort()).sync().addListener(future -> {
            if (future.isSuccess()) {
                log.info("yim netty server start success");
            }
        });
    }

    /**
     * 关闭
     */
    @PreDestroy
    public void destroy() {
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
        log.info("yim netty server closed");
    }

}
