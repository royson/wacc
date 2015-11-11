package frontend;

import org.antlr.v4.runtime.tree.ParseTree;

import antlr.WACCParser;
import antlr.WACCParserBaseVisitor;

public class MyVisitor extends WACCParserBaseVisitor<Void> {

    private int syntaxErrorCount = 0;
    private boolean inFunction = false;

    private Void syntaxError() {
        syntaxErrorCount += 1;
        return null;
    }

    private Void returnCheck(String statement) {
        final int returnLength = 6;
        if (statement.length() > returnLength) {
            statement = statement.substring(0, returnLength);
        }
        if (!statement.equals("return")) {
            syntaxError();
        }
        return null;

    }

    public Void visitProgram(WACCParser.ProgramContext ctx) {
        return visitChildren(ctx);
    }

    public Void visitExpr(WACCParser.ExprContext ctx) {
        return null;
    }

    public Void visitStat(WACCParser.StatContext ctx) {
        // System.out.println("-Statement");
        return visitChildren(ctx);
    }

    public Void visitFunc(WACCParser.FuncContext ctx) {
        System.out.println("I found a function definition!");
        inFunction = true;
//        int childCount = ctx.getChildCount();
        // for (int i = 0; i < childcount; i++) {
        // System.out.println(ctx.getChild(i));
        // }

        // Visit the statements
        int statementCount = ctx.stat().getChildCount();
        System.out.println(ctx.stat());
        System.out.println("depth: " + ctx.depth());
        System.out.println(statementCount);

        if (statementCount == 2) {
            returnCheck(ctx.stat().getChild(0).toString());
        } else {
            for (int i = 0; i < statementCount; i++) {
                ParseTree child = ctx.stat().getChild(i);
                System.out.println(child.getClass());
                System.out.println(child.getParent().getClass());
                visit(child);
                if (i == statementCount - 1) {
                    if (child.getClass() != WACCParser.ReturnstatementContext.class) {
                        syntaxError();
                    }
                }
            }
        }
        
        inFunction = false;
        return null;
    }

    // Visiting literals
    public Void visitIntegerliteral(
                    WACCParser.IntegerliteralContext ctx) {
        try {
            @SuppressWarnings("unused")
            Integer value = Integer.parseInt(ctx.getChild(0)
                            .getText());
        } catch (NumberFormatException e) {
            syntaxError();
        }
        return null;
    }

    public int getSyntaxErrorCount() {
        return syntaxErrorCount;
    }
}
