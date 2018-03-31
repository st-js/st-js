package org.stjs.generator.exec.ints;

import static org.stjs.javascript.JSObjectAdapter.$js;

public class CharPlusString {
    public static final char CYRILLIC_IA = 'я';

    public static String method(String hello, char a) {
        return hello + a;
    }

    public static String main(String[] args) {
        String result = CharPlusString.method("hello", CYRILLIC_IA);
        $js("console.log(result)");
        return result;
    }
}
