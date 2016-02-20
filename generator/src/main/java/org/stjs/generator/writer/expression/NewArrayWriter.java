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
import com.sun.tools.javac.code.Type.JCPrimitiveType;
import com.sun.tools.javac.tree.JCTree.JCPrimitiveTypeTree;

/**
 * useless as arrays are supposed to be checked before
 * 
 * @author acraciun
 */
public class NewArrayWriter<JS> implements WriterContributor<NewArrayTree, JS> {

	private final List<JS> emptyList = Collections.<JS>emptyList();

	@Override
	public JS visit(WriterVisitor<JS> visitor, NewArrayTree tree, GenerationContext<JS> context) {
		String jsTypename = java2js.get(typeName(tree));
		if (jsTypename == null) {
			throw context.addError(tree, "Java array " + tree + " not supported.");
		}
		JavaScriptBuilder<JS> b = context.js();
		JS typeName = b.name(jsTypename);
		int dim = dim(tree);
		List<? extends ExpressionTree> dimensions = tree.getDimensions();
		List<? extends ExpressionTree> initializers = tree.getInitializers();
		int initsize = initializers == null ? 0 : initializers.size();
		int dimsize = dimensions.size();
		if (dim == 1 && dimsize == 0 && initsize == 0) {
			// new Float32Array()
			return b.newExpression(typeName, emptyList);
		}

		if (dim == 1 && initsize == 1) {
			// new Float32Array([1,2,3])
			return b.newExpression(typeName, singleDimInit(b, visitor, context, tree.getInitializers().get(0)));
		}
		if (dim == 1 && initsize > 1) {
			JS array = initArray(visitor, context, b, initializers);
			return b.newExpression(typeName, singletonList(array));
		}
		if (dim > 1 && initsize >= 1) {
			return initArray(visitor, context, b, initializers);
		}

		if (dim > 1 && dimsize == 0 && initsize == 0) {
			return b.array(emptyList);
		}
		if (dimsize >= 1 && initsize == 0) {
			JS _js = newPrimitiveArray(typeName, b, dimensions.get(dimsize - 1), visitor, context);
			for (int i = dimsize - 2; i >= 0; i--) {
				JS item = apply(b, newArray(b, dimensions.get(i), visitor, context));
				_js = map(b, item, _js);
			}
			return _js;
		}

		throw context.addError(tree, "Java arrays are not supported. This is a ST-JS bug.");
	}

	private static Map<String, String> java2js = new HashMap<>();

	static {
		java2js.put("int", "Int32Array");
		java2js.put("byte", "Int8Array");
		java2js.put("char", "Uint16Array");
		java2js.put("short", "Int16Array");
		java2js.put("float", "Float32Array");
		java2js.put("double", "Float64Array");
	}

	private static String typeName(NewArrayTree tree) {
		Tree type = tree.getType();
		if (type == null) {
			try {
				Field field = tree.getClass().getField("type");
				Object _type = field.get(tree);
				while (_type instanceof com.sun.tools.javac.code.Type.ArrayType) {
					com.sun.tools.javac.code.Type.ArrayType atype = (com.sun.tools.javac.code.Type.ArrayType) _type;
					com.sun.tools.javac.code.Type elemtype2 = atype.elemtype;
					boolean primitive = elemtype2.isPrimitive();
					if (primitive) {
						JCPrimitiveType prim = (JCPrimitiveType) elemtype2;
						return prim.toString();
					}
					_type = elemtype2;
				}

			}
			catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
				return null;
			}
		} else if (type instanceof JCPrimitiveTypeTree) {
			JCPrimitiveTypeTree prim = (JCPrimitiveTypeTree) type;
			return prim.toString();

		}
		return null;
	}

	private JS initArray(WriterVisitor<JS> visitor, GenerationContext<JS> context, JavaScriptBuilder<JS> b,
			List<? extends ExpressionTree> initializers) {
		List<JS> _init = new ArrayList<>();
		for (ExpressionTree init : initializers) {
			_init.add(visitor.scan(init, context));
		}
		return b.array(_init);
	}

	private JS map(JavaScriptBuilder<JS> b, JS target, JS returns) {
		return b.functionCall(map(b, target), singletonList(b.function(null, emptyList, b.returnStatement(returns))));
	}

	private JS newPrimitiveArray(JS typeName, JavaScriptBuilder<JS> b, ExpressionTree dim, WriterVisitor<JS> visitor,
			GenerationContext<JS> context) {
		return b.newExpression(typeName, singleDimension(visitor, context, dim));
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

	private static int dim(NewArrayTree tree) {
		Tree type = tree.getType();
		int dim = tree.getDimensions().size();
		if (dim == 0) {
			try {
				Field field = tree.getClass().getField("type");
				Object _type = field.get(tree);
				while (_type instanceof com.sun.tools.javac.code.Type.ArrayType) {
					com.sun.tools.javac.code.Type.ArrayType atype = (com.sun.tools.javac.code.Type.ArrayType) _type;
					com.sun.tools.javac.code.Type elemtype2 = atype.elemtype;
					_type = elemtype2;
					dim++;
				}

			}
			catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
				return 0;
			}
		}
		return dim;

	}

	private List<JS> singleDimension(WriterVisitor<JS> visitor, GenerationContext<JS> context, ExpressionTree dim) {
		return singletonList(visitor.scan(dim, context));
	}

	private List<JS> singleDimInit(JavaScriptBuilder<JS> b, WriterVisitor<JS> visitor, GenerationContext<JS> context, ExpressionTree init) {
		return singletonList(b.array(singletonList(visitor.scan(init, context))));
	}
}
