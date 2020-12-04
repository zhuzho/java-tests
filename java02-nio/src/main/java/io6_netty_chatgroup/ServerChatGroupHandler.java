package io6_netty_chatgroup;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.ChannelMatchers;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * @author farmer.zs@qq.com
 * @date 2020-08-17 16:01
 * @description
 */
public class ServerChatGroupHandler extends SimpleChannelInboundHandler<String> {

  public  final  static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

  /**
   * 通道建立，建立通道之后第一个执行的方法
   * @param ctx
   * @throws Exception
   */
  public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
    System.out.println("handlerAdded");
    final Channel channel = ctx.channel();
    channelGroup.writeAndFlush("[客户端]"+ channel.remoteAddress()+"加入聊天群\n");
    channelGroup.add(channel);
  }

  /**
   * 通道被移除
   * @param ctx
   * @throws Exception
   */
  public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
    System.out.println("handlerRemoved");
    final Channel channel = ctx.channel();
    channelGroup.writeAndFlush("[客户端]"+ channel.remoteAddress()+"离开聊天群\n");
    channelGroup.remove(channel);
  }

  /**
   * 通道注册
   * @param ctx
   * @throws Exception
   */
  public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
    System.out.println("channelRegistered");
  }

  /**
   * 通过注销
   * @param ctx
   * @throws Exception
   */
  public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
    System.out.println("channelUnregistered");
  }

  /**
   * 通道可用
   * @param ctx
   * @throws Exception
   */
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    System.out.println("channelActive");
  }

  /***
   * 通道失效
   * @param ctx
   * @throws Exception
   */
  public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    System.out.println("channelInactive");
  }

  /***
   * 通道读取事件
   * @param ctx
   * @param msg
   * @throws Exception
   */
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    System.out.println("channelRead");
    channelGroup.writeAndFlush(ctx.channel().remoteAddress()+"说："+msg, ChannelMatchers.isNot(ctx.channel()));
  }

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
    System.out.println("channelRead0");

  }

  /**
   * 读取事件完成
   * @param ctx
   * @throws Exception
   */
  public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
    System.out.println("channelReadComplete");

  }

  /***
   * 用户事件触发
   * @param ctx
   * @param evt
   * @throws Exception
   */
  public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
  }

  public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
  }

  /**
   * 发生异常
   * @param ctx
   * @param cause
   * @throws Exception
   */
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
  }
}
