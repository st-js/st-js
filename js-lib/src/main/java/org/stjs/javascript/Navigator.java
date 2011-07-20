package org.stjs.javascript;

abstract public class Navigator {
	public String appCodeName;
	public String appName;
	public String appVersion;
	public boolean cookieEnabled;
	public String platform;
	public String userAgent;

	abstract public boolean javaEnabled();

	abstract public boolean taintEnabled();
}
