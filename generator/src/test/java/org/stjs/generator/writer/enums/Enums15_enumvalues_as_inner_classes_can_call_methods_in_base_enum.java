package org.stjs.generator.writer.enums;

public class Enums15_enumvalues_as_inner_classes_can_call_methods_in_base_enum {

	public enum MyEnum {
		VALUE_AS_INNER_CLASS{};

		public String methodDefinedAtEnumLevel(String s) {
			return s.toUpperCase();
		}
	}

	public static String main(String[] args) {
		return MyEnum.VALUE_AS_INNER_CLASS.methodDefinedAtEnumLevel("abc");
	}

}
