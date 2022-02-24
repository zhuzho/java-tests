package test.smvc;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
//        System.out.println( -1<<29|0 );
        ThreadPoolExecutor threadPoolExecutor
            = new ThreadPoolExecutor(
            3,
            5,
            5,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<Runnable>(5)
        );

        for (int i=0;i<10;i++){
            threadPoolExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(5000l);
                    } catch ( Exception e) {
                        e.printStackTrace();
                    }
                    System.out.println(" heihei:" +Thread.currentThread().getId());
                }
            });
        }
        threadPoolExecutor.shutdown();
    }
}
