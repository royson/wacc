package semantics;

public class FUNCTION extends IDENTIFIER {
  private PARAM[] args;
  int size;

  public FUNCTION(String returnType) {
	super(returnType);
	this.args = null;
	size = 0;
  }
  
  public void setParamSize(int s){
	this.args = new PARAM[s];
  }
  
  public int getParamSize(){
	return size;
  }
  
  public void addParam(PARAM p){
	args[size] = p;
	size++;
  }
  
  public PARAM getParam(int pos){
	if(pos < size){
	  return args[pos];
	}
	return null;
  }

}
