package test.callbackwatch.testLock;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static org.apache.zookeeper.KeeperException.Code.NONODE;

public class ZKLock  implements
        AsyncCallback.StringCallback,
        AsyncCallback.Children2Callback,
        AsyncCallback.StatCallback,
        Watcher {

    final ZooKeeper zk;
    final String threadName;
    final String lockPrefix;
    String lockId;
    CountDownLatch cc = new CountDownLatch(1);
    public ZKLock(ZooKeeper zk,String lockPrefix,String threadName) {
        this.zk = zk;
        this.lockPrefix = lockPrefix;
        this.threadName = threadName;
    }



    public void tryLock(){
        zk.create(lockPrefix,threadName.getBytes()
                , ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL_SEQUENTIAL,this,"");
        try {
            cc.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void unlock(){
        try {
            System.out.println(threadName+"worker over....."+lockId);
            zk.delete(lockId,-1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }
    /**
     *create AsyncCallback.StringCallback
     * */
    public void processResult(int rc, String path, Object ctx, String name) {
        if (name!=null){
            zk.getChildren("/",false,this,"");
            lockId = name;
        }
    }

    /**
     * get child call back
     * @param rc
     * @param path
     * @param ctx
     * @param children
     * @param stat
     */
    public void processResult(int rc, String path, Object ctx, List<String> children, Stat stat) {
        Collections.sort(children);
        int myIndex = children.indexOf(lockId.substring(1));
        if (myIndex ==0 ){
            System.out.println(threadName+" begin work....."+lockId);
            try {
                zk.setData("/",threadName.getBytes(),stat.getVersion());
            } catch ( Exception e) {
                e.printStackTrace();
            }
            cc.countDown();
            return;
        }
        String preLock = children.get(myIndex-1);
        zk.exists("/"+preLock,this,this,preLock);
    }


    public void process(WatchedEvent event) {
        switch (event.getType()) {
            case None:
                break;
            case NodeCreated:
                break;
            case NodeDeleted:
                zk.getChildren("/",false,this,"");
                break;
            case NodeDataChanged:
                break;
            case NodeChildrenChanged:
                break;
        }
    }

    /**
     * exists stat callback
     * @param rc
     * @param path
     * @param ctx
     * @param stat
     */
    public void processResult(int rc, String path, Object ctx, Stat stat) {
        if (rc == NONODE.intValue()){
            System.out.println("========================prefix lock"+ctx+" not exists -========================");
            zk.getChildren("/",false,this,ctx);
        }
    }
}
