package test;


@SuppressWarnings("unused")
public class DeclarationWithOuter1 extends ParentDeclaration1 {
	int type = 3;
	int out = 4;

	public void x(int param) {
		new Runnable() {
			int type = 1;

			@Override
			public void run() {
				int exp5 = out + DeclarationWithOuter1.this.type + 1;
			}
		};
	}

}
