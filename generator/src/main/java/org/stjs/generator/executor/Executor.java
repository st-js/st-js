package org.stjs.generator.executor;

import javax.script.ScriptException;
import java.io.File;
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
	ExecutionResult run(Collection<File> srcFiles, boolean mainClassDisabled) throws ScriptException;
}
