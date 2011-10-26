/**
 *  Copyright 2011 Alexandru Craciun, Eyal Kaspi
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"){throw new UnsupportedOperationException();}
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
package org.stjs.javascript.dom;

public class Table extends Element {
	public HTMLCollection<TableSection> TBodies;
	public TableSection TFoot;
	public TableSection THead;
	public String align;
	public String bgColor;
	public String border;
	public TableCaption caption;
	public String cellPadding;
	public String cellSpacing;
	public String frame;
	public HTMLCollection<TableRow> rows;
	public String rules;
	public String summary;
	public String width;

	public Element createCaption() {
		throw new UnsupportedOperationException();
	}

	public Element createTFoot() {
		throw new UnsupportedOperationException();
	}

	public Element createTHead() {
		throw new UnsupportedOperationException();
	}

	public void deleteCaption() {
		throw new UnsupportedOperationException();
	}

	public void deleteRow(int arg0) {
		throw new UnsupportedOperationException();
	}

	public void deleteTFoot() {
		throw new UnsupportedOperationException();
	}

	public void deleteTHead() {
		throw new UnsupportedOperationException();
	}

	public Element insertRow(int arg0) {
		throw new UnsupportedOperationException();
	}
}
