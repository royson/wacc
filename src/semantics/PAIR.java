package semantics;

public class PAIR extends IDENTIFIER {
    private String fstType;
    private String sndType;

    public PAIR(String fstType, String sndType) {
        super("pair");
        this.fstType = fstType;
        this.sndType = sndType;
    }

    public String toString() {
        return "pair(" + fstType + "," + sndType + ")";
    }

}
