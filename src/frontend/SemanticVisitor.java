package frontend;

import java.util.ArrayList;
import java.util.Arrays;
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
    private Stack<String> stack = new Stack<String>();

    private final static String INT = "INT";
    private final static String BOOL = "BOOL";
    private final static String CHAR = "CHAR";
    private final static String STRING = "CHAR[]";
    
    private String[] primitiveTypes = { INT, BOOL, CHAR,
                    STRING };
    
    private boolean DEBUG = true;

    /* Helper functions */

    private void printStack() {
        System.out.println("-----PRINTING STACK-----");
        System.out.println(Arrays.toString(stack.toArray()));
        System.out.println("------------------------");
    }

    private String renameStringToCharArray(String s) {
        return s.equals("STRING") ? STRING : s;
    }

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
        if (stack.isEmpty()) {
            if (DEBUG) {
                System.out.println("Something ridiculous that we will know");
            }
            return;
        }
        String compareType = stack.pop();

        if (!compareType.equals(type)) {
            String errorMessage = "Incompatible type at " + value;
            errorMessage += " (expected: " + compareType;
            errorMessage += ", actual: " + type + ")";
            semanticError(ctx, errorMessage);
        }
    }

    private String checkDefinedVariable(ParserRuleContext ctx) {
        String curIdentToCheck = stack.peek();
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

        String varName = ctx.IDENT().toString();

        // Check for duplicate variable
        IDENTIFIER object = currentST.lookup(varName);
        if (object != null) {
            semanticError(ctx, "\"" + varName
                            + "\" is already defined in this scope");
        }
        stack.push(varName);
        visit(ctx.type());

        String varType = stack.peek();
        // TODO: Fix with array arrives.
        if (!varType.startsWith("Pair")) {
            currentST.add(ctx.IDENT().toString(),
                            new VARIABLE(stack.peek()));
        }
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

        visit(ctx.assignLHS());

        String readType = stack.pop();

        // Only char and integers are allowed.
        System.out.println("READ TYPE IS : " + readType);
        if (!readType.equals(INT) || !readType.equals(CHAR)) {
            semanticError(ctx, "Incompatible type " + readType);
        }
        return null;
    }

    public Void visitFreestatement(WACCParser.FreestatementContext ctx) {
        // Can only free arrays and pairs
        if (DEBUG) {
            System.out.println("-Free statement");
            contextDepth(ctx);
        }
        visit(ctx.expr());
        String freeType = stack.pop();
        stack.pop(); // Remove unnecessary varname from stack
        if (Arrays.asList(primitiveTypes).contains(freeType)) {
            semanticError(ctx, "Incompatible type " + freeType);
        }
        return null;
    }

    public Void visitReturnstatement(
                    WACCParser.ReturnstatementContext ctx) {
        if (DEBUG) {
            System.out.println("-Return statement");
            contextDepth(ctx);
        }
        String functionName = stack.pop();
        FUNCTION curFunc = (FUNCTION) currentST
                        .lookupAll(functionName);

        visit(ctx.expr());

        String type = stack.pop();
        String varname = stack.pop();

        if (!curFunc.getType().equals(type)) {
            stack.push(curFunc.getType());
            checkType(ctx, varname, type);
        }

        return null;
    }

    public Void visitExitstatement(WACCParser.ExitstatementContext ctx) {
        if (DEBUG) {
            System.out.println("-Exit statement");
            contextDepth(ctx);
        }
        return visitChildren(ctx);
    }

    public Void visitPrintstatement(
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
        // TODO: Cover println case
        visit(ctx.expr());
        stack.pop();
        stack.pop();
        return null;
    }

    public Void visitIfstatement(WACCParser.IfstatementContext ctx) {
        if (DEBUG) {
            System.out.println("-If statement");
            contextDepth(ctx);
        }
        
        visit(ctx.expr());
        
        String ifExprType = stack.pop();
        stack.pop();
        
        //Check if the condition is a BOOL
        stack.push(BOOL);
        String condition = ctx.expr().getText().replaceAll("\\s","");
        checkType(ctx.expr(),condition,ifExprType);
        
        visit(ctx.stat(0));
        visit(ctx.stat(1));
        return null;
    }

    public Void visitWhilestatement(
                    WACCParser.WhilestatementContext ctx) {
        if (DEBUG) {
            System.out.println("-While statement");
            contextDepth(ctx);
        }
        visit(ctx.expr());
        
        String whileExprType = stack.pop();
        stack.pop();
        
        //Check if the condition is a BOOL
        stack.push(BOOL);
        String condition = ctx.expr().getText().replaceAll("\\s","");
        checkType(ctx.expr(),condition,whileExprType);
        
        visit(ctx.stat());
        
        return null;
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
        visit(ctx.stat(0));
        visit(ctx.stat(1));
        return null;
    }

    /* Assign LHS and assign RHS */

    public Void visitAssignrhsexpr(WACCParser.AssignrhsexprContext ctx) {
        if (DEBUG) {
            System.out.print("-Assign RHS EXPR ");
            contextDepth(ctx);
        }
        visit(ctx.expr());
        String type = stack.pop();
        String varname = stack.pop();
        checkType(ctx, varname, type);
        return null;
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
        visit(ctx.expr(0)); // Visit fst
        visit(ctx.expr(1)); // Visit snd

        String sndType = stack.pop();
        String sndVarname = stack.pop();
        String fstType = stack.pop();
        String fstVarname = stack.pop();
        PAIR comparePair = new PAIR(fstType, sndType);

        checkType(ctx, "newpair(" + fstVarname + "," + sndVarname
                        + ")", comparePair.toString());
        return null;
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
        stack.push(ctx.IDENT().toString());
        stack.push(checkDefinedVariable(ctx));
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
        String curType = renameStringToCharArray(ctx.BASETYPE()
                        .toString().toUpperCase());
        stack.push(curType);
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

        String curVarName = stack.pop();
        visit(ctx.pairelementype(0));
        visit(ctx.pairelementype(1));

        String sndType = stack.pop();
        String fstType = stack.pop();

        PAIR newPair = new PAIR(fstType, sndType);

        currentST.add(curVarName, newPair);
        stack.push(newPair.toString());

        return null;
    }

    public Void visitPairetbasetype(
                    WACCParser.PairetbasetypeContext ctx) {
        if (DEBUG) {
            System.out.print("-Pairelementype basetype ");
            contextDepth(ctx);
        }
        String curType = renameStringToCharArray(ctx.BASETYPE()
                        .toString().toUpperCase());
        stack.push(curType);
        return null;
    }

    public Void visitPairetarraytype(
                    WACCParser.PairetarraytypeContext ctx) {
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
        stack.push(ctx.INTLITERAL().toString());
        stack.push(INT);
        return null;
    }

    public Void visitBooleanliteral(
                    WACCParser.BooleanliteralContext ctx) {
        if (DEBUG) {
            System.out.print("-Boolean literal ");
            contextDepth(ctx);
        }
        stack.push(ctx.BOOLEANLITERAL().toString());
        stack.push(BOOL);
        return null;
    }

    public Void visitCharliteral(WACCParser.CharliteralContext ctx) {
        if (DEBUG) {
            System.out.print("-Char literal ");
            contextDepth(ctx);
        }
        stack.push(ctx.CHARLITERAL().toString());
        stack.push(CHAR);
        return null;
    }

    public Void visitStringliteral(WACCParser.StringliteralContext ctx) {
        if (DEBUG) {
            System.out.print("-String literal ");
            contextDepth(ctx);
        }
        stack.push(ctx.STRINGLITERAL().toString());
        stack.push(STRING);
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
        stack.push(ctx.IDENT().toString());
        stack.push(checkDefinedVariable(ctx));
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

        // Visit RHS
        visit(ctx.expr(1));

        String rhsType = stack.pop();
        String rhsVarName = stack.pop();

        checkType(ctx.expr(1), rhsVarName, rhsType);
        
        stack.push(rhsType);

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

    public Void visitParam_list(WACCParser.Param_listContext ctx) {
        if (DEBUG) {
            System.out.print("-Param_list ");
            contextDepth(ctx);
        }
        String functionName = stack.peek();
        FUNCTION curFunc = (FUNCTION) currentST
                        .lookupAll(functionName);

        System.out.println("CHILDREN COUNT: " + ctx.param().size());
        curFunc.paramSize(ctx.param().size());
        return visitChildren(ctx);
    }

    public Void visitParam(WACCParser.ParamContext ctx) {
        if (DEBUG) {
            System.out.print("-Param ");
            contextDepth(ctx);
        }
        String functionName = stack.peek();
        FUNCTION curFunc = (FUNCTION) currentST
                        .lookupAll(functionName);

        String paramName = ctx.IDENT().toString();
        stack.push(paramName);
        visit(ctx.type());
        String paramReturnType = stack.pop();

        PARAM newParam = new PARAM(paramReturnType);
        currentST.add(paramName, newParam);
        curFunc.addParam(newParam);

        return null;
    }

    public Void visitFunc(WACCParser.FuncContext ctx) {
        if (DEBUG) {
            System.out.print("-Function: " + ctx.IDENT().toString());
            contextDepth(ctx);
        }
        // TODO
        // Check for duplicate function
        String functionName = ctx.IDENT().toString();
        IDENTIFIER object = currentST.lookupAll(functionName);
        if (object != null) {
            semanticError(ctx, "\"" + functionName
                            + "\" is already defined in this scope");
        }

        stack.push(functionName);
        // Check for function type
        visit(ctx.type());
        String functionReturnType = stack.pop();
        currentST.add(functionName, new FUNCTION(functionReturnType));

        SymbolTable<String, IDENTIFIER> st = new SymbolTable<String, IDENTIFIER>(
                        currentST);
        currentST = st;

        // Grabs the params and store it in the function object
        if (ctx.param_list() != null) {
            visit(ctx.param_list());
        }

        visit(ctx.stat());

        currentST = st.getEncSymTable();

        return null;
    }
}
