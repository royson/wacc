package semantics;

public class ARRAY extends TYPE {
  private TYPE elementType;
  private int elements;

  public ARRAY(TYPE elementType, int elements) {
	this.elementType = elementType;
	this.elements = elements;
  }

}
