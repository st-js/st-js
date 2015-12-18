package org.stjs.generator.writer.methods;

public class Methods27_overrride_and_overload_in_subclass_no_conflict<E> {

    public static Object main(String[] args) {
        SubClassClass subClassClass = new SubClassClass();
        BaseClass subClassClassAsBaseClass = new SubClassClass();
        BaseClass baseClass = new SubClassClass();
        Interface1 subClassAsInterface = new SubClassClass();
        BaseClass realBaseClass = new BaseClass();

        return subClassClass.getMessage("a") + " - " +
                subClassClassAsBaseClass.getMessage() + " - " +
                baseClass.getMessage() + " - " +
                realBaseClass.getMessage() + " - " +
                subClassAsInterface.getMessage();
    }

    public static class BaseClass {
        public String getMessage() {
            return "Hello world!";
        }
    }

    public interface Interface1 {
        String getMessage();
        String getMessage(String userName);
    }

    public static class SubClassClass extends BaseClass implements Interface1 {
        public String getMessage() {
            return "Hello world from Mars!";
        }

        public String getMessage(String userName) {
            return "Hello " + userName + "!";
        }
    }

}
