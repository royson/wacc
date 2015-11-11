package frontend;

import java.io.*;
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
        MyVisitor visitor = new MyVisitor();
        visitor.visit(tree);

        // Syntax error
        int syntaxErrorCount = visitor.getSyntaxErrorCount() + parser.getNumberOfSyntaxErrors();
        if (syntaxErrorCount > 0) {
            System.err.println("Found " + syntaxErrorCount
                            + " syntax error(s)");
            System.out.println("#syntax error#");
            System.exit(100);
        }
        
        // Semantic error
        int semanticErrorCount = 0;
        if (semanticErrorCount > 0) {
            System.err.println("Found " + semanticErrorCount
                            + " semantic errors(s)");
            System.out.println("#semantic error#");
            System.exit(200);
        }
    }
}
