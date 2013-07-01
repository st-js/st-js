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
package org.stjs.generator.writer;

import japa.parser.ast.expr.MethodCallExpr;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.JavascriptFileGenerationException;
import org.stjs.generator.STJSRuntimeException;
import org.stjs.generator.ast.ASTNodeData;
import org.stjs.generator.ast.SourcePosition;
import org.stjs.generator.type.MethodWrapper;
import org.stjs.generator.writer.template.MethodCallTemplate;

import com.google.common.io.Closeables;

/**
 * this is a handler to handle special method names (those starting with $).
 * 
 * @author <a href='mailto:ax.craciun@gmail.com'>Alexandru Craciun</a>
 */
public class MethodCallTemplates {
	private static final String STJS_TEMPLATES_CONFIG_FILE = "META-INF/stjs.templates";
	private final Map<String, MethodCallTemplate> methodTemplates = new HashMap<String, MethodCallTemplate>();

	public MethodCallTemplates() {

		// methodHandlers.put("java.lang.String.length", methodToPropertyHandler);

		Enumeration<URL> configFiles;
		try {
			configFiles = Thread.currentThread().getContextClassLoader().getResources(STJS_TEMPLATES_CONFIG_FILE);
		} catch (IOException e) {
			throw new STJSRuntimeException(e);
		}
		while (configFiles.hasMoreElements()) {
			loadConfigFile(configFiles.nextElement());
		}
	}

	private void loadConfigFile(URL configFile) {
		InputStream input = null;
		try {
			input = configFile.openStream();
			Properties props = new Properties();
			props.load(input);
			for (Map.Entry<Object, Object> entry : props.entrySet()) {
				methodTemplates.put(entry.getKey().toString(),
						(MethodCallTemplate) Class.forName(entry.getValue().toString()).newInstance());
			}
		} catch (IOException e) {
			throw new STJSRuntimeException(e);
		} catch (InstantiationException e) {
			throw new STJSRuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new STJSRuntimeException(e);
		} catch (ClassNotFoundException e) {
			throw new STJSRuntimeException(e);
		} finally {
			Closeables.closeQuietly(input);
		}
	}

	public boolean handleMethodCall(JavascriptWriterVisitor currentHandler, MethodCallExpr n, GenerationContext context) {
		MethodWrapper method = ASTNodeData.resolvedMethod(n);
		org.stjs.javascript.annotation.Template templateAnn = method
				.getAnnotation(org.stjs.javascript.annotation.Template.class);
		if (templateAnn != null) {
			MethodCallTemplate handler = methodTemplates.get(templateAnn.value());
			if (handler == null) {
				throw new JavascriptFileGenerationException(context.getInputFile(), new SourcePosition(n),
						"The template named '" + templateAnn.value() + " was not found");
			}
			if (handler.write(currentHandler, n, context)) {
				return true;
			}
		}

		return false;
	}

}
