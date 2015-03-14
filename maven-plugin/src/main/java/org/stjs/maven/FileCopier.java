package org.stjs.maven;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.google.common.base.Strings;

/**
 * This class is used to copy files from a folder (inside a jar or on the disk) to another folder. copied from
 * http://stackoverflow.com/questions/1386809/copy-directory-from-a-jar-file
 * @author acraciun
 */
public class FileCopier {
	public static boolean copyFile(final File toCopy, final File destFile) {
		try {
			return copyStream(new FileInputStream(toCopy), new FileOutputStream(destFile));
		}
		catch (final FileNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}

	private static boolean copyFilesRecusively(final File toCopy, final File destDir, FilenameFilter filter) {
		assert destDir.isDirectory();

		if (!toCopy.isDirectory()) {
			if (filter.accept(toCopy.getParentFile(), toCopy.getName())) {
				return copyFile(toCopy, new File(destDir, toCopy.getName()));
			}
			return true;
		} else {
			final File newDestDir = new File(destDir, toCopy.getName());
			if (!newDestDir.exists() && !newDestDir.mkdir()) {
				return false;
			}
			for (final File child : toCopy.listFiles()) {
				if (!copyFilesRecusively(child, newDestDir, filter)) {
					return false;
				}
			}
		}
		return true;
	}

	public static boolean copyJarResourcesRecursively(final File destDir, final JarURLConnection jarConnection, FilenameFilter filter)
			throws IOException {

		final JarFile jarFile = jarConnection.getJarFile();

		for (final Enumeration<JarEntry> e = jarFile.entries(); e.hasMoreElements();) {
			final JarEntry entry = e.nextElement();
			String startName = jarConnection.getEntryName();
			if (startName == null) {
				startName = "";
			}
			if (entry.getName().startsWith(startName)) {
				final String filename = removeStart(entry.getName(), //
						startName);

				if (!entry.isDirectory()) {
					final File f = new File(destDir, filename);
					final InputStream entryInputStream = jarFile.getInputStream(entry);
					if (filter.accept(destDir, filename)) {
						if (!ensureDirectoryExists(f.getParentFile())) {
							throw new IOException("Could not create directory: " + f.getParentFile().getAbsolutePath());
						}
						if (!copyStream(entryInputStream, f)) {
							return false;
						}
					}
					entryInputStream.close();
				}
			}
		}
		return true;
	}

	public static boolean copyResourcesRecursively( //
			final URL originUrl, final File destination, FilenameFilter filter) {
		try {
			final URLConnection urlConnection = originUrl.openConnection();
			if (urlConnection instanceof JarURLConnection) {
				return copyJarResourcesRecursively(destination, (JarURLConnection) urlConnection, filter);
			} else {
				return copyFilesRecusively(new File(originUrl.getPath()), destination, filter);
			}
		}
		catch (final IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	private static boolean copyStream(final InputStream is, final File f) {
		try {
			return copyStream(is, new FileOutputStream(f));
		}
		catch (final FileNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}

	private static boolean copyStream(final InputStream is, final OutputStream os) {
		try {
			final byte[] buf = new byte[1024];

			int len = 0;
			while ((len = is.read(buf)) > 0) {
				os.write(buf, 0, len);
			}
			is.close();
			os.close();
			return true;
		}
		catch (final IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	private static boolean ensureDirectoryExists(final File f) {
		return f.exists() || f.mkdirs();
	}

	public static String removeStart(String str, String remove) {
		if (Strings.isNullOrEmpty(str) || Strings.isNullOrEmpty(remove)) {
			return str;
		}
		if (str.startsWith(remove)) {
			return str.substring(remove.length());
		}
		return str;
	}
}
