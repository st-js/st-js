package org.stjs.testing.driver;

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
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.Date;

import com.google.common.io.ByteStreams;
import com.google.common.io.CharStreams;
import com.google.common.io.Closeables;
import com.sun.net.httpserver.HttpExchange;

import sun.net.www.protocol.file.FileURLConnection;

public class TestResource {

	private final ClassLoader classLoader;
	private final String httpUrl;
	private final URL resourceUrl;

	public TestResource(ClassLoader classLoader, String httpUrl) throws URISyntaxException {
		this.classLoader = classLoader;
		this.httpUrl = httpUrl;

		URI uri = new URI(httpUrl);
		if (uri.getPath() == null) {
			throw new IllegalArgumentException("Wrong path in uri:" + httpUrl);
		}
		this.resourceUrl = classLoader.getResource(uri.getPath().substring(1));
	}

	public Date getModifiedDate() throws IOException {
		if (resourceUrl == null) {
			return new Date();
		}

		return withConnection(new ConnectionOperation<Date>() {
			@Override
			public Date doWithConnection(URLConnection connection) throws IOException {
				return new Date(connection.getLastModified());
			}
		});
	}

	public boolean copyTo(final Writer out) throws IOException {
		if(resourceUrl == null){
			return false;
		}

		return withConnection(new ConnectionOperation<Boolean>() {
			@Override
			public Boolean doWithConnection(URLConnection connection) throws IOException {
				InputStream is = connection.getInputStream();
				Reader reader = new InputStreamReader(is, Charset.forName("UTF-8"));
				try {
					CharStreams.copy(reader, out);
					out.flush();
				}
				finally {
					Closeables.closeQuietly(reader);
					Closeables.closeQuietly(is);
				}
				return true;
			}
		});
	}

	public boolean copyTo(final HttpExchange exchange) throws IOException {
		if(resourceUrl == null){
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, -1);
			return false;
		}

		return withConnection(new ConnectionOperation<Boolean>() {
			@Override
			public Boolean doWithConnection(URLConnection connection) throws IOException {
				InputStream is = connection.getInputStream();
				try {
					exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, connection.getContentLengthLong());
					OutputStream out = exchange.getResponseBody();
					ByteStreams.copy(is, out);
					out.flush();
				}
				finally {
					Closeables.closeQuietly(is);
				}
				return true;
			}
		});
	}


	private <T> T withConnection(ConnectionOperation<T> operation) throws IOException {
		URLConnection conn = this.resourceUrl.openConnection();
		conn.connect();

		try {
			T result = operation.doWithConnection(conn);
			return result;
		} finally {
			Closeables.closeQuietly(conn.getInputStream());
		}
	}

	private interface ConnectionOperation<T> {
		T doWithConnection(URLConnection connection) throws IOException ;
	}
}
