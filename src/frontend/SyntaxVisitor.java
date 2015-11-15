package frontend;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

import antlr.WACCParser;
import antlr.WACCParserBaseVisitor;

public class SyntaxVisitor extends WACCParserBaseVisitor<Void> {
    //TODO: Refactor the syntax error nicely
    private int syntaxErrorCount = 0;
    private boolean inFunction = false;
    private boolean returnExitFound = false;

    private boolean DEBUG = false;
    
    // Visit program
    public Void visitProgram(WACCParser.ProgramContext ctx) {
        if (DEBUG) {
            System.out.println("-Program");
        }
        int childCount = ctx.getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (i != 0) {
                String expectBegins = ctx.getChild(i).toString();
                if (expectBegins.equals("begin")) {
                    Token endToken = (Token) ctx.getChild(i)
                                    .getPayload();
                    int line = endToken.getLine();
                    int pos = endToken.getCharPositionInLine();
                    String errorMessage = "mismatched input '"
                                    + expectBegins
                                    + "' expecting <EOF>";
                    System.err.println("Syntatic Error at " + line
                                    + ":" + pos + " -- "
                                    + errorMessage);
                    syntaxErrorCount++;
                }
            }
        }
        return visitChildren(ctx);
    }

    // Visit function
    public Void visitFunc(WACCParser.FuncContext ctx) {
        if (DEBUG) {
            System.out.println("-function");
        }
        inFunction = true;
        visitChildren(ctx);
        if (!returnExitFound) {
            int line = ctx.getStart().getLine();
            int pos = ctx.getStart().getCharPositionInLine();
            String errorMessage = "Function "
                            + ctx.IDENT().toString()
                            + " is not ended with a return or an exit statement.";
            System.err.println("Syntatic Error at " + line + ":"
                            + pos + " -- " + errorMessage);
            syntaxErrorCount++;
        }
        returnExitFound = false;
        inFunction = false;
        return null;
    }

    // If statement
    public Void visitIfstatement(WACCParser.IfstatementContext ctx) {
        if (DEBUG) {
            System.out.println("-if statement");
        }

        if (inFunction) {
            visit(ctx.IF());
            visit(ctx.expr());
            visit(ctx.THEN());
            
            returnExitFound = false;
            visit(ctx.stat(0));

            visit(ctx.ELSE());
            
            returnExitFound = false;
            visit(ctx.stat(1));
            
            visit(ctx.FI());
        } else {
            visitChildren(ctx);
        }
        return null;
    }

    // Return statement
    public Void visitReturnstatement(
                    WACCParser.ReturnstatementContext ctx) {
        if (DEBUG) {
            System.out.println("-return");
        }
        returnExitFound = true;
        return null;
    }

    // Exit statement
    public Void visitExitstatement(WACCParser.ExitstatementContext ctx) {
        if (DEBUG) {
            System.out.println("-exit");
        }
        returnExitFound = true;
        return null;
    }

    // Visiting literals
    public Void visitIntegerliteral(
                    WACCParser.IntegerliteralContext ctx) {
        if (DEBUG) {
            System.out.println("-Int literal");
        }
        try {
            @SuppressWarnings("unused")
            Integer value = Integer.parseInt(ctx.getChild(0)
                            .getText());
        } catch (NumberFormatException e) {
            System.err.println("Integer value "
                            + ctx.getStart().getText() + " on line "
                            + ctx.getStart().getLine()
                            + " is too large "
                            + "for a 32-bit signed integer");
            syntaxErrorCount++;
        }
        return null;
    }

    public int getSyntaxErrorCount() {
        return syntaxErrorCount;
    }
}
