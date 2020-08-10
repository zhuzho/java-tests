package test.sort;

/**
 * O(n^2)
 * @author zhuzhong@yunsom.com
 * @date 2020-08-10 9:27
 * @description
 */
public class Sort2_Bubble extends InitArray {


  public static void main(String[] args) {
    System.out.println("------------------------------------------");
    bubble();
  }

  static void bubble(){
    for (int i =0, k =0 ;i<arr.length-k;k++){
      for (int j=i+1;j<arr.length;j++){
        if (arr[j-1]>arr[j]){
          exchange(j-1,j);
        }
      }
    }
    print(arr);
  }
}
