package org.stjs.generator.exec.ints;

import static org.stjs.javascript.JSObjectAdapter.$js;

public class CharAppendToString {
    public static final char CYRILLIC_IA = 'я';

    public static String method(String hello, char a) {
        hello += func(a);
        return hello;
    }

    private static char func(char a) {
        return a;
    }

    public static String main(String[] args) {
        String result = CharAppendToString.method("hello", CYRILLIC_IA);
        $js("console.log(result)");
        return result;
    }
}
