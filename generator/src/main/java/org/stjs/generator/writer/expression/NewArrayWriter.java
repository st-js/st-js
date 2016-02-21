package org.stjs.generator.writer.expression;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.stjs.generator.writer.JavascriptKeywords.NULL;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.javascript.JavaScriptBuilder;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;

import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.NewArrayTree;
import com.sun.source.tree.Tree;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.code.Type.ArrayType;
import com.sun.tools.javac.tree.JCTree.JCPrimitiveTypeTree;

/**
 * useless as arrays are supposed to be checked before
 * 
 * @author acraciun
 */
public class NewArrayWriter<JS> implements WriterContributor<NewArrayTree, JS> {
	private final List<JS> emptyList = Collections.<JS>emptyList();
	private static Map<String, String> java2js = new HashMap<>();

	static {
		java2js.put("int", "Int32Array");
		java2js.put("boolean", "Int8Array");
		java2js.put("byte", "Int8Array");
		java2js.put("char", "Uint16Array");
		java2js.put("short", "Int16Array");
		java2js.put("float", "Float32Array");
		java2js.put("double", "Float64Array");
	}

	private static class TypedArrayTree {
		private NewArrayTree tree;
		private String jsTypeName;
		private int typeDim;
		private List<? extends ExpressionTree> dimensions;
		private List<? extends ExpressionTree> initializers;
		private boolean hasInitializer;
		private boolean hasDimension;
	}

	private TypedArrayTree fromNewArrayTree(NewArrayTree tree) {
		TypedArrayTree t = new TypedArrayTree();
		t.tree = tree;
		t.jsTypeName = java2js.get(typeName(tree));
		t.typeDim = typeDim(tree);
		t.dimensions = tree.getDimensions();
		t.initializers = tree.getInitializers();
		int initsize = t.initializers == null ? 0 : t.initializers.size();
		int dimsize = t.dimensions.size();
		t.hasInitializer = initsize > 0;
		t.hasDimension = dimsize > 0;
		return t;
	}

	@Override
	public JS visit(WriterVisitor<JS> visitor, NewArrayTree tree, GenerationContext<JS> context) {
		TypedArrayTree typedArrayTree = fromNewArrayTree(tree);

		sanityCheck(context, typedArrayTree);

		if (typedArrayTree.hasInitializer) {
			return arrayFromInitializer(visitor, typedArrayTree, context);
		}

		if (typedArrayTree.hasDimension) {
			return arrayFromDimension(visitor, typedArrayTree, context);
		}

		return simpleArray(typedArrayTree, context);
	}

	private void sanityCheck(GenerationContext<JS> context, TypedArrayTree t) {
		if (t.typeDim < 1) {
			throw context.addError(t.tree, "Java array " + t.tree + " not supported.");
		}
		if (t.hasInitializer && t.hasDimension) {
			throw context.addError(t.tree, "Java array " + t.tree + " not supported.");
		}

	}

	private JS simpleArray(TypedArrayTree tree, GenerationContext<JS> context) {
		JavaScriptBuilder<JS> b = context.js();
		int typeDim = tree.typeDim;
		if (typeDim > 1 || tree.jsTypeName == null) {
			// float[][] a = {};
			// var a = [];
			return b.array(emptyList);
		} else {
			// float[] a = {};
			// var a = new Float32Array();
			return newPrimitiveArray(b, b.name(tree.jsTypeName), null);
		}
	}

	private JS arrayFromInitializer(WriterVisitor<JS> visitor, TypedArrayTree tree, GenerationContext<JS> context) {
		List<? extends ExpressionTree> initializers = tree.initializers;
		JavaScriptBuilder<JS> b = context.js();
		int typeDim = tree.typeDim;
		// float[][] a = {{}, {}};
		// var a = [[], []];
		JS args = initArgs(visitor, context, b, initializers);
		if (typeDim > 1 || tree.jsTypeName == null) {
			return args;
		}

		// var a = [1,2,3];
		// new Float32Array([1,2,3])
		return newPrimitiveArray(b, b.name(tree.jsTypeName), args);
	}

	private JS arrayFromDimension(WriterVisitor<JS> visitor, TypedArrayTree tree, GenerationContext<JS> context) {
		JavaScriptBuilder<JS> b = context.js();
		List<? extends ExpressionTree> dimensions = tree.dimensions;
		JS js;
		if (tree.typeDim == dimensions.size() && tree.jsTypeName != null) {
			JS typeName = b.name(tree.jsTypeName);
			// float[] a = new float[3]
			JS args = visitor.scan(dimensions.get(dimensions.size() - 1), context);
			js = newPrimitiveArray(b, typeName, args);
		} else {
			js = newArray(b, dimensions.get(dimensions.size() - 1), visitor, context);
		}
		for (int i = dimensions.size() - 2; i >= 0; i--) {
			// float[][][] aaa = new float[1][2][3]
			JS item = apply(b, newArray(b, dimensions.get(i), visitor, context));
			js = map(b, item, js);
		}
		return js;

	}

	private JS newPrimitiveArray(JavaScriptBuilder<JS> b, JS typeName, JS initArg) {
		return b.newExpression(typeName, initArg == null ? emptyList : singletonList(initArg));
	}

	private static String typeName(NewArrayTree tree) {
		Tree type = tree.getType();
		if (type instanceof JCPrimitiveTypeTree) {
			JCPrimitiveTypeTree prim = (JCPrimitiveTypeTree) type;
			return prim.toString();
		}
		Type elementType = elementType(tree);
		return elementType.toString();
	}

	private static Type elementType(NewArrayTree tree) {
		try {
			Field field = tree.getClass().getField("type");
			Type elementType = (Type) field.get(tree);
			while (elementType instanceof ArrayType) {
				Type.ArrayType atype = (Type.ArrayType) elementType;
				elementType = atype.elemtype;
			}
			return elementType;
		}
		catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			return null;
		}
	}

	private JS initArgs(WriterVisitor<JS> visitor, GenerationContext<JS> context, JavaScriptBuilder<JS> b,
			List<? extends ExpressionTree> initializers) {
		List<JS> args = new ArrayList<>();
		if (initializers != null) {
			for (ExpressionTree init : initializers) {
				args.add(visitor.scan(init, context));
			}
		}
		return b.array(args);
	}

	private JS map(JavaScriptBuilder<JS> b, JS target, JS returns) {
		return b.functionCall(map(b, target), singletonList(b.function(null, emptyList, b.returnStatement(returns))));
	}

	private JS apply(JavaScriptBuilder<JS> b, JS newArray) {
		return b.functionCall(arrayApply(b), asList(b.name(NULL), newArray));
	}

	private JS newArray(JavaScriptBuilder<JS> js, ExpressionTree dim, WriterVisitor<JS> visitor, GenerationContext<JS> context) {
		return js.functionCall(js.name("Array"), singletonList(visitor.scan(dim, context)));
	}

	private JS map(JavaScriptBuilder<JS> js, JS first) {
		return js.property(first, "map");
	}

	private JS arrayApply(JavaScriptBuilder<JS> js) {
		return js.property(js.name("Array"), "apply");
	}

	private static int typeDim(NewArrayTree tree) {
		// int dim = tree.getDimensions().size();
		int dim = 0;
		if (dim == 0) {
			try {
				Field field = tree.getClass().getField("type");
				Object ftype = field.get(tree);
				while (ftype instanceof com.sun.tools.javac.code.Type.ArrayType) {
					com.sun.tools.javac.code.Type.ArrayType atype = (com.sun.tools.javac.code.Type.ArrayType) ftype;
					com.sun.tools.javac.code.Type elemtype2 = atype.elemtype;
					ftype = elemtype2;
					dim++;
				}

			}
			catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
				return 0;
			}
		}
		return dim;

	}
}
