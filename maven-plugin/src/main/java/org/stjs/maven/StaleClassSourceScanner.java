package org.stjs.maven;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.codehaus.plexus.compiler.util.scan.AbstractSourceInclusionScanner;
import org.codehaus.plexus.compiler.util.scan.InclusionScanException;
import org.codehaus.plexus.compiler.util.scan.mapping.SourceMapping;
import org.codehaus.plexus.compiler.util.scan.mapping.SuffixMapping;

/**
 * This class checks if the files corresponding to a source file are older than the corresponding .class. It checks this
 * instead of the source file's timestamp to be able to regenerate the code when a "clean" is made.
 *
 * @author jdcasey,acraciun
 * @version $Id: $Id
 */
@SuppressWarnings("rawtypes")
public class StaleClassSourceScanner extends AbstractSourceInclusionScanner {
	private static final String JAVA_CLASS_SUFFIX = ".java";

	private final long lastUpdatedWithinMsecs;

	private final Set sourceIncludes;

	private final Set sourceExcludes;

	private final SourceMapping classFileMapping;

	private final File classTargetDir;

	// ----------------------------------------------------------------------
	//
	// ----------------------------------------------------------------------

	/**
	 * <p>Constructor for StaleClassSourceScanner.</p>
	 *
	 * @param classTargetDir a {@link java.io.File} object.
	 */
	public StaleClassSourceScanner(File classTargetDir) {
		this(0, Collections.singleton("**/*"), Collections.EMPTY_SET, classTargetDir);
	}

	/**
	 * <p>Constructor for StaleClassSourceScanner.</p>
	 *
	 * @param lastUpdatedWithinMsecs a long.
	 * @param classTargetDir a {@link java.io.File} object.
	 */
	public StaleClassSourceScanner(long lastUpdatedWithinMsecs, File classTargetDir) {
		this(lastUpdatedWithinMsecs, Collections.singleton("**/*"), Collections.EMPTY_SET, classTargetDir);
	}

	/**
	 * <p>Constructor for StaleClassSourceScanner.</p>
	 *
	 * @param lastUpdatedWithinMsecs a long.
	 * @param sourceIncludes a {@link java.util.Set} object.
	 * @param sourceExcludes a {@link java.util.Set} object.
	 * @param classTargetDir a {@link java.io.File} object.
	 */
	public StaleClassSourceScanner(long lastUpdatedWithinMsecs, Set sourceIncludes, Set sourceExcludes,
			File classTargetDir) {
		this.lastUpdatedWithinMsecs = lastUpdatedWithinMsecs;

		this.sourceIncludes = sourceIncludes;

		this.sourceExcludes = sourceExcludes;

		this.classFileMapping = new SuffixMapping(".java", ".class");
		this.classTargetDir = classTargetDir;
	}

	private File getClassFile(File classTargetDir, String sourceFile) throws InclusionScanException {
		Set targetFiles = classFileMapping.getTargetFiles(classTargetDir, sourceFile);
		if (targetFiles.size() > 0) {
			return (File) targetFiles.iterator().next();
		}
		return null;
	}

	// ----------------------------------------------------------------------
	// SourceInclusionScanner Implementation
	// ----------------------------------------------------------------------

	/** {@inheritDoc} */
	@SuppressWarnings("unchecked")
	public Set getIncludedSources(File sourceDir, File targetDir) throws InclusionScanException {
		List srcMappings = getSourceMappings();

		if (srcMappings.isEmpty()) {
			return Collections.EMPTY_SET;
		}

		String[] potentialIncludes = scanForSources(sourceDir, sourceIncludes, sourceExcludes);

		Set matchingSources = new HashSet();

		for (int i = 0; i < potentialIncludes.length; i++) {
			String path = potentialIncludes[i];

			if (!path.endsWith(JAVA_CLASS_SUFFIX)) {
				continue;
			}

			File sourceFile = new File(sourceDir, path);
			File classFile = getClassFile(classTargetDir, path);
			if (classFile == null) {
				throw new RuntimeException("Cannot find class file for source: " + sourceFile);
			}

			staleSourceFileTesting: for (Iterator patternIt = srcMappings.iterator(); patternIt.hasNext();) {
				SourceMapping mapping = (SourceMapping) patternIt.next();

				Set targetFiles = mapping.getTargetFiles(targetDir, path);

				// never include files that don't have corresponding target mappings.
				// the targets don't have to exist on the filesystem, but the
				// mappers must tell us to look for them.
				for (Iterator targetIt = targetFiles.iterator(); targetIt.hasNext();) {
					File targetFile = (File) targetIt.next();

					if (!targetFile.exists()
							|| (targetFile.lastModified() + lastUpdatedWithinMsecs < classFile.lastModified())) {
						matchingSources.add(sourceFile);
						break staleSourceFileTesting;
					}
				}
			}
		}

		return matchingSources;
	}
}
