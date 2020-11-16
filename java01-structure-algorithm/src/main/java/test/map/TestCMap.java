package test.map;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhuzhong@yunsom.com
 * @date 2020-09-14 11:24
 * @description
 */
public class TestCMap {

  public static void main(String[] args) {
    Map map = new ConcurrentHashMap<>();
    map.put("abc","abc");
    map.put("abc","abcd");
    long i=-100;
    System.out.println(i<<2);
    System.out.println(i>>2);
    System.out.println(i>>>2);
    System.out.println(Long.MIN_VALUE);
  }
}
