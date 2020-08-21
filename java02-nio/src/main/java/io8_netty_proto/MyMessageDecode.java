package io8_netty_proto;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.util.List;

/**
 * @author zhuzhong@yunsom.com
 * @date 2020-08-21 14:03
 * @description
 */
public class MyMessageDecode extends ByteToMessageDecoder {

  @Override
  protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
    final int length = in.readInt();
    ProtoDTO data = new ProtoDTO();
    data.setLength(length);
    byte[] dt = new byte[length];
    in.readBytes(dt);
    data.setContent(dt);
    out.add(data);
  }
}
