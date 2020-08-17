package io6_netty_chatgroup;

import config.IoDemo;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import java.nio.charset.Charset;
import java.util.Scanner;

/**
 * @author zhuzhong@yunsom.com
 * @date 2020-08-17 16:21
 * @description
 */
public class Client extends IoDemo {

  public static void main(String[] args) throws InterruptedException {
    final NioEventLoopGroup loopGroup = new NioEventLoopGroup(1);
    try {
      Bootstrap client = new Bootstrap();
      client
          .group(loopGroup)
          .channel(NioSocketChannel.class)
          .handler(new ChannelInitializer<SocketChannel>(){
            protected void initChannel(SocketChannel ch)  {
              final ChannelPipeline pipeline = ch.pipeline();
              pipeline.addLast("decoder",new StringDecoder(CharsetUtil.UTF_8));
              pipeline.addLast("encoder",new StringEncoder(CharsetUtil.UTF_8));
              pipeline.addLast(new ClientChatGroupHandler());
            }
          });
      final ChannelFuture channelFuture = client.connect(host, port).sync();
      final Channel channel = channelFuture.channel();
      Scanner scanner = new Scanner(System.in);
      while (scanner.hasNext()){
        final String nextLine = scanner.nextLine();
        channel.writeAndFlush(nextLine);
      }
      channel.closeFuture().sync();
    }finally {
      loopGroup.shutdownGracefully();
    }
  }
}
