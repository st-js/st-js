package org.stjs.testing.driver;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import org.webjars.WebJarAssetLocator;

public class TestResourceResolver {

	public static final String WEBJARS_ROOT = "/webjars/";

	private final ConcurrentHashMap<String, TestResource> cache = new ConcurrentHashMap<>();
	private final ClassLoader classLoader;
	private final WebJarAssetLocator webjarLocator;

	public TestResourceResolver(ClassLoader classLoader) {
		this.classLoader = classLoader;
		this.webjarLocator = new WebJarAssetLocator(WebJarAssetLocator.getFullPathIndex( //
				Pattern.compile(".*"), //
				classLoader //
		));
	}

	public TestResource resolveResource(String httpPath) throws URISyntaxException {
		TestResource resource = cache.get(httpPath);
		if (resource == null) {
			URL resourceUrl = resolveResourceUrl(httpPath);
			resource = new TestResource(classLoader, httpPath, resourceUrl);
			TestResource existing = cache.putIfAbsent(httpPath, resource);
			if (existing != null) {
				resource = existing;
			}
		}

		return resource;
	}

	private URL resolveResourceUrl(String httpPath) {
		if (httpPath.startsWith(WEBJARS_ROOT)) {
			return resolveWebjarResourceName(httpPath);
		}
		return resolveClassPathResourceName(httpPath);
	}

	private URL resolveClassPathResourceName(String httpPath) {

		try {
			URI uri = new URI(httpPath);
			if (uri.getPath() == null) {
				throw new IllegalArgumentException("Wrong path in uri: " + httpPath);
			}
			return classLoader.getResource(uri.getPath().substring(1));

		} catch(URISyntaxException use){
			throw new IllegalArgumentException("Unable to parse uri: " + httpPath);
		}
	}

	private URL resolveWebjarResourceName(String httpPath) {
		// we are in a unit-test or integration test environment. Since the maven test phase runs before the package phase
		// all the classes of the current project are not yet packed into a .jar, but are exploded outside of the jar.
		// We must try to find the files in the exploded compiled classes directly before handing control over to
		// WebJarAssetLocator if we can't find anything.


		try {
			final Enumeration<URL> enumeration = classLoader.getResources("META-INF/resources/webjars");
			while (enumeration.hasMoreElements()) {
				System.out.println(enumeration.nextElement());
			}
		} catch(Exception e){
			throw new RuntimeException(e);
		}

		System.out.println("httpPath: " + httpPath);
		String partialPath = httpPath.substring(WEBJARS_ROOT.length());
		System.out.println("partialPath: " + partialPath);
		String fullPath = webjarLocator.getFullPath(partialPath);
		System.out.println("fullPath: " + fullPath);

		return classLoader.getResource(fullPath);
	}
}
