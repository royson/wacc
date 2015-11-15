package frontend;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

import antlr.WACCParser;
import antlr.WACCParserBaseVisitor;

public class SyntaxVisitor extends WACCParserBaseVisitor<Void> {

    private int syntaxErrorCount = 0;

    private boolean DEBUG = true;

    // private void syntaxError(ParserRuleContext ctx,
    // String errorMessage) {
    // Token startToken = ctx.getStop();
    // int line = startToken.getLine();
    // int pos = startToken.getCharPositionInLine();
    //
    // System.err.println("Syntactic Error at " + line + ":" + pos
    // + " -- " + errorMessage);
    //
    // syntaxErrorCount++;
    // }

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
                                    + expectBegins + "' expecting <EOF>";
                    System.err.println("Syntatic Error at " + line
                                    + ":" + pos + " -- "
                                    + errorMessage);
                    syntaxErrorCount++;
                }
            }
        }
        return visitChildren(ctx);
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
