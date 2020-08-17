package io6_netty_chatgroup;

import config.IoDemo;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

/**
 * @author zhuzhong@yunsom.com
 * @date 2020-08-17 15:54
 * @description
 */
public class Server extends IoDemo {

  public static void main(String[] args) throws InterruptedException {
    final EventLoopGroup boss = new NioEventLoopGroup(1);
    final EventLoopGroup worker = new NioEventLoopGroup();
    try {
      ServerBootstrap server = new ServerBootstrap();
      server.group(boss,worker)
          .channel(NioServerSocketChannel.class)
          .option(ChannelOption.SO_BACKLOG,128)
          .childHandler(new ChannelInitializer<NioSocketChannel>() {
            protected void initChannel(NioSocketChannel ch)   {
              final ChannelPipeline pipeline = ch.pipeline();
              pipeline.addLast("decoder",new StringDecoder(CharsetUtil.UTF_8));
              pipeline.addLast("encoder",new StringEncoder(CharsetUtil.UTF_8));
              pipeline.addLast(new ServerChatGroupHandler());
            }
          })
          .childOption(ChannelOption.SO_KEEPALIVE,true);
      final ChannelFuture channelFuture = server.bind(port).sync();
      channelFuture.channel().closeFuture().sync();
    }finally {
      boss.shutdownGracefully();
      worker.shutdownGracefully();
    }
  }
}
