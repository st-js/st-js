package org.stjs.testing;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;
import org.stjs.generator.Generator;
import org.stjs.generator.GeneratorConfigurationBuilder;
import org.stjs.generator.JavascriptGenerationException;

public class GeneratorWrapper {

  public File generateCode(final TestClass testClass, final FrameworkMethod method, List<File> files) throws IOException, AssertionError {
    Generator generator = new Generator();
    File outputFile = File.createTempFile(testClass.getName(), ".js");
    outputFile.deleteOnExit();

    try {
      generator.generateJavascript(Thread.currentThread().getContextClassLoader(), files,
          outputFile, new GeneratorConfigurationBuilder().
              allowedPackage(testClass.getJavaClass().getPackage().getName()).
              allowedPackage("org.stjs.javascript").
              allowedPackage("org.w3c.dom.html").
              allowedPackage("org.junit.Test").
              allowedPackage("org.junit.runner").
              allowedPackage("junit.framework.Assert")
              .build());


    } catch (JavascriptGenerationException e) {
      e.printStackTrace();
      throw new AssertionError(e.getMessage());
    }
    return outputFile;
  }
}
