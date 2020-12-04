package io5_netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * @author farmer.zs@qq.com
 * @date 2020-08-17 11:42
 * @description
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    ByteBuf byteBuf = (ByteBuf) msg;
    System.out.println(byteBuf.toString(CharsetUtil.UTF_8));
  }

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    System.out.println(ctx.channel().remoteAddress());
  }

  @Override
  public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
    ByteBuf byteBuf = Unpooled.buffer();
    byteBuf.writeBytes("客服端你好！我是服务端".getBytes());
    ctx.channel().writeAndFlush(byteBuf);
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    System.err.println("服务器发生异常"+cause.getMessage());
    ctx.channel().close();
  }
}
