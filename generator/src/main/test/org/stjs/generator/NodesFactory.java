package org.stjs.generator;

import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.visitor.GenericVisitor;
import japa.parser.ast.visitor.VoidVisitor;

import org.stjs.generator.handlers.RuleBasedVisitor;

public class NodesFactory {

	public static ClassOrInterfaceDeclarationPartial newClassOrIntefaceDeclaration() {
		return new ClassOrInterfaceDeclarationPartial();
	}
	
	public static class ClassOrInterfaceDeclarationPartial {
		private final ClassOrInterfaceDeclaration declaration = new ClassOrInterfaceDeclaration();
		
		private ClassOrInterfaceDeclarationPartial() {}
		
		public ClassOrInterfaceDeclarationPartial withName(String name) {
			declaration.setName(name);
			return this;
		}
		
		public ClassOrInterfaceDeclarationPartial addBodyDeclaration(BodyDeclaration bodyDeclaration) {
			if (declaration.getMembers() == null) {
				declaration.setMembers(Lists.<BodyDeclaration>newArrayList());
			}
			declaration.getMembers().add(bodyDeclaration);
			return this;
		}
		
		public ClassOrInterfaceDeclaration build() {
			return declaration;
		}
		
	}

	public static BodyDeclarationStub stub(String fakeBody) {
		return new BodyDeclarationStub(fakeBody);
	}
	
	public static class BodyDeclarationStub extends BodyDeclaration {

		private final String fakeBody;
		
		private BodyDeclarationStub(String fakeBody) {
			this.fakeBody = fakeBody;
		}

		@Override
		public <R, A> R accept(GenericVisitor<R, A> v, A arg) {
			throw new AssertionError("Should not call accept");
		}

		@Override
		public <A> void accept(VoidVisitor<A> v, A arg) {
			if (v instanceof RuleBasedVisitor) {
				((RuleBasedVisitor)v).getPrinter().print(fakeBody);
			}
		}
		
	}
}
