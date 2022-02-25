package test.callbackwatch.testLock;

import org.apache.zookeeper.ZooKeeper;
import test.callbackwatch.util.ZKUtil;

public class TestLock {
    public static void main(String[] args) {

        for (int i = 0; i <10 ; i++) {
            new Thread(() -> {
                String name = Thread.currentThread().getName();
                ZooKeeper zooKeeper = ZKUtil.getZooKeeper("testLock");
                ZKLock zkLock = new ZKLock(zooKeeper,"/lock",name);
                zkLock.tryLock();
                System.out.println(name+" do some thing~");
                zkLock.unlock();;
            }).start();
        }
        while (true){

        }
    }
}
