/*******************************************************************************
 * Copyright (c) 2022 THALES GLOBAL SERVICES and others.
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.business.api.logger;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import org.eclipse.sirius.business.internal.logger.RuntimeLoggerInterpreterImpl;


/**
 * Builder for invocation ensuring an always clean interpreter when done.
 * <p>
 * Such instance is meant to be used in 
 * </p>
 * 
 * @author nperansin
 */
public final class InterpretationContext {

    private List<String> variables;
    private final RuntimeLoggerInterpreterImpl implementation;
    
    
    /**
     * Default constructor.
     * 
     * @param interpreter to use
     */
    private InterpretationContext(RuntimeLoggerInterpreter interpreter) {
        if (!(interpreter instanceof RuntimeLoggerInterpreterImpl)) {
            throw new UnsupportedOperationException("This context can only operate on specific implementation.");  //$NON-NLS-1$            
        }
        implementation = (RuntimeLoggerInterpreterImpl) interpreter;
    }

    /**
     * Sets a variable.
     * 
     * @param name
     *            the name of the variable.
     * @param value
     *            the value of the variable.
     */
    public void setVariable(String name, Object value) {
        if (variables == null) {
            variables = new ArrayList<>();
        }
        implementation.getDecorated().setVariable(name, value);
    }
    
    /**
     * Enables if errors are logged.
     * <p>
     * When this context is closed, error logging is always restored.
     * </p>
     * 
     * @param log error
     */
    public void setLogError(boolean log) {
        implementation.setLogError(log);
    }
    
    private void close() {
        if (variables != null) {
            variables.forEach(it -> implementation.getDecorated().unSetVariable(it));
        }
        setLogError(true);
    }
    
    /**
     * Provide a cleanable context to evaluate expressions.
     * 
     * @param interpreter to use
     * @param task to perform
     */
    public static void with(RuntimeLoggerInterpreter interpreter, Consumer<InterpretationContext> task) {
        InterpretationContext ctx = new InterpretationContext(interpreter);
        try { // Cannot use closable unless by exposing IOException
            task.accept(ctx);
        } finally {
            ctx.close();
        }
    }
    
    /**
     * Provide a cleanable context to evaluate expressions.
     * 
     * @param <T> type of task
     * @param interpreter to use
     * @param task to perform
     * @return task result
     */
    public static <T> T with(RuntimeLoggerInterpreter interpreter, Function<InterpretationContext, T> task) {
        InterpretationContext ctx = new InterpretationContext(interpreter);
        try { // Cannot use closable unless by exposing IOException
            return task.apply(ctx);
        } finally {
            ctx.close();
        }
    }
    
    


}
