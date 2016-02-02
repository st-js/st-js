package org.stjs.generator.writer.fields;

public class Fields34_field_initializer_class_hierarchy {

	public static class BaseClass {
		public String baseClassField = "baseClassField";
	}

	public static class SubClass extends BaseClass {
		public String subClassField = "subClassField";
	}

	public static String main(String[] args) {
		BaseClass baseClass = new BaseClass();
		SubClass subClass = new SubClass();
		return baseClass.baseClassField + "-" + subClass.baseClassField + "," + subClass.subClassField;
	}
}
