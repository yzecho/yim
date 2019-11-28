package io.yzecho.yimserver.server;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import io.yzecho.yimcommon.constant.MessageConstant;
import io.yzecho.yimcommon.protobuf.MessageProto;
import io.yzecho.yimserver.config.SpringBeanFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * @author yzecho
 * @desc
 * @date 19/11/2019 21:50
 */
@ChannelHandler.Sharable
@Slf4j
public class YimServerHandler extends SimpleChannelInboundHandler<MessageProto.ChatMessage> {


    private AttributeKey<Integer> userId = AttributeKey.valueOf("userId");

    private ChannelMap channelMap = ChannelMap.newInstance();

    private ClientProcessor clientProcessor;

    public YimServerHandler() {
        clientProcessor = SpringBeanFactory.getBean(ClientProcessor.class);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MessageProto.ChatMessage chatMessage) throws Exception {

        // 处理客户端向服务端推送的消息
        if (MessageConstant.LOGIN.equals(chatMessage.getCommand())) {

            // 登录，保存Channel
            channelHandlerContext.channel().attr(userId).set(chatMessage.getUserId());
            channelMap.putClient(chatMessage.getUserId(), channelHandlerContext.channel());
            log.info("客户端登录成功.userId:" + chatMessage.getUserId());
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Integer uid = ctx.channel().attr(userId).get();
        // 从Channel缓存中删除客户端
        channelMap.getCHANNEL_MAP().remove(uid);
        clientProcessor.down(uid);
        log.info("客户端强制下线.userId:" + uid);
    }
}
