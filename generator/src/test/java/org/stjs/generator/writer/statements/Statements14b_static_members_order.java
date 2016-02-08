package org.stjs.generator.writer.statements;

public class Statements14b_static_members_order {

    private static int valueA = 10;

    static {
        valueA = valueA + 100;
    }

    private static int valueB = valueA;

    static {
        valueB = valueB + 100;
    }

    public static String main(String[] args) {
        return "A:" + valueA + " B:" + valueB;
    }

}
