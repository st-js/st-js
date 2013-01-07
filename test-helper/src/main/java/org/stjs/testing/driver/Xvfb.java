package org.stjs.testing.driver;

import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Controls an Xvfb (X virtual frame buffer) server so that any browser can be started in a headless way on Linux. Most of the code from this
 * class was shamelessly ripped from the selenium-maven-plugin (though the xauth stuff was intentionally left out)s
 * @author lordofthepigs
 */
public class Xvfb implements AsyncProcess {

	/** The default display to use. SSH usualy eats up :10, so lets use :20. That starts at port 6020. */
	private static final int DEFAULT_DISPLAY_NUMBER = 20;
	/** A pattern that matches the syntax for the unix DISPLAY environment variable */
	private static final Pattern DISPLAY_EXP = Pattern.compile("[^:]*:([0-9]*)(\\.([0-9]*))?");

	/** The 'Xvfb' command to execute. */
	// TODO: read this from the configuration file
	private String xvfbExecutable = "Xvfb";

	/** The X11 display to use. Default value is <tt>:20</tt>. */
	private String display;

	/** A list of additional options to pass to the Xvfb process. */
	// TODO: read this from the configuration file
	private String[] options = new String[0];

	/** The file that Xvfb output will be written to. */
	// TODO: not implemented yet
	private File logFile;

	private final DriverConfiguration config;
	private Process xvfbProcess;

	public Xvfb(DriverConfiguration config) {
		this.config = config;

		// TODO: load the other fields according to the system properties in config 
	}

	@Override
	public void start() {
		if (config.isDebugEnabled()) {
			System.out.println("Starting Xvfb...");
		}

		// Figure out what the display number is, and generate the properties file
		if (display == null) {
			display = detectUsableDisplay();
		} else {
			if (isDisplayInUse(display)) {
				throw new RuntimeException("It appears that the configured display is already in use: " + display);
			}
		}

		if (config.isDebugEnabled()) {
			System.out.println("Using display: " + display);
		}

		List<String> command = new ArrayList<String>();
		command.add(this.xvfbExecutable);
		command.add(this.display);
		for (String option : this.options) {
			command.add(option);
		}

		ProcessBuilder pb = new ProcessBuilder();
		pb.command(command);

		try {
			xvfbProcess = pb.start();
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}

		if (this.logFile != null) {
			// TODO: figure out how to redirect output
			// log.info("Redirecting output to: $logFile")
			// redirector(output: logFile)
		}

		// TODO: wait until the process is really started before returning

		//	       
		//	        launcher.verifier = {
		//	            def success = isDisplayInUse(display)
		//	            if (success && reuse) {
		//	                System.setProperty(DISPLAY_FILE_PROP, displayPropertiesFile.getAbsolutePath())
		//	            }
		//	            return success
		//	        }
	}

	/**
	 * Detect which display is usable.
	 */
	private String detectUsableDisplay() {
		if (config.isDebugEnabled()) {
			System.out.println("Detecting a usable display...");
		}

		boolean found = false;
		int n = DEFAULT_DISPLAY_NUMBER;

		while (!found && (n <= DEFAULT_DISPLAY_NUMBER + 10)) {
			String d = ":" + n;
			if (config.isDebugEnabled()) {
				System.out.println("Trying display: " + n);
			}

			if (!isDisplayInUse(d)) {
				return d;
			} else {
				n++;
			}
		}

		throw new RuntimeException("Could not find a usable display");
	}

	/**
	 * Decode the port number for the display.
	 */
	private int decodeDisplayPort(String display) {
		assert display != null;

		Matcher m = DISPLAY_EXP.matcher(display);
		m.matches();

		int i = Integer.parseInt(m.group(1));

		// Normally, the first X11 display is on port 6000, the next on port 6001,
		// which get abbreviated as :0, :1 and so on.
		return 6000 + i;
	}

	/**
	 * Check if the given display is in use or not.
	 */
	private boolean isDisplayInUse(String display) {
		int port = decodeDisplayPort(display);

		if (config.isDebugEnabled()) {
			System.out.println("Checking if display is in use: " + display + " on port: " + port);
		}

		try {
			Socket socket = new Socket("localhost", port);
			socket.close();
			return true;
		}
		catch (ConnectException e) {
			return false;
		}
		catch (UnknownHostException e) {
			// can't really happen, unless something is really screwed up on the target system
			return false;
		}
		catch (IOException e) {
			return false;
		}
	}

	@Override
	public void stop() {
		xvfbProcess.destroy();
	}

	public String getDisplay() {
		return this.display;
	}
}
