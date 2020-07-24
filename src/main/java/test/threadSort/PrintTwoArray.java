package test.threadSort;

import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TransferQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 2个线程有序交叉打印2个数组
 * @author zhuzhong@yunsom.com
 * @date 2020-07-23 15:52
 * @description
 */
public class PrintTwoArray {

  final static  Object object = new Object();
  final static char[] num = "123456".toCharArray();
  final static char[] word = "abcdef".toCharArray();
  public static void main(String[] args) {

//    testWaitNotify(num,word);
//    testCondition(num,word);
//    testLockSupport(num,word);
    testLockQueue(num,word);
  }
  static Thread t1;
  static Thread t2;
  public static void testLockQueue(final char[] num,final char[] word) {
    final TransferQueue<Character> que = new LinkedTransferQueue();
    t1 = new Thread(new Runnable() {
      @Override
      public void run() {
        for (char ch:num){
          try {
            que.transfer(ch);
            System.out.print(que.take());
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      }
    });
    t2 = new Thread(new Runnable() {
      @Override
      public void run() {
        for (char ch:word){
          try {
            System.out.print(que.take());
            que.transfer(ch);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      }
    });
    t1.start();
    t2.start();

  }
  public static void testLockSupport(final char[] num,final char[] word){
      t1 = new Thread(new Runnable() {
      @Override
      public void run() {
        for (char ch:num){
          System.out.print(ch);
          LockSupport.unpark(t2);
          LockSupport.park();
        }
      }
    });
      t2 = new Thread(new Runnable() {
      @Override
      public void run() {
        for (char ch:word){
          LockSupport.park();
          System.out.print(ch);
          LockSupport.unpark(t1);
        }
      }
    });
    t1.start();
    t2.start();

  }
  public static void testCondition(final char[] num,final char[] word){
    final Lock lock = new ReentrantLock();
    final Condition condition1 = lock.newCondition();
    final Condition condition2 = lock.newCondition();

    t1 = new Thread(new Runnable() {
      @Override
      public void run() {
        lock.lock();
          for (char ch:num){
            System.out.print(ch);
            try {
              condition2.signal();
              condition1.await();
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
          }
        lock.unlock();
      }
    });
    t2 = new Thread(new Runnable() {
      @Override
      public void run() {
        lock.lock();
        boolean flag = true;
          for (char ch:word){
            try {
              if (flag){
                //有可能t1先进锁 t2还没有wait 所以t1的notify无效，
                // 如果第一次t2没人叫醒超时之后自动醒来执行后面的逻辑
                condition2.await(1,TimeUnit.MILLISECONDS );
                flag=false;
              }else {
                condition2.await();
              }
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
            System.out.print(ch);
            condition1.signal();
          }
        condition1.signal();
        lock.unlock();
        }
    });
    t1.start();
    t2.start();

  }
  public static void testWaitNotify(final  char[] num,final char[] word){

    t1 = new Thread(new Runnable() {
      @Override
      public void run() {
        synchronized (object){
          for (char ch:num){
            System.out.print(ch);
            try {
              object.notify();
              object.wait();
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
          }
          object.notify();
        }
      }
    });
    t2 = new Thread(new Runnable() {
      @Override
      public void run() {
        synchronized (object){
          boolean flag =true;
          for (char ch:word){
            try {
              if (flag){
                //有可能t1先进锁 t2还没有wait 所以t1的notify无效，
                // 如果第一次t2没人叫醒超时之后自动醒来执行后面的逻辑
                object.wait(1);
                flag=false;
              }else{
                object.wait();
              }
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
            System.out.print(ch);
            object.notify();
          }
          object.notify();
        }
      }
    });
    t1.start();
    t2.start();
  }
}
