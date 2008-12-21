package org.meta_environment.rascal.interpreter.env;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.ast.FunctionDeclaration;
import org.meta_environment.rascal.ast.Name;
import org.meta_environment.rascal.ast.QualifiedName;
import org.meta_environment.rascal.interpreter.EvalResult;
import org.meta_environment.rascal.interpreter.Names;
import org.meta_environment.rascal.interpreter.RascalBug;

/**
 * An environment that implements the scoping rules of Rascal.
 * 
 */
public class  EnvironmentStack {
	protected final Stack<Environment> stack = new Stack<Environment>();
    
	public void clean(ModuleEnvironment bottom) {
		stack.clear();
		stack.push(bottom);
	}
	
	public EnvironmentStack(ModuleEnvironment bottom) {
		stack.push(bottom);
	}
	
	public void pushFrame() {
		stack.push(new Environment());
	}
	
	public void pushModule(ModuleEnvironment e) {
		if (e == null) { 
			throw new RascalBug("null environment");
		}
		stack.push(e);
	}

	public void pushFrame(Environment env) {
		stack.push(env);
	}

	public void popFrame() {
		stack.pop();
	}
	
	public void popModule() {
		stack.pop();
	}

	protected Environment bottom() {
		return stack.get(0);
	}
	
	protected Environment top() {
		return stack.peek();
	}
	
	public void storeFunction(String name, FunctionDeclaration function) {
		top().storeFunction(name, function);
	}

	public void storeVariable(String name, EvalResult value) {
		getVariableDefiningEnvironment(name).storeVariable(name, value);
	}
	
	public ModuleEnvironment getModuleEnvironment() {
		for (int i = stack.size() - 1; i >= 0; i--) {
			Environment env = stack.get(i);
			if (env.isModuleEnvironment()) {
				return (ModuleEnvironment) env;
			}
		}
		
		return (ModuleEnvironment) bottom();
	}

	public Environment getFunctionDefiningEnvironment(String name, Type formals, EnvironmentHolder h) {
		int i;
		
		//System.err.println("getFunctionDefiningEnvironment: stacksize=" + stack.size());
		for (i = stack.size() - 1; i >= 0; i--) {
			Environment env = stack.get(i);
			//System.err.println("stack(" + i + ")\n" + env);
			
			if (env.isModuleEnvironment()
					|| env.getFunction(name, formals, h) != null) {
				return env;
			}
		}
		
		return top();
	}
	
	public Environment getVariableDefiningEnvironment(String name) {
		int i;
		
		for (i = stack.size() - 1; i >= 0; i--) {
			Environment env = stack.get(i);
			
            if (env.isModuleEnvironment() || env.getVariable(name) != null) {
            	
            	return env;
            }
		}
		
		return top();
	}

	public Map<Type, Type> getTypeBindings() {
		Map<Type,Type> types = new HashMap<Type,Type>();
		
		for (int i = 0; i < stack.size(); i++) {
			Environment environment = stack.get(i);
			types.putAll(environment.getTypeBindings());
		}
		
		// result can not be given to a match
		return Collections.unmodifiableMap(types);
	}

	public void storeParameterType(Type par, Type type) {
		bottom().storeParameterType(par, type);
	}
	
	public void storeTypeBindings(Map<Type, Type> bindings) {
		top().storeTypeBindings(bindings);
	}

	public FunctionDeclaration getFunction(Name name, Type actuals, EnvironmentHolder h) {
		return getFunction(Names.name(name), actuals, h);
	}
	
	public FunctionDeclaration getFunction(String name, Type actuals, EnvironmentHolder h) {
		Environment env = getFunctionDefiningEnvironment(name, actuals, h);
		return env.getFunction(name, actuals, h);
	}
	
	public Type getParameterType(Type par) {
		return bottom().getParameterType(par);
	}

	public EvalResult getVariable(String name) {
		Environment env = getVariableDefiningEnvironment(name);
		return env.getVariable(name);
	}

	public void storeNamedType(Type decl) {
		getModuleEnvironment().storeNamedType(decl);
	}

	public void storeNamedTreeType(Type decl) {
		getModuleEnvironment().storeNamedTreeType(decl);
	}

	public void storeTreeNodeType(Type decl) {
		getModuleEnvironment().storeTreeNodeType(decl);
	}

	public FunctionDeclaration getFunction(QualifiedName name, Type actuals, EnvironmentHolder h) {
		return getFunction(Names.lastName(name), actuals, h);
	}

	public EvalResult getVariable(QualifiedName name) {
		return getVariable(Names.lastName(name));
	}

	public void storeFunction(QualifiedName name, FunctionDeclaration function) {
		storeFunction(Names.lastName(name), function);
		
	}

	public void storeVariable(QualifiedName name, EvalResult value) {
		storeVariable(Names.lastName(name), value);
	}

	public EvalResult getVariable(Name name) {
		return getVariable(Names.name(name));
	}

	public void storeFunction(Name name, FunctionDeclaration function) {
		storeFunction(Names.name(name), function);
		
	}

	public void storeVariable(Name name, EvalResult value) {
		storeVariable(Names.name(name), value);
	}

	public Type getNamedTreeType(String sort) {
		return getModuleEnvironment().getNamedTreeType(sort);
	}

	public Type getTreeNodeType(Type sort, String cons,
			Type args) {
		return getModuleEnvironment().getTreeNodeType(sort, cons, args);
	}

	public void storeAnnotation(Type onType, String name, Type annoType) {
		getModuleEnvironment().storeAnnotation(onType, name, annoType);
	}

	public Type getTreeNodeType(String cons, Type args) {
		return getModuleEnvironment().getTreeNodeType(cons, args);
	}

}
