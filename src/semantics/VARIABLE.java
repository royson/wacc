package semantics;

public class VARIABLE extends IDENTIFIER {
  private TYPE type;

  public VARIABLE(TYPE type) {
	this.type = type;
  }
}
