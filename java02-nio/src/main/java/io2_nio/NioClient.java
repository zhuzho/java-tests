package io2_nio;

import config.IoDemo;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
/**
 * nonblocking-input-output
 * @author zhuzhong@yunsom.com
 * @date 2020-08-11 11:53
 * @description
 */
public class NioClient extends IoDemo{

  public static void main(String[] args) {
    try {
      SocketChannel channel = SocketChannel.open();
      channel.connect(new InetSocketAddress(host,port));
      channel.configureBlocking(false);
      ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
      System.out.println("step1:链接已经建立");
      while (channel.isConnected()){
        if (!channel.socket().isOutputShutdown()){
          channel.write(ByteBuffer.wrap("i had md5 rsa".getBytes()));
          channel.shutdownOutput();
          byteBuffer.clear();
        }else if ( !channel.socket().isInputShutdown()){
          int lg = channel.read(byteBuffer);
          if (lg>0){
            System.out.println("receive from server:"+new String(byteBuffer.array()));
            channel.shutdownInput();
            channel.finishConnect();
          }
          byteBuffer.clear();
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
