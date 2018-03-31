package org.stjs.generator.executor;

import com.google.common.base.Charsets;
import com.google.common.io.Closeables;
import com.google.common.io.Files;

import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.Reader;
import java.util.Collection;

public interface Executor {
	/**
	 * <p>run.</p>
	 *
	 * @param srcFiles          a {@link java.util.Collection} object.
	 * @param mainClassDisabled a boolean.
	 * @return a {@link org.stjs.generator.executor.ExecutionResult} object.
	 * @throws javax.script.ScriptException if any.
	 */
	public ExecutionResult run(Collection<File> srcFiles, boolean mainClassDisabled) throws ScriptException;
}
