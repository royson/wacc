package compiler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.TerminalNode;

import semantics.ARRAY;
import semantics.IDENTIFIER;
import semantics.PAIR;
import semantics.SymbolTable;
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
    private int PASS = 1;

    // Storage of program - PASS 2
    private List<String> text = new ArrayList<String>();
    private List<String> data = new ArrayList<String>();
    private List<String> print = new ArrayList<String>();

    // Generating information about scope - PASS 1
    private Stack<String> scopeStack = new Stack<String>();
    private String currentScope;

    // Stuff needed by Codegenerator
    private int messageCount = 0;
    private int branchCount = 0;

    // TODO: spPosition has to be modified on entering / exiting scope
    private int spPosition = 0;

    public List<String> getText() {
        return text;
    }

    public List<String> getData() {
        return data;
    }

    public List<String> getPrint() {
        return print;
    }

    public int getPass() {
        return PASS;
    }

    public void setPass(int PASS) {
        this.PASS = PASS;
        DEBUG = false;
    }

    /* Helper functions */
    private void printStack() {
        System.out.println("-----PRINTING STACK-----");
        System.out.println(Arrays.toString(stack.toArray()));
        System.out.println("Scope stack: "
                        + Arrays.toString(scopeStack.toArray()));
        System.out.println("------------------------");
    }

    private void printspPosition() {
        System.out.println("STACK POINTER pos: " + spPosition);
    }

    @SuppressWarnings("unchecked")
    private void newScope(String scopeName) {
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

    // Print statements
    private boolean printINT = false;
    private boolean printLN = false;
    private boolean printSTRING = false;

    private void addPrintINT() {
        if (printINT) {
            return;
        }
        printINT = true;
        // Modify data
        data.add("msg_" + messageCount + ":");
        data.add(".word 3");
        data.add(".ascii  \"%d\\0\"");

        // Add the print instruction
        print.add("p_print_int:");
        print.add("PUSH {lr}");
        print.add("MOV r1, r0");
        print.add("LDR r0, =msg_" + messageCount);
        print.add("ADD r0, r0, #4");
        print.add("BL printf");
        print.add("MOV r0, #0");
        print.add("BL fflush");
        print.add("POP {pc}");
        messageCount += 1;
    }

    private void addPrintLN() {
        if (printLN) {
            return;
        }
        printLN = true;
        // Modify data
        data.add("msg_" + messageCount + ":");
        data.add(".word 1");
        data.add(".ascii  \"\\0\"");

        // Add the print instruction
        print.add("p_print_ln:");
        print.add("PUSH {lr}");
        print.add("LDR r0, =msg_" + messageCount);
        print.add("ADD r0, r0, #4");
        print.add("BL puts");
        print.add("MOV r0, #0");
        print.add("BL fflush");
        print.add("POP {pc}");
        messageCount += 1;
    }

    private void addPrintSTRING() {
        if (printSTRING) {
            return;
        }
        printSTRING = true;
        // Modify data
        data.add("msg_" + messageCount + ":");
        data.add(".word 5");
        data.add(".ascii  \"%.*s\\0\"");

        // Add the print instruction
        print.add("p_print_string:");
        print.add("PUSH {lr}");
        print.add("LDR r1, [r0]");
        print.add("ADD r0, r0, #4");
        print.add("LDR r0, =msg_" + messageCount);
        print.add("ADD r0, r0, #4");
        print.add("BL printf");
        print.add("MOV r0, #0");
        print.add("BL fflush");
        print.add("POP {pc}");
        messageCount += 1;
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
        System.out.println("Current pass: " + PASS);
        if (PASS == 1) {
            currentST = new SymbolTableWrapper<String>();
        }
        if (DEBUG) {
            System.out.println("-Program");
        }

        scopeStack.push("main");

        // Traverse all functions first
        List<FuncContext> funcList = ctx.func();
        for (FuncContext f : funcList) {
            visit(f);
        }

        int scopeSize = currentST.getScopeSize();

        // Go into the main program
        if (PASS == 2) {
            text.add("main:");
            pushLR();

            // Allocate memory
            allocateScopeMemory(scopeSize);
        }

        currentScope = scopeStack.pop();
        visit(ctx.stat());

        if (PASS == 1) {
            // Update the positions of variables in the memory
            HashMap<String, Integer> stHashMap = currentST
                            .getStLabel().getSt();
            for (String key : stHashMap.keySet()) {
                stHashMap.put(key, spPosition - stHashMap.get(key));
            }
            currentST.setScopeSize(spPosition);
            spPosition = 0;
        }

        // TODO: Need to figure out where this LDR r0 =0 goes, will have clash
        // on if statements
        if (PASS == 2) {
            currentST.printST();

            // Deallocate memory
            deallocateScopeMemory(scopeSize);

            text.add("LDR r0, =0");
        }
        visit(ctx.END());
        return null;
    }

    private void deallocateScopeMemory(int scopeSize) {
        if (scopeSize > 0) {
            while (scopeSize > 0) {
                if (scopeSize >= 1024) {
                    text.add("ADD sp, sp, #" + 1024);
                } else {
                    text.add("ADD sp, sp, #" + scopeSize);
                }
                scopeSize -= 1024;
            }
        }
    }

    private void allocateScopeMemory(int scopeSize) {
        if (scopeSize > 0) {
            while (scopeSize > 0) {
                if (scopeSize >= 1024) {
                    text.add("SUB sp, sp, #" + 1024);
                } else {
                    text.add("SUB sp, sp, #" + scopeSize);
                }
                scopeSize -= 1024;
            }
        }
    }

    /* Functions to visit statements */

    public Void visitVarinit(WACCParser.VarinitContext ctx) {
        String varName = ctx.IDENT().toString();

        if (DEBUG) {
            System.out.println("-Variable init statement " + varName
                            + " ");
        }

        stack.push(varName);
        visit(ctx.type());
        String varType = stack.peek();

        if (PASS == 1) {
            spPosition += addSP(varType);
            currentST.addLabel(varName, spPosition);

            if (!varType.startsWith("Pair")
                            && !(Utils.isAnArray(varType))) {
                currentST.addIdentifier(varName,
                                new VARIABLE(varType));
            } else if (Utils.isAnArray(varType)) {
                currentST.addIdentifier(varName, new ARRAY(varType));
            }
        }
        visit(ctx.assignRHS());

        return null;
    }

    private int addSP(String varType) {
        if (varType.equals("BOOL") || varType.equals("CHAR")) {
            return 1;
        }
        return 4;
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

        // Clearing the stack
        String exprType = stack.pop();
        String exprName = stack.pop();

        if (PASS == 2) {
            text.add("MOV r0, r4");
            text.add("POP {pc}");
        }
        return null;
    }

    public Void visitExitstatement(WACCParser.ExitstatementContext ctx) {
        if (DEBUG) {
            System.out.println("-Exit statement");
        }
        visit(ctx.expr());

        // Clearing the stack
        String exprType = stack.pop();
        String exprName = stack.pop();

        if (PASS == 2) {
            text.add("MOV r0, r4");
            text.add("BL exit");
        }
        return null;
    }

    public Void visitPrintstatement(
                    WACCParser.PrintstatementContext ctx) {
        if (DEBUG) {
            System.out.println("-Print statement");

        }
        visit(ctx.expr());

        // Clear the stack
        String varType = stack.pop(); // Extract the type
        stack.pop(); // Clear the name

        if (PASS == 1) {
            printHelperPass1(varType);
        }

        if (PASS == 2) {
            text.add("MOV r0, r4");
            printHelperPass2(varType);
        }

        return null;
    }

    public Void visitPrintlnstatement(
                    WACCParser.PrintlnstatementContext ctx) {
        if (DEBUG) {
            System.out.println("-Print line statement ");
        }
        visit(ctx.expr());

        // Clear the stack
        String varType = stack.pop(); // Extract the type
        stack.pop(); // Clear the name

        if (PASS == 1) {
            printHelperPass1(varType);
            addPrintLN();
        }

        if (PASS == 2) {
            text.add("MOV r0, r4"); // TODO: Print - figure out what this is for
            printHelperPass2(varType);
            text.add("BL p_print_ln");
        }

        return null;
    }

    // Adds the print functions into the code for output
    private void printHelperPass1(String type) {
        switch (type) {
        case "INT":
            addPrintINT();
            break;
        case "CHAR[]":
            addPrintSTRING();
            break;
        }
    }

    // Creates link to print functions in main code
    private void printHelperPass2(String type) {
        switch (type) {
        case "INT":
            text.add("BL p_print_int");
            break;
        case "CHAR[]":
            text.add("BL p_print_string");
            break;
        }
    }

    public Void visitIfstatement(WACCParser.IfstatementContext ctx) {
        if (DEBUG) {
            System.out.println("-If statement");
        }
        // TODO: If statements removed for now
        // visit(ctx.expr());
        // // giving a new scope for each stat
        // for (int i = 0; i < 2; i++) {
        // newScope();
        // visit(ctx.stat(i));
        // freeScope();
        // }
        return null;
    }

    public Void visitWhilestatement(
                    WACCParser.WhilestatementContext ctx) {
        if (DEBUG) {
            System.out.println("-While statement");
        }
        // TODO: While statement removed for now
        // visit(ctx.expr());
        // newScope();
        // visit(ctx.stat());
        // freeScope();
        return null;
    }

    public Void visitBeginendstatement(
                    WACCParser.BeginendstatementContext ctx) {
        if (DEBUG) {
            System.out.println("-Begin end statement ");
        }
        // TODO: Begin-end statement removed for now
        // newScope();
        // visit(ctx.stat());
        // freeScope();
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
        String varName = stack.pop();
        int offset = currentST.lookUpAllLabel(varName);

        visit(ctx.expr());
        String exprType = stack.pop();
        String exprName = stack.pop();

        if (PASS == 2) {
            if (varType.equals("BOOL") || varType.equals("CHAR")) {
                if (offset != 0) {
                    text.add("STRB r4, [sp, #" + offset + "]");
                } else {
                    text.add("STRB r4, [sp]");
                }
            } else {
                if (offset != 0) {
                    text.add("STR r4, [sp, #" + offset + "]");
                } else {
                    text.add("STR r4, [sp]");
                }
            }
        }
        return null;
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
        stack.push(ctx.IDENT().toString());
        stack.push(checkDefinedVariable(ctx));
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

        if (PASS == 2) {
            if (terminalString.equals("end")) {
                text.add("POP {pc}");
                text.add(".ltorg");
            }
        }
        return null;
    }

    /* Visit expressions */
    public Void visitIntegerliteral(
                    WACCParser.IntegerliteralContext ctx) {
        if (DEBUG) {
            System.out.println("-Int literal");
        }
        int value = Integer.parseInt(ctx.getText());

        stack.push(Integer.toString(value));
        stack.push("INT");

        if (PASS == 2) {
            text.add("LDR r4, =" + value);
        }
        return null;
    }

    public Void visitBooleanliteral(
                    WACCParser.BooleanliteralContext ctx) {
        if (DEBUG) {
            System.out.println("-Boolean literal ");
        }
        String value = ctx.getText();

        stack.push(value);
        stack.push("BOOL");

        if (PASS == 2) {
            if (value.equals("true")) {
                text.add("MOV r4, #1");
            } else {
                text.add("MOV r4, #0");
            }
        }
        return null;
    }

    public Void visitCharliteral(WACCParser.CharliteralContext ctx) {
        if (DEBUG) {
            System.out.println("-Char literal");
        }

        stack.push(ctx.getText());
        stack.push("CHAR");

        if (PASS == 2) {
            text.add("MOV r4, #" + ctx.getText());
        }
        return null;
    }

    public Void visitStringliteral(WACCParser.StringliteralContext ctx) {
        if (DEBUG) {
            System.out.println("-String literal");
        }
        String message = ctx.getText();

        stack.push(message);
        stack.push("CHAR[]");
        
        //TODO: treat escape seq as 1 char
        if (PASS == 2) {
            text.add("LDR r4, =msg_" + messageCount);
            data.add("msg_" + messageCount + ":");
            // Subtract 2 for the "" surrounding string
            data.add(".word " + (message.length() - 2));
            data.add(".ascii " + message);
            messageCount += 1;
        }
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

        stack.push(ctx.IDENT().toString());
        stack.push(checkDefinedVariable(ctx));

        if (PASS == 2) {
            int offset = currentST.lookUpAllLabel(ctx.getText());
            if (offset == 0) {
                text.add("LDR r4, [sp]");
            } else {
                text.add("LDR r4, [sp, #" + offset + "]");
            }
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
        newScope(functionName);

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
