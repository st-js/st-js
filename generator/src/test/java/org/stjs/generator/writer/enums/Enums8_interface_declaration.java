package org.stjs.generator.writer.enums;

public interface Enums8_interface_declaration {
	SimpleEnum methodReturningEnum();

	class InnerClass implements Enums8_interface_declaration {
		@Override
		public SimpleEnum methodReturningEnum() {
			return SimpleEnum.FIRST;
		}
	}
}
