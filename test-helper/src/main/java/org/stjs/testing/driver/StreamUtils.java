/**
 *  Copyright 2011 Alexandru Craciun, Eyal Kaspi
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.stjs.testing.driver;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Date;

import com.google.common.io.ByteStreams;
import com.google.common.io.CharStreams;
import com.google.common.io.Closeables;
import com.sun.net.httpserver.HttpExchange;

/**
 * utils to copy different urls to an output stream
 * @author acraciun
 */
@SuppressWarnings("restriction")
public class StreamUtils {
	private StreamUtils() {// static methods only
	}

	public static boolean copy(ClassLoader classLoader, String url, Writer out) throws IOException, URISyntaxException {
		URI uri = new URI(url);

		if (uri.getPath() == null) {
			throw new IllegalArgumentException("Wrong path in uri:" + url);
		}

		InputStream is = classLoader.getResourceAsStream(uri.getPath().substring(1));
		if (is == null) {
			return false;
		}
		Reader reader = new InputStreamReader(is, Charset.forName("UTF-8"));
		try {
			CharStreams.copy(reader, out);
			out.flush();
		}
		finally {
			reader.close();
		}
		return true;
	}

	private static long getResourceSize(ClassLoader classLoader, String path) throws IOException {
		final InputStream is = classLoader.getResourceAsStream(path);
		try {
			// TODO improve this
			long n = 0;
			while (is.read() >= 0) {
				n++;
			}
			return n;
		}
		finally {
			Closeables.closeQuietly(is);
		}
	}

	public static Date getResourceModifiedDate(ClassLoader classLoader, String url) throws URISyntaxException {
		URI uri = new URI(url);

		if (uri.getPath() == null) {
			throw new IllegalArgumentException("Wrong path in uri:" + url);
		}
		URL resourceUrl = classLoader.getResource(uri.getPath().substring(1));
		if (resourceUrl == null) {
			return new Date();
		}

		if (resourceUrl.getProtocol().equals("file")) {
			File file = new File(resourceUrl.getFile());
			return new Date(file.lastModified());
		}
		if (resourceUrl.getProtocol().equals("jar")) {
			String fullPath = resourceUrl.getFile();
			String jarFile = fullPath.replace("file:", "").replaceAll("!.+", "");
			File file = new File(jarFile);
			return new Date(file.lastModified());
		}
		//return now
		return new Date();
	}

	public static boolean copy(ClassLoader classLoader, String url, HttpExchange exchange) throws IOException, URISyntaxException {
		URI uri = new URI(url);

		if (uri.getPath() == null) {
			throw new IllegalArgumentException("Wrong path in uri:" + url);
		}

		InputStream is = classLoader.getResourceAsStream(uri.getPath().substring(1));
		if (is == null) {
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, -1);
			return false;
		}
		try {
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, getResourceSize(classLoader, uri.getPath().substring(1)));
			OutputStream out = exchange.getResponseBody();
			ByteStreams.copy(is, out);
			out.flush();
		}
		finally {
			is.close();
		}

		// TODO handle remote urls too
		return true;
	}

}
