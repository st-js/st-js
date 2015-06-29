package org.stjs.testing.driver;

import java.net.URISyntaxException;
import java.util.concurrent.ConcurrentHashMap;

public class TestResourceResolver {

	private final ConcurrentHashMap<String, TestResource> cache = new ConcurrentHashMap<>();
	private final ClassLoader classLoader;

	public TestResourceResolver(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}

	public TestResource resolveResource(String httpUrl) throws URISyntaxException {
		TestResource resource = cache.get(httpUrl);
		if (resource == null) {
			resource = new TestResource(classLoader, httpUrl);
			TestResource existing = cache.putIfAbsent(httpUrl, resource);
			if (existing != null) {
				resource = existing;
			}
		}

		return resource;
	}

}
