package io.yzecho.yimclient.client;

import com.vdurmont.emoji.EmojiParser;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.yzecho.yimclient.config.SpringBeanFactory;
import io.yzecho.yimcommon.protobuf.MessageProto;
import lombok.extern.slf4j.Slf4j;

/**
 * @author yzecho
 * @desc
 * @date 25/11/2019 11:02
 */
@Slf4j
public class YimClientHandler extends SimpleChannelInboundHandler<MessageProto.ChatMessage> {


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MessageProto.ChatMessage chatMessage) throws Exception {
        log.info("客户端[{}]说:{}", chatMessage.getUserId(), EmojiParser.parseToUnicode(chatMessage.getContent()));
    }

    /**
     * 当客户端发现服务端断线后，发起重连
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        YimClient client = SpringBeanFactory.getBean(YimClient.class);
        log.info("所连接的服务端断了连接，发送重新连接请求");
        try {
            client.restart();
            log.info("客户端重连成功");
        } catch (Exception e) {
            log.info("客户端重连失败");
            e.printStackTrace();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
