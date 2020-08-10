package test.sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * @author zhuzhong@yunsom.com
 * @date 2020-08-10 9:28
 * @description
 */
public class InitArray {
  static  final  int size = 21;
  static  Integer[] arr = {1032,132, 1804, 1387, 1999, 196, 1938, 1462, 42, 1227, 471, 21, 1545, 1913, 1953, 1409, 693, 203, 842, 1536, 649} ;

  static {
//    init();
    print(arr);
  }

  static void init(){
    Random random = new Random();
    List<Integer> init = new ArrayList<>();
    for (;init.size()<size;){
      int r = random.nextInt(50);
      if (!init.contains(r)){
        init.add(r);
      }
    }
    arr= init.toArray(new Integer[size]);
  }

  static void print(Integer[] arr){
    System.out.println(Arrays.toString(arr));
  }

  static void exchange(int i, int j){
    int tmp = arr[i];
    arr[i]  = arr[j];
    arr[j] = tmp;
  }

  static void print(int i){
    System.out.println("position:"+i+"，arr:"+Arrays.toString(arr));
  }
}
