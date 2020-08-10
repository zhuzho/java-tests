package test.sort;

/**
 * time:O(n^1.3),n*log2n  space:1 ,不稳
 * 每次排序
 * @author zhuzhong@yunsom.com
 * @date 2020-08-10 9:27
 * @description
 */
public class Sort7_Shell extends InitArray {


  public static void main(String[] args) {
    System.out.println("------------------------------------------");
    int gap = 4;
    int h=1;
    while (h<h*3+1){
      h=h*3+1;//knuth序列
    }
    gap = (h-1)/3;
    shell(gap);
  }

  static void shell(int gap ){
    for (int g=gap;g>0;g--){
      for (int i = 0;i<arr.length;i+=g){
        bk:
        for (int k=i+g;k>0 && k<arr.length;k-=g){
          if (arr[k-g]>arr[k]){
            exchange(k,k-g);
          }else {
            break bk;
          }
        }
      }
    }
    print(arr);
  }
}
