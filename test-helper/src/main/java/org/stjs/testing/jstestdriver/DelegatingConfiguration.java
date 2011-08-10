package org.stjs.testing.jstestdriver;

import java.io.File;
import java.util.List;
import java.util.Set;
import com.google.gson.JsonArray;
import com.google.jstestdriver.FileInfo;
import com.google.jstestdriver.Flags;
import com.google.jstestdriver.PathResolver;
import com.google.jstestdriver.Plugin;
import com.google.jstestdriver.config.Configuration;
import com.google.jstestdriver.model.HandlerPathPrefix;

public class DelegatingConfiguration implements Configuration {

  private Configuration delegate;
  
  public DelegatingConfiguration(Configuration delegate) {
    this.delegate = delegate;
  }

  @Override
  public Set<FileInfo> getFilesList() {
    return delegate.getFilesList();
  }

  @Override
  public String getServer(String paramString, int paramInt, HandlerPathPrefix paramHandlerPathPrefix) {
    return delegate.getServer(paramString, paramInt, paramHandlerPathPrefix);
  }

  @Override
  public List<Plugin> getPlugins() {
    return delegate.getPlugins();
  }

  @Override
  public long getTestSuiteTimeout() {
    return delegate.getTestSuiteTimeout();
  }

  @Override
  public Configuration resolvePaths(PathResolver paramPathResolver, Flags paramFlags) {
    this.delegate = delegate.resolvePaths(paramPathResolver, paramFlags);
    return this;
  }

  @Override
  public List<FileInfo> getTests() {
    return delegate.getTests();
  }

  @Override
  public File getBasePath() {
    return delegate.getBasePath();
  }

  @Override
  public JsonArray getProxyConfiguration() {
    return delegate.getProxyConfiguration();
  }


}
