package io8_netty_proto;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author farmer.zs@qq.com
 * @date 2020-08-21 14:17
 * @description
 */
public class ClientHandler extends SimpleChannelInboundHandler<String> {

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    ctx.writeAndFlush("我是张三");
  }

  protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
  }
}
