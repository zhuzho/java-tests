package io4_selector;

import config.IoDemo;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author farmer.zs@qq.com
 * @date 2020-08-14 13:46
 * @description
 */
public class Server extends IoDemo implements Runnable{
  final  ServerSocketChannel server;
  final Selector selector;
  public Server() throws  Exception {
      server = ServerSocketChannel.open();
      server.configureBlocking(false);
      server.bind(new InetSocketAddress(host,port));
      selector = Selector.open();
      server.register(selector, SelectionKey.OP_ACCEPT);
  }


  public static void main(String[] args) throws  Exception {
    new Thread(new Server()).start();
  }

  @Override
  public void run() {
    while ( true){
      int count=0;
      try {
        count = selector.select(100);
      } catch (IOException e) {
        e.printStackTrace();
      }
      if (count==0){
        continue;
      }
      final Set<SelectionKey> selectionKeys = selector.selectedKeys();
      final Iterator<SelectionKey> iterator = selectionKeys.iterator();
      while (iterator.hasNext()){
        final SelectionKey key = iterator.next();
        iterator.remove();
        if (key.isAcceptable()){
          try {
            ServerSocketChannel serverSocket = (ServerSocketChannel) key.channel();
            SocketChannel socketChannel = serverSocket.accept();
            socketChannel.configureBlocking(false);
            socketChannel.register(selector,SelectionKey.OP_READ);
            System.out.println("用户"+socketChannel.socket().getRemoteSocketAddress()+"已上线");
          } catch (Exception e) {
            e.printStackTrace();
          }
        }else if (key.isReadable()){
          SocketChannel socketChannel = (SocketChannel) key.channel();
          ByteBuffer byteBuffer = ByteBuffer.allocate(128);
          try {
            int length = socketChannel.read(byteBuffer);
            if (length>0){
              String msg = new String(byteBuffer.array());
              System.out.println(socketChannel.socket().getRemoteSocketAddress().toString()+msg);
              for (SelectionKey keyPer:selector.keys()){
                Channel channel = keyPer.channel();
                if (channel instanceof SocketChannel && keyPer!=key){
                  SocketChannel otherChannel = (SocketChannel)channel ;
                  otherChannel.write(ByteBuffer.wrap((socketChannel.socket().getRemoteSocketAddress().toString()+msg).getBytes()));
                  otherChannel.register(selector,SelectionKey.OP_READ);
                }
              }
            }
            socketChannel.write(ByteBuffer.wrap("消息已收到".getBytes()));
            socketChannel.register(selector,SelectionKey.OP_READ);
            byteBuffer.clear();
          } catch (IOException e) {
            try {
              System.out.println("用户"+socketChannel.socket().getRemoteSocketAddress()+"下线");
              socketChannel.close();
            } catch (IOException e1) {
            }
          }
        }
      }
    }
  }
}
