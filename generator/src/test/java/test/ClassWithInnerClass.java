package test;

public class ClassWithInnerClass {

  static int X = 1;

  
  static class InnerClass{
    static int Y = 2;
    
    int instanceMethod() {
      return X+Y;
    }
    
    static int staticMethod() {
      return X+Y;
    }
  }
}
