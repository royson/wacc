package frontend;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

public class Utils {
    
    private final static String INT = "INT";
    private final static String BOOL = "BOOL";
    private final static String CHAR = "CHAR";
    private final static String STRING = "CHAR[]";
    
    public static boolean isAnArray(String type) {
        // STRING is not an array
        if (type.equals(STRING))
            return false;
        else
            return (type.endsWith("[]"));
    }
    
    public static boolean isANullPair(String type) {
        return (type.equals("pair"));
    }
    
    public static boolean isAPair(String type) {
        return (type.startsWith("Pair(") && type.endsWith(")"));
    }
    
    public static String stripArrayTypeBracket(String arrayType) {
        // This function returns the types stored in an array
        // "INT[]" --> "INT"; "INT[][]" --> "INT[]"
        return arrayType.substring(0, arrayType.length() - 2);
    }
    
    public static String getPrimitiveType(String s) {
        String int_regex = "-?\\d+";
        String char_regex = "\'.\'";
        String bool_regex = "true|false";
        String string_regex = "\".*\"";
        if (s.matches(int_regex))
            return INT;
        else if (s.matches(char_regex))
            return CHAR;
        else if (s.matches(bool_regex))
            return BOOL;
        else if (s.matches(string_regex))
            return STRING;
        else
            return null;
    }
    
    public static String renameStringToCharArray(String s) {
        return s.equals("STRING") ? STRING : s;
    }
    
    public static void contextDepth(ParserRuleContext ctx) {
        System.out.println("");
    }
    
    public static void checkParameters(ParserRuleContext ctx,
                    String funcName, int paramSize, int funcSize) {
        if (paramSize != funcSize) {
            String errorMessage = "Incorrect number of parameters for "
                            + funcName
                            + " (expected: "
                            + funcSize
                            + ", actual: " + paramSize + ")";
            semanticError(ctx, errorMessage);
        }
    }
    
    public static void semanticError(ParserRuleContext ctx,
                    String errorMessage) {
        Token startToken = ctx.getStart();
        int line = startToken.getLine();
        int pos = startToken.getCharPositionInLine();

        System.err.println("Errors detected during compilation! Exit code 200 returned.");
        System.err.println("Semantic Error at " + line + ":" + pos
                        + " -- " + errorMessage);
        System.exit(200);
    }


}
