package org.stjs.generator.javac;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

import javax.tools.FileObject;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;

/**
 * @author atamur
 * @since 15-Oct-2009
 */
public class CustomClassloaderJavaFileManager implements JavaFileManager {
	private final ClassLoader classLoader;
	private final StandardJavaFileManager standardFileManager;
	private final PackageInternalsFinder finder;

	@edu.umd.cs.findbugs.annotations.SuppressWarnings(
			value = "DP_CREATE_CLASSLOADER_INSIDE_DO_PRIVILEGED", justification = "harmless")
	public CustomClassloaderJavaFileManager(ClassLoader classLoader, StandardJavaFileManager standardFileManager) {
		// acraciun: for an unclear reason Java 7 screws up with the classloader and getResource gets in trouble. issue
		// #41
		this.classLoader = new URLClassLoader(new URL[0], classLoader);
		this.standardFileManager = standardFileManager;
		finder = new PackageInternalsFinder(this.classLoader);
	}

	@Override
	public ClassLoader getClassLoader(Location location) {
		if (location == StandardLocation.CLASS_PATH) {
			return classLoader;
		}
		return standardFileManager.getClassLoader(location);
	}

	@Override
	public String inferBinaryName(Location location, JavaFileObject file) {
		if (file instanceof CustomJavaFileObject) {
			return ((CustomJavaFileObject) file).binaryName();
		} else { // if it's not CustomJavaFileObject, then it's coming from standard file manager - let it handle the
					// file
			return standardFileManager.inferBinaryName(location, file);
		}
	}

	@Override
	public boolean isSameFile(FileObject a, FileObject b) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean handleOption(String current, Iterator<String> remaining) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean hasLocation(Location location) {
		return location == StandardLocation.CLASS_PATH || location == StandardLocation.PLATFORM_CLASS_PATH; // we don't
																											// care
																											// about
																											// source
																											// and other
																											// location
																											// types -
																											// not
																											// needed
																											// for
																											// compilation
	}

	@Override
	public JavaFileObject getJavaFileForInput(Location location, String className, JavaFileObject.Kind kind) throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public JavaFileObject getJavaFileForOutput(Location location, String className, JavaFileObject.Kind kind, FileObject sibling)
			throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public FileObject getFileForInput(Location location, String packageName, String relativeName) throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public FileObject getFileForOutput(Location location, String packageName, String relativeName, FileObject sibling) throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void flush() throws IOException {
		// do nothing
	}

	@Override
	public void close() throws IOException {
		// do nothing
	}

	@Override
	public Iterable<JavaFileObject> list(Location location, String packageName, Set<JavaFileObject.Kind> kinds, boolean recurse)
			throws IOException {
		if (location == StandardLocation.PLATFORM_CLASS_PATH) { // let standard manager hanfle
			return standardFileManager.list(location, packageName, kinds, recurse);
		} else if (location == StandardLocation.CLASS_PATH && kinds.contains(JavaFileObject.Kind.CLASS)) {
			if (packageName.startsWith("java.")) { // a hack to let standard manager handle locations like "java.lang"
													// or
													// "java.util". Prob would make sense to join results of standard
													// manager with those of my finder here
				return standardFileManager.list(location, packageName, kinds, recurse);
			} else { // app specific classes are here
				return finder.find(packageName, recurse);
			}
		}
		return Collections.emptyList();

	}

	@Override
	public int isSupportedOption(String option) {
		return -1;
	}

}