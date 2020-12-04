package io7_netty_websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;

/**
 * @author farmer.zs@qq.com
 * @date 2020-08-20 11:52
 * @description
 */
public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

  private final String uri;

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    //添加连接
    System.out.println("客户端加入连接："+ctx.channel());
  }

  public HttpRequestHandler(String uri) {
    super();
    this.uri = uri;
  }

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
    System.out.println("channelRead0:"+msg);
    if (uri.equalsIgnoreCase(msg.uri())) {
      ctx.fireChannelRead(msg.retain());
    }
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable e) throws Exception {
    ctx.close();
    e.printStackTrace();
  }
}