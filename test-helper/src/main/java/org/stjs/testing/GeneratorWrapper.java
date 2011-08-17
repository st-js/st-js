package org.stjs.testing;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Pattern;

import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;
import org.stjs.generator.Generator;
import org.stjs.generator.GeneratorConfigurationBuilder;
import org.stjs.generator.JavascriptGenerationException;

import com.google.common.collect.Sets;

public class GeneratorWrapper {

  public File generateCode(final TestClass testClass, final FrameworkMethod method) throws IOException, AssertionError {
    Generator generator = new Generator();
    File outputFile = File.createTempFile(testClass.getName(), ".js");
    outputFile.deleteOnExit();
    Pattern exceptions = Pattern.compile("java\\.lang.*|org\\.stjs\\.testing.*|org\\.junit.*|junit.*");
    try {
      Set<String> newImports = Sets.newHashSet();
      newImports.add(testClass.getJavaClass().getName());
      
      Set<String> convertedClasses = Sets.newHashSet();
      
      do {
    	  Iterator<String> iterator = newImports.iterator();
    	  String nextFile = iterator.next();
    	  iterator.remove();
    	  Set<String> iterationResolvedImports = generator.generateJavascript(
    			  Thread.currentThread().getContextClassLoader(),
    			  new File("src/test/java/"+nextFile.replaceAll("\\.", "/")+".java"),
		          outputFile,
		          new GeneratorConfigurationBuilder().
		              allowedPackage(testClass.getJavaClass().getPackage().getName()).
		              allowedPackage("org.stjs.javascript").
		              allowedPackage("org.w3c.dom.html").
		              allowedPackage("org.junit.Test").
		              allowedPackage("org.junit.runner").
		              allowedPackage("org.stjs.testing").
		              allowedPackage("junit.framework.Assert")
		              .build(),
		          true);
    	  for (String iterationResolvedImport : iterationResolvedImports) {
    		  
    		  if (!exceptions.matcher(iterationResolvedImport).matches() && convertedClasses.add(iterationResolvedImport)) {
    			  newImports.add(iterationResolvedImport);
    		  }
    	  }
      } while (!newImports.isEmpty());


    } catch (JavascriptGenerationException e) {
      e.printStackTrace();
      throw new AssertionError(e.getMessage());
    }
    return outputFile;
  }
}
