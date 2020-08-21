package io8_netty_proto;

import config.IoDemo;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author zhuzhong@yunsom.com
 * @date 2020-08-21 14:13
 * @description
 */
public class Client extends IoDemo {

  public static void main(String[] args) {
    EventLoopGroup group = new NioEventLoopGroup();
    try {
      Bootstrap client = new Bootstrap();
      client.group(group).channel(NioSocketChannel.class)
          .handler(new ChannelInitializer<SocketChannel>(){
            protected void initChannel(SocketChannel ch)  {
              final ChannelPipeline pipeline = ch.pipeline();
              //encode
              pipeline.addLast(new MyMessageEncode());
              // handler
              pipeline.addLast(new ClientHandler());
            }
          });
      final ChannelFuture future = client.connect(host, port).sync();
      future.channel().closeFuture().sync();
    } catch (InterruptedException e) {
      e.printStackTrace();
    } finally {
      group.shutdownGracefully();
    }

  }
}
