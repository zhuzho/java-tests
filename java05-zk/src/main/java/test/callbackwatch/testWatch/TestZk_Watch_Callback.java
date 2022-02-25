package test.callbackwatch.testWatch;

import org.apache.zookeeper.ZooKeeper;
import test.callbackwatch.util.ZKUtil;

import java.util.concurrent.CountDownLatch;

public class TestZk_Watch_Callback {

    public static void main(String[] args) {
        TestZk_Watch_Callback watch_callback = new TestZk_Watch_Callback();
        watch_callback.testWatch();
    }
    public void  testWatch(){
        ZkWatchCallback zkWatchCallback = new ZkWatchCallback();
        String configPath = "testConfig";
        //
        ZooKeeper zooKeeper = ZKUtil.getZooKeeper(configPath);
        CountDownLatch cc = new CountDownLatch(1);
        ConfigResult configResult = new ConfigResult();
        zkWatchCallback.setCc(cc);
        zkWatchCallback.setConfigPath("/appConfig");
        zkWatchCallback.setZooKeeper(zooKeeper);
        zkWatchCallback.setConfigResult(configResult);
        zkWatchCallback.await();
        while (true){
            System.out.println(configResult.getResult());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
