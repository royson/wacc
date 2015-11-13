package frontend;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import semantics.*;
import antlr.WACCParser;
import antlr.WACCParserBaseVisitor;

public class SemanticVisitor extends WACCParserBaseVisitor<Void> {

    private SymbolTable<String, IDENTIFIER> top_st;
    // TODO: Preload initial symbol table with globally visible identifiers

    private SymbolTable<String, IDENTIFIER> current_st;
    private String curTypeToCheck = "";
    private String curIdentToCheck = "";

    private boolean DEBUG = true;
    
    /* Helper functions */
    
    private void semanticError(ParserRuleContext ctx,
            String errorMessage) {
        Token startToken = ctx.getStart();
        int line = startToken.getLine();
        int pos = startToken.getCharPositionInLine();

        System.err
                .println("Errors detected during compilation! Exit code 200 returned.");
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
        if (curTypeToCheck.equals("")) {
            if (DEBUG) {
                System.out
                        .println("Something ridiculous that we will know");
            }
            return;
        }

        if (!curTypeToCheck.equals(type)) {
            String errorMessage = "Incompatible type at " + value;
            errorMessage += " (expected: "
                    + curTypeToCheck.toUpperCase();
            errorMessage += ", actual: " + type.toUpperCase() + ")";
            semanticError(ctx, errorMessage);
        }

        // Reset the current type
        curTypeToCheck = "";
    }
    
    private String checkDefinedVariable(ParserRuleContext ctx) {
        IDENTIFIER object = current_st.lookupAll(curIdentToCheck);
        
        // Variable is not declared
        if(object == null) {
            String errorMessage = "Variable " + curIdentToCheck + " is not defined in this scope";
            semanticError(ctx,errorMessage);
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
        current_st = new SymbolTable<String, IDENTIFIER>();
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
        IDENTIFIER object = current_st.lookup(varname);
        if (object != null) {
            semanticError(ctx, "\"" + varname
                    + "\" is already defined in this scope");
        }

        current_st.add(varname,
                new VARIABLE(curTypeToCheck));

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
        return visitChildren(ctx);
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

    public Void visitAssignlhsident(WACCParser.AssignlhsidentContext ctx) {
        if (DEBUG) {
            System.out.println("-Assign LHS ident");
            contextDepth(ctx);
        }
        curIdentToCheck = ctx.IDENT().toString();
        curTypeToCheck = checkDefinedVariable(ctx);
        return null;
    }
    
    public Void visitAssignlhsarrayelem(WACCParser.AssignlhsarrayelemContext ctx) {
        if (DEBUG) {
            System.out.println("-Assign LHS array elem");
            contextDepth(ctx);
        }
        return visitChildren(ctx);
    }
    
    public Void visitAssignpairelem(WACCParser.AssignlhspairelemContext ctx) {
        if (DEBUG) {
            System.out.println("-Assign LHS pair elem");
            contextDepth(ctx);
        }
        return visitChildren(ctx);
    }

    /* Type */

    public Void visitType(WACCParser.TypeContext ctx) {
        if (DEBUG) {
            System.out.print("-Type ");
            contextDepth(ctx);
        }
        curTypeToCheck = ctx.BASETYPE().toString();

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
        checkType(ctx, ctx.INTLITERAL().toString(), "int");
        return null;
    }

    public Void visitBooleanliteral(
            WACCParser.BooleanliteralContext ctx) {
        if (DEBUG) {
            System.out.print("-Boolean literal ");
            contextDepth(ctx);
        }
        checkType(ctx, ctx.BOOLEANLITERAL().toString(), "bool");
        return null;
    }

    public Void visitCharliteral(WACCParser.CharliteralContext ctx) {
        if (DEBUG) {
            System.out.print("-Char literal ");
            contextDepth(ctx);
        }
        checkType(ctx, ctx.CHARLITERAL().toString(), "char");
        return null;
    }

    public Void visitStringliteral(WACCParser.StringliteralContext ctx) {
        if (DEBUG) {
            System.out.print("-String literal ");
            contextDepth(ctx);
        }
        checkType(ctx, ctx.STRINGLITERAL().toString(), "string");
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
        return visitChildren(ctx);
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
