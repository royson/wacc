package semantics;

import java.util.ArrayList;
import java.util.List;

public class ARRAY extends IDENTIFIER {
  private List<String> elems;
  int size;

  public ARRAY(String type) {
	super(type);
	elems = new ArrayList<String>();
  }
  
  public int getSize(){
	return elems.size();
  }
  
  public void addElem(String type){
	elems.add(type);
  }
  
  public String getType(int index){
	return elems.get(index);
  }
  
  public void clear(){
	elems.clear();
  }
  

}
