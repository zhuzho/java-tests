package io2_nio;

import config.IoDemo;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.List;

/**
 * nonblocking-input-output
 * @author zhuzhong@yunsom.com
 * @date 2020-08-11 11:53
 * @description
 */
public class NioServer extends IoDemo {

  public static void main(String[] args) {
    try {
      ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
      serverSocketChannel.bind(new InetSocketAddress(host,port));
      serverSocketChannel.configureBlocking(false);
      List<SocketChannel> clients = new LinkedList<>();
      ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
      System.out.println("server is ready");
      while (true){
        SocketChannel client = serverSocketChannel.accept();
        if (client == null){
        } else {
          client.configureBlocking(false);
          clients.add(client);
        }
        for (SocketChannel cl:clients){
          int length = cl.read(byteBuffer);
          if (length>0){
            System.out.println("receive client :"+cl.socket().getPort()+":"+new String(byteBuffer.array()));
            byteBuffer.clear();
            cl.write(ByteBuffer.wrap("rsa , public key is “ABC”".getBytes()));
            byteBuffer.flip();
          }
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
