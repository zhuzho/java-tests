package io1_bio;

import config.IoDemo;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author farmer.zs@qq.com
 * @date 2020-08-10 15:53
 * @description
 */
public class SocketSever01 extends IoDemo {

  public static void main(String[] args) {
    ServerSocket server ;
    try {
      server = new ServerSocket();
      server.bind(new InetSocketAddress(host,port));
      Socket socket;

      System.out.println("server is readying!");
      ExecutorService executorService
          = new ThreadPoolExecutor(
              3,5,30, TimeUnit.SECONDS
              ,new ArrayBlockingQueue<>(100)
      );
      while (true){
        socket = server.accept();
        System.out.println("请求来了！");
        executorService.execute(new Sayer(socket));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  static class Sayer implements Runnable{
    Socket socket;
    public Sayer(Socket socket){
      this.socket = socket;
    }
    @Override
    public void run() {
      OutputStream outputStream ;
      InputStream inputStream;
      try {
        inputStream = socket.getInputStream();
        byte[] result = new byte[24];
        while (inputStream.read(result)!=-1){
        }
        System.out.println(new String(result));
        if (!socket.isOutputShutdown()){
          outputStream = socket.getOutputStream();
          outputStream.write("hi John".getBytes());
          outputStream.flush();
          socket.shutdownOutput();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }finally {
        try {
          socket.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }
}
