package semantics;

public class ARRAY extends TYPE {
    private int elements;

    public ARRAY(String elementType, int elements) {
        super(elementType);
        this.elements = elements;
    }

}
