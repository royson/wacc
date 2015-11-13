package frontend;

import antlr.WACCParser;
import antlr.WACCParserBaseVisitor;

public class SyntaxVisitor extends WACCParserBaseVisitor<Void> {

    private int syntaxErrorCount = 0;

    public Void visitProgram(WACCParser.ProgramContext ctx) {
        visitChildren(ctx);
        return null;
    }

    public Void visitExpr(WACCParser.ExprContext ctx) {
        return null;
    }

    public Void visitStat(WACCParser.StatContext ctx) {
        // System.out.println("-Statement");
        return super.visitChildren(ctx);
    }

    public Void visitFunc(WACCParser.FuncContext ctx) {
        return null;
    }

    // Visiting literals
    public Void visitIntegerliteral(WACCParser.IntegerliteralContext ctx) {
        try {
            @SuppressWarnings("unused")
            Integer value = Integer.parseInt(ctx.getChild(0).getText());
        } catch (NumberFormatException e) {
            System.err.println("Integer value " + ctx.getStart().getText()
                    + " on line " + ctx.getStart().getLine() + " is too large "
                    + "for a 32-bit signed integer");
	    syntaxErrorCount++;
        }
        return null;
    }

    public int getSyntaxErrorCount() {
        return syntaxErrorCount;
    }
}

