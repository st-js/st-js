package org.stjs.generator.writer.fields;

public class Fields30_two_dimens_array_loop {
    private int[][] anIntTwoDimensArray;

    public void arrayLoopTwoDimenseArray() {
        anIntTwoDimensArray = new int[][]{{0, 2}, {4, 6}, {8, 10}};
        for (int i = 0; i < anIntTwoDimensArray.length; i++) {
            anIntTwoDimensArray[i][0] = i;
        }
        int sum = 0;
        for (int[] element : anIntTwoDimensArray) {
            sum += element[0];
        }
    }
}
