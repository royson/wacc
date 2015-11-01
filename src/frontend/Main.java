package frontend;

import java.io.*;
import java.util.List;
import org.antlr.v4.runtime.*;
import antlr.WACCLexer;

public class Main {
    public static void main(String[] args) {
        // System.out.println(args[0]);
        String inputFile = args[0];

        ANTLRInputStream input = null;
        try {
            InputStream is = new FileInputStream(inputFile);
            Reader r = new InputStreamReader(is, "utf-8");
            input = new ANTLRInputStream(r);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        WACCLexer myLexer = new WACCLexer(input);
        List<? extends Token> tokens = myLexer.getAllTokens();
        for (Token t: tokens) {
//            System.out.println(t.getLine() + ":" + t.getCharPositionInLine() + " " + t.getText() + "  " + t.getType());
        } 
    }
}
