package io7_netty_websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

/**
 * @author farmer.zs@qq.com
 * @date 2020-08-18 10:18
 * @description
 */
public class ServerWebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

  @Override
  public void userEventTriggered(ChannelHandlerContext ctx, Object obj) throws Exception {
    try {
      if(!(obj instanceof WebSocketServerProtocolHandler.ServerHandshakeStateEvent)) {
        return;
      }
      WebSocketServerProtocolHandler.ServerHandshakeStateEvent.valueOf(obj.toString());
      ctx.pipeline().remove(HttpRequestHandler.class);
      String msg = ctx.channel().toString();
      ctx.writeAndFlush(new TextWebSocketFrame(msg + "连接成功"));
      System.out.println("userEventTriggered");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
    System.out.println("channelRead0");
    ctx.writeAndFlush(new TextWebSocketFrame("收到消息："+msg.text()));
  }
  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    //添加连接
    System.out.println("客户端加入连接2："+ctx.channel());
  }

}
