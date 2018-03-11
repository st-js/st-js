package org.stjs.generator.exec.ints;

public class CharPlusString {
    public static final char CYRILLIC_IA = 'я';

    public static String method(String hello, char a) {
        return hello + a;
    }

    public static String main(String[] args) {
        return CharPlusString.method("hello", CYRILLIC_IA);
    }
}
