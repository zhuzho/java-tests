package test.unsafe_test;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * @author farmer.zs@qq.com
 * @date 2020-08-11 16:01
 * @description
 */
public class TestUnsafe {

  public static void main(String[] args) throws NoSuchFieldException {
    Unsafe unsafe = null;
    try {
      Field field = Unsafe.class.getDeclaredField("theUnsafe");
      field.setAccessible(true);
      unsafe = (Unsafe) field.get(null);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      e.printStackTrace();
    }
    System.out.println(unsafe.addressSize());
    System.out.println(unsafe.pageSize());
    try {
      Object object = unsafe.allocateInstance(User.class);
      Field age = User.class.getDeclaredField("age");
      long offset = unsafe.objectFieldOffset(age);
      unsafe.compareAndSwapInt(object,offset,0,1);
      String[] strings = {"2","1","3"};
      long i = unsafe.arrayBaseOffset(String[].class);
      unsafe.putOrderedObject(object,i,strings);
      System.out.println(object);
    } catch (InstantiationException e) {
      e.printStackTrace();
    }
  }

  static class User{
    int age;

    String[] likes=new String[10];

    @Override
    public String toString() {
      return "User{" +
          "age=" + age +
          ", likes=" + Arrays.toString(likes) +
          '}';
    }
  }
}
