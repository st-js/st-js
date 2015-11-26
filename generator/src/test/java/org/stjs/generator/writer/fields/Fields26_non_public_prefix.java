package org.stjs.generator.writer.fields;

public class Fields26_non_public_prefix {
    String packageField;
    private String privateField;
    public String publicField;

    public String getThisPackageField() {
        Fields26_non_public_prefix myFields25nonpublicprefix = new Fields26_non_public_prefix();
        myFields25nonpublicprefix.packageField = "test";

        return this.packageField;
    }

    public String getThisPrivateField() {
        return this.privateField;
    }

    public String getThisPublicField() {
        return this.publicField;
    }

    public String getPackageField() {
        return packageField;
    }

    public String getPrivateField() {
        return privateField;
    }

    public String getPublicField() {
        return publicField;
    }

    private static class InnerClass {
        String innerPackageField;
        private String innerPrivateField;
        public String innerPublicField;
        private Fields26_non_public_prefix parent;

        public InnerClass(Fields26_non_public_prefix parent) {
            this.parent = parent;
        }

        public String getThisPackageField() {
            Fields26_non_public_prefix myFields25nonpublicprefix = new Fields26_non_public_prefix();
            myFields25nonpublicprefix.packageField = "test";

            return this.innerPackageField;
        }

        public String getThisPrivateField() {
            return this.innerPrivateField;
        }

        public String getThisPublicField() {
            return this.innerPublicField;
        }

        public String getPackageField() {
            return parent.packageField;
        }

        public String getPrivateField() {
            return parent.privateField;
        }

        public String getPublicField() {
            return parent.publicField;
        }
    }
}
