package frontend;

import java.io.*;
import java.util.List;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import antlr.*;

public class Main {
    public static void main(String[] args) {
        // System.out.println(args[0]);
        String inputFile = args[0];

        // Load file/input stream into ANTLRInputStream input
        ANTLRInputStream input = null;
        try {
            InputStream is = new FileInputStream(inputFile);
            input = new ANTLRInputStream(is);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create a lexer that reads from ANTLRInputStream
        WACCLexer lexer = new WACCLexer(input);

        // Create a buffer of tokens read from the lexer
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);

        // Create a parser that reads form the tokens buffer
        WACCParser parser = new WACCParser(tokenStream);
        
        // Removes the default error Listener
        parser.removeErrorListeners();
        
        parser.addErrorListener(new WACCErrorListener());
        ParseTree tree = parser.program();

//        System.out.println("====");
//        MyVisitor visitor = new MyVisitor();
//        visitor.visit(tree);
//        System.out.println("====");

    }
}
