package semantics;

public class SymbolTableWrapper<V> {

    SymbolTable<V, IDENTIFIER> stIdent;
    SymbolTable<V, FUNCTION> stFunc;
    SymbolTable<V, PARAM> stParam;

    SymbolTableWrapper<V> encSymTable;

    public SymbolTableWrapper() {
        stIdent = new SymbolTable<V, IDENTIFIER>();
        stFunc = new SymbolTable<V, FUNCTION>();
        stParam = new SymbolTable<V, PARAM>();
        this.encSymTable = null;
    }

    public SymbolTableWrapper(SymbolTableWrapper<V> encSymTable) {
        stIdent = new SymbolTable<V, IDENTIFIER>();
        stFunc = new SymbolTable<V, FUNCTION>();
        stParam = new SymbolTable<V, PARAM>();
        this.encSymTable = encSymTable;
    }

    public void addIdentifier(V name, IDENTIFIER obj) {
        stIdent.add(name, obj);
    }

    public IDENTIFIER lookUpIdentifier(V name) {
        return stIdent.lookUp(name);
    }

    public IDENTIFIER lookUpAllIdentifier(V name) {
        SymbolTableWrapper<V> temp = this;
        while (temp != null) {

            IDENTIFIER obj = temp.stIdent.lookUp(name);
            if (obj != null) {
                return obj;
            }
            temp = temp.encSymTable;
        }
        return null;
    }

    public void addFunction(V name, FUNCTION newFunc) {
        stFunc.add(name, newFunc);
    }

    public FUNCTION lookUpAllFunction(V name) {
        SymbolTableWrapper<V> temp = this;
        while (temp != null) {

            FUNCTION obj = temp.stFunc.lookUp(name);
            if (obj != null) {
                return obj;
            }
            temp = temp.encSymTable;
        }
        return null;
    }

    public void addParam(V name, PARAM newParam) {
        stParam.add(name, newParam);
    }

    public PARAM lookUpParam(V name) {
        return stParam.lookUp(name);
    }
    
    public PARAM lookUpAllParam(V name) {
        SymbolTableWrapper<V> temp = this;
        while (temp != null) {

            PARAM obj = temp.stParam.lookUp(name);
            if (obj != null) {
                return obj;
            }
            temp = temp.encSymTable;
        }
        return null;
    }

    public SymbolTableWrapper<V> getEncSymTable() {
        return encSymTable;
    }
    
    public void printST() {
        System.out.println("----- Print ST -----");
        System.out.println(stParam);
        System.out.println("--------------------");
    }
}
