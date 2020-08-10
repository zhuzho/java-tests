package test.sort;

/**
 * time:O(n^2), space:1 ,稳定
 * 每次排序
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
      bk:
      for (int j=i+1;j>0;j--){
        if (arr[j]<arr[j-1]){
          exchange(j,j-1);
        }else{
          break bk;
        }
      }
    }
    print(arr);
  }
}
