package org.stjs.generator.writer.fields;

public class Fields32_array2Dimensions {

    public static Object main(String[] args) {
        String[][] strings = new String[getDimension1()][3];
        return arrayToString(strings);
    }

    private static String arrayToString(String[][] strings) {
        String result = "";
        for (String[] subArray : strings) {
            result = result + "[";

            int i = 0;
            for (String s : subArray) {
                if (i > 0) {
                    result = result + ",";
                }
                result = result + s;
                i++;
            }

            result = result + "]";
        }

        return result;
    }

    private static int getDimension1() {
        return 2;
    }

}
