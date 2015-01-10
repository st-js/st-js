package org.stjs.maven;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.List;

import com.google.common.io.Files;
import com.google.debugging.sourcemap.FilePosition;
import com.google.debugging.sourcemap.SourceMapConsumerV3;
import com.google.debugging.sourcemap.SourceMapConsumerV3.EntryVisitor;
import com.google.debugging.sourcemap.SourceMapGeneratorV3;
import com.google.debugging.sourcemap.SourceMapParseException;

/**
 * remake the source map merge of closure compiler in order to allow the rewrite of the original source map's path
 * 
 * @author acraciun
 */
public class SourceMapUtils {

	public static int appendFileSkipSourceMap(File gendir, OutputStream allSourcesFile, File jsFile, int currentLine,
			SourceMapGeneratorV3 packSourcemap, String charsetName) throws IOException, SourceMapParseException {
		Charset charset = charsetName != null ? Charset.forName(charsetName) : Charset.defaultCharset();
		List<String> lines = Files.readLines(jsFile, charset);
		// remove the @SourceMap stuff
		for (int i = 0; i < lines.size() - 1; ++i) {
			allSourcesFile.write(lines.get(i).getBytes());
			allSourcesFile.write('\n');
		}

		File sourcemapFile = new File(jsFile.getParentFile(), Files.getNameWithoutExtension(jsFile.getName()) + ".map");
		String relSourceMapFile = getRelativePath(jsFile, gendir);
		mergeMapSection(new File(relSourceMapFile), packSourcemap, currentLine, 1, Files.toString(sourcemapFile, charset));
		return currentLine + lines.size() - 1;
	}

	private static String getRelativePath(File file, File folder) {
		// remove the common folder name from the target file name
		return file.getAbsolutePath().substring(folder.getAbsolutePath().length() + 1);
	}

	public static void mergeMapSection(File sourceMapFile, SourceMapGeneratorV3 packSourcemap, int line, int column, String mapSectionContents)
			throws SourceMapParseException {
		packSourcemap.setStartingPosition(line, column);
		SourceMapConsumerV3 section = new SourceMapConsumerV3();
		section.parse(mapSectionContents);
		section.visitMappings(new ConsumerEntryVisitor(packSourcemap, sourceMapFile));
	}

	static class ConsumerEntryVisitor implements EntryVisitor {
		private final SourceMapGeneratorV3 packSourcemap;
		private final File sourceMapFile;

		public ConsumerEntryVisitor(SourceMapGeneratorV3 packSourcemap, File sourceMapFile) {
			this.packSourcemap = packSourcemap;
			this.sourceMapFile = sourceMapFile;
		}

		@Override
		public void visit(String sourceName, String symbolName, FilePosition sourceStartPosition, FilePosition startPosition,
				FilePosition endPosition) {
			packSourcemap.addMapping(new File(sourceMapFile.getParentFile(), sourceName).getPath().replace(File.separatorChar, '/'), symbolName,
					sourceStartPosition, startPosition, endPosition);
		}
	}
}
