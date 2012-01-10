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
package org.stjs.generator.name;

import org.stjs.generator.scope.VariableWithScope;
import org.stjs.generator.type.ClassWrapper;
import org.stjs.generator.type.MethodWrapper;
import org.stjs.generator.type.TypeWrapper;
import org.stjs.generator.utils.ClassUtils;

/**
 * this class provides the names directly without any transformation
 * 
 * @author acraciun
 * 
 */
public class DefaultNameProvider implements NameProvider {

	public String getTypeName(TypeWrapper typeWrapper) {
		if (typeWrapper instanceof ClassWrapper) {
			String namespace = ClassUtils.getNamespace(typeWrapper);
			String prefix = namespace != null ? namespace + "." : "";
			return prefix + ((ClassWrapper) typeWrapper).getExternalName();
		}
		// XXX: should this appear?!
		return typeWrapper.getType().toString();
	}

	@Override
	public String getVariableName(VariableWithScope var) {
		return var.getVariable().getName();
	}

	@Override
	public String getMethodName(MethodWrapper method) {
		return method.getName();
	}

}
