package frontend;

import antlr.*;
import antlr.WACCParser.TypeContext;

public class MyVisitor extends WACCParserBaseVisitor<Void> {
    public Void visitProgram(WACCParser.ProgramContext ctx) {
//        System.out.println("Entry");
        return visitChildren(ctx);
    }

    public Void visitFunc(WACCParser.FuncContext ctx) {
//        System.out.println("I found a function definition!");
//        System.out.println(ctx.name.value);
//        System.out.print("Type info: ");
//        // need to visit function args in a loop
//        for (int i = 0; i < ctx.params.size(); i++) {
//            visit(ctx.params.get(i));
//        }
//        System.out.print(" => ");
//        // vist funtion return type (note this is out of normal tree order)
//        visitChildren(ctx.param);
//        System.out.println("");
        return null;
    }

    public Void visitBaseType(TypeContext ctx) {
        System.out.print(ctx.toString());
        return null;
    }
}
