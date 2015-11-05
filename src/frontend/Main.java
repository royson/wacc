package frontend;

import java.io.*;
import java.util.List;

import org.antlr.runtime.tree.ParseTree;
import org.antlr.v4.runtime.*;
import antlr.WACCLexer;
import antlr.WACCParser;

public class Main {
    public static void main(String[] args) {
        // System.out.println(args[0]);
        String inputFile = args[0];

        // Load file/input stream into ANTLRInputStream input
        ANTLRInputStream input = null;
        try {
            InputStream is = new FileInputStream(inputFile);
            Reader r = new InputStreamReader(is, "utf-8");
            input = new ANTLRInputStream(r);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // Create a lexer that reads from ANTLRInputStream         
        WACCLexer lexer = new WACCLexer(input);
        
        // TESTING: print out all the tokens using the lexer
        List<? extends Token> tokensL = lexer.getAllTokens();
        for (Token t: tokensL) {
            System.out.println(t.getLine() + ":" + t.getCharPositionInLine() + " " + t.getText() + "  " + t.getType());
        } 
        
        
        // Create a buffer of tokens read from the lexer
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        
        // Create a parser that reads form the tokens buffer
        WACCParser parser = new WACCParser(tokenStream);
        
        //TESTING: print out all the tokens using the parser
        TokenStream parsertokens = parser.getTokenStream();
        
//        while (parsertokens.) {
//        	Token t = parser.getCurrentToken();
//            System.out.println(t.getLine() + ":" + t.getCharPositionInLine() + " " + t.getText() + "  " + t.getType());
//        }
//        
        
//        ParseTree tree = parser.program();
        
        // Print a LISP-style parse tree
//        System.out.println(tree.toStringTree());
    }
}
