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
 * This annotates <i>adapter</i> classes. These are classes that are used to supply methods for Java types when they
 * don't have a method that their Javascript counterpart has. For example for Number in Javascript you can do
 * number.toFixed(2). As in Java this method does not exist and as the Java Number-derived classes are all final, the
 * only alternative is to put this method in another class - an adapter class. All the methods of an adapter class must
 * have their first parameter the object to which the method is applied. The other parameters are the parameters
 * normally supplied to the Javascript method. For the number.toFixed example, the adapter will have a method
 * NumberAdapter.toFixed(String number, int position). The generated javascript code is the expected one:
 * number.toFixer(position).
 * 
 * Note: The adapter's method must be all static.
 * 
 * @author acraciun
 */
@Target({ ElementType.TYPE, ElementType.PACKAGE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Adapter {

}
