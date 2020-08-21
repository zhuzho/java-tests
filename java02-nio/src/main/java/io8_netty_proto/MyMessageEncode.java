package io8_netty_proto;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.CharsetUtil;

/**
 * @author zhuzhong@yunsom.com
 * @date 2020-08-21 14:11
 * @description
 */
public class MyMessageEncode extends MessageToByteEncoder<String> {
  @Override
  protected void encode(ChannelHandlerContext ctx, String msg, ByteBuf out) throws Exception {
    final byte[] bytes = msg.getBytes(CharsetUtil.UTF_8);
    out.writeInt(bytes.length);
    out.writeBytes(bytes);
  }
}
