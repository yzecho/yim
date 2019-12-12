package io.yzecho.yimclient.encode;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.yzecho.yimclient.pojo.CustomProtocol;

/**
 * @author yzecho
 * @desc
 * @date 12/12/2019 15:21
 */
public class HeartBeatEncoder extends MessageToByteEncoder<CustomProtocol> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, CustomProtocol customProtocol, ByteBuf byteBuf) throws Exception {
        byteBuf.writeInt(customProtocol.getId());
        byteBuf.writeBytes(customProtocol.getContent().getBytes());
    }
}
