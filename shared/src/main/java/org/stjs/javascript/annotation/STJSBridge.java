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
 * This annotation is used to mark types that part of a bridge library (like the jquery one). The source the javascript
 * implementation is found should be set. The annotation can also be set on the package containing the type or any other
 * parent package.
 * 
 * @author acraciun
 */
@Target({ ElementType.TYPE , ElementType.PACKAGE})
@Retention(RetentionPolicy.RUNTIME)
public @interface STJSBridge {//
	String[] sources() default "";
}
