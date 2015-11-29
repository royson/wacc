package compiler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.TerminalNode;

import semantics.ARRAY;
import semantics.FUNCTION;
import semantics.IDENTIFIER;
import semantics.PAIR;
import semantics.PARAM;
import semantics.SymbolTableWrapper;
import semantics.VARIABLE;
import antlr.WACCParser;
import antlr.WACCParser.ExprContext;
import antlr.WACCParser.FuncContext;
import antlr.WACCParserBaseVisitor;

public class CodeGenVisitor extends WACCParserBaseVisitor<Void> {

    private SymbolTableWrapper<String> currentST;
    private Stack<String> stack = new Stack<String>();
    private Stack<String> saveStack = new Stack<String>();
    private String currentFunctionName = "";

    private final static String INT = "INT";
    private final static String BOOL = "BOOL";
    private final static String CHAR = "CHAR";
    private final static String STRING = "CHAR[]";

    private String[] primitiveTypes = { INT, BOOL, CHAR, STRING };

    private boolean DEBUG = true;

    // Storage of program
    private List<String> text = new ArrayList<String>();
    private List<String> data = new ArrayList<String>();
    private List<String> print = new ArrayList<String>();

    // Stuff needed by Codegenerator
    private int messageCount = 0;
    private int branchCount = 0;

    public List<String> getText() {
        return text;
    }

    public List<String> getData() {
        return data;
    }

    public List<String> getPrint() {
        return print;
    }

    /* Helper functions */
    private void printStack() {
        System.out.println("-----PRINTING STACK-----");
        System.out.println(Arrays.toString(stack.toArray()));
        System.out.println("------------------------");
    }

    @SuppressWarnings("unchecked")
    private void newScope() {
        SymbolTableWrapper<String> st = new SymbolTableWrapper<String>(
                        currentST);
        currentST = st;
        saveStack = (Stack<String>) stack.clone();
        pushLR();
    }

    @SuppressWarnings("unchecked")
    private void freeScope() {
        currentST = currentST.getEncSymTable();
        stack = (Stack<String>) saveStack.clone();
    }

    private void pushLR() {
        text.add("PUSH {lr}");
    }

    private void visitPairElem(ParserRuleContext ctx, boolean fst) {

        // type of pair is not needed
        stack.pop();

        String pairName = stack.peek();
        IDENTIFIER obj = currentST.lookUpAllIdentifier(pairName);

        if (!(obj instanceof PAIR)) {
            // obj must be in local parameters
            obj = currentST.lookUpAllParam(pairName);

            if (!(obj instanceof PAIR)) {
                Utils.semanticError(ctx, "Something went wrong");
            }
        }
        PAIR pair = (PAIR) obj;
        if (fst) {
            stack.push(pair.getFstType());
        } else {
            stack.push(pair.getSndType());
        }
    }

    // This function checks x in a[x] where arrayElemName is x;
    private void checkArrayElementVariableName(String arrayElemName,
                    ParserRuleContext ctx) {
        String typeOfArrayElemName = Utils
                        .getPrimitiveType(arrayElemName);
        if (typeOfArrayElemName == null) {
            // ArrayElemName is an object
            stack.push(arrayElemName);

            String arrayElemType = checkDefinedVariable(ctx);
            stack.push(INT);
            checkType(ctx, arrayElemName, arrayElemType);

            // arrayElem is not needed in stack
            stack.pop();
        } else if (!typeOfArrayElemName.equals(INT)) {
            stack.push(INT);
            checkType(ctx, arrayElemName, typeOfArrayElemName);
        } else {
            // Pass validation
        }
    }

    private void visitBinaryoperator(ParserRuleContext ctx,
                    String binaryOp, ParserRuleContext lhs,
                    ParserRuleContext rhs) {
        // Visit LHS
        visit(lhs);

        String lhsType = stack.pop();

        // Visit RHS
        visit(rhs);

        String rhsType = stack.pop();
        String rhsExpr = stack.pop();

        // check arguments for binary operation
        String lhsExpr = stack.peek();

        if (lhsType.contains("Pair(")) {
            lhsType = "pair";
        }
        if (rhsType.contains("Pair(")) {
            rhsType = "pair";
        }

        String returnType = checkBinaryOpArgument(ctx, lhs, binaryOp,
                        lhsExpr, lhsType);
        returnType = checkBinaryOpArgument(ctx, rhs, binaryOp,
                        rhsExpr, rhsType);

        // check if both argument types are the same
        // mainly for '>' '>=' '<=' '<' cases
        stack.push(rhsType);
        checkType(ctx, lhsExpr, lhsType);

        // Push the new expression into the stack
        // remove unused expression
        stack.pop();
        String newExpr = (lhsExpr + binaryOp + rhsExpr).replaceAll(
                        "\\s", "");
        stack.push(newExpr);
        stack.push(returnType);
    }

    private void checkBinaryOpType(ParserRuleContext ctx, String type) {
        if (!(Arrays.asList(primitiveTypes).contains(type))) {
            Utils.semanticError(ctx, "Incompatible type " + type);
        }
    }

    private String checkBinaryOpArgument(ParserRuleContext ctx,
                    ParserRuleContext ectx, String binaryOp,
                    String binaryExpr, String binaryType) {
        String returnType = "";

        // check if rhsType match with operator's requirement
        switch (binaryOp) {
        case "*":
        case "/":
        case "%":
        case " + ":
        case " - ":
            stack.push(INT);
            checkType(ectx, binaryExpr, binaryType);
            checkBinaryOpType(ctx, binaryType);
            returnType = binaryType;
            break;
        case ">":
        case ">=":
        case "<":
        case "<=":
            if (!binaryType.equals(INT) && !binaryType.equals(CHAR)) {
                stack.push(INT);
                checkType(ectx, binaryExpr, binaryType);
                checkBinaryOpType(ctx, binaryType);
            }
            returnType = BOOL;
            break;
        case "&&":
        case "||":
            stack.push(BOOL);
            checkType(ectx, binaryExpr, binaryType);
            checkBinaryOpType(ctx, binaryType);
        case "==":
        case "!=":
            returnType = BOOL;
            break;
        }

        return returnType;
    }

    private void checkType(ParserRuleContext ctx, String value,
                    String type) {
        String compareType = stack.pop();

        // Comparing null values to pairs
        if ((Utils.isANullPair(type) && Utils.isAPair(compareType))
                        || (Utils.isANullPair(compareType) && Utils
                                        .isAPair(type))) {
            return;
        }

        if (!compareType.equals(type)) {
            String errorMessage = "Incompatible type at "
                            + value.replaceAll("\\s", "");
            errorMessage += " (expected: " + compareType;
            errorMessage += ", actual: " + type + ")";
            Utils.semanticError(ctx, errorMessage);
        }
    }

    private String checkDefinedVariable(ParserRuleContext ctx) {
        String curIdentToCheck = stack.peek();

        // Object should take precedence
        IDENTIFIER object = currentST
                        .lookUpAllIdentifier(curIdentToCheck);

        if (object == null) {
            // Need to go up and find all params
            object = currentST.lookUpAllParam(curIdentToCheck);

            if (object == null) {
                String errorMessage = "Variable " + curIdentToCheck
                                + " is not defined in this scope";
                Utils.semanticError(ctx, errorMessage);
            }
        }
        return object.getType();
    }

    /* Write functions to traverse tree below here */

    /* Visit the main program */

    public Void visitProgram(WACCParser.ProgramContext ctx) {
        if (DEBUG) {
            System.out.println("-Program");
        }

        // Traverse all functions first
        List<FuncContext> funcList = ctx.func();
        for (FuncContext f : funcList) {
            visit(f);
        }

        // Go into the main program
        text.add("main:");
        pushLR();

        visit(ctx.stat());

        // TODO: Need to figure out where this LDR r0 =0 goes, will have clash
        // on if statements
        text.add("LDR r0, =0");
        visit(ctx.END());
        return null;
    }

    /* Functions to visit statements */

    public Void visitVarinit(WACCParser.VarinitContext ctx) {
        String varName = ctx.IDENT().toString();

        if (DEBUG) {
            System.out.println("-Variable init statement " + varName
                            + " ");
        }
        visit(ctx.type());
        String varType = stack.peek();
        subSP(varType);

        visit(ctx.assignRHS());

        addSP(varType);
        return null;
    }

    private void addSP(String varType) {
        switch (varType) {
        case "INT":
        case "CHAR[]":
            text.add("ADD sp, sp, #4");
            break;
        case "BOOL":
        case "CHAR":
            text.add("ADD sp, sp, #1");
            break;
        }
    }

    private void subSP(String varType) {
        switch (varType) {
        case "INT":
        case "CHAR[]":
            text.add("SUB sp, sp, #4");
            break;
        case "BOOL":
        case "CHAR":
            text.add("SUB sp, sp, #1");
            break;
        }
    }

    public Void visitAssignment(WACCParser.AssignmentContext ctx) {
        if (DEBUG) {
            System.out.println("-Assignment statement");
        }
        visit(ctx.assignLHS());
        visit(ctx.assignRHS());
        return null;
    }

    public Void visitReadstatement(WACCParser.ReadstatementContext ctx) {
        if (DEBUG) {
            System.out.println("-Read statement");
        }
        visit(ctx.assignLHS());
        return null;
    }

    public Void visitFreestatement(WACCParser.FreestatementContext ctx) {
        // Can only free arrays and pairs
        if (DEBUG) {
            System.out.println("-Free statement");
        }
        visit(ctx.expr());
        return null;
    }

    public Void visitReturnstatement(
                    WACCParser.ReturnstatementContext ctx) {
        if (DEBUG) {
            System.out.println("-Return statement");
        }
        visit(ctx.expr());
        text.add("MOV r0, r4");
        text.add("POP {pc}");
        return null;
    }

    public Void visitExitstatement(WACCParser.ExitstatementContext ctx) {
        if (DEBUG) {
            System.out.println("-Exit statement");
        }
        visit(ctx.expr());
        text.add("MOV r0, r4");
        text.add("BL exit");
        return null;
    }

    public Void visitPrintstatement(
                    WACCParser.PrintstatementContext ctx) {
        if (DEBUG) {
            System.out.println("-Print statement");

        }
        visit(ctx.expr());
        return null;
    }

    public Void visitPrintlnstatement(
                    WACCParser.PrintlnstatementContext ctx) {
        if (DEBUG) {
            System.out.println("-Print line statement ");
        }
        visit(ctx.expr());
        return null;
    }

    public Void visitIfstatement(WACCParser.IfstatementContext ctx) {
        if (DEBUG) {
            System.out.println("-If statement");
        }
        visit(ctx.expr());
        // giving a new scope for each stat
        for (int i = 0; i < 2; i++) {
            newScope();
            visit(ctx.stat(i));
            freeScope();
        }
        return null;
    }

    public Void visitWhilestatement(
                    WACCParser.WhilestatementContext ctx) {
        if (DEBUG) {
            System.out.println("-While statement");
        }
        visit(ctx.expr());
        newScope();
        visit(ctx.stat());
        freeScope();
        return null;
    }

    public Void visitBeginendstatement(
                    WACCParser.BeginendstatementContext ctx) {
        if (DEBUG) {
            System.out.println("-Begin end statement ");
        }
        newScope();
        visit(ctx.stat());
        freeScope();
        return null;
    }

    public Void visitStatementblock(
                    WACCParser.StatementblockContext ctx) {
        if (DEBUG) {
            System.out.println("-Statement block statement ");
        }
        visit(ctx.stat(0));
        visit(ctx.stat(1));
        return null;
    }

    /* Assign LHS and assign RHS */

    public Void visitAssignrhsexpr(WACCParser.AssignrhsexprContext ctx) {
        if (DEBUG) {
            System.out.println("-Assign RHS EXPR ");
        }
        String varType = stack.pop();
        visit(ctx.expr());
        assignrhsexprhelper(varType);
        return null;
    }

    private void assignrhsexprhelper(String varType) {
        switch (varType) {
        case "INT":
        case "CHAR[]":
            text.add("STR r4, [sp]");
            break;
        case "CHAR":
        case "BOOL":
            text.add("STRB r4, [sp]");
            break;
        }
    }

    public Void visitAssignrhsarraylit(
                    WACCParser.AssignrhsarraylitContext ctx) {
        if (DEBUG) {
            System.out.println("-Assign RHS arraylit");
        }
        return visitChildren(ctx);
    }

    public Void visitArrayLiter(WACCParser.ArrayLiterContext ctx) {
        if (DEBUG) {
            System.out.println("-Assign ArrayLiter");
        }
        List<ExprContext> exprs = ctx.expr();
        for (ExprContext ectx : exprs) {
            visit(ectx);
        }
        return null;
    }

    public Void visitAssignrhsnewpair(
                    WACCParser.AssignrhsnewpairContext ctx) {
        if (DEBUG) {
            System.out.println("-Assign RHS newpair");
        }
        visit(ctx.expr(0)); // Visit fst
        visit(ctx.expr(1)); // Visit snd
        return null;
    }

    public Void visitAssignrhspairelem(
                    WACCParser.AssignrhspairelemContext ctx) {
        if (DEBUG) {
            System.out.println("-Assign RHS pairelem");
        }
        visit(ctx.pairElem());
        return null;
    }

    public Void visitArg_list(WACCParser.Arg_listContext ctx) {
        if (DEBUG) {
            System.out.println("-ArgList call ");
        }

        // List of args
        List<ExprContext> args = ctx.expr();
        for (int i = 0; i < args.size(); i++) {
            visit(args.get(i));
        }
        return null;
    }

    public Void visitAssignrhscall(WACCParser.AssignrhscallContext ctx) {
        if (DEBUG) {
            System.out.println("-Assign RHS call");
        }
        // TODO: [CODEGEN - Assignrhscall] Traversal removed at this point
        return null;
    }

    public Void visitAssignlhsident(
                    WACCParser.AssignlhsidentContext ctx) {
        if (DEBUG) {
            System.out.println("-Assign LHS ident");
        }
        return null;
    }

    public Void visitAssignlhsarrayelem(
                    WACCParser.AssignlhsarrayelemContext ctx) {
        if (DEBUG) {
            System.out.println("-Assign LHS array elem");
        }
        return null;
    }

    public Void visitAssignlhspairelem(
                    WACCParser.AssignlhspairelemContext ctx) {
        if (DEBUG) {
            System.out.println("-Assign LHS pair elem");
        }
        visit(ctx.pairElem());
        return null;
    }

    public Void visitPairfstelem(WACCParser.PairfstelemContext ctx) {
        if (DEBUG) {
            System.out.println("-Assign Pair fst Elem");
        }
        visit(ctx.expr());
        visitPairElem(ctx, true);
        return null;
    }

    public Void visitPairsndelem(WACCParser.PairsndelemContext ctx) {
        if (DEBUG) {
            System.out.println("-Assign Pair snd Elem");
        }
        visit(ctx.expr());
        visitPairElem(ctx, false);
        return null;
    }

    /* Type */

    public Void visitTypebasetype(WACCParser.TypebasetypeContext ctx) {
        if (DEBUG) {
            System.out.println("-Type BASETYPE");
        }
        String arrayBrackets = "";
        List<TerminalNode> brackets = ctx.LBRACK();

        int i = 0;
        while (i < brackets.size()) {
            arrayBrackets += "[]";
            i++;
        }

        String curType = Utils.renameStringToCharArray(ctx.BASETYPE()
                        .toString().toUpperCase());
        curType += arrayBrackets;

        stack.push(curType);

        return null;
    }

    public Void visitTypepairtype(WACCParser.TypepairtypeContext ctx) {
        if (DEBUG) {
            System.out.println("-Type PAIRTYPE");
        }
        visit(ctx.pairtype());
        return null;
    }

    public Void visitArraytype(WACCParser.ArraytypeContext ctx) {
        if (DEBUG) {
            System.out.println("-Arraytype ");
        }
        visit(ctx.type());
        return null;
    }

    public Void visitPairtype(WACCParser.PairtypeContext ctx) {
        if (DEBUG) {
            System.out.println("-Pairtype ");
        }
        return null;
    }

    public Void visitPairetbasetype(
                    WACCParser.PairetbasetypeContext ctx) {
        if (DEBUG) {
            System.out.println("-Pairelementype basetype");
        }
        return null;
    }

    public Void visitPairetarraytype(
                    WACCParser.PairetarraytypeContext ctx) {
        if (DEBUG) {
            System.out.println("-Pairelementype arraytype");
        }
        visit(ctx.arraytype());
        return null;
    }

    public Void visitPairetpair(WACCParser.PairetpairContext ctx) {
        if (DEBUG) {
            System.out.println("-Pairelementype pair");

        }
        return null;
    }

    public Void visitTerminal(TerminalNode node) {
        if (DEBUG) {
            System.out.println("-Terminal " + node.toString());
        }
        String terminalString = node.toString();

        if (terminalString.equals("end")) {
            text.add("POP {pc}");
            text.add(".ltorg");
        }
        return null;
    }

    /* Visit expressions */
    public Void visitIntegerliteral(
                    WACCParser.IntegerliteralContext ctx) {
        if (DEBUG) {
            System.out.println("-Int literal");
        }
        text.add("LDR r4, =" + ctx.getText());
        return null;
    }

    public Void visitBooleanliteral(
                    WACCParser.BooleanliteralContext ctx) {
        if (DEBUG) {
            System.out.println("-Boolean literal ");
        }
        String value = ctx.getText();
        if (value.equals("true")) {
            text.add("MOV r4, #1");
        } else {
            text.add("MOV r4, #0");
        }
        return null;
    }

    public Void visitCharliteral(WACCParser.CharliteralContext ctx) {
        if (DEBUG) {
            System.out.println("-Char literal");
        }
        text.add("MOV r4, #" + ctx.getText());
        return null;
    }

    public Void visitStringliteral(WACCParser.StringliteralContext ctx) {
        if (DEBUG) {
            System.out.println("-String literal");
        }
        String message = ctx.getText();

        text.add("LDR r4, =msg" + messageCount);
        data.add("msg_" + messageCount);
        // Subtract 2 for the "" surrounding string
        data.add(".word " + (message.length() - 2));
        data.add(".ascii " + message);
        messageCount += 1;
        return null;
    }

    public Void visitPairliteral(WACCParser.PairliteralContext ctx) {
        if (DEBUG) {
            System.out.println("-Pair literal");
        }
        return null;
    }

    public Void visitIdentifier(WACCParser.IdentifierContext ctx) {
        if (DEBUG) {
            System.out.println("-Identifier");
        }
        return null;
    }

    public Void visitArrayelement(WACCParser.ArrayelementContext ctx) {
        if (DEBUG) {
            System.out.println("-Array elements");
        }
        visit(ctx.arrayElem());
        return null;
    }

    public Void visitArrayElem(WACCParser.ArrayElemContext ctx) {
        if (DEBUG) {
            System.out.println("-Arrayelem");
        }
        return null;
    }

    public Void visitUnaryoperator(WACCParser.UnaryoperatorContext ctx) {
        if (DEBUG) {
            System.out.println("-Unary operator");
        }
        visit(ctx.expr());
        return null;
    }

    public Void visitBinarymultipledivideoperator(
                    WACCParser.BinarymultipledivideoperatorContext ctx) {
        visitBinaryoperator(ctx, ctx.multiplyDivideOp().getText(),
                        ctx.expr(0), ctx.expr(1));
        return null;
    }

    public Void visitBinaryaddsubtractoperator(
                    WACCParser.BinaryaddsubtractoperatorContext ctx) {
        visitBinaryoperator(ctx, ctx.addSubtractOp().getText(),
                        ctx.expr(0), ctx.expr(1));
        return null;
    }

    public Void visitBinarycomparatoroperator(
                    WACCParser.BinarycomparatoroperatorContext ctx) {
        visitBinaryoperator(ctx, ctx.comparatorOp().getText(),
                        ctx.expr(0), ctx.expr(1));
        return null;
    }

    public Void visitBinaryequalityoperator(
                    WACCParser.BinaryequalityoperatorContext ctx) {
        visitBinaryoperator(ctx, ctx.equalityOp().getText(),
                        ctx.expr(0), ctx.expr(1));
        return null;
    }

    public Void visitBinarylogicalandoperator(
                    WACCParser.BinarylogicalandoperatorContext ctx) {
        visitBinaryoperator(ctx, ctx.logicalAndOp().getText(),
                        ctx.expr(0), ctx.expr(1));
        return null;
    }

    public Void visitBinarylogicaloroperator(
                    WACCParser.BinarylogicaloroperatorContext ctx) {
        visitBinaryoperator(ctx, ctx.logicalOrOp().getText(),
                        ctx.expr(0), ctx.expr(1));
        return null;
    }

    public Void visitBrackets(WACCParser.BracketsContext ctx) {
        if (DEBUG) {
            System.out.println("-Brackets");
        }
        return visitChildren(ctx);
    }

    /* Visit function */

    public Void visitParam_list(WACCParser.Param_listContext ctx) {
        if (DEBUG) {
            System.out.println("-Param_list");
        }
        for (int i = 0; i < ctx.param().size(); i++) {
            visit(ctx.param(i));
        }
        return null;
    }

    public Void visitParam(WACCParser.ParamContext ctx) {
        if (DEBUG) {
            System.out.println("-Param");
        }
        visit(ctx.type());
        return null;
    }

    public Void visitFunc(WACCParser.FuncContext ctx) {
        String functionName = ctx.IDENT().toString();
        if (DEBUG) {
            System.out.println("-Function: " + functionName);
        }

        text.add("f_" + functionName + ":");
        newScope();

        // Visit parameters
        if (ctx.param_list() != null) {
            visit(ctx.param_list());
        }

        currentFunctionName = functionName;
        visit(ctx.stat());
        freeScope();
        currentFunctionName = "";
        visit(ctx.END());
        return null;
    }
}
