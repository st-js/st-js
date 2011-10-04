package org.stjs.testing;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import junit.framework.AssertionFailedError;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.stjs.generator.executor.ExecutionResult;
import org.stjs.generator.executor.NodeJSExecutor;

public class NodeJSTestRunner extends BlockJUnit4ClassRunner {

	public NodeJSTestRunner(Class<?> testClass) throws InitializationError {
		super(testClass);
	}

	@Override
	protected Statement methodBlock(final FrameworkMethod method) {
		return new Statement() {

			@Override
			public void evaluate() throws Throwable {
				File outputFile = new GeneratorWrapper().generateCode(getTestClass());
				FileWriter writer = new FileWriter(outputFile, true);
				// TODO : need to let the user plug or at least choose a test framework
				writer.append("Assert={assertEquals:function(a,b){if(a!=b){console.log('__STSJS__Expected '+a+' got '+b);}}};");
				// call method
				writer.append("new " + getTestClass().getJavaClass().getSimpleName() + "()." + method.getName() + "();");
				writer.flush();
				writer.close();
				NodeJSExecutor executor = new NodeJSExecutor();
				ExecutionResult execution = executor.run(outputFile);
				for (String line : execution.getStdOut().split("\n")) {
					if (line.contains("__STSJS__")) {
						throw new AssertionFailedError(line.substring("__STSJS__".length()));
					}
				}
				if (execution.getExitValue() != 0) {
					throw new AssertionError("NodeJS Runtime error " + execution.getStdOut() + execution.getStdErr());
				}
			}

		};

	}

	private List<File> getSourceFiles(SourceFiles sourceFileAnnote) {
		if (sourceFileAnnote != null) {
			List<File> files = new ArrayList<File>();
			for (String sourceFileName : sourceFileAnnote.files()) {
				files.add(new File(sourceFileName));
			}
			return files;
		} else {
			return Collections.singletonList(new File("src/test/java/"
					+ getTestClass().getJavaClass().getName().replaceAll("\\.", "/") + ".java"));
		}
	}

}
