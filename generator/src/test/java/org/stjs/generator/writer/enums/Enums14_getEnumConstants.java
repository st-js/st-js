package org.stjs.generator.writer.enums;

public class Enums14_getEnumConstants {
	public enum SampleEnum {
		FIRST, SECOND, THIRD
	}

	public static String main(String[] args) {
		SampleEnum[] allEnums = SampleEnum.class.getEnumConstants();

		String result = "";
		for (SampleEnum anEnumValue : allEnums) {
			result = result + anEnumValue + ",";
		}
		return result;
	}
}
