package io6_netty_chatgroup;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author zhuzhong@yunsom.com
 * @date 2020-08-17 16:28
 * @description
 */
public class ClientChatGroupHandler extends SimpleChannelInboundHandler<String> {
  protected void channelRead0(ChannelHandlerContext ctx, String msg)   {
    System.out.println(msg);
  }
}
