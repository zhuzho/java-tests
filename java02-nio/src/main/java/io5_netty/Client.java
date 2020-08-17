package io5_netty;

import config.IoDemo;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author zhuzhong@yunsom.com
 * @date 2020-08-17 11:55
 * @description
 */
public class Client extends IoDemo {

  public static void main(String[] args) throws InterruptedException {
    Bootstrap bootstrap = new Bootstrap();
    final ChannelFuture channelFuture = bootstrap
        .group(new NioEventLoopGroup())
        .channel(NioSocketChannel.class)
        .handler(new ChannelInitializer<SocketChannel>() {
          @Override
          protected void initChannel(SocketChannel ch) {
            final ChannelPipeline pipeline = ch.pipeline();
            pipeline.addLast(new ClientHandler());
          }
        })
        .connect(host, port).sync();
//    Scanner scanner = new Scanner(System.in);
//    while (scanner.hasNext()){
//      final String nextLine = scanner.nextLine();
//      channelFuture.channel().write(Unpooled.buffer().writeBytes(nextLine.getBytes()));
//    }
    channelFuture.channel().closeFuture().sync();
  }
}
