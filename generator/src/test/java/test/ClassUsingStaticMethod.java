package test;

import static test.ClassDefiningStaticMethod.doSth;

public class ClassUsingStaticMethod {

  static int classSth() {
    return test.ClassDefiningStaticMethod.doSth()+ClassDefiningStaticMethod.doSth()+doSth();
  }
}
