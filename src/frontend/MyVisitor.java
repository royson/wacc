package frontend;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;

import antlr.*;
import antlr.WACCParser.ProgramContext;

public class MyVisitor extends WACCParserBaseVisitor<Void> {

    private Void syntaxError() {
        System.out.println("#syntax error#");
        System.exit(100);
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
        int childCount = ctx.getChildCount();
        // for (int i = 0; i < childcount; i++) {
        // System.out.println(ctx.getChild(i));
        // }

        // Visit the statements
        int statementCount = ctx.stat().getChildCount();
        System.out.println(ctx.stat());
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

        return null;
    }

    // Visiting literals
    public Void visitIntegerliteral(
                    WACCParser.IntegerliteralContext ctx) {
        try {
            Integer value = Integer.parseInt(ctx.getChild(0)
                            .getText());
        } catch (NumberFormatException e) {
            syntaxError();
        }
        return null;
    }
}
