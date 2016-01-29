package org.stjs.generator.lib.javaclass;

import org.stjs.javascript.annotation.Namespace;

@Namespace("stjs.myclass.namespace")
public class JavaClassTest4_Class_getName_withNameSpace {

    public static String main(String[] args) {
        return JavaClassTest4_Class_getName_withNameSpace.class.getName();
    }

}
