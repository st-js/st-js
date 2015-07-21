package org.stjs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

/**
 * Some utils for jar files
 *
 * @author acraciun
 */
public class JarUtils {
	public static Set<String> getJarEntries(String filename) throws IOException {
		final JarFile jarFile = new JarFile(filename);

		Set<String> entryNames = new HashSet<String>();

		for (final Enumeration<JarEntry> e = jarFile.entries(); e.hasMoreElements(); ) {
			final JarEntry entry = e.nextElement();
			entryNames.add(entry.getName());
		}
		return entryNames;
	}

	public static String getJarEntryBody(String jarFileName, String entryName) throws IOException {
		final JarFile jarFile = new JarFile(jarFileName);
		ZipEntry entry = jarFile.getEntry(entryName);

		StringBuilder body = new StringBuilder();
		try (InputStream in = jarFile.getInputStream(entry);
				BufferedReader r = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {

			String str = null;
			StringBuilder sb = new StringBuilder(8192);
			while ((str = r.readLine()) != null) {
				sb.append(str);
				sb.append('\n');
			}

			return sb.toString();
		}
	}
}
