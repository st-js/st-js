package org.stjs.generator.writer.fields;

import java.util.Collection;

public class Fields28_array {
    private boolean[] aBooleanArray;
    private String[] aStringArray;
    private int[][] anIntTwoDimensArray;
    private char aCharArray[];
    private Object[] anObjectArray;
    private float[] aFloatArray;
    private Collection<?>[] aCollectionArray;
    private SimpleInterface[] anInterfaceArray;
    private int[][][] anIntThreeDimensArrayInitialized;

    public Fields28_array() {
        this.aBooleanArray = new boolean[]{true, false};
        this.aStringArray = new String[]{"a", "b", "c", "d", "e"};
        this.anIntTwoDimensArray = new int[][]{{0, 1}, {2, 3}};
        this.aCharArray = new char[]{ 'n', 'o', 't', ' ', 'a', ' ', 'S', 't', 'r', 'i', 'n', 'g' };
        this.anObjectArray = new Object[]{new SimpleObject(this.anIntTwoDimensArray), new SimpleObject(new int[][]{{0}})};
        this.aFloatArray = new float[]{2f, 3.6969f};
        this.aCollectionArray = new Collection[]{};
        this.anInterfaceArray = new SimpleInterface[]{new SimpleInterface() {
            @Override
            public void doNothing() {
                // Thanks!
            }
        }};
        this.anIntThreeDimensArrayInitialized = new int[][][]{{{0}}};
    }

    private class SimpleObject {
        private int[][] anIntTwoDimensArray;

        public SimpleObject(int[][] anIntTwoDimensArray) {
            this.anIntTwoDimensArray = anIntTwoDimensArray;
        }
    }

    private interface SimpleInterface {
        void doNothing();
    }
}
