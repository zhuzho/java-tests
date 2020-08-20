package io7_netty_websocket;

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
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @author zhuzhong@yunsom.com
 * @date 2020-08-18 9:36
 * @description
 */
public class Server extends IoDemo {

  public static void main(String[] args) throws Exception {
    EventLoopGroup boss = new NioEventLoopGroup(1);
    EventLoopGroup worker = new NioEventLoopGroup();
    try {

      ServerBootstrap server = new ServerBootstrap();
      server
          .group(boss,worker)
          .channel(NioServerSocketChannel.class)
          .option(ChannelOption.SO_BACKLOG,128)
          .childHandler(new ChannelInitializer<SocketChannel>() {
            protected void initChannel(SocketChannel ch)  {
              final ChannelPipeline pipeline = ch.pipeline();
              pipeline.addLast(new HttpServerCodec());
              //聚合http请求的包
              pipeline.addLast(new HttpObjectAggregator(64*1024));
              pipeline.addLast(new ChunkedWriteHandler());
              pipeline.addLast(new WebSocketServerProtocolHandler("/wsd"));
              pipeline.addLast(new HttpRequestHandler("/wsd"));
              pipeline.addLast(new ServerWebSocketHandler());
            }
          }).childOption(ChannelOption.SO_KEEPALIVE,true);
      final ChannelFuture channelFuture = server.bind(port).sync();
      System.out.println("server ready");
      channelFuture.channel().closeFuture().sync();
    }finally {
      boss.shutdownGracefully();
      worker.shutdownGracefully();
    }
  }
}
