package frontend;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.ParserRuleContext;

import antlr.WACCParser;
import antlr.WACCParserBaseVisitor;

public class SyntaxVisitor extends WACCParserBaseVisitor<Void> {
    private List<String> errorMessages = new ArrayList<String>();
    private int syntaxErrorCount = 0;
    private boolean inFunction = false;
    private boolean returnExitFound = false;

    private boolean DEBUG = false;

    private void syntaxError(ParserRuleContext ctx,
                    String errorMessage) {
        int line = ctx.getStart().getLine();
        int pos = ctx.getStart().getCharPositionInLine();
        errorMessages.add("Syntatic Error at " + line + ":" + pos
                        + " -- " + errorMessage);
        syntaxErrorCount++;
    }
    
    public List<String> getErrorMessages() {
        return errorMessages;
    }

    // Visit program
    public Void visitProgram(WACCParser.ProgramContext ctx) {
        if (DEBUG) {
            System.out.println("-Program");
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
            String errorMessage = "Function "
                            + ctx.IDENT().toString()
                            + " is not ended with a return or an exit statement.";
            syntaxError(ctx, errorMessage);
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
            String errorMessage = "Integer value "
                            + ctx.getStart().getText()
                            + " is too large "
                            + "for a 32-bit signed integer";
            syntaxError(ctx, errorMessage);
        }
        return null;
    }

    public int getSyntaxErrorCount() {
        return syntaxErrorCount;
    }
}
