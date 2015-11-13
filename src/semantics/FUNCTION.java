package semantics;

public class FUNCTION extends IDENTIFIER {
  private PARAM[] formals;
  int paramCounter;

  public FUNCTION(String returnType) {
	super(returnType);
	this.formals = null;
	paramCounter = 0;
  }
  
  //Call this in visitParamList
  public void paramSize(int s){
	this.formals = new PARAM[s];
  }
  
  //Call this in visitParam
  public void addParam(PARAM p){
	formals[paramCounter] = p;
	paramCounter++;
  }

}
