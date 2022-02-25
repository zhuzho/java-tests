package test.callbackwatch.testLock;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static org.apache.zookeeper.KeeperException.Code.NONODE;
/**
 *
 * <ul>
 * <li>step 1 :创建临时节点，设置StringCallback回调,阻塞业务线程
 *  <li>step 2 :创建节点成功，获取自己的节点，并且开始获取目录下的子节点，传入Children2Callback回调。
 *  <li>step 3 :子节点获取成功，开始排序，判断自己是否是最小的节点。
 *           <pre>
 *         |---3.1 是：加锁成功countDown(),设置业务线程唯一标识，后续可以重入，业务线程恢复
 *          |---3.2 否：开始监听前一节点的是否存在，设置状态回调，以及监听；
 *                  |3.2.1: 回调:
 *                          |3.2.1.1:前一节点存在,回调不管
 *                          |3.2.2.2: 状态回调判断出前一节点不存在，再次执行step3获取子节点数据
 *                |3.2.2: 监听到前一节点被删除，执行step3
 *                 <pre/>
 *  <li>step4:释放锁,删除节点
 *
 *<ul/>
 */
public class ZKLock  implements
        AsyncCallback.StringCallback,//创建临时有序节点回调
        AsyncCallback.Children2Callback,//获取子元素回调
        AsyncCallback.StatCallback,//检查前一节点释放存在回调
        Watcher //前一节点发生变化时，被删除回调
{

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

    /**step 1 创建临时节点，设置StringCallback回调*/
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
     *step 2 :创建节点成功，获取自己的节点，并且开始获取目录下的子节点，传入Children2Callback回调
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
