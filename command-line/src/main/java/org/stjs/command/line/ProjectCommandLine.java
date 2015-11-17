package org.stjs.command.line;

import static com.google.common.collect.Lists.newArrayList;

import java.io.File;
import java.util.List;

public class ProjectCommandLine {

	public static void main(String[] args) {
		if (args.length != 3) {
			System.err.println("Usage: st-js <srcDir> <libDir> <outputDir>");
			return;
		}
		String path = args[0];
		if (!path.endsWith(File.separator)) {
			path += File.separator;
		}
		String libDir = args[1];
		List<File> dependencies = listFiles(new File(libDir), ".jar");

		String outputDir = args[2];
		List<File> classNames = listFiles(new File(path), ".java");

		CommandLine.compile(path, classNames, dependencies);
		generate(path, classNames, dependencies, outputDir);

	}

	private static void generate(String path, List<File> files, List<File> dependencies, String outputDir) {
		File srcPath = new File(path);
		for (File file : files) {
			// remove the leading srcPath from each file to get the source name
			CommandLine.generate(
					path,
					file.getAbsolutePath().substring(srcPath.getAbsolutePath().length() + 1).replace(".java", "")
							.replace(File.separatorChar, '.'), dependencies, outputDir);
		}
	}

	private static List<File> listFiles(File srcDir, String suffix) {
		List<File> files = newArrayList();
		listFiles0(srcDir, files, suffix);
		return files;
	}

	private static void listFiles0(File srcDir, List<File> output, String suffix) {
		File[] files = srcDir.listFiles();
		if(files == null){
			throw new IllegalArgumentException("Not a directory: " + srcDir);
		}
		for (File file : srcDir.listFiles()) {
			if (file.isDirectory()) {
				listFiles0(file, output, suffix);
			} else {
				if (file.getName().endsWith(suffix)) {
					output.add(file);
				}
			}
		}
	}
}