package org.stjs.generator.writer.inlineObjects;


public class InlineObjects11b_InnerClass_calling_outer<K> {

    public boolean callInnerClass() {
        InnerClass innerClass = new InnerClass();
        return innerClass.doIt(this);
    }

    public boolean callbackFromInnerClass(Object key) {
        return key == this;
   }

    private class InnerClass {
        public boolean doIt(Object o) {
            return callbackFromInnerClass(o);
        }
    }

    public static boolean main(String[] args) {
        InlineObjects11b_InnerClass_calling_outer inlineObjects11b_innerClass_calling_outer = new InlineObjects11b_InnerClass_calling_outer();
        return inlineObjects11b_innerClass_calling_outer.callInnerClass();
    }

}

