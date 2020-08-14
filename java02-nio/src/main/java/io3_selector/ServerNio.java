package io3_selector;

import config.IoDemo;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author zhuzhong@yunsom.com
 * @date 2020-08-13 15:01
 * @description
 */
public class ServerNio extends IoDemo {

  private Selector selector;
  public ServerNio(){
    try {
      selector = Selector.open();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  private void listen() throws IOException {
    System.out.println("server running....");
    while(true){
      selector.select();
      Set<SelectionKey> set = selector.selectedKeys();
      Iterator<SelectionKey> ite = set.iterator();
      while(ite.hasNext()){
        SelectionKey selectionKey = ite.next();
        ite.remove();
        if(selectionKey.isAcceptable()){//如果有客户端连接进来
          ServerSocketChannel serverSocketChannel = (ServerSocketChannel)selectionKey.channel();
          SocketChannel socketChannel = serverSocketChannel.accept();
          System.out.println("客户端"+socketChannel.socket().getRemoteSocketAddress()+"连接到服务器！！！");
          socketChannel.configureBlocking(false);//将此通道设置为非阻塞
          socketChannel.write(ByteBuffer.wrap("hello client!".getBytes()));
          socketChannel.register(selector, SelectionKey.OP_READ);
        }
        else if(selectionKey.isReadable()){//客户端发送数据过来了
          SocketChannel socketChannel = (SocketChannel)selectionKey.channel();
          ByteBuffer buf = ByteBuffer.allocate(128);
          socketChannel.read(buf);
          byte[] receivData = buf.array();
          String msg = new String(receivData).trim();
          System.out.println("接收来自客户端的数据为："+msg);
          buf.clear();
          socketChannel.write(ByteBuffer.wrap("用户你好!!!".getBytes()));
        }
      }
    }
  }
  private void init(int port) {
    try {
      ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
      serverSocketChannel.socket().bind(new InetSocketAddress(port));
      serverSocketChannel.configureBlocking(false);//设置为非阻塞模式
      serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) throws IOException {
    ServerNio server = new ServerNio();
    server.init(port);
    server.listen();
  }
}
