package io4_selector;

import config.IoDemo;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

/**
 * @author farmer.zs@qq.com
 * @date 2020-08-14 13:46
 * @description
 */
public class Client extends IoDemo {

  SocketChannel socketChannel;
  public Client() throws  Exception {
    socketChannel= SocketChannel.open();
    socketChannel.connect(new InetSocketAddress(host,port));
    socketChannel.configureBlocking(false);
    selector = Selector.open();
    socketChannel.register(selector, SelectionKey.OP_READ);
  }

  public static void main(String[] args) throws Exception {
    final Client client = new Client();
    new Thread(() -> {
      while (true){
        try {
          client.read();
        } catch (IOException e) {
          System.out.println("与服务器断开");
        }
      }
    }).start();
    Scanner scanner = new Scanner(System.in);
    while (true){
      String msg =  scanner.nextLine();
      client.send(msg);
    }
  }

  Selector selector;

  void read() throws IOException {
    int count = selector.select(100);
    if (count==0){
      return;
    }
    Set<SelectionKey> selectionKeys = selector.selectedKeys();
    final Iterator<SelectionKey> iterator = selectionKeys.iterator();
    ByteBuffer buffer = ByteBuffer.allocate(1024);
    while (iterator.hasNext()) {
      SelectionKey next = iterator.next();
      iterator.remove();
      SocketChannel channel = (SocketChannel) next.channel();
       if (next.isReadable()){
        int length =channel.read(buffer);
        if(length>0){
          System.out.println(new String(buffer.array()));
        }
        channel.register(selector,SelectionKey.OP_READ);
      }
      buffer.clear();
    }
  }

  public void send(String msg) throws IOException {
    socketChannel.write(ByteBuffer.wrap((" 说："+msg).getBytes()));
  }

}
