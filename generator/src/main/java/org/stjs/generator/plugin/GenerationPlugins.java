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
import org.stjs.javascript.annotation.UsePlugin;

import com.google.common.io.Closeables;

/**
 * This class contains all the generation plugins defined by the users and the default ones
 *
 * @author acraciun
 * @version $Id: $Id
 */
public class GenerationPlugins<JS> {
	private static final String STJS_PLUGINS_CONFIG_FILE = "META-INF/stjs.plugins";

	private static final String JAVA_VERSION_ENTRY = "java.version";

	private final Map<String, STJSGenerationPlugin<JS>> mandatoryPlugins = new HashMap<String, STJSGenerationPlugin<JS>>();
	private final Map<String, STJSGenerationPlugin<JS>> optionalPlugins = new HashMap<String, STJSGenerationPlugin<JS>>();

	private CheckVisitor checkVisitor = new CheckVisitor();
	private WriterVisitor<JS> writerVisitor = new WriterVisitor<JS>();

	/**
	 * <p>Constructor for GenerationPlugins.</p>
	 */
	public GenerationPlugins() {

		MainGenerationPlugin<JS> mainPlugin = new MainGenerationPlugin<JS>();
		mainPlugin.contributeCheckVisitor(checkVisitor);
		mainPlugin.contributeWriteVisitor(writerVisitor);
		mandatoryPlugins.put("default", mainPlugin);

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
		if (plugin.loadByDefault()) {
			plugin.contributeCheckVisitor(checkVisitor);
			plugin.contributeWriteVisitor(writerVisitor);
			mandatoryPlugins.put(key, plugin);
		} else {
			optionalPlugins.put(key, plugin);
		}
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

	/**
	 * <p>Getter for the field <code>checkVisitor</code>.</p>
	 *
	 * @return a {@link org.stjs.generator.check.CheckVisitor} object.
	 */
	public CheckVisitor getCheckVisitor() {
		return checkVisitor;
	}

	/**
	 * <p>Getter for the field <code>writerVisitor</code>.</p>
	 *
	 * @return a {@link org.stjs.generator.writer.WriterVisitor} object.
	 */
	public WriterVisitor<JS> getWriterVisitor() {
		return writerVisitor;
	}

	/**
	 * <p>forClass.</p>
	 *
	 * @param clazz a {@link java.lang.Class} object.
	 * @return a {@link org.stjs.generator.plugin.GenerationPlugins} object.
	 */
	@SuppressWarnings("unchecked")
	public GenerationPlugins<JS> forClass(Class<?> clazz) {
		UsePlugin usePlugins = clazz.getAnnotation(UsePlugin.class);
		if (usePlugins == null || usePlugins.value() == null || usePlugins.value().length == 0) {
			// this class uses the default plugins - no need to create a new one
			return this;
		}

		// TODO - here I can add a cache using the list of plugin names as key
		GenerationPlugins<JS> newPlugins = new GenerationPlugins<JS>();
		newPlugins.checkVisitor = new CheckVisitor(checkVisitor);
		newPlugins.writerVisitor = new WriterVisitor<JS>(writerVisitor);

		for (String pluginName : usePlugins.value()) {
			STJSGenerationPlugin<JS> plugin = optionalPlugins.get(pluginName);
			if (plugin == null) {
				throw new STJSRuntimeException("The class:" + clazz.getName() + " need an unknown Generation Plugin :" + pluginName);
			}
			plugin.contributeCheckVisitor(newPlugins.checkVisitor);
			plugin.contributeWriteVisitor(newPlugins.writerVisitor);
		}
		return newPlugins;
	}
}
