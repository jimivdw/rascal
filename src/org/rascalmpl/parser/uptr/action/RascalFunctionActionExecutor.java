/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 */
package org.rascalmpl.parser.uptr.action;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.control_exceptions.Failure;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.result.ICallableValue;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.staticErrors.ArgumentsMismatchError;
import org.rascalmpl.interpreter.types.RascalTypeFactory;
import org.rascalmpl.parser.gtd.result.action.IActionExecutor;
import org.rascalmpl.values.uptr.TreeAdapter;

/**
 * This is the way of executing actions for Rascal syntax definitions. Each function
 * that returns a non-terminal type and is named one of the constructor names of one
 * of the alternatives and has the same argument types as the syntax production will
 * be called when a certain production is constructed, e.g:
 * 
 * Stat if(Exp e, Stat thenPart, Stat elsePart);
 * 
 * Also, on ambiguity clusters functions named 'amb' are called with a set[&T] argument
 * for the alternatives, e.g. Stat amb(set[Stat] alternatives);
 * 
 * Also, on entering a production the 'enter' function is called with a reifed type argument 
 * for the production type that is entered: void enter(type[Stat.If] prod);
 * 
 * Also on exiting a production the 'exit' function is called, similarly:
 * void exit(type[Stat.If] prod);
 * 
 * Note that RascalFunctionActionExecutors use functions visible from the call site of the parse
 * function.
 */
public class RascalFunctionActionExecutor implements IActionExecutor {
	private final static TypeFactory TF = TypeFactory.getInstance();
	private final IEvaluatorContext ctx;

	public RascalFunctionActionExecutor(IEvaluatorContext ctx) {
		this.ctx = ctx;
	}
	
	public void completed(Object environment, boolean filtered) {

	}

	public Object createRootEnvironment() {
		return ctx.getCurrentEnvt();
	}

	public Object enteringListNode(IConstructor production, int index,
			Object environment) {
		return environment;
	}

	public Object enteringListProduction(IConstructor production,
			Object env) {
		return env;
	}

	public Object enteringNode(IConstructor production, int index,
			Object environment) {
		return environment;
	}

	public Object enteringProduction(IConstructor production,
			Object env) {
		return env;
	}

	public void exitedListProduction(IConstructor production, boolean filtered,
			Object environment) {

	}

	public void exitedProduction(IConstructor production, boolean filtered,
			Object environment) {
	}

	public IConstructor filterAmbiguity(IConstructor ambCluster,
			Object environment) {
		ISet alts = (ISet) ambCluster.get("alternatives");
		
		if (alts.size() == 0) {
			return null;
		}
		
		Environment env = (Environment) environment;
		
		Result<IValue> var = env.getVariable("amb");
		
		if (var != null && var instanceof ICallableValue) {
			Type type = RascalTypeFactory.getInstance().nonTerminalType(ambCluster);
			ICallableValue func = (ICallableValue) var;
			try {
				return (IConstructor) func.call(
						new Type[] {TF.setType(type)}, new IValue[] {alts}
				);
			}
			catch (ArgumentsMismatchError e) {
				return ambCluster;
			}
		}
		
		return ambCluster;
	}

	public IConstructor filterCycle(IConstructor cycle, Object environment) {
		return cycle;
	}

	public IConstructor filterListAmbiguity(IConstructor ambCluster,
			Object environment) {
		return filterAmbiguity(ambCluster, environment);
	}

	public IConstructor filterListCycle(IConstructor cycle,
			Object environment) {
		return cycle;
	}

	public IConstructor filterListProduction(IConstructor tree,
			Object environment) {
		return tree;
	}

	public IConstructor filterProduction(IConstructor tree,
			Object environment) {
		String cons = TreeAdapter.getConstructorName(tree);
		
		if (cons != null) {
			Environment env = (Environment) environment;
			Result<IValue> var = env.getVariable(cons);
			
			if (var != null && var instanceof ICallableValue) {
				ICallableValue function = (ICallableValue) var;
				
				try{
					// First try without layout and literal args and an actual parameter for each "field"
					IConstructor result = call(function, TreeAdapter.getASTArgs(tree));
					if(result == null){
						result = call(function, TreeAdapter.getArgs(tree));
					}
					
					if(result == null){
						System.err.println("ERROR: action failed: " + function.getType());
						return tree; // TODO Handle the error properly.
					}
					
					return result;
				}catch(Failure f){
					return null;
				}
			}
		}
		
		return tree;
	}

	private IConstructor call(ICallableValue function, IList args) {
		try{
			int nrOfArgs = args.length();
			Type[] types = new Type[nrOfArgs];
			IValue[] actuals = new IValue[nrOfArgs];
			
			for(int i = nrOfArgs - 1; i >= 0; --i){
				IValue arg = args.get(i);
				types[i] = arg.getType();
				actuals[i] = arg;
			}
			
			return (IConstructor) function.call(types, actuals);
		}catch(ArgumentsMismatchError e){
			return null;
		}
	}

	public boolean isImpure(IConstructor rhs) {
		return true;
	}

}
