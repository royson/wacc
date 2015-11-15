package semantics;

public class PAIR extends IDENTIFIER {
    private String fstType;
    private String sndType;

    public PAIR(String fstType, String sndType) {
        super("Pair("+fstType+","+sndType+")");
        this.fstType = fstType;
        this.sndType = sndType;
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
