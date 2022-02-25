package test.callbackwatch.testWatch;

import lombok.Getter;
import lombok.Setter;
import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.CountDownLatch;

@Setter
@Getter
public class ZkWatchCallback implements AsyncCallback.DataCallback , Watcher , AsyncCallback.StatCallback {

    CountDownLatch cc;

    ConfigResult configResult;

    ZooKeeper zooKeeper;

    String configPath;


    public void await(){
        zooKeeper.exists(configPath,this,this,"abc");
        try {
            //加个门把手 阻止主线程直接跳过
            cc.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /*** getData 回调  */
    public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
        if (data != null){
            configResult.setResult(new String(data));
            cc.countDown();
        }
    }

    /**stat watch*/
    public void process(WatchedEvent event) {
        switch (event.getType()) {
            case None:
                break;
            case NodeCreated:
                //节点不存在，如果有人此时创建了节点
                zooKeeper.getData(configPath,this,this,"sdf");
                break;
            case NodeDeleted:
                //
                cc= new CountDownLatch(1);
                await();
                break;
            case NodeDataChanged:
                //监听到数据发生变化
                zooKeeper.getData(configPath,this,this,"sdf");
                break;
            case NodeChildrenChanged:
                break;
        }
    }

    /***exists  stat calll back   */
    public void processResult(int rc, String path, Object ctx, Stat stat) {
        if (stat != null){
            zooKeeper.getData(configPath,this,this,"sdf");
        }
    }
}
