package semantics;

public class FUNCTION extends IDENTIFIER {
  private TYPE returnType;
  private PARAM formals[];
  private SymbolTable<String, IDENTIFIER> fst;

  public FUNCTION(TYPE returnType, PARAM[] formals,
	  SymbolTable<String, IDENTIFIER> fst) {
	this.returnType = returnType;
	this.formals = formals;
	this.fst = fst;
  }

}
