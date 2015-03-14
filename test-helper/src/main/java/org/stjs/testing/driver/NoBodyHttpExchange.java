package org.stjs.testing.driver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpPrincipal;

public class NoBodyHttpExchange extends HttpExchange {

	private final HttpExchange delegate;

	public NoBodyHttpExchange(final HttpExchange delegate) {
		this.delegate = delegate;
	}

	@Override
	public Headers getRequestHeaders() {
		return delegate.getRequestHeaders();
	}

	@Override
	public Headers getResponseHeaders() {
		return delegate.getResponseHeaders();
	}

	@Override
	public URI getRequestURI() {
		return delegate.getRequestURI();
	}

	@Override
	public String getRequestMethod() {
		return delegate.getRequestMethod();
	}

	@Override
	public HttpContext getHttpContext() {
		return delegate.getHttpContext();
	}

	@Override
	public void close() {
		delegate.close();
	}

	@Override
	public InputStream getRequestBody() {
		return delegate.getRequestBody();
	}

	@Override
	public OutputStream getResponseBody() {
		// we call the superclass to make sure that the lifecycle of the real exchange is respected
		// but we don't actually do anything with the result.
		delegate.getResponseBody();
		return new NullOutputStream();
	}

	@Override
	public void sendResponseHeaders(final int i, final long l) throws IOException {
		delegate.sendResponseHeaders(i, l);
	}

	@Override
	public InetSocketAddress getRemoteAddress() {
		return delegate.getRemoteAddress();
	}

	@Override
	public int getResponseCode() {
		return delegate.getResponseCode();
	}

	@Override
	public InetSocketAddress getLocalAddress() {
		return delegate.getLocalAddress();
	}

	@Override
	public String getProtocol() {
		return delegate.getProtocol();
	}

	@Override
	public Object getAttribute(final String s) {
		return delegate.getAttribute(s);
	}

	@Override
	public void setAttribute(final String s, final Object o) {
		delegate.setAttribute(s, o);
	}

	@Override
	public void setStreams(final InputStream inputStream, final OutputStream outputStream) {
		delegate.setStreams(inputStream, outputStream);
	}

	@Override
	public HttpPrincipal getPrincipal() {
		return delegate.getPrincipal();
	}

	private static class NullOutputStream extends OutputStream {
		@Override public void write(final int b) throws IOException {
			// don't write anything...
		}
	}
}
