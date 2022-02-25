package test.callbackwatch.util;

import org.apache.zookeeper.ZooKeeper;
import test.callbackwatch.dw.DefaultWatcher;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class ZKUtil {

    public static volatile ZooKeeper zooKeeper;

    public final static Object lock = new Object();

    /**
     * 注意预先创建根目录
     * @param rootPath
     * @return
     */
    public static ZooKeeper getZooKeeper(String rootPath){
        if (zooKeeper == null){
            synchronized (lock){
                if (zooKeeper == null){
                    try {
                        CountDownLatch cc = new CountDownLatch(1);
                        zooKeeper = new ZooKeeper("localhost:2181/"+rootPath,3000,new DefaultWatcher(cc));
                        cc.await();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return zooKeeper;
    }
}
