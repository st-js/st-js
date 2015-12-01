package org.stjs.generator.writer.fields;

public class Fields31_create_fixed_array_size {

    private String[] aStringArray;
    private String[][] aDoubleStringArray;

    public Fields31_create_fixed_array_size() {
        aStringArray = new String[10];
        aStringArray = new String[this.getSize2()];

        aDoubleStringArray = new String[200][300];
        aDoubleStringArray = new String[this.getSize2()][300];
        aDoubleStringArray = new String[this.getSize2()][this.getSize3()];
    }

    private static int getSize2() {
        return 5;
    }

    private static int getSize3() {
        return 3;
    }

}
