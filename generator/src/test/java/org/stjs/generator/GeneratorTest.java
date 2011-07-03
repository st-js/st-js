package org.stjs.generator;

import japa.parser.ParseException;

import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.stjs.generator.node.js.NodeJSExecutor;
import org.stjs.generator.node.js.NodeJSExecutor.ExecutionResult;

import test.Declaration1;

public class GeneratorTest {
	@Test
	public void testGenerator() throws ParseException, IOException {
		generate("src/test/java/test/Declaration1.java", Declaration1.class);
	}

	private void generate(String sourceFile, Class<?> clazz) throws ParseException, IOException {

		Generator generator = new Generator();
		NodeJSExecutor executor = new NodeJSExecutor();
		File outputFile = new File("target/x.js");
		generator.generateJavascript(Thread.currentThread().getContextClassLoader(), clazz, new File(sourceFile),
				outputFile);
		ExecutionResult execution = executor.run(outputFile);
		System.out.println(execution.toString());
	}

}
