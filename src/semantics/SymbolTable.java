package semantics;

import java.util.HashMap;

public class SymbolTable<V, O> {

  HashMap<V, O> st;
  SymbolTable<V, O> encSymTable;

  public SymbolTable() {
	st = new HashMap<V, O>();
	this.encSymTable = null;
  }

  public SymbolTable(SymbolTable<V, O> encSymTable) {
	st = new HashMap<V, O>();
	this.encSymTable = encSymTable;
  }

  public void add(V name, O obj) {
	st.put(name, obj);
  }

  public O lookup(V name) {
	return st.get(name);
  }

  public O lookupAll(V name){
		SymbolTable<V,O> temp = this;
		while(temp !=null){
		  O obj = temp.lookup(name);
		  if(obj != null){
			return obj;
		  }
		  temp = temp.encSymTable;
		}
		return null;
	}
}
