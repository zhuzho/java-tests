package id1_word_base;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zhuzhong@yunsom.com
 * @date 2021-03-19 11:26
 * @description
 */
public class Test100Char {
  final static Object lock = new Object();
  final  static int  poolSize = 62;
  volatile static int j=0;
  volatile static char[] chars ;
  private static void append(int start,int end){
    for (int i=start;i<=end;i++){
      chars[j] = (char) i;
      j ++;
    }
  }
  private static volatile List<AtomicInteger> indexes = new ArrayList<>(10);

  public static char[] init(int size){
    if (chars == null){
      synchronized (lock){
        if (chars == null){
          chars = new char[poolSize];
          append(48,57);
          int h_start = 97;
          int h_end = 97+25;
          int i_start = 65;
          int i_end = 65+25;
          append(h_start,h_end);
          append(i_start,i_end);
          initInitIndexes(size);
        }
      }
    }
    return chars;
  }


  private static void increment(int size,int idx){
    synchronized (lock){
      final AtomicInteger integer = indexes.get(idx);
      if (integer.get() >=poolSize-1){
        integer.set(0);
        increment(size,++idx);
      }else{
        integer.incrementAndGet();
        return;
      }
    }
  }

  private static void initInitIndexes(int size){
    for (int i =0;i<size;i++){
      indexes.add(new AtomicInteger(0));
    }
  }


  public static String getId(Integer size) {
    char[] charArr = new char[size];
    synchronized (lock){
      for (int idx=0;idx<size;idx++){
        charArr[size-1-idx] = chars[indexes.get(idx).get()];
      }
      increment(size,0);
    }
    return new String(charArr);
  }

  public static void main(String[] args) {
     init(7);
    final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
        10, Integer.MAX_VALUE,
        60L, TimeUnit.SECONDS,
        new ArrayBlockingQueue<>(10));
    List<Future> futures = new ArrayList<>();
    for (int i=0;i<10;i++){
      futures.add(threadPoolExecutor.submit(() -> task(1000000))) ;
    }
    for (Future future:futures){
      try {
        future.get();
      } catch (InterruptedException e) {
        e.printStackTrace();
      } catch (ExecutionException e) {
        e.printStackTrace();
      }
    }
    threadPoolExecutor.shutdown();
    for (AtomicInteger integer:indexes){
      System.out.println(":::"+integer.get());
    }
  }


  public static void task(int size){
    long begin  = System.currentTimeMillis();
    for (int lg=0;lg<size;lg++){
      getId(7);
    }
    long end  = System.currentTimeMillis();
    System.out.println("["+Thread.currentThread().getName()+"] get ["+ size+"] ids cost:"+(end - begin));
  }
}
