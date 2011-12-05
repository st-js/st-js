/**
 *  Copyright 2011 Alexandru Craciun, Eyal Kaspi
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.stjs.generator.scope.declarations;

import static org.stjs.generator.scope.declarations.Bean1.parentPrivate;
import static org.stjs.generator.scope.declarations.Bean1.stat;

@SuppressWarnings("unused")
public class Declaration1 extends ParentDeclaration1 {
	int param = 1;
	int var2 = 2;
	int type = 3;
	int out = 4;
	int imp = 5;
	int stat2 = 6;

	public void x(int param) {
		int var2 = 11;
		int exp1 = param + var2 + type + out + imp + stat + stat2;
		int exp2 = org.stjs.generator.scope.declarations.Bean1.full;
		new MyCallback() {
			int type = 1;

			@Override
			public void $invoke() {
				int exp3 = type + 1;
				int exp4 = this.type + 1;
			}
		};
		int exp6 = parentPrivate + parentProtected + parentPackage + parentPublic;
	}

}
