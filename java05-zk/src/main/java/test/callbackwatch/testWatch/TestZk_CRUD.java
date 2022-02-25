package test.callbackwatch.testWatch;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import test.callbackwatch.util.ZKUtil;
import java.util.concurrent.CountDownLatch;

public class TestZk_CRUD {

    public static void main(String[] args) throws InterruptedException, KeeperException {
        ZooKeeper zooKeeper = ZKUtil.getZooKeeper("testCRUD");
        String path = "/appConfig";
        zooKeeper.create(path,
                "oldData".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL);
        final Stat watchState = new Stat();
        byte[] i_get_data = zooKeeper.getData(path, new Watcher() {
            public void process(WatchedEvent event) {
                //发生事件
                System.out.printf(event.toString());
                try {
                    zooKeeper.getData(path,this,watchState);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, watchState);
        System.out.printf("i get data"+new String(i_get_data));
        //触发读数据事件的Watcher
        Stat updateStat = zooKeeper.setData(path, "newData".getBytes(), 0);
        Stat updateStat2 = zooKeeper.setData(path, "newData".getBytes(), updateStat.getVersion());
        System.out.printf(updateStat.toString());
        /* ------------------------------------async-------------------------------------------------*/
        Stat stat2 = zooKeeper.setData(path, "newData2".getBytes(), updateStat.getVersion());
        zooKeeper.getData(
                path, false, (rc, path1, ctx, data, stat) -> {
                    System.out.printf("async call back");
                    System.out.printf(new String(data));
                },"abc");

        Thread.sleep(10000);
    }
}
