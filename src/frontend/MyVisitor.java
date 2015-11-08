package frontend;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;

import antlr.*;
import antlr.WACCParser.ProgramContext;

public class MyVisitor extends WACCParserBaseVisitor<Void> {

    public Void visitProgram(WACCParser.ProgramContext ctx) {
        for (int i = 0; i < ctx.getChildCount(); i++) {
            System.out.println(ctx.getChild(i));
        }
        return null;
    }

    public Void visitExpr(WACCParser.ExprContext ctx) {
        return null;
    }

    public Void visitStat(WACCParser.StatContext ctx) {
        System.out.println("-Statement");
        return visitChildren(ctx);
    }

    public Void visitFunc(WACCParser.FuncContext ctx) {
        System.out.println("I found a function definition!");
        // System.out.println(ctx.name.value);
        // System.out.print("Type info: ");
        // // need to visit function args in a loop
        // for (int i = 0; i < ctx.params.size(); i++) {
        // visit(ctx.params.get(i));
        // }
        // System.out.print(" => ");
        // // vist funtion return type (note this is out of normal tree order)
        // visitChildren(ctx.param);
        // System.out.println("");
        return null;
    }
}
