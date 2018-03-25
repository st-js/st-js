package org.stjs.generator.javac;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URI;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.NestingKind;
import javax.tools.JavaFileObject;

class CustomJavaFileObject implements JavaFileObject {
	private final String binaryName;
	private final URI uri;
	private final String name;

	/**
	 * <p>Constructor for CustomJavaFileObject.</p>
	 *
	 * @param binaryName a {@link java.lang.String} object.
	 * @param uri a {@link java.net.URI} object.
	 */
	public CustomJavaFileObject(String binaryName, URI uri) {
		this.uri = uri;
		this.binaryName = binaryName;
		name = uri.getPath() == null ? uri.getSchemeSpecificPart() : uri.getPath(); // for FS based URI the path is not
																					// null, for JAR URI the scheme
																					// specific part is not null
	}

	/** {@inheritDoc} */
	@Override
	public URI toUri() {
		return uri;
	}

	/** {@inheritDoc} */
	@Override
	public InputStream openInputStream() throws IOException {
		return uri.toURL().openStream(); // easy way to handle any URI!
	}

	/** {@inheritDoc} */
	@Override
	public OutputStream openOutputStream() throws IOException {
		throw new UnsupportedOperationException();
	}

	/** {@inheritDoc} */
	@Override
	public String getName() {
		return name;
	}

	/** {@inheritDoc} */
	@Override
	public Reader openReader(boolean ignoreEncodingErrors) throws IOException {
		throw new UnsupportedOperationException();
	}

	/** {@inheritDoc} */
	@Override
	public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
		throw new UnsupportedOperationException();
	}

	/** {@inheritDoc} */
	@Override
	public Writer openWriter() throws IOException {
		throw new UnsupportedOperationException();
	}

	/** {@inheritDoc} */
	@Override
	public long getLastModified() {
		return 0;
	}

	/** {@inheritDoc} */
	@Override
	public boolean delete() {
		throw new UnsupportedOperationException();
	}

	/** {@inheritDoc} */
	@Override
	public Kind getKind() {
		return Kind.CLASS;
	}

	/** {@inheritDoc} */
	@Override
	// copied from SImpleJavaFileManager
	public boolean isNameCompatible(String simpleName, Kind kind) {
		String baseName = simpleName + kind.extension;
		return kind.equals(getKind()) && (baseName.equals(getName()) || getName().endsWith("/" + baseName));
	}

	/** {@inheritDoc} */
	@Override
	public NestingKind getNestingKind() {
		throw new UnsupportedOperationException();
	}

	/** {@inheritDoc} */
	@Override
	public Modifier getAccessLevel() {
		throw new UnsupportedOperationException();
	}

	/**
	 * <p>binaryName.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String binaryName() {
		return binaryName;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return "CustomJavaFileObject{" + "uri=" + uri + '}';
	}
}
