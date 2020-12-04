package io8_netty_proto;

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

/**
 * 利用自定义协议解决拆包和粘包问题
 * @author farmer.zs@qq.com
 * @date 2020-08-21 13:52
 * @description
 */
public class Server extends IoDemo {

  public static void main(String[] args) {
    EventLoopGroup boss = new NioEventLoopGroup(1);
    EventLoopGroup worker = new NioEventLoopGroup(2);
    try {
      ServerBootstrap server = new ServerBootstrap();
      server.group(boss,worker)
      .channel(NioServerSocketChannel.class)
      .option(ChannelOption.SO_BACKLOG,128)
      .childHandler(new ChannelInitializer<SocketChannel>(){
        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
          final ChannelPipeline pipeline = ch.pipeline();
          pipeline.addLast(new MyMessageDecode());//decode
          pipeline.addLast(new ServerHandler());
        }
      }).childOption(ChannelOption.SO_KEEPALIVE,true);
      System.out.println("server is ready");
      final ChannelFuture future = server.bind(port).sync();
      future.channel().closeFuture().sync();
    } catch (InterruptedException e) {
      e.printStackTrace();
    } finally {
      boss.shutdownGracefully();
      worker.shutdownGracefully();
    }
  }

}
