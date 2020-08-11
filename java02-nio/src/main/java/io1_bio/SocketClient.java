package io1_bio;

import config.IoDemo;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author zhuzhong@yunsom.com
 * @date 2020-08-10 16:14
 * @description
 */
public class SocketClient extends IoDemo {

  public static void main(String[] args) throws IOException {
    for (int i=0;i<10;i++){
      Socket socket = new Socket();
      socket.connect(new InetSocketAddress(host,port));
      OutputStream outputStream = socket.getOutputStream();
      outputStream.write("I am John".getBytes());
      outputStream.flush();
      socket.shutdownOutput();
      if (!socket.isInputShutdown()) {
        InputStream inputStream = socket.getInputStream();
        byte[] result = new byte[24];
        while (inputStream.read(result)!=-1){

        }
        System.out.println(new String(result));
      }
      socket.close();
    }
  }
}
