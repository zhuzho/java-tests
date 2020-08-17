package io5_netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * @author zhuzhong@yunsom.com
 * @date 2020-08-17 13:31
 * @description
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {


  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    System.out.println("channelRegistered");
    final ByteBuf buffer = Unpooled.copiedBuffer("ლ(′◉❥◉｀ლ)", CharsetUtil.UTF_8);
    ctx.channel().writeAndFlush(buffer);
  }

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    ByteBuf buf = (ByteBuf) msg;
    System.out.println("服务器回复的消息是:"+buf.toString(CharsetUtil.UTF_8));
    System.out.println("服务器的地址:"+ctx.channel().remoteAddress());
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    System.err.println("客服端链接中断"+cause.getMessage());
    ctx.channel().close();
  }
}
