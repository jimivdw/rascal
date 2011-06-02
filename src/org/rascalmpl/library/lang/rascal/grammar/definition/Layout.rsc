@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@bootstrapParser

module lang::rascal::grammar::definition::Layout

import lang::rascal::syntax::RascalRascal;
import lang::rascal::grammar::definition::Modules;
import Grammar;
import ParseTree;
import List;


public GrammarDefinition \layouts(GrammarDefinition def) {
  deps = extends(def) + imports(def);
  for (str name <- def.modules) {
    def.modules[name].grammar = layouts(def.modules[name].grammar, activeLayout(name, deps[name], def));
  }
  return def;
}

@doc{computes which layout definitions are visible in a certain given module.
     if a module contains a layout definition, this overrides any imported layout definition
     if a module does not contain a layout definition, it will collect the definitions from all imports (not recursively),
     and also collect the definitions from all extends (recursively).
     the static checker should check whether multiple visible layout definitions are active, because this function
     will just produce an arbitrary one if there are multiple definitions
}
public Symbol activeLayout(str name, set[str] deps, GrammarDefinition def) {
  if (/prod(_,l:layouts(_),_) := def.modules[name]) 
    return l;
  else if (i <- deps, /prod(_,l:layouts(_),_) := def.modules[i]) 
    return l;
  else 
    return layouts("***default***"); // TODO: replace this by something like layouts(empty()), (high impact change) 
}  

@doc{intersperses layout symbols in all non-lexical productions}
public Grammar \layouts(Grammar g, Symbol l) {
  return top-down-break visit (g) {
    case prod([Symbol x],start(x),as) => prod([l, x, l], start(x), as)
    case prod(list[Symbol] lhs,Symbol rhs,attrs(list[Attr] as)) => prod(intermix(lhs, l),rhs,attrs(as)) 
      when start(_) !:= rhs, \lex() notin as  
    case prod(list[Symbol] lhs,Symbol rhs,\no-attrs()) => prod(intermix(lhs, l),rhs,\no-attrs()) 
      when start(_) !:= rhs
  }
} 

private list[Symbol] intermix(list[Symbol] syms, Symbol l) {
  if (syms == []) 
    return syms;
  return tail([l, s | s <- syms]);
}