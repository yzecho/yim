package io.yzecho.yimserver.controller;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import io.yzecho.yimcommon.constant.MessageConstant;
import io.yzecho.yimcommon.entity.Chat;
import io.yzecho.yimcommon.protobuf.MessageProto;
import io.yzecho.yimserver.server.ChannelMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author yzecho
 * @desc
 * @date 26/11/2019 13:19
 */
@RestController
@RequestMapping("/")
@Slf4j
public class YimServerController {
    private ChannelMap channelMap = ChannelMap.newInstance();

    private AttributeKey<Integer> userId = AttributeKey.valueOf("userId");

    /**
     * 服务端接收消息，并转发给指定客户端
     *
     * @param chat
     */
    @PostMapping("/pushMessage")
    public void pushMessage(@RequestBody Chat chat) {
        // 1.接受客户端封装好的消息对象
        MessageProto.ChatMessage message = MessageProto.ChatMessage.newBuilder()
                .setCommand(chat.getCommand())
                .setTime(chat.getTime())
                .setUserId(chat.getUserId())
                .setContent(chat.getContent())
                .build();
        // 2.根据消息发送给客户端(私发)
        if (MessageConstant.PRIVATE.equals(chat.getCommand())) {
            Channel channel = channelMap.getCHANNEL_MAP().get(chat.getUserId());
            channel.writeAndFlush(message);
        }
        // 3.或者根据消息发送给客户端(群发)
        // 根据userId，从本地Map集合中得到对应的客户端Channel，发送消息
        if (MessageConstant.CHAT.equals(chat.getCommand())) {
            for (Map.Entry<Integer, Channel> channelEntry : channelMap.getCHANNEL_MAP().entrySet()) {
                // 过滤掉客户端本身
                if (channelEntry.getKey() != message.getUserId()) {
                    log.info("服务端向" + channelEntry.getValue().attr(userId).get() + "发送了消息,来自useId=" + message.getUserId());
                    channelEntry.getValue().writeAndFlush(message);
                }
            }
        }
    }

    /**
     * 服务端处理客户端下线逻辑事件
     *
     * @param chat
     */
    @PostMapping("/clientLogout")
    public void clientLogout(@RequestBody Chat chat) {
        channelMap.getCHANNEL_MAP().remove(chat.getUserId());
        log.info("客户端已下线[" + chat.getUserId() + "]");
    }
}
