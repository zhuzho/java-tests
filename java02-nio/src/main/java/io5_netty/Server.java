package io5_netty;
import config.IoDemo;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @author farmer.zs@qq.com
 * @date 2020-08-14 16:33
 * @description
 */
public class Server extends IoDemo {

  public static void main(String[] args) throws InterruptedException {
    ServerBootstrap server = new ServerBootstrap();
    EventLoopGroup boss = new NioEventLoopGroup(1);
    EventLoopGroup worker = new NioEventLoopGroup();
    try {
      server.group(boss,worker)
          .channel(NioServerSocketChannel.class)
          .option(ChannelOption.SO_BACKLOG,128)
          .childOption(ChannelOption.SO_KEEPALIVE,true)
          .childHandler(new ChannelInitializer<SocketChannel>() {
            protected void initChannel(SocketChannel ch) {
              final ChannelPipeline pipeline = ch.pipeline();
              pipeline.addLast(new LoggingHandler(LogLevel.INFO));
              pipeline.addLast(new io5_netty.ServerHandler());
            }
          });
      final ChannelFuture channelFuture = server.bind(port).sync();
      System.out.println("server ready on "+port);
      channelFuture.channel().closeFuture().sync();
    } finally {
      boss.shutdownGracefully();
      worker.shutdownGracefully();
    }
  }
}
