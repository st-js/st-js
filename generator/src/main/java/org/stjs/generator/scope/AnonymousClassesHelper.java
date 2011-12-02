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
package org.stjs.generator.scope;

import japa.parser.ast.Node;
import japa.parser.ast.body.ConstructorDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.InitializerDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.expr.ObjectCreationExpr;

import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Pattern;

import org.apache.bcel.Repository;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.ClassGen;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.MethodGen;
import org.apache.bcel.generic.NEW;
import org.stjs.generator.ast.ASTNodeData;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

/**
 * As the anonymous class names using numbering seem not to be the same between compilers, this class identifies the
 * anonymous class using their placement (either directly in the class for instance and static members initialization,
 * or inside methods) and the signature composed of fields (and their type) and methods (and their signature) There is a
 * strange order number given to inline types in the same statement. the numbers are assigned backwards. it looks like
 * the Eclipse incremental builder assigns the numbers in the correct order, but the command line compiler assigns them
 * the other way around
 * 
 * @author acraciun
 * 
 */
public class AnonymousClassesHelper {
	private final static String STATIC_INIT_METHOD = "<clinit>";
	private final static String CONSTRUCTOR_METHOD = "<init>";
	private final static Pattern ANONYMOUS_CLASS_PATTERN = Pattern.compile("\\$\\d+$");
	private final Multimap<String, String> classesByMethod = ArrayListMultimap.create();

	@SuppressWarnings("unchecked")
	public AnonymousClassesHelper(Class<?> ownerClass) {
		JavaClass clazz;
		try {
			// XXX: should put this somewhere else
			Repository.clearCache();
			clazz = Repository.lookupClass(ownerClass);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Cannot load BCEL wrapper for the class:" + ownerClass);
		}
		ClassGen g = new ClassGen(clazz);
		for (Method m : clazz.getMethods()) {
			MethodGen mg = new MethodGen(m, g.getClassName(), g.getConstantPool());
			String methodName = mg.getName();

			if (mg.getInstructionList() == null) {
				continue;
			}
			Iterator<InstructionHandle> it = mg.getInstructionList().iterator();
			while (it.hasNext()) {
				InstructionHandle h = it.next();
				if (h.getInstruction() instanceof NEW) {
					NEW newInstr = (NEW) h.getInstruction();
					String typeName = newInstr.getType(g.getConstantPool()).toString();
					if (isAnonymousClass(typeName)) {
						classesByMethod.put(methodName, typeName);
					}
				}
			}
		}

	}

	private boolean isAnonymousClass(String typeName) {
		return ANONYMOUS_CLASS_PATTERN.matcher(typeName).find();
	}

	private String getMethodName(ObjectCreationExpr node) {
		for (Node parent = ASTNodeData.parent(node); parent != null; parent = ASTNodeData.parent(parent)) {
			if (parent instanceof MethodDeclaration) {
				return ((MethodDeclaration) parent).getName();
			}
			if (parent instanceof ConstructorDeclaration) {
				return CONSTRUCTOR_METHOD;
			}
			if (parent instanceof FieldDeclaration) {
				FieldDeclaration field = (FieldDeclaration) parent;
				return Modifier.isStatic(field.getModifiers()) ? STATIC_INIT_METHOD : CONSTRUCTOR_METHOD;
			}
			if (parent instanceof InitializerDeclaration) {
				InitializerDeclaration init = (InitializerDeclaration) parent;
				return init.isStatic() ? STATIC_INIT_METHOD : CONSTRUCTOR_METHOD;
			}

		}
		return STATIC_INIT_METHOD;
	}

	/**
	 * 
	 * @param node
	 * @param scopeBuilder
	 * @param scope
	 * @return null if a matching anonymous class was not found. This usually means a bug in the search algorithm as all
	 *         the node should find their corresponding class.
	 */
	public String findAnonymousClass(ObjectCreationExpr node, Scope scope, ScopeBuilder scopeBuilder) {
		// FIXME field initializers are considered part of the init constructor -> they should be resolved before!
		// for the moment static blocks are forbidden but they alter the order of nodes
		String method = getMethodName(node);
		Collection<String> classes = classesByMethod.get(method);
		if ((classes == null) || classes.isEmpty()) {
			return null;
		}
		// remove first because the calls will come ordered
		Iterator<String> it = classes.iterator();
		String result = it.next();
		it.remove();
		return result;
	}

}
