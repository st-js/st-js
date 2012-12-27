package org.stjs.testing.driver;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.stjs.javascript.annotation.STJSBridge;

@STJSBridge
public class STJSAsyncTestDriverRunner extends BlockJUnit4ClassRunner{

	private final DriverConfiguration config;
	private final List<AsyncBrowserSession> browsers;
	private final AsyncServerSession serverSession;

	public STJSAsyncTestDriverRunner(Class<?> klass) throws InitializationError, IOException {
		super(klass);

		config = new DriverConfiguration(klass);

		serverSession = AsyncServerSession.getInstance(config);

		browsers = new CopyOnWriteArrayList<AsyncBrowserSession>();
		for (int i = 0; i < config.getBrowserCount(); i ++){
			final AsyncBrowserSession browser = new AsyncBrowserSession(new PhantomjsBrowser(config), i);
			browsers.add(browser);
			serverSession.addBrowserConnection(browser);

			// let's start the browsers asynchronously. Synchronization will
			// be done later when the browser GET's and URL from the server
			new Thread(new Runnable(){
				@Override
				public void run() {
					browser.start();
				}
			},"browser-" + i).start();
		}
	}

	@Override
	protected Statement methodBlock(final FrameworkMethod method){
		return new Statement() {
			@Override
			public void evaluate() throws Throwable {
				if(config.isDebugEnabled()){
					System.out.println("Executing Statement for " + method.getMethod().toString());
				}
				AsyncMethod aMethod = new AsyncMethod(getTestClass(), method, config.getBrowserCount());

				for(AsyncBrowserSession browser : browsers){
					serverSession.queueTest(aMethod, browser);
				}

				aMethod.awaitExecutionResult();
			}
		};
	}

	//	@Override
	//	protected Statement methodBlock(final FrameworkMethod method) {
	//		return new Statement() {
	//
	//			@Override
	//			@SuppressWarnings("deprecation")
	//			public void evaluate() throws Throwable {
	//
	//				ClassWithJavascript stjsClass = new Generator().getExistingStjsClass(config.getClassLoader(),
	//						getTestClass().getJavaClass());
	//
	//				final HTMLFixture htmlFixture = getTestClass().getJavaClass().getAnnotation(HTMLFixture.class);
	//
	//				final Scripts addedScripts = getTestClass().getJavaClass().getAnnotation(Scripts.class);
	//				final ScriptsBefore addedScriptsBefore = getTestClass().getJavaClass().getAnnotation(
	//						ScriptsBefore.class);
	//				final ScriptsAfter addedScriptsAfter = getTestClass().getJavaClass().getAnnotation(ScriptsAfter.class);
	//				File htmlFile = null;
	//				FileWriter writer = null;
	//				try {
	//					targetDirectory.mkdirs();
	//					htmlFile = new File(targetDirectory, getTestClass().getJavaClass().getName() + "-"
	//							+ method.getName() + ".html");
	//					writer = new FileWriter(htmlFile);
	//
	//					writer.append("<html>");
	//					writer.append("<head>");
	//					addScript(writer, "/stjs.js");
	//					addScript(writer, "/junit.js");
	//
	//					writer.append("<script language='javascript'>stjs.mainCallDisabled=true;</script>\n");
	//
	//					// scripts added explicitly
	//					if (addedScripts != null) {
	//						for (String script : addedScripts.value()) {
	//							addScript(writer, script);
	//						}
	//					}
	//					// scripts before - new style
	//					if (addedScriptsBefore != null) {
	//						for (String script : addedScriptsBefore.value()) {
	//							addScript(writer, script);
	//						}
	//					}
	//
	//					Set<URI> jsFiles = new LinkedHashSet<URI>();
	//					for (ClassWithJavascript dep : new DependencyCollection(stjsClass).orderAllDependencies(config
	//							.getClassLoader())) {
	//
	//						if (addedScripts != null && dep instanceof BridgeClass) {
	//							// bridge dependencies are not added when using @Scripts
	//							System.out
	//									.println("WARNING: You're using @Scripts deprecated annotation that disables the automatic inclusion of the Javascript files of the bridges you're using! "
	//											+ "Please consider using @ScriptsBefore and/or @ScriptsAfter instead.");
	//							continue;
	//						}
	//						for (URI file : dep.getJavascriptFiles()) {
	//							jsFiles.add(file);
	//						}
	//					}
	//
	//					for (URI file : jsFiles) {
	//						addScript(writer, file.toString());
	//					}
	//
	//					// scripts after - new style
	//					if (addedScriptsAfter != null) {
	//						for (String script : addedScriptsAfter.value()) {
	//							addScript(writer, script);
	//						}
	//					}
	//					writer.append("<script language='javascript'>");
	//					writer.append("onload=function(){");
	//
	//					// Adapter between generated assert (not global) and JS-test-driver assert (which is a
	//					// set of global methods)
	//					writer.append("Assert=window;");
	//
	//					String testedClassName = getTestClass().getJavaClass().getSimpleName();
	//
	//					writer.append("try{");
	//					writer.append("new " + testedClassName + "()." + method.getName() + "();");
	//					writer.append("parent.sendOK('" + testedClassName + "." + method.getName() + "');");
	//					writer.append("}catch(ex){");
	//					writer.append("parent.sendError('" + testedClassName + "." + method.getName() + "', ex);");
	//					writer.append("}");
	//					writer.append("}");
	//					writer.append("</script>");
	//					writer.append("</head>");
	//					writer.append("<body>");
	//					if (htmlFixture != null) {
	//						if (!Strings.isNullOrEmpty(htmlFixture.value())) {
	//							writer.append(htmlFixture.value());
	//						} else if (!Strings.isNullOrEmpty(htmlFixture.url())) {
	//							StreamUtils.copy(serverSession.getServer().getClassLoader(), htmlFixture.url(), writer);
	//						}
	//					}
	//					writer.append("</body>");
	//					writer.append("</html>");
	//
	//					writer.flush();
	//					writer.close();
	//
	//					if (config.isDebugEnabled()) {
	//						System.out.println("Added source file");
	//						Files.copy(htmlFile, System.out);
	//						System.out.flush();
	//					}
	//					TestResultCollection response = serverSession.getServer().test(htmlFile, testedClassName,
	//							method.getName());
	//					if (!response.isOk()) {
	//						// take the first wrong result
	//						throw response.buildException(getTestClass().getJavaClass().getName(), method.getName());
	//					}
	//				} catch (IOException e) {
	//					throw new RuntimeException(e);
	//				} finally {
	//					Closeables.closeQuietly(writer);
	//					if (htmlFile != null) {
	//						htmlFile.delete();
	//					}
	//				}
	//			}
	//		};

}
