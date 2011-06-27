package org.stjs.generator.handlers.utils;

import static org.stjs.generator.PreConditions.checkNotNull;
import japa.parser.ast.Node;

import java.util.Iterator;

import org.stjs.generator.handlers.RuleBasedVisitor;

public class Joiner {

    private final String separator;
	private final RuleBasedVisitor visitor; 
    
    private Joiner(String separator, RuleBasedVisitor visitor) {
        this.visitor = checkNotNull(visitor);
		this.separator = checkNotNull(separator);
    }
    
    public void join(Iterable<? extends Node> nodes, Object arg) {
    	if (nodes != null) { 
    		Iterator<? extends Node> iterator = nodes.iterator();
	    	if (iterator.hasNext()) {
	             iterator.next().accept(visitor, arg);
	             while (iterator.hasNext()) {
	                 visitor.getPrinter().print(separator);
	                 iterator.next().accept(visitor, arg);
	             }
	         }
    	}
    }
    
    public static JoinerBuilder joiner(RuleBasedVisitor visitor) {
    	return new JoinerBuilder(visitor);
    }
    
    public static class JoinerBuilder {
    	private final RuleBasedVisitor visitor; 
    	
    	public JoinerBuilder(RuleBasedVisitor visitor) {
            this.visitor = checkNotNull(visitor);
        }
    	
    	public Joiner on(String separator) {
    		return new Joiner(separator, visitor);
    	}
    }
    
 

}
