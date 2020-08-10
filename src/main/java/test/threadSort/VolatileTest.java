package test.threadSort;

/**
 * 指令重排序
 * @author zhuzhong@yunsom.com
 * @date 2020-07-23 17:26
 * @description
 */
public class VolatileTest {
  static int  a   ,x  ;
  static int b  ,y  ;

  public static void main(String[] args) {
    for (int i=0;i<Integer.MAX_VALUE;i++){
      a = 0 ;x = 0;
      b = 0 ;y = 0;
      Thread t1 = new Thread(new Runnable() {
        @Override
        public void run() {
            a = 1;
            x = b ;
        }
      });
      Thread t2 = new Thread(new Runnable() {
        @Override
        public void run() {
            b  = 1;
            y  = a;
        }
      });

      try {
        t1.start();t2.start();t1.join();t2.join();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      if (y ==0&&x ==0){
        System.err.println("y==0 && xx ==0 when run "+i+" times");
        break;
      }
    }
  }
}
