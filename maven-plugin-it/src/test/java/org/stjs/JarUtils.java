package org.stjs;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Some utils for jar files
 * 
 * @author acraciun
 * 
 */
public class JarUtils {
	public static Set<String> getJarEntries(String filename) throws IOException {
		final JarFile jarFile = new JarFile(filename);

		Set<String> entryNames = new HashSet<String>();

		for (final Enumeration<JarEntry> e = jarFile.entries(); e.hasMoreElements();) {
			final JarEntry entry = e.nextElement();
			entryNames.add(entry.getName());
		}
		return entryNames;
	}
}
