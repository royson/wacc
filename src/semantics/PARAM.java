package semantics;

public class PARAM extends IDENTIFIER {
    private String type;
    private String name;

    public PARAM(String type, String name) {
        super(type);
        this.type = type;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
}
