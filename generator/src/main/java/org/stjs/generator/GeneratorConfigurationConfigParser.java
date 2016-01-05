package org.stjs.generator;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

public class GeneratorConfigurationConfigParser {
    public static final String CONFIG_PROPERTIES_RESOURCES_FILENAME = "config.properties";
    public static final String SPLIT_CHAR_TOKEN = "${SPLIT_CHAR}";
    public static final String CONFIG_PROPERTIES_REGEX = "\\s*" + SPLIT_CHAR_TOKEN + "\\s*";
    public static final String CONFIG_PROPERTIES_SPLIT_CHAR = ",";
    public static final String CONFIG_PROPERTIES_MAP_SPLIT_CHAR = ":";
    public static final String ALLOWED_JAVA_LANG_CLASSES_CONFIG_KEY = "allowedJavaLangClasses";
    public static final String ALLOWED_PACKAGES_CONFIG_KEY = "allowedPackages";
    public static final String RENAMED_METHOD_SIGNATURES_CONFIG_KEY = "renamedMethodSignatures";
    public static final String NAMESPACES_CONFIG_KEY = "namespaces";
    public static final String FORBIDDEN_METHOD_INVOCATIONS_CONFIG_KEY = "forbiddenMethodInvocations";
    public static final String ANNOTATIONS = "annotations";

    public void readFromConfigFile(InputStream configFileInputStream, GeneratorConfigurationBuilder builder) throws IOException {
        Properties prop = new Properties();
        try {
            prop.load(configFileInputStream);

            String allowedJavaLangClassesConfig = prop.getProperty(ALLOWED_JAVA_LANG_CLASSES_CONFIG_KEY);
            String allowedPackagesConfig = prop.getProperty(ALLOWED_PACKAGES_CONFIG_KEY);
            String forbiddenMethodInvocationsConfig = prop.getProperty(FORBIDDEN_METHOD_INVOCATIONS_CONFIG_KEY);
            String namespacesConfig = prop.getProperty(NAMESPACES_CONFIG_KEY);
            String renamedMethodSignaturesConfig = prop.getProperty(RENAMED_METHOD_SIGNATURES_CONFIG_KEY);
            String annotationsConfig = prop.getProperty(ANNOTATIONS);

            builder.allowedJavaLangClasses(getConfigValueAsCollection(allowedJavaLangClassesConfig));
            builder.allowedPackages(getConfigValueAsCollection(allowedPackagesConfig));
            builder.forbiddenMethodInvocations(getConfigValueAsCollection(forbiddenMethodInvocationsConfig));
            builder.namespaces(getConfigValueAsMap(namespacesConfig));
            builder.renamedMethodSignatures(getConfigValueAsMap(renamedMethodSignaturesConfig));
            builder.annotations(getConfigValueAsCollection(annotationsConfig));
        }
        finally {
            if (configFileInputStream != null) {
                configFileInputStream.close();
            }
        }
    }

    public void writeToConfigFile(OutputStream configFileOutputStream, GeneratorConfiguration configuration) throws IOException {
        Properties prop = new Properties();
        try {
            prop.setProperty(ALLOWED_JAVA_LANG_CLASSES_CONFIG_KEY, getCollectionAsConfigValue(configuration.getAllowedJavaLangClasses()));
            prop.setProperty(FORBIDDEN_METHOD_INVOCATIONS_CONFIG_KEY, getCollectionAsConfigValue(configuration.getForbiddenMethodInvocations()));
            prop.setProperty(ALLOWED_PACKAGES_CONFIG_KEY, getCollectionAsConfigValue(configuration.getAllowedPackages()));
            prop.setProperty(NAMESPACES_CONFIG_KEY, getMapAsConfigValue(configuration.getNamespaces()));
            prop.setProperty(RENAMED_METHOD_SIGNATURES_CONFIG_KEY, getMapAsConfigValue(configuration.getRenamedMethodSignatures()));
            prop.setProperty(ANNOTATIONS, getCollectionAsConfigValue(configuration.getAnnotations()));

            prop.store(configFileOutputStream, null);
        } finally {
            if (configFileOutputStream != null) {
                configFileOutputStream.close();
            }
        }
    }

    private Collection<String> getConfigValueAsCollection(String config) {
        if (config != null && !config.isEmpty()) {
            return Arrays.asList(config.split(CONFIG_PROPERTIES_REGEX.replace(SPLIT_CHAR_TOKEN, CONFIG_PROPERTIES_SPLIT_CHAR)));
        } else {
            return new ArrayList<>();
        }
    }

    private Map<String, String> getConfigValueAsMap(String config) {
        Map<String, String> namespacesMap = new HashMap<>();
        if (config != null && !config.isEmpty()) {
            namespacesMap = processConfigValueAsMap(config);
        }
        return namespacesMap;
    }

    private Map<String, String> processConfigValueAsMap(String config) {
        Map<String, String> map = new HashMap<>();
        String[] pairs = config.split(CONFIG_PROPERTIES_REGEX.replace(SPLIT_CHAR_TOKEN, CONFIG_PROPERTIES_SPLIT_CHAR));
        for (int i = 0; i < pairs.length; i++) {
            String pair = pairs[i];
            String[] keyValue = pair.split(CONFIG_PROPERTIES_REGEX.replace(SPLIT_CHAR_TOKEN, CONFIG_PROPERTIES_MAP_SPLIT_CHAR));
            map.put(keyValue[0], keyValue[1]);
        }
        return map;
    }

    private String getCollectionAsConfigValue(Collection<String> collection) {
        StringBuilder builder = new StringBuilder();
        Iterator<String> iterator = collection.iterator();
        while (iterator.hasNext()) {
            if (builder.length() > 0) {
                builder.append(CONFIG_PROPERTIES_SPLIT_CHAR);
            }
            builder.append(iterator.next());
        }
        return builder.toString();
    }

    private String getMapAsConfigValue(Map<String, String> map) {
        StringBuilder builder = new StringBuilder();
        Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            if (builder.length() > 0) {
                builder.append(CONFIG_PROPERTIES_SPLIT_CHAR);
            }
            Map.Entry<String, String> entry = iterator.next();
            builder.append(entry.getKey() + CONFIG_PROPERTIES_MAP_SPLIT_CHAR + entry.getValue());
        }
        return builder.toString();
    }
}
