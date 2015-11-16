package semantics;

import java.util.HashMap;

public class SymbolTable<V, O> {

    HashMap<V, O> st;
    SymbolTable<V, O> encSymTable;

    public SymbolTable() {
        st = new HashMap<V, O>();
        this.encSymTable = null;
    }

    public void add(V name, O obj) {
        st.put(name, obj);
    }

    public O lookUp(V name) {
        return st.get(name);
    }
    
    @Override
    public String toString() {
        return st.toString();
    }
}