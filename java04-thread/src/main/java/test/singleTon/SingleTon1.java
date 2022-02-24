package test.singleTon;

/**
 * 饿汉
 * @author farmer.zs@qq.com
 * @date 2020-07-28 9:59
 * @description
 */
public class SingleTon1 {

  private static final SingleTon1 instance = new SingleTon1();

  private SingleTon1(){

  }

  public static SingleTon1 getInstance() {
    return instance;
  }
}
