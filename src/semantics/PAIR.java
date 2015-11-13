package semantics;

public class PAIR extends IDENTIFIER{
    private String fstType;
    private String sndType;

    public PAIR(String fstType, String sndType) {
        super("Pair");
        this.fstType = fstType;
        this.sndType = sndType;
    }
    
}
