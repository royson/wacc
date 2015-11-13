package semantics;

public class ARRAY extends IDENTIFIER {
    private int elements;

    public ARRAY(String elementType, int elements) {
        super(elementType);
        this.elements = elements;
    }

}
