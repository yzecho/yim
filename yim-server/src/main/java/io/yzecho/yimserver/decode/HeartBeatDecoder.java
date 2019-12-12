package io.yzecho.yimserver.decode;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.yzecho.yimserver.pojo.CustomProtocol;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author yzecho
 * @desc
 * @date 12/12/2019 15:23
 */
public class HeartBeatDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        int id = byteBuf.readInt();
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);

        String content = new String(bytes, StandardCharsets.UTF_8);

        CustomProtocol customProtocol = new CustomProtocol();
        customProtocol.setId(id);
        customProtocol.setContent(content);
        list.add(customProtocol);

    }
}
