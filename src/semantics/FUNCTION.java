package semantics;

public class FUNCTION extends IDENTIFIER {
  private PARAM formals[];
  private SymbolTable<String, IDENTIFIER> fst;

  public FUNCTION(String returnType, PARAM[] formals,
	  SymbolTable<String, IDENTIFIER> fst) {
	super(returnType);
	this.formals = formals;
	this.fst = fst;
  }

}
