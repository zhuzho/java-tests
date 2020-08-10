package test.sort;

/**
 * @author zhuzhong@yunsom.com
 * @date 2020-08-10 9:27
 * @description
 */
public class Sort3_Insert extends InitArray {


  public static void main(String[] args) {
    System.out.println("------------------------------------------");
    insert();
  }

  static void insert(){
    for (int i = 0;i<arr.length-1;i++){
      for (int j=i+1;j>0;j--){
        if (arr[j]<arr[j-1]){
          exchange(j,j-1);
        }
      }
    }
    print(arr);
  }
}
