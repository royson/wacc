package frontend;

import java.util.HashMap;

public class SymbolTable<V,O> {
	
	HashMap<V,O> st;
	SymbolTable<V,O> encSymTable;
	
	public SymbolTable(){
		st = new HashMap<V,O>();
	}
	
	public SymbolTable(SymbolTable<V,O> encSymTable){
		st = new HashMap<V,O>();
		this.encSymTable = encSymTable;
	}
	
	public void add(V name, O obj){
		st.put(name, obj);
	}
	
	public O lookup(V name){
		return st.get(name);
	}
	
	//TODO
	//public O lookupAll
	
	
}
