package org.stjs.generator.writer.inlineObjects;


public class InlineObjects12a_InnerClass_initializingFieldFromParent<K> {

    public static class AbstractClassLevel1 {
        protected int abstractClassLevel1Field;
    }

    public static class AbstractClassLevel2 extends AbstractClassLevel1 {
        protected int abstractClassLevel2Field;
    }


    private static class ConcreteClass extends AbstractClassLevel2 {
        protected int concreteClassField;
        private class InnerClass {
            private int abstractClassLevel1FieldCopy = abstractClassLevel1Field;
            private int abstractClassLevel2FieldCopy = abstractClassLevel2Field;
            private int concreteClassFieldCopy = concreteClassField;
        }
    }


}

