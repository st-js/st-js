package org.stjs.generator.lib.string;

public class String15_valueOf {
    public static String main(String[] args) {
        String result = "";
        result = appendValue(result, "boolean", String.valueOf(true));
        result = appendValue(result, "int", String.valueOf(1));
        result = appendValue(result, "long", String.valueOf(1l));
        result = appendValue(result, "char", String.valueOf('a'));
        result = appendValue(result, "Object", String.valueOf("an object"));
        result = appendValue(result, "float", String.valueOf(1.2f));
        result = appendValue(result, "double", String.valueOf(1.3d));

        return result;
    }

    private static String appendValue(String original, String typeDescription, String stringValue) {
        String result = original;
        if (!result.isEmpty()) {
            result = result + "\n";
        }

        result = result + typeDescription + ": " + stringValue;
        return result;
    }
}
