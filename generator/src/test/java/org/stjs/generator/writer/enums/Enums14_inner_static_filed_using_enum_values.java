package org.stjs.generator.writer.enums;

public class Enums14_inner_static_filed_using_enum_values {

    public enum MyEnum {
        FIRST, SECOND, THIRD;
        public static MyEnum aValueFromMyEnum = values()[1];
    }

    public static String main(String[] args) {
        return MyEnum.aValueFromMyEnum.name();
    }

}
