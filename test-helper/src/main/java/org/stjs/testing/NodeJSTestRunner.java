package org.stjs.testing;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import junit.framework.AssertionFailedError;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.stjs.generator.Generator;
import org.stjs.generator.GeneratorConfigurationBuilder;
import org.stjs.generator.JavascriptGenerationException;
import org.stjs.generator.node.js.NodeJSExecutor;
import org.stjs.generator.node.js.NodeJSExecutor.ExecutionResult;

public class NodeJSTestRunner extends BlockJUnit4ClassRunner {

  public NodeJSTestRunner(Class<?> testClass) throws InitializationError {
    super(testClass);
  }

  @Override
  protected Statement methodBlock(final FrameworkMethod method) {
    return new Statement() {
      
      @Override
      public void evaluate() throws Throwable {
        Generator generator = new Generator();
        File outputFile = File.createTempFile(getTestClass().getName(), ".js");
        outputFile.deleteOnExit();

        SourceFiles sourceFileAnnote = getTestClass().getJavaClass().getAnnotation(SourceFiles.class);
        List<File> files = getSourceFiles(sourceFileAnnote);
        try {
          generator.generateJavascript(Thread.currentThread().getContextClassLoader(), files,
              outputFile, new GeneratorConfigurationBuilder().
                  allowedPackage(getTestClass().getJavaClass().getPackage().getName()).
                  allowedPackage("org.stjs.javascript").
                  allowedPackage("org.w3c.dom.html").
                  allowedPackage("org.junit.Test").
                  allowedPackage("org.junit.runner").
                  allowedPackage("junit.framework.Assert")
                  .build());

          FileWriter writer = new FileWriter(outputFile, true);
          // TODO : need to let the user plug or at least choose a test framework
          writer.append("Assert={assertEquals:function(a,b){if(a!=b){console.log('__STSJS__Expected '+a+' got '+b);}}};");
          // call method
          writer.append("new "+getTestClass().getJavaClass().getSimpleName()+"()."+method.getName()+"();");
          writer.flush();
          writer.close();
          
          
          NodeJSExecutor executor = new NodeJSExecutor();
          ExecutionResult execution = executor.run(outputFile);
          for (String line : execution.getStdOut().split("\n")) {
            if (line.contains("__STSJS__")) {
              throw new AssertionFailedError(line.substring("__STSJS__".length()));
            }
          }
           throw new AssertionError("NodeJS Runtime error " + execution.getStdOut()+execution.getStdErr());
        } catch (JavascriptGenerationException e) {
          e.printStackTrace();
          throw new AssertionError(e.getMessage());
        }
      }
    };
    

  }

  private List<File> getSourceFiles(SourceFiles sourceFileAnnote) {
    List<File> files = new ArrayList<File>();
    for (String sourceFileName : sourceFileAnnote.files()) {
      files.add(new File(sourceFileName));
    }
    return files;
  }

}
