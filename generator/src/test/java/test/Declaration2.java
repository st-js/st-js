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
package test;

public class Declaration2 {
	int x = 0;

	B instanceB = null;
	{
		instanceB.y = 2;
	}

	class B {

		int y;

		void m() {
			x = y + x + Declaration2.this.z();
			x = this.z() + w();
		}

		int z() {
			return 3;
		}
	}

	int z() {
		return 2;
	}

	static int w() {
		return 5;
	}
}
