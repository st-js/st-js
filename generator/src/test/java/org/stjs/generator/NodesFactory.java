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
package org.stjs.generator;

import static org.stjs.generator.handlers.utils.PreConditions.checkState;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.VariableDeclarator;
import japa.parser.ast.body.VariableDeclaratorId;
import japa.parser.ast.expr.Expression;
import japa.parser.ast.expr.NameExpr;
import japa.parser.ast.type.Type;
import japa.parser.ast.visitor.GenericVisitor;
import japa.parser.ast.visitor.VoidVisitor;
import org.stjs.generator.handlers.RuleBasedVisitor;
import org.stjs.generator.handlers.utils.Lists;

public class NodesFactory {

	public static PartialClassOrInterfaceDeclaration newClassOrIntefarceDeclaration() {
		return new PartialClassOrInterfaceDeclaration();
	}

	
	public static class PartialClassOrInterfaceDeclaration implements PartialObjectBuilder<ClassOrInterfaceDeclaration> {
		
		private final ClassOrInterfaceDeclaration declaration = new ClassOrInterfaceDeclaration();
		
		private PartialClassOrInterfaceDeclaration() {}
		
		public PartialClassOrInterfaceDeclaration withName(String name) {
			declaration.setName(name);
			return this;
		}
		 
		public PartialClassOrInterfaceDeclaration addBodyDeclaration(BodyDeclaration bodyDeclaration) {
			if (declaration.getMembers() == null) {
				declaration.setMembers(Lists.<BodyDeclaration>newArrayList());
			}
			declaration.getMembers().add(bodyDeclaration);
			return this;
		}
		
		@Override
		public ClassOrInterfaceDeclaration build() {
			return declaration;
		}
		
	}
	
	public static PartialFieldDeclaration newFieldDeclaration() {
		return new PartialFieldDeclaration();
	}
	
	public static NameExpr nameExpr(String name) {
	  return new NameExpr(name);
	}
	
	public static class PartialFieldDeclaration implements PartialObjectBuilder<FieldDeclaration> {
		
		private final FieldDeclaration field;
		
		private PartialFieldDeclaration() {
			field = new FieldDeclaration();
			field.setVariables(Lists.<VariableDeclarator>newArrayList());
		}

		public PartialFieldDeclaration withType(japa.parser.ast.type.Type type) {
			field.setType(type);
			return this;
		}
		
		public PartialFieldDeclaration withVariable(VariableDeclarator variable) {
			field.getVariables().add(variable);
			return this;
		}
		
		public PartialFieldDeclaration withVariable(String variable) {
			field.getVariables().add(variableDeclarator(variable));
			return this;
		}

		@Override
		public FieldDeclaration build() {
			if (field.getType() == null) {
				field.setType(new Type() {
					
					@Override
					public <A> void accept(VoidVisitor<A> v, A arg) {
						castAndPrint(v, "var ");
					}
					
					@Override
					public <R, A> R accept(GenericVisitor<R, A> v, A arg) {
						throw new IllegalStateException("should not be called");

					}
				});
			}
			checkState(!field.getVariables().isEmpty(), "a field declaration must have at least one variable");
			return field;
		}

	}
	
	private static interface PartialObjectBuilder<T> {
		T build();
	}
	
	
	
	public static BodyDeclaration bodyDeclarator(final String fakeBody) {
		return new BodyDeclaration() {
			
			@Override
			public <R, A> R accept(GenericVisitor<R, A> v, A arg) {
				throw new AssertionError("Should not call accept");
			}
	
			@Override
			public <A> void accept(VoidVisitor<A> v, A arg) {
				castAndPrint(v, fakeBody);
			}
		};
		
	}

	public static VariableDeclarator variableDeclarator(String name) {
		return new VariableDeclarator(new VariableDeclaratorId(name));
	}
	
	public static VariableDeclarator variableDeclarator(final String name, final String initializer) {
		return new VariableDeclarator(new VariableDeclaratorId(name), new Expression() {
			
			@Override
			public <A> void accept(VoidVisitor<A> v, A arg) {
				castAndPrint(v, initializer);
				
			}
			
			@Override
			public <R, A> R accept(GenericVisitor<R, A> v, A arg) {
				throw new AssertionError("Should not call accept");
			}
		});
	}
	
	
	
	private static void castAndPrint(VoidVisitor<?> visitor, String string) {
		((RuleBasedVisitor)visitor).getPrinter().print(string);
	}
}
