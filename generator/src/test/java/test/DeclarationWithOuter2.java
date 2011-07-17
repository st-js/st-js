package test;

import static test.Bean1.parentPrivate;

@SuppressWarnings("unused")
public class DeclarationWithOuter2 extends ParentDeclaration1 {
	public void x(int param) {
		new Runnable() {
			@Override
			public void run() {
				int exp6 = parentPrivate + parentProtected + parentPackage + parentPublic;
			}
		};
	}

}
