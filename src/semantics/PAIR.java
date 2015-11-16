package semantics;

public class PAIR extends IDENTIFIER {
    private String fstType;
    private String sndType;

    public PAIR(String fstType, String sndType) {
        super("Pair("+fstType+","+sndType+")");
        this.fstType = fstType;
        this.sndType = sndType;
    }
    
    public PAIR(String pairformat) {
        super(pairformat);
        int indexOfFirstElem = pairformat.indexOf('(');
        int indexOfLastElem = pairformat.indexOf(')');
        int indexOfMiddle = pairformat.indexOf(',');
        this.fstType = pairformat.substring(indexOfFirstElem+1,indexOfMiddle);
        this.sndType = pairformat.substring(indexOfMiddle+1, indexOfLastElem);
    }

    public String toString() {
        return "Pair(" + fstType + "," + sndType + ")";
    }
    
    public String getFstType(){
      return fstType;
    }
    
    public String getSndType(){
      return sndType;
    }
}
