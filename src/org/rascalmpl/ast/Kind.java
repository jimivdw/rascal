/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Tijs van der Storm - Tijs.van.der.Storm@cwi.nl
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Mark Hills - Mark.Hills@cwi.nl (CWI)
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
 *   * Michael Steindorfer - Michael.Steindorfer@cwi.nl - CWI
 *******************************************************************************/
package org.rascalmpl.ast;


import org.eclipse.imp.pdb.facts.IConstructor;

public abstract class Kind extends AbstractAST {
  public Kind(IConstructor node) {
    super();
  }

  

  

  
  public boolean isAlias() {
    return false;
  }

  static public class Alias extends Kind {
    // Production: sig("Alias",[])
  
    
  
    public Alias(IConstructor node ) {
      super(node);
      
    }
  
    @Override
    public boolean isAlias() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitKindAlias(this);
    }
  
    	
  }
  public boolean isAll() {
    return false;
  }

  static public class All extends Kind {
    // Production: sig("All",[])
  
    
  
    public All(IConstructor node ) {
      super(node);
      
    }
  
    @Override
    public boolean isAll() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitKindAll(this);
    }
  
    	
  }
  public boolean isAnno() {
    return false;
  }

  static public class Anno extends Kind {
    // Production: sig("Anno",[])
  
    
  
    public Anno(IConstructor node ) {
      super(node);
      
    }
  
    @Override
    public boolean isAnno() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitKindAnno(this);
    }
  
    	
  }
  public boolean isData() {
    return false;
  }

  static public class Data extends Kind {
    // Production: sig("Data",[])
  
    
  
    public Data(IConstructor node ) {
      super(node);
      
    }
  
    @Override
    public boolean isData() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitKindData(this);
    }
  
    	
  }
  public boolean isFunction() {
    return false;
  }

  static public class Function extends Kind {
    // Production: sig("Function",[])
  
    
  
    public Function(IConstructor node ) {
      super(node);
      
    }
  
    @Override
    public boolean isFunction() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitKindFunction(this);
    }
  
    	
  }
  public boolean isModule() {
    return false;
  }

  static public class Module extends Kind {
    // Production: sig("Module",[])
  
    
  
    public Module(IConstructor node ) {
      super(node);
      
    }
  
    @Override
    public boolean isModule() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitKindModule(this);
    }
  
    	
  }
  public boolean isTag() {
    return false;
  }

  static public class Tag extends Kind {
    // Production: sig("Tag",[])
  
    
  
    public Tag(IConstructor node ) {
      super(node);
      
    }
  
    @Override
    public boolean isTag() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitKindTag(this);
    }
  
    	
  }
  public boolean isVariable() {
    return false;
  }

  static public class Variable extends Kind {
    // Production: sig("Variable",[])
  
    
  
    public Variable(IConstructor node ) {
      super(node);
      
    }
  
    @Override
    public boolean isVariable() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitKindVariable(this);
    }
  
    	
  }
  public boolean isView() {
    return false;
  }

  static public class View extends Kind {
    // Production: sig("View",[])
  
    
  
    public View(IConstructor node ) {
      super(node);
      
    }
  
    @Override
    public boolean isView() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitKindView(this);
    }
  
    	
  }
}