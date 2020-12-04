package test.id;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author farmer.zs@qq.com
 * @date 2020-11-16 14:28
 * @description
 */
public class ShortIdCreator {

  static char[] chars= {
      'v', 'w', 'x', 'y', 'z',
      '4','5','6',
      'h', 'i', 'j', 'k', 'l', 'm', 'n'
      ,'0', '1','2','3',
      'a', 'b', 'c', 'd', 'e', 'f', 'g',
      'o', 'p', 'q', 'r', 's', 't', 'u'
      ,'7','8','9'};
  /**
   * 可以用redis替代
   */
  static AtomicInteger index[] ={
      new AtomicInteger(0)
      ,new AtomicInteger(0)
      ,new AtomicInteger(0)
      ,new AtomicInteger(0)};

  /**
   * 多实例的时候，加个hash(机器主机+端口)
   * @return
   */
  public static String getId(){
    char[] ch = new char[index.length];
    for (int idx=0;idx<index.length;idx++){
      ch[idx] = chars[index[idx].get()];
    }
    increment(index.length-1);
    return new String(ch);
  }

  static void increment(int idx){
    if (index[idx].get()>=chars.length-1){
      index[idx].set(0);
      increment(idx-1);
    } else {
      index[idx].incrementAndGet();
    }
  }

  public static void main(String[] args) {
    for (int i =0;i<1000;i++){
      System.out.println("第["+(i+1)+"个id= "+getId());
    }
  }
}
