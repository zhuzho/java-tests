package test.singleTon;

/**
 * @author zhuzhong@yunsom.com
 * @date 2020-07-28 10:01
 * @description
 */
public class SingleTon2 {
  int i = 10;

  //volatile 防止指令重排序 ，在new SingleTon2的时候有个半初始化状态，i=0;
  // 半初始化状态对象的变量还是默认值没有进行赋值
  private static volatile SingleTon2 instance;

  private SingleTon2 (){

  }

  //DCL double check lock
  public static SingleTon2 getInstance() {
    if (instance != null){
      return instance;
    }
    synchronized (SingleTon2.class){
      if (instance != null){
        return instance;
      }
      instance = new SingleTon2();
    }
    return instance;
  }
}
