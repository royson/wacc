package semantics;

public class SymbolTableWrapper<V> {

    SymbolTable<V, IDENTIFIER> stIdent;
    SymbolTable<V, FUNCTION> stFunc;
    SymbolTable<V, IDENTIFIER> stParam;
    SymbolTable<V, Integer> stLabel;

    SymbolTableWrapper<V> encSymTable;
    private int scopeSize = 0;
    private int spPos = 0;
    private String scopeName = null;

    public String getScopeName() {
        return scopeName;
    }

    public void setScopeName(String scopeName) {
        this.scopeName = scopeName;
    }

    public int getSpPos() {
        return spPos;
    }

    public void setSpPos(int spPos) {
        this.spPos = spPos;
    }

    public int getScopeSize() {
        return scopeSize;
    }

    public void setScopeSize(int scopeSize) {
        this.scopeSize = scopeSize;
    }

    public SymbolTableWrapper() {
        stIdent = new SymbolTable<V, IDENTIFIER>();
        stFunc = new SymbolTable<V, FUNCTION>();
        stParam = new SymbolTable<V, IDENTIFIER>();
        stLabel = new SymbolTable<V, Integer>();
        this.encSymTable = null;
    }

    public SymbolTableWrapper(SymbolTableWrapper<V> encSymTable) {
        stIdent = new SymbolTable<V, IDENTIFIER>();
        stFunc = new SymbolTable<V, FUNCTION>();
        stParam = new SymbolTable<V, IDENTIFIER>();
        stLabel = new SymbolTable<V, Integer>();
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

    public void addParam(V name, IDENTIFIER newParam) {
        stParam.add(name, newParam);
    }

    public IDENTIFIER lookUpParam(V name) {
        return stParam.lookUp(name);
    }

    public IDENTIFIER lookUpAllParam(V name) {
        SymbolTableWrapper<V> temp = this;
        while (temp != null) {

            IDENTIFIER obj = temp.stParam.lookUp(name);
            if (obj != null) {
                return obj;
            }
            temp = temp.encSymTable;
        }
        return null;
    }

    public void addLabel(V name, Integer location) {
        stLabel.add(name, location);
    }

    public Integer lookUpLabel(V name) {
        return stLabel.lookUp(name);
    }

    public Integer lookUpAllLabel(V name) {
        SymbolTableWrapper<V> temp = this;
        int offset = 0;
        while (temp != null) {

            Integer obj = temp.stLabel.lookUp(name);
//            System.out.println(obj + offset);
            if (obj != null) {
                return obj + offset;
            }
            offset += temp.getScopeSize(); // For correct memory location in
                                           // nested scopes
            temp = temp.encSymTable;
        }
        return null;
    }

    public SymbolTable<V, Integer> getStLabel() {
        return stLabel;
    }

    public void setStLabel(SymbolTable<V, Integer> stLabel) {
        this.stLabel = stLabel;
    }

    public SymbolTableWrapper<V> getEncSymTable() {
        return encSymTable;
    }
    
    public void clear() {
        stIdent = new SymbolTable<V, IDENTIFIER>();
        stFunc = new SymbolTable<V, FUNCTION>();
        stParam = new SymbolTable<V, IDENTIFIER>();
    }

    public void printST() {
        System.out.println("----- Print ST -----");
        System.out.println("Scopesize: " + scopeSize);
        System.out.println("ID: " + stIdent);
        System.out.println("Func: " + stFunc);
        System.out.println("Param: " + stParam);
        System.out.println("Label: " + stLabel);
        System.out.println("--------------------");
    }
}
