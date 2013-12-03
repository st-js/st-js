package org.stjs.generator.writer.templates;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mozilla.javascript.ast.AstNode;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.visitor.VisitorContributor;

import com.sun.source.tree.MethodInvocationTree;

public class MethodCallTemplates {
	private Map<String, VisitorContributor<MethodInvocationTree, List<AstNode>, GenerationContext>> templates = new HashMap<String, VisitorContributor<MethodInvocationTree, List<AstNode>, GenerationContext>>();

	public MethodCallTemplates() {
		templates.put("adapter", new AdapterTemplate());
		templates.put("array", new ArrayTemplate());
		templates.put("delete", new DeleteTemplate());
		templates.put("get", new GetTemplate());
		templates.put("invoke", new InvokeTemplate());
		templates.put("js", new JsTemplate());
		templates.put("map", new MapTemplate());
		templates.put("toProperty", new MethodToPropertyTemplate());
		templates.put("or", new OrTemplate());
		templates.put("prefix", new PrefixTemplate());
		templates.put("properties", new PropertiesTemplate());
		templates.put("put", new PutTemplate());
		templates.put("set", new SetTemplate());
		templates.put("typeOf", new TypeOfTemplate());
	}

	public VisitorContributor<MethodInvocationTree, List<AstNode>, GenerationContext> getTemplate(String name) {
		return templates.get(name);
	}
}
