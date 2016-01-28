package org.stjs.generator.lib.string;

import org.stjs.generator.utils.TestUtils;

import java.nio.charset.StandardCharsets;

public class String20_getBytesUtf8 {

    public static String main(String[] args) {
        String stringWithUnicodeChars = "éâç";

        byte[] bytes = stringWithUnicodeChars.getBytes(StandardCharsets.UTF_8);

        return TestUtils.byteArrayToString(bytes);
    }

}
