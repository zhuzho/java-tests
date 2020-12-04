package io8_netty_proto;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * @author farmer.zs@qq.com
 * @date 2020-08-21 14:08
 * @description
 */
public class ServerHandler extends SimpleChannelInboundHandler<ProtoDTO> {

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, ProtoDTO msg) throws Exception {
    System.out.println("收到消息："+new String(msg.getContent(), CharsetUtil.UTF_8));
  }
}
