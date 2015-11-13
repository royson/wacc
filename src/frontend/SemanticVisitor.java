package frontend;

import java.util.Stack;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import semantics.*;
import antlr.WACCParser;
import antlr.WACCParserBaseVisitor;

public class SemanticVisitor extends WACCParserBaseVisitor<Void> {

    private SymbolTable<String, IDENTIFIER> topST;
    // TODO: Preload initial symbol table with globally visible identifiers

    private SymbolTable<String, IDENTIFIER> currentST;
    private Stack<String> typeStack = new Stack<String>();
    private String curIdentToCheck = "";

    private boolean DEBUG = true;

    /* Helper functions */

    private void semanticError(ParserRuleContext ctx,
                    String errorMessage) {
        Token startToken = ctx.getStart();
        int line = startToken.getLine();
        int pos = startToken.getCharPositionInLine();

        System.err.println("Errors detected during compilation! Exit code 200 returned.");
        System.err.println("Semantic Error at " + line + ":" + pos
                        + " -- " + errorMessage);
        System.exit(200);
    }

    private void contextDepth(ParserRuleContext ctx) {
        System.out.println(ctx.depth() + " " + ctx.getChildCount()
                        + " " + ctx);
    }

    private void checkType(ParserRuleContext ctx, String value,
                    String type) {
        if (typeStack.isEmpty()) {
            if (DEBUG) {
                System.out.println("Something ridiculous that we will know");
            }
            return;
        }
        
        String compareType = typeStack.pop();

        if (!compareType.equals(type)) {
            String errorMessage = "Incompatible type at " + value;
            errorMessage += " (expected: "
                            + compareType.toUpperCase();
            errorMessage += ", actual: " + type.toUpperCase() + ")";
            semanticError(ctx, errorMessage);
        }

        // Reset the current type
    }

    private String checkDefinedVariable(ParserRuleContext ctx) {
        IDENTIFIER object = currentST.lookupAll(curIdentToCheck);

        // Variable is not declared
        if (object == null) {
            String errorMessage = "Variable " + curIdentToCheck
                            + " is not defined in this scope";
            semanticError(ctx, errorMessage);
        }
        return object.getType();
    }

    /* Write functions to traverse tree below here */

    /* Visit the main program */

    public Void visitProgram(WACCParser.ProgramContext ctx) {
        if (DEBUG) {
            System.out.print("-Program ");
            contextDepth(ctx);
        }
        currentST = new SymbolTable<String, IDENTIFIER>();
        return visitChildren(ctx);
    }

    /* Functions to visit statements */

    public Void visitVarinit(WACCParser.VarinitContext ctx) {
        if (DEBUG) {
            System.out.print("-Variable init statement ");
            contextDepth(ctx);
        }
        visit(ctx.type());
        ParseTree varnode = ctx.IDENT();
        String varname = varnode.toString();

        // Check for duplicate variable
        IDENTIFIER object = currentST.lookup(varname);
        if (object != null) {
            semanticError(ctx, "\"" + varname
                            + "\" is already defined in this scope");
        }
        
        System.out.println("Type assigned: " + typeStack.peek());
        currentST.add(varname, new VARIABLE(typeStack.peek()));

        visit(ctx.assignRHS());

        return null;
    }

    public Void visitAssignment(WACCParser.AssignmentContext ctx) {
        if (DEBUG) {
            System.out.println("-Assignment statement");
            contextDepth(ctx);
        }
        // String lhsIdentifier = ctx.assignLHS().IDENT();
        return visitChildren(ctx);
    }

    public Void visitReadstatement(WACCParser.ReadstatementContext ctx) {
        if (DEBUG) {
            System.out.println("-Read statement");
            contextDepth(ctx);
        }
        return visitChildren(ctx);
    }

    public Void visitFreeStatement(WACCParser.FreestatementContext ctx) {
        if (DEBUG) {
            System.out.println("-Free statement");
            contextDepth(ctx);
        }
        return visitChildren(ctx);
    }

    public Void visitReturnStatement(
                    WACCParser.ReturnstatementContext ctx) {
        if (DEBUG) {
            System.out.println("-Return statement");
            contextDepth(ctx);
        }
        return visitChildren(ctx);
    }

    public Void visitExitStatement(WACCParser.ExitstatementContext ctx) {
        if (DEBUG) {
            System.out.println("-Exit statement");
            contextDepth(ctx);
        }
        return visitChildren(ctx);
    }

    public Void visitPrintStatement(
                    WACCParser.PrintstatementContext ctx) {
        if (DEBUG) {
            System.out.println("-Print statement");
            contextDepth(ctx);
        }
        return visitChildren(ctx);
    }

    public Void visitPrintlnstatement(
                    WACCParser.PrintlnstatementContext ctx) {
        if (DEBUG) {
            System.out.println("-Print line statement");
            contextDepth(ctx);
        }
        return visitChildren(ctx);
    }

    public Void visitIfstatement(WACCParser.IfstatementContext ctx) {
        if (DEBUG) {
            System.out.println("-If statement");
            contextDepth(ctx);
        }
        return visitChildren(ctx);
    }

    public Void visitWhilestatement(
                    WACCParser.WhilestatementContext ctx) {
        if (DEBUG) {
            System.out.println("-While statement");
            contextDepth(ctx);
        }
        return visitChildren(ctx);
    }

    public Void visitBeginendstatement(
                    WACCParser.BeginendstatementContext ctx) {
        if (DEBUG) {
            System.out.println("-Begin end statement");
            contextDepth(ctx);
        }
        SymbolTable<String, IDENTIFIER> st = new SymbolTable<String, IDENTIFIER>(
                        currentST);
        currentST = st;

        visit(ctx.stat());

        currentST = st.getEncSymTable();
        return null;
    }

    public Void visitStatementblock(
                    WACCParser.StatementblockContext ctx) {
        if (DEBUG) {
            System.out.println("-Statement block statement");
            contextDepth(ctx);
        }
        return visitChildren(ctx);
    }

    /* Assign LHS and assign RHS */

    public Void visitAssignrhsexpr(WACCParser.AssignrhsexprContext ctx) {
        if (DEBUG) {
            System.out.print("-Assign RHS EXPR ");
            contextDepth(ctx);
        }
        return visitChildren(ctx);
    }

    public Void visitAssignrhsarraylit(
                    WACCParser.AssignrhsarraylitContext ctx) {
        if (DEBUG) {
            System.out.print("-Assign RHS arraylit ");
            contextDepth(ctx);
        }
        return visitChildren(ctx);
    }

    public Void visitAssignrhsnewpair(
                    WACCParser.AssignrhsnewpairContext ctx) {
        if (DEBUG) {
            System.out.print("-Assign RHS newpair ");
            contextDepth(ctx);
        }
        return visitChildren(ctx);
    }

    public Void visitAssignrhspairelem(
                    WACCParser.AssignrhspairelemContext ctx) {
        if (DEBUG) {
            System.out.print("-Assign RHS pairelem ");
            contextDepth(ctx);
        }
        return visitChildren(ctx);
    }

    public Void visitAssignrhscall(WACCParser.AssignrhscallContext ctx) {
        if (DEBUG) {
            System.out.print("-Assign RHS call ");
            contextDepth(ctx);
        }
        return visitChildren(ctx);
    }

    public Void visitAssignlhsident(
                    WACCParser.AssignlhsidentContext ctx) {
        if (DEBUG) {
            System.out.println("-Assign LHS ident");
            contextDepth(ctx);
        }
        curIdentToCheck = ctx.IDENT().toString();
        typeStack.push(checkDefinedVariable(ctx));
        return null;
    }

    public Void visitAssignlhsarrayelem(
                    WACCParser.AssignlhsarrayelemContext ctx) {
        if (DEBUG) {
            System.out.println("-Assign LHS array elem");
            contextDepth(ctx);
        }
        return visitChildren(ctx);
    }

    public Void visitAssignpairelem(
                    WACCParser.AssignlhspairelemContext ctx) {
        if (DEBUG) {
            System.out.println("-Assign LHS pair elem");
            contextDepth(ctx);
        }
        return visitChildren(ctx);
    }

    /* Type */

    public Void visitTypebasetype(WACCParser.TypebasetypeContext ctx) {
        if (DEBUG) {
            System.out.print("-Type BASETYPE ");
            contextDepth(ctx);
        }
        typeStack.push(ctx.BASETYPE().toString());
        return null;
    }
    
    public Void visitTypepairtype(WACCParser.TypepairtypeContext ctx) {
        if (DEBUG) {
            System.out.print("-Type PAIRTYPE ");
            contextDepth(ctx);
        }
        return visitChildren(ctx);
    }
    
    public Void visitArraytype(WACCParser.ArraytypeContext ctx) {
        if (DEBUG) {
            System.out.print("-Arraytype ");
            contextDepth(ctx);
        }
        return null;
    }
    
    public Void visitPairtype(WACCParser.PairtypeContext ctx) {
        if (DEBUG) {
            System.out.print("-Pairtype ");
            contextDepth(ctx);
        }
        visit(ctx.pairelementype(0));
        visit(ctx.pairelementype(1));
        return null;
    }
    
    public Void visitPairetbasetype(WACCParser.PairetbasetypeContext ctx) {
        if (DEBUG) {
            System.out.print("-Pairelementype basetype ");
            contextDepth(ctx);
        }
        return null;
    }
    
    public Void visitPairetarraytype(WACCParser.PairetarraytypeContext ctx) {
        if (DEBUG) {
            System.out.print("-Pairelementype arraytype ");
            contextDepth(ctx);
        }
        return null;
    }
    
    public Void visitPairetpair(WACCParser.PairetpairContext ctx) {
        if (DEBUG) {
            System.out.print("-Pairelementype pair ");
            contextDepth(ctx);
        }
        return null;
    }
 

    public Void visitTerminal(TerminalNode node) {
        if (DEBUG) {
            System.out.println("-Terminal " + node.toString());
        }
        return null;
    }

    /* Visit expressions */
    public Void visitIntegerliteral(
                    WACCParser.IntegerliteralContext ctx) {
        if (DEBUG) {
            System.out.print("-Int literal ");
            contextDepth(ctx);
        }
        if (!typeStack.empty()) {
            checkType(ctx, ctx.INTLITERAL().toString(), "int");
        } else {
            typeStack.push("int");
        }
        return null;
    }

    public Void visitBooleanliteral(
                    WACCParser.BooleanliteralContext ctx) {
        if (DEBUG) {
            System.out.print("-Boolean literal ");
            contextDepth(ctx);
        }
        if (!typeStack.empty()) {
            checkType(ctx, ctx.BOOLEANLITERAL().toString(), "bool");
        } else {
            typeStack.push("bool");
        }
        return null;
    }

    public Void visitCharliteral(WACCParser.CharliteralContext ctx) {
        if (DEBUG) {
            System.out.print("-Char literal ");
            contextDepth(ctx);
        }
        if (!typeStack.empty()) {
            checkType(ctx, ctx.CHARLITERAL().toString(), "char");
        } else {
            typeStack.push("char");
        }
        return null;
    }

    public Void visitStringliteral(WACCParser.StringliteralContext ctx) {
        if (DEBUG) {
            System.out.print("-String literal ");
            contextDepth(ctx);
        }
        if (!typeStack.empty()) {
            checkType(ctx, ctx.STRINGLITERAL().toString(), "string");
        } else {
            typeStack.push("string");
        }
        return null;
    }

    public Void visitPairliteral(WACCParser.PairliteralContext ctx) {
        if (DEBUG) {
            System.out.print("-Pair literal ");
            contextDepth(ctx);
        }
        return visitChildren(ctx);
    }

    public Void visitIdentifier(WACCParser.IdentifierContext ctx) {
        if (DEBUG) {
            System.out.print("-Identifier ");
            contextDepth(ctx);
        }
        curIdentToCheck = ctx.IDENT().toString();
        checkDefinedVariable(ctx);
        return null;
    }

    public Void visitArrayelement(WACCParser.ArrayelementContext ctx) {
        if (DEBUG) {
            System.out.print("-Array elements ");
            contextDepth(ctx);
        }
        return visitChildren(ctx);
    }

    public Void visitUnaryoperator(WACCParser.UnaryoperatorContext ctx) {
        if (DEBUG) {
            System.out.print("-Unary operator ");
            contextDepth(ctx);
        }
        return visitChildren(ctx);
    }

    public Void visitBinaryoperator(
                    WACCParser.BinaryoperatorContext ctx) {
        if (DEBUG) {
            System.out.print("-Binary operator ");
            contextDepth(ctx);
        }
        // Visit LHS
        visit(ctx.expr(0));
        if (curIdentToCheck != "") {
            typeStack.push(currentST.lookupAll(curIdentToCheck)
                            .getType());
        }

        // Visit RHS
        visit(ctx.expr(1));
        if (curIdentToCheck != "") {
            String rhsType = currentST.lookupAll(curIdentToCheck)
                            .getType();
            checkType(ctx.expr(1), curIdentToCheck, rhsType);
        }

        return null;
    }

    public Void visitBrackets(WACCParser.BracketsContext ctx) {
        if (DEBUG) {
            System.out.print("-Brackets ");
            contextDepth(ctx);
        }
        return visitChildren(ctx);
    }

    /* Visit function */

    public Void visitFunc(WACCParser.FuncContext ctx) {
        System.out.println("I found a function definition!");
        SymbolTable<String, IDENTIFIER> st = new SymbolTable<String, IDENTIFIER>();

        return null;
    }
}
