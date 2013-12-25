package org.stjs.generator.plugin;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.stjs.generator.STJSRuntimeException;
import org.stjs.generator.check.CheckVisitor;
import org.stjs.generator.writer.WriterVisitor;

import com.google.common.io.Closeables;

/**
 * This class contains all the generation plugins defined by the users and the default ones
 * 
 * @author acraciun
 * 
 */
public class GenerationPlugins<JS> {
	private static final String STJS_PLUGINS_CONFIG_FILE = "META-INF/stjs.plugins";

	private static final String JAVA_VERSION_ENTRY = "java.version";

	private final Map<String, STJSGenerationPlugin<JS>> plugins = new HashMap<String, STJSGenerationPlugin<JS>>();
	private final CheckVisitor checkVisitor = new CheckVisitor();
	private final WriterVisitor<JS> writerVisitor = new WriterVisitor<JS>();

	public GenerationPlugins() {

		MainGenerationPlugin<JS> mainPlugin = new MainGenerationPlugin<JS>();
		mainPlugin.contributeCheckVisitor(checkVisitor);
		mainPlugin.contributeWriteVisitor(writerVisitor);
		plugins.put("default", mainPlugin);

		Enumeration<URL> configFiles;
		try {
			configFiles = Thread.currentThread().getContextClassLoader().getResources(STJS_PLUGINS_CONFIG_FILE);
		}
		catch (IOException e) {
			throw new STJSRuntimeException(e);
		}
		while (configFiles.hasMoreElements()) {
			loadConfigFile(configFiles.nextElement());
		}
	}

	private void loadConfigFile(URL configFile) {
		InputStream input = null;
		try {
			input = configFile.openStream();
			Properties props = new Properties();
			props.load(input);

			String javaVersion = props.getProperty(JAVA_VERSION_ENTRY);
			String runningVersion = System.getProperty(JAVA_VERSION_ENTRY);

			if (compareVersion(javaVersion, runningVersion) > 0) {
				// this plugin is for a next version of java
				return;
			}
			for (Map.Entry<Object, Object> entry : props.entrySet()) {
				loadPlugin(entry.getKey().toString(), entry.getValue().toString());
			}
		}
		catch (IOException e) {
			throw new STJSRuntimeException(e);
		}

		finally {
			Closeables.closeQuietly(input);
		}
	}

	@SuppressWarnings("unchecked")
	private void loadPlugin(String key, String value) {
		if (key.equals(JAVA_VERSION_ENTRY)) {
			return;
		}
		STJSGenerationPlugin<JS> plugin;
		try {
			plugin = (STJSGenerationPlugin<JS>) Class.forName(value).newInstance();
		}
		catch (InstantiationException e) {
			throw new STJSRuntimeException(e);
		}
		catch (IllegalAccessException e) {
			throw new STJSRuntimeException(e);
		}
		catch (ClassNotFoundException e) {
			throw new STJSRuntimeException(e);
		}
		plugin.contributeCheckVisitor(checkVisitor);
		plugin.contributeWriteVisitor(writerVisitor);
		plugins.put(key, plugin);
	}

	// public GenerationContext<JS> newContext() {
	// return null;
	// }

	private int compareVersion(String javaVersion, String runningVersion) {
		if (javaVersion == null) {
			// default version
			return -1;
		}
		// TODO do a proper check
		return javaVersion.compareTo(runningVersion);
	}

	public CheckVisitor getCheckVisitor() {
		return checkVisitor;
	}

	public WriterVisitor<JS> getWriterVisitor() {
		return writerVisitor;
	}
}
