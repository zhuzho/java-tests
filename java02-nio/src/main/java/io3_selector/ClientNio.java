package io3_selector;

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
 * @date 2020-08-13 16:38
 * @description
 */
public class ClientNio  extends IoDemo {


  private Selector selector;

  public ClientNio() throws IOException {
    this.selector =Selector.open();
  }

  private void init(String address,int port) throws IOException{
    SocketChannel socketChannel = SocketChannel.open();
    socketChannel.configureBlocking(false);
    socketChannel.connect(new InetSocketAddress(address,port));
    socketChannel.register(selector, SelectionKey.OP_CONNECT);
  }

  public static void main(String[] args) throws IOException {
    ClientNio client = new ClientNio();
    client.init(host,port);
    client.connect();
  }

  private void connect() throws IOException {
    int data = 1;
    ByteBuffer buf = ByteBuffer.allocate(128);
    Scanner scanner = new Scanner(System.in);
    while(true){
      selector.select(10);//
      Set<SelectionKey> set = selector.selectedKeys();
      Iterator<SelectionKey> ite = set.iterator();
      while(ite.hasNext()){
        SelectionKey selectionKey =   ite.next();
        ite.remove(); //删除已选的key,以防重复处理
        if(selectionKey.isConnectable()){//看是否有连接发生
          SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
          //如果正在连接，则完成连接
          if(socketChannel.isConnectionPending()){
            socketChannel.finishConnect();
          }
          socketChannel.configureBlocking(false);//设置为非阻塞模式
//          System.out.println("客户端连接上了服务器端。。。。");
//          socketChannel.write(ByteBuffer.wrap( scanner.nextLine().getBytes()));
          socketChannel.register(selector, SelectionKey.OP_READ);
        }
        else if(selectionKey.isReadable()){
          System.out.println("客户端连接上了服务器端");
          SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
          socketChannel.read(buf);
          byte[] receData = buf.array();
          String msg = new String(receData).trim();
          System.out.println(msg);
          buf.clear();
//          String readLine = scanner.nextLine();//单线程会阻塞
//          socketChannel.write(ByteBuffer.wrap(readLine.getBytes()));
          data++ ;
        }
      }
    }
  }
}
