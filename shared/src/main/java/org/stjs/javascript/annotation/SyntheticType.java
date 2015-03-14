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
 * There are in this library classes that do not correspond to real types in the javascript library counterpart. So the
 * constructor of these types should be replaced with a anonymous object constructor in javascript: {}. If annotated
 * type is an interface, when the Javascript code is generated for a class implementing this interface, the interface
 * name does not appear in the "extends" part.
 * 
 * @author acraciun
 */
@Target({ ElementType.TYPE, ElementType.PACKAGE })
@Retention(RetentionPolicy.RUNTIME)
public @interface SyntheticType {//

}
