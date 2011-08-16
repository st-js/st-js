/**
 *  Copyright 2011 Alexandru Craciun, Eyal Kaspi
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.stjs.maven;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.codehaus.plexus.compiler.util.scan.mapping.SourceMapping;

/**
 * This class maps a source to a target folder by both changing the suffix and compressing the target folder by
 * suppressing a part of the folder structure. That is it will transform a file [src folder]/org/stjs/example/Test.java
 * in [target folder]/Test.java
 * 
 * @author <a href='mailto:ax.craciun@gmail.com'>Alexandru Craciun</a>
 * 
 */
public class SuffixMappingWithCompressedTarget implements SourceMapping {
	private final String sourceSuffix;

	private final String targetSuffix;

	private final String removePrefix;

	public SuffixMappingWithCompressedTarget(String removePrefix, String sourceSuffix, String targetSuffix) {
		this.sourceSuffix = sourceSuffix;

		this.targetSuffix = targetSuffix;
		this.removePrefix = removePrefix;
	}

	@Override
	public Set<File> getTargetFiles(File targetDir, String source) {
		Set<File> targetFiles = new HashSet<File>();

		if (source.endsWith(sourceSuffix) && source.startsWith(removePrefix)) {
			String base = source.substring(removePrefix.length());
			base = base.substring(0, base.length() - sourceSuffix.length());

			targetFiles.add(new File(targetDir, base + targetSuffix));

		}

		return targetFiles;
	}
}
