package org.stjs.generator.writer.inlineObjects;


public class InlineObjects11c_InnerClass_hierarchy<K> {

    public static String main(String[] args) {
        InlineObjects11c_InnerClass_hierarchy inlineObjects11c_innerClass_hierarchy = new InlineObjects11c_InnerClass_hierarchy();
        return inlineObjects11c_innerClass_hierarchy.callInnerClass();
    }

    public String callInnerClass() {
        InnerClassB innerClass = new InnerClassB("InnerClass");
        return innerClass.doIt(this);
    }

    public String callbackFromInnerClass(String s) {
        return "Received call from: " + s;
   }

    private class InnerClassA {
        private final String id;

        public InnerClassA(String id) {
            this.id = id;
        }

        public String doIt(Object o) {
            return callbackFromInnerClass("A-" + id);
        }
    }

    private class InnerClassB extends InnerClassA {
        public InnerClassB(String id) {
            super("B-" + id);
        }
    }


}

