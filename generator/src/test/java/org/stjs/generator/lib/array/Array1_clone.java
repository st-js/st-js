package org.stjs.generator.lib.array;

public class Array1_clone {

    public static String main(String[] args) {
        String[] sourceArray = new String[]{"A", "B", "C"};
        String[] clonedArray = sourceArray.clone();

        clonedArray[0] = "AA";
        clonedArray[1] = "BB";
        clonedArray[2] = "CC";

        // Source array still intact
        return sourceArray.toString() + " - " + clonedArray.toString();
    }
}
