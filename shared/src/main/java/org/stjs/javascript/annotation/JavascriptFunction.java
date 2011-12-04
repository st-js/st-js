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
package org.stjs.javascript.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation annotates interfaces that are used to simulate Javascript functions. When implementing inline one of
 * those interfaces, the generated Javascript code will be an anonymous function. Consequently whenever in the code the
 * unique method of this interface is called, it will be generated a direct call to the function. Example:<br>
 * 
 * <pre>
 * doSomething(new Function1&lt;P, R&gt;() {
 * 	public R $invoke(P param) {
 * 		return null;
 * 	}
 * });
 * </pre>
 * 
 * will generate:<br>
 * 
 * <pre>
 *  doSomething(function(param) {
 * 	});
 * </pre>
 * 
 * And <br>
 * 
 * <pre>
 * Function1&lt;String,Integer&gt; f = ...;
 * Integer x = f.$invoke("test");
 * </pre>
 * 
 * will generate:<br>
 * 
 * <pre>
 * var f = ...;
 * var x = f("test");
 * </pre>
 * 
 * 
 * @author acraciun
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface JavascriptFunction {

}
