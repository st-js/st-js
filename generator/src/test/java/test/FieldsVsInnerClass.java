package test;

public class FieldsVsInnerClass {

  FieldsVsInnerClass MyInnerClass;

  public static void main(String[] args) {
    new FieldsVsInnerClass().doSth();
  }
  
  public void doSth() {
    MyInnerClass = new FieldsVsInnerClass();
    MyInnerClass.print();
    MyInnerClass2.print();
  }
  
  private void print() {
    System.out.println("instance");
  }

  static class MyInnerClass {
  
     static void print() {
       System.out.println("static");
     }
  }
  
  static class MyInnerClass2 {
    
    static void print() {
      System.out.println("static");
    }
 }
}
