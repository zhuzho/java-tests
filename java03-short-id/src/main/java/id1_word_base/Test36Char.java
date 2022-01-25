package id1_word_base;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zhuzhong@yunsom.com
 * @date 2021-03-19 11:26
 * @description
 */
public class Test36Char {


  final byte indexSize = 7;
  //prod:0
  // dev:1
  // test:2
  //test2:3
  //p100:4
  final int global_temp_id = 1;

  final long max = 2176782336l;

  int delta = 10;

  final AtomicInteger counter = new AtomicInteger(0);

  public String getId() throws Exception {
    return calId(counter.incrementAndGet() + delta, indexSize);
  }

  private String calId(int atomicCounter, byte size) throws Exception {
    if (max <= atomicCounter) {
      throw new Exception("ID超过最大限制");
    }
    final String s = Integer.toUnsignedString(atomicCounter, 36);
    final String s1 = appendZero(s, size);
    return s1;
  }

  public static String appendZero(String s, int size) {
    if (s.length() >= size) {
      return s;
    }
    String s1 = s;
    while (s1.length() < size) {
      s1 = "0" + s1;
    }
    return s1;
  }

  private String getKey() {
    return "form:global:short_id";
  }

  public List<String> getId(int size) throws Exception {
    List<String> arr = new ArrayList<>();
    for (int i = 0; i < size; i++) {
      arr.add(calId(counter.incrementAndGet() + delta, indexSize));
    }
    return arr;
  }


}