package org.stjs.generator.writer.fields;

public class Fields29_array_loop {
    private int[] anIntArray;

    public void arrayLoopSimpleArray() {
        anIntArray = new int[]{0, 2, 4, 6, 8, 10};
        for (int i = 0; i < anIntArray.length; i++) {
            anIntArray[i] = i;
        }
        int sum = 0;
        for (int element : anIntArray) {
            sum += element;
        }
    }
}
