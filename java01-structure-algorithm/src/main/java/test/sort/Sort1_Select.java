package test.sort;

/**
 * 时间复杂度O(n^2) 不稳定(相同的值有可能会被调换顺序)
 * @author zhuzhong@yunsom.com
 * @date 2020-08-10 9:27
 * @description
 */
public class Sort1_Select extends InitArray {


  public static void main(String[] args) {
//    selectV1();
    System.out.println("------------------------------------------");
    selectV2();
  }

  static void selectV1(){
    int minPos  ;
    for (int i=0;i<arr.length;i++){
      minPos = i;
      for (int j=i+1;j<arr.length;j++){
        if (arr[minPos]>arr[j]){
          minPos =j;
        }
      }
      exchange(i,minPos);
    }
    print(arr);
  }

  static void selectV2(){
    int maxPos ;
    int minPos ;
    for (int i=0;i<arr.length/2;i++){
      maxPos = i;
      minPos = i;
      for (int j = i+1;j<arr.length-i;j++){
        if (arr[minPos]>arr[j]){
          minPos = j;
        }
        if (arr[maxPos]<arr[j]){
          maxPos = j ;
        }
      }
      int maxExchangePos = arr.length-1-i;
      if (maxPos==i){
        exchange(maxPos,maxExchangePos);
        exchange(minPos,i);
      } else {
        exchange(minPos,i);
        exchange(maxPos,maxExchangePos);
      }
      print(arr);
    }
  }
}
