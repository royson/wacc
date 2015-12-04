package compiler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
    private HashMap<String, SymbolTableWrapper<String>> storeScopes = new HashMap<>();
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
    private int nonFunctionBlockCount = 0;

    // TODO: [Scope] spPosition has to be modified on entering / exiting scope
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
        DEBUG = true;
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
        if (DEBUG) {
            System.out.println("-newScope");
        }
        currentST.setSpPos(spPosition);
        spPosition = 0;
        SymbolTableWrapper<String> st = new SymbolTableWrapper<String>(
                        currentST);
        currentST = st;
        currentST.setScopeName(scopeName);
        saveStack = (Stack<String>) stack.clone();
    }

    @SuppressWarnings("unchecked")
    private void freeScope() {
        if (DEBUG) {
            System.out.println("-freeScope");
        }
        
        currentST = currentST.getEncSymTable();
        spPosition = currentST.getSpPos();
        stack = (Stack<String>) saveStack.clone();
    }

    private void addMessageToData(String message) {
        data.add("msg_" + messageCount + ":");
        data.add(".word " + stringLength(message));
        data.add(".ascii  " + message);
        messageCount += 1;
    }

    private int stringLength(String message) {
        // TODO: [String] Improve support for escape characters
        message = message.replace("\\", "");
        int length = message.length();
        length -= 2;
        return length;
    }

    // Print statements
    private boolean printINT = false;
    private boolean printLN = false;
    private boolean printSTRING = false;
    private boolean printBOOL = false;
    private boolean printReference = false;

    private void addPrintINT() {
        if (printINT) {
            return;
        }
        printINT = true;

        String message = "\"%d\\0\"";
        int cMsgCount = messageCount;

        // Modify data
        addMessageToData(message);

        // Add to print list
        print.add("p_print_int:");
        print.add("PUSH {lr}");
        print.add("MOV r1, r0");
        print.add("LDR r0, =msg_" + cMsgCount);
        print.add("ADD r0, r0, #4");
        print.add("BL printf");
        print.add("MOV r0, #0");
        print.add("BL fflush");
        print.add("POP {pc}");
    }

    private void addPrintLN() {
        if (printLN) {
            return;
        }
        printLN = true;

        String message = "\"\\0\"";
        int cMsgCount = messageCount;

        // Modify data
        addMessageToData(message);

        // Add to print list
        print.add("p_print_ln:");
        print.add("PUSH {lr}");
        print.add("LDR r0, =msg_" + cMsgCount);
        print.add("ADD r0, r0, #4");
        print.add("BL puts");
        print.add("MOV r0, #0");
        print.add("BL fflush");
        print.add("POP {pc}");
    }

    private void addPrintSTRING() {
        if (printSTRING) {
            return;
        }
        printSTRING = true;

        String message = "\"%.*s\\0\"";
        int cMsgCount = messageCount;

        // Modify data
        addMessageToData(message);

        // Add to print list
        print.add("p_print_string:");
        print.add("PUSH {lr}");
        print.add("LDR r1, [r0]");
        print.add("ADD r2, r0, #4");
        print.add("LDR r0, =msg_" + cMsgCount);
        print.add("ADD r0, r0, #4");
        print.add("BL printf");
        print.add("MOV r0, #0");
        print.add("BL fflush");
        print.add("POP {pc}");
    }

    private void addPrintBOOL() {
        if (printBOOL) {
            return;
        }
        printBOOL = true;

        String trueStr = "\"true\\0\"";
        String falseStr = "\"false\\0\"";
        int trueCnt = messageCount;
        int falseCnt = messageCount + 1;

        // Modify data
        addMessageToData(trueStr);
        addMessageToData(falseStr);

        // Add to print list
        print.add("p_print_bool:");
        print.add("PUSH {lr}");
        print.add("CMP r0, #0");
        print.add("LDRNE r0, =msg_" + trueCnt);
        print.add("LDREQ r0, =msg_" + falseCnt);
        print.add("ADD r0, r0, #4");
        print.add("BL printf");
        print.add("MOV r0, #0");
        print.add("BL fflush");
        print.add("POP {pc}");
    }

    private void addPrintReference() {
        if (printReference) {
            return;
        }
        printReference = true;

        String message = "\"%p\\0\"";
        int cMsgCount = messageCount;

        // Modify data
        addMessageToData(message);

        // Add to print list
        print.add("p_print_reference:");
        print.add("PUSH {lr}");
        print.add("MOV r1, r0");
        print.add("LDR r0, =msg_" + cMsgCount);
        print.add("ADD r0, r0, #4");
        print.add("BL printf");
        print.add("MOV r0, #0");
        print.add("BL fflush");
        print.add("POP {pc}");
    }

    // Read statements
    boolean readINT = false;
    boolean readCHAR = false;

    private void addReadINT() {
        if (readINT) {
            return;
        }
        readINT = true;

        String message = "\"%d\\0\"";
        int cMsgCount = messageCount;

        // Modify data
        addMessageToData(message);

        // Add to print list
        print.add("p_read_int:");
        print.add("PUSH {lr}");
        print.add("MOV r1, r0");
        print.add("LDR r0, =msg_" + cMsgCount);
        print.add("ADD r0, r0, #4");
        print.add("BL scanf");
        print.add("POP {pc}");
    }

    private void addReadCHAR() {
        if (readCHAR) {
            return;
        }
        readCHAR = true;

        String message = "\" %c\\0\"";
        int cMsgCount = messageCount;

        // Modify data
        addMessageToData(message);

        // Add to print list
        print.add("p_read_char:");
        print.add("PUSH {lr}");
        print.add("MOV r1, r0");
        print.add("LDR r0, =msg_" + cMsgCount);
        print.add("ADD r0, r0, #4");
        print.add("BL scanf");
        print.add("POP {pc}");
    }

    boolean divideByZeroError = false;
    boolean overflowError = false;
    boolean throwRuntimeError = false;
    boolean arrayBounds = false;

    private void addDivideByZeroError() {
        if (divideByZeroError) {
            return;
        }
        divideByZeroError = true;

        String message = "\"DivideByZeroError: divide or modulo by zero\\n\\0\"";
        int cMsgCount = messageCount;

        // Modify data
        addMessageToData(message);

        // Add to print list
        print.add("p_check_divide_by_zero:");
        print.add("PUSH {lr}");
        print.add("CMP r1, #0");
        print.add("LDREQ r0, =msg_" + cMsgCount);
        print.add("BLEQ p_throw_runtime_error");
        print.add("POP {pc}");
        addThrowRuntimeError();
    }

    private void addOverflowError() {
        if (overflowError) {
            return;
        }
        overflowError = true;

        String message = "\"OverflowError: the result is too small/large to store in a 4-byte signed-integer.\\n\"";
        int cMsgCount = messageCount;

        // Modify data
        addMessageToData(message);

        // Add to print list
        print.add("p_throw_overflow_error:");
        print.add("LDR r0, =msg_" + cMsgCount);
        print.add("BL p_throw_runtime_error");
        addThrowRuntimeError();
    }

    private void addThrowRuntimeError() {
        if (throwRuntimeError) {
            return;
        }
        throwRuntimeError = true;

        print.add("p_throw_runtime_error:");
        print.add("BL p_print_string");
        print.add("MOV r0, #-1");
        print.add("BL exit");
        addPrintSTRING();
    }

    private void addCheckArrayBounds() {
        if (arrayBounds) {
            return;
        }
        arrayBounds = true;

        int cMsgCount = messageCount;
        String firstMessage = "\"ArrayIndexOutOfBoundsError: negative index\\n\\0\"";
        String secondMessage = "\"ArrayIndexOutOfBoundsError: index too large\\n\\0\"";

        // Modify data
        addMessageToData(firstMessage);
        addMessageToData(secondMessage);

        print.add("p_check_array_bounds:");
        print.add("PUSH {lr}");
        print.add("CMP r0, #0");
        print.add("LDRLT r0, =msg_" + cMsgCount);
        print.add("BLLT p_throw_runtime_error");
        print.add("LDR r1, [r1]");
        print.add("CMP r0, r1");
        print.add("LDRCS r0, =msg_" + (cMsgCount + 1));
        print.add("BLCS p_throw_runtime_error");
        print.add("POP {pc}");
        addThrowRuntimeError();

    }

    // Other helper functions
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
        if (DEBUG) {
            System.out.println("-Binary operator " + binaryOp);
        }
        // Visit LHS
        String lhsReg = null, rhsReg = null;
        if (PASS == 2) {
            // assignReg();
            lhsReg = currentReg;
            // System.out.println(lhs.getText() + " " + rhs.getText()
            // + " " + lhsReg);
        }
        visit(lhs);
        String lhsType = stack.pop();

        // Lock register for LHS
        if (PASS == 2) {
            lockReg();
            if (!lhsReg.equals(lastReg)) {
                // assignReg();
                rhsReg = currentReg;
            } else {
                text.add("PUSH {" + lhsReg + "}");
                rhsReg = stackReg;
            }
            // System.out.println(lhsReg + " " + rhsReg);
        }

        // Visit RHS
        visit(rhs);

        // Release register for LHS
        if (PASS == 2) {
            releaseReg();
        }

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

        if (PASS == 2) {
            if (rhsReg.equals(stackReg)) {
                text.add("POP {" + stackReg + "}");
                binaryOpOutOfRegHelper(binaryOp, lhsReg, rhsReg);
            } else {
                binaryOpHelper(binaryOp, lhsReg, rhsReg);
            }
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

    private void binaryOpHelper(String binaryOp, String lhsReg,
                    String rhsReg) {
        switch (binaryOp) {
        case "&&":
            text.add("AND " + lhsReg + ", " + lhsReg + ", " + rhsReg);
            break;
        case "||":
            text.add("ORR " + lhsReg + ", " + lhsReg + ", " + rhsReg);
            break;
        case "==":
            text.add("CMP " + lhsReg + ", " + rhsReg);
            text.add("MOVEQ " + lhsReg + ", " + "#1");
            text.add("MOVNE " + lhsReg + ", " + "#0");
            break;
        case "!=":
            text.add("CMP " + lhsReg + ", " + rhsReg);
            text.add("MOVNE " + lhsReg + ", " + "#1");
            text.add("MOVEQ " + lhsReg + ", " + "#0");
            break;
        case "<":
            text.add("CMP " + lhsReg + ", " + rhsReg);
            text.add("MOVLT " + lhsReg + ", " + "#1");
            text.add("MOVGE " + lhsReg + ", " + "#0");
            break;
        case "<=":
            text.add("CMP " + lhsReg + ", " + rhsReg);
            text.add("MOVLE " + lhsReg + ", " + "#1");
            text.add("MOVGT " + lhsReg + ", " + "#0");
            break;
        case ">":
            text.add("CMP " + lhsReg + ", " + rhsReg);
            text.add("MOVGT " + lhsReg + ", " + "#1");
            text.add("MOVLE " + lhsReg + ", " + "#0");
            break;
        case ">=":
            text.add("CMP " + lhsReg + ", " + rhsReg);
            text.add("MOVGE " + lhsReg + ", " + "#1");
            text.add("MOVLT " + lhsReg + ", " + "#0");
            break;
        case "/":
            text.add("MOV r0, " + lhsReg);
            text.add("MOV r1, " + rhsReg);
            text.add("BL p_check_divide_by_zero");
            text.add("BL __aeabi_idiv");
            text.add("MOV " + lhsReg + ", r0");
            addDivideByZeroError();
            break;
        case "%":
            text.add("MOV r0, " + lhsReg);
            text.add("MOV r1, " + rhsReg);
            text.add("BL p_check_divide_by_zero");
            text.add("BL __aeabi_idivmod");
            text.add("MOV " + lhsReg + ", r1");
            addDivideByZeroError();
            break;
        case " + ":
            text.add("ADDS " + lhsReg + ", " + lhsReg + ", " + rhsReg);
            text.add("BLVS p_throw_overflow_error");
            addOverflowError();
            break;
        case " - ":
            text.add("SUBS " + lhsReg + ", " + lhsReg + ", " + rhsReg);
            text.add("BLVS p_throw_overflow_error");
            addOverflowError();
            break;
        case "*":
            text.add("SMULL " + lhsReg + ", " + rhsReg + ", "
                            + lhsReg + ", " + rhsReg);
            text.add("CMP " + rhsReg + ", " + lhsReg + ", ASR #31");
            text.add("BLNE p_throw_overflow_error");
            addOverflowError();
            break;
        }
    }

    // TODO: [Expression - Binary Op] Support all cases where out of reg
    private void binaryOpOutOfRegHelper(String binaryOp,
                    String lhsReg, String rhsReg) {
        switch (binaryOp) {
        case "&&":
            text.add("AND " + lhsReg + ", " + lhsReg + ", " + rhsReg);
            break;
        case "||":
            text.add("ORR " + lhsReg + ", " + lhsReg + ", " + rhsReg);
            break;
        case "==":
            text.add("CMP " + lhsReg + ", " + rhsReg);
            text.add("MOVEQ " + lhsReg + ", " + "#1");
            text.add("MOVNE " + lhsReg + ", " + "#0");
            break;
        case "!=":
            text.add("CMP " + lhsReg + ", " + rhsReg);
            text.add("MOVNE " + lhsReg + ", " + "#1");
            text.add("MOVEQ " + lhsReg + ", " + "#0");
            break;
        case "<":
            text.add("CMP " + lhsReg + ", " + rhsReg);
            text.add("MOVLT " + lhsReg + ", " + "#1");
            text.add("MOVGE " + lhsReg + ", " + "#0");
            break;
        case "<=":
            text.add("CMP " + lhsReg + ", " + rhsReg);
            text.add("MOVLE " + lhsReg + ", " + "#1");
            text.add("MOVGT " + lhsReg + ", " + "#0");
            break;
        case ">":
            text.add("CMP " + lhsReg + ", " + rhsReg);
            text.add("MOVGT " + lhsReg + ", " + "#1");
            text.add("MOVLE " + lhsReg + ", " + "#0");
            break;
        case ">=":
            text.add("CMP " + lhsReg + ", " + rhsReg);
            text.add("MOVGE " + lhsReg + ", " + "#1");
            text.add("MOVLT " + lhsReg + ", " + "#0");
            break;
        case "/":
            text.add("MOV r0, " + lhsReg);
            text.add("MOV r1, " + rhsReg);
            text.add("BL p_check_divide_by_zero");
            text.add("BL __aeabi_idiv");
            text.add("MOV " + lhsReg + ", r0");
            addDivideByZeroError();
            break;
        case "%":
            text.add("MOV r0, " + lhsReg);
            text.add("MOV r1, " + rhsReg);
            text.add("BL p_check_divide_by_zero");
            text.add("BL __aeabi_idivmod");
            text.add("MOV " + lhsReg + ", r1");
            addDivideByZeroError();
            break;
        case " + ":
            // Modified to support outOfReg
            text.add("ADDS " + lhsReg + ", " + rhsReg + ", " + lhsReg);
            text.add("BLVS p_throw_overflow_error");
            addOverflowError();
            break;
        case " - ":
            // Modified to support outOfReg
            text.add("SUBS " + lhsReg + ", " + rhsReg + ", " + lhsReg);
            text.add("BLVS p_throw_overflow_error");
            addOverflowError();
            break;
        case "*":
            text.add("SMULL " + lhsReg + ", " + rhsReg + ", "
                            + lhsReg + ", " + rhsReg);
            text.add("CMP " + rhsReg + ", " + lhsReg + ", ASR #31");
            text.add("BLNE p_throw_overflow_error");
            addOverflowError();
            break;
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

    private void checkBinaryOpType(ParserRuleContext ctx, String type) {
        if (!(Arrays.asList(primitiveTypes).contains(type))) {
            Utils.semanticError(ctx, "Incompatible type " + type);
        }
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

    /* Functions to manage registers */
    // TODO: [Z - HELPER] Read this for how registers are managed
    /*
     * Register management plan
     * 
     * initReg() - locks r0 r1 r2 r3 to exclusive usage, begin with r4
     * 
     * assignReg() - gives the lowest number available reg
     * 
     * lockReg() - locks the lowest numbered available reg
     * 
     * releaseReg() - releases the lowest numbered unavailable reg
     */

    // Check the number of registers given
    private boolean[] reg = new boolean[11];
    private String lastReg = "r" + (reg.length - 1);
    private String stackReg = "r11";
    private String currentReg = "r4"; // TODO: [ZZ - HOTFIX] Starting register
                                      // r4

    private void initReg() {
        Arrays.fill(reg, true);

        // Reserve r0 r1 r2 r3
        for (int i = 0; i < 4; i++) {
            reg[i] = false;
        }
    }

    private void assignReg() {
        for (int i = 4; i < reg.length; i++) {
            if (reg[i]) {
                currentReg = "r" + i;
                return;
            }
        }

        if (DEBUG) {
            System.out.println("-REG out of registers");
        }
        // We have run out of registers
        currentReg = lastReg;
    }

    // Use lockReg / releaseReg with caution
    private void lockReg() {
        for (int i = 4; i < reg.length; i++) {
            if (reg[i]) {
                System.out.println("-REG locking r" + i);
                reg[i] = false;
                assignReg();
                return;
            }
        }
        assignReg();
    }

    private void releaseReg() {
        for (int i = reg.length - 1; i >= 4; i--) {
            if (!reg[i]) {
                System.out.println("-REG releasing r" + i);
                reg[i] = true;
                assignReg();
                return;
            }
        }
        assignReg();
    }

    /* Functions for dealing with memory */
    private void storeToMemory(String varName, String varType) {
        if (DEBUG) {
            System.out.println("-Store to memory " + varName + " "
                            + varType);
        }
        int offset = 0;
        if (currentST.lookUpAllLabel(varName + ".p") != null) {
            offset = currentST.lookUpAllLabel(varName + ".p");
        } else {
            offset = currentST.lookUpAllLabel(varName);
        }
        if (varType.equals("BOOL") || varType.equals("CHAR")) {
            if (offset != 0) {
                text.add("STRB " + currentReg + ", [sp, #" + offset
                                + "]");
            } else {
                text.add("STRB " + currentReg + ", [sp]");
            }
        } else {
            if (offset != 0) {
                text.add("STR " + currentReg + ", [sp, #" + offset
                                + "]");
            } else {
                text.add("STR " + currentReg + ", [sp]");
            }
        }
    }

    private void storeArrayLitToMemory(String varName,
                    String varType, String arrayReg, int offset) {
        if (varType.equals("BOOL") || varType.equals("CHAR")) {
            if (offset != 0) {
                text.add("STRB " + currentReg + ", [" + arrayReg
                                + ", #" + offset + "]");
            } else {
                text.add("STRB " + currentReg + ", [" + arrayReg
                                + "]");
            }
        } else {
            if (offset != 0) {
                text.add("STR " + currentReg + ", [" + arrayReg
                                + ", #" + offset + "]");
            } else {
                text.add("STR " + currentReg + ", [" + arrayReg + "]");
            }
        }
    }

    private void loadFromMemory(String varName, String varType) {
        if (DEBUG) {
            System.out.println("-Load from memory " + varName + " "
                            + varType);
            currentST.printST();
        }
        int offset = 0;
        if (currentST.lookUpAllLabel(varName + ".p") != null) {
            offset = currentST.lookUpAllLabel(varName + ".p");
        } else {
            offset = currentST.lookUpAllLabel(varName);
        }
        offset += argListAdjustment;
        System.out.println(offset + " " + spPosition);
        // assignReg();
        if (offset == 0) {
            if (varType.equals("CHAR") || varType.equals("BOOL")) {
                text.add("LDRSB " + currentReg + ", [sp]");
            } else {
                text.add("LDR " + currentReg + ", [sp]");
            }
        } else {
            if (varType.equals("CHAR") || varType.equals("BOOL")) {
                text.add("LDRSB " + currentReg + ", [sp, #" + offset
                                + "]");
            } else {
                text.add("LDR " + currentReg + ", [sp, #" + offset
                                + "]");
            }
        }
    }

    private void loadFromArrayElem(String arrayReg, String elemReg,
                    String arrayElemType) {
        if (DEBUG) {
            System.out.println("-Loading from array elem " + arrayReg
                            + " " + elemReg + " " + arrayElemType);
        }
        text.add("LDR " + arrayReg + ", [" + arrayReg + "]");
        text.add("MOV r0, " + elemReg);
        text.add("MOV r1, " + arrayReg);
        text.add("BL p_check_array_bounds");
        text.add("ADD " + arrayReg + ", " + arrayReg + ", #4");
        if (arrayElemType.equals("CHAR")
                        || arrayElemType.equals("BOOL")) {
            text.add("ADD " + arrayReg + ", " + arrayReg + ", "
                            + elemReg);
        } else {
            text.add("ADD " + arrayReg + ", " + arrayReg + ", "
                            + elemReg + ", LSL #2");
        }
        addCheckArrayBounds();
    }

    private void storeToArrayElem(String arrayName,
                    String arrayElemType, String elemLoc) {
        if (DEBUG) {
            System.out.println("-Storing to array element "
                            + arrayName + " " + arrayElemType + " "
                            + elemLoc);
        }
        String reg1 = currentReg;
        lockReg();
        String reg2 = currentReg;
        lockReg();
        String reg3 = currentReg;

        int arrayLoc = currentST.lookUpAllLabel(arrayName);
        boolean elemLocIsNumber = true;
        int elemLocNum = 0;

        text.add("ADD " + reg2 + ", sp, #" + arrayLoc);

        // The case where the element location is a variable
        try {
            elemLocNum = Integer.parseInt(elemLoc);
        } catch (Exception e) {
            elemLocIsNumber = false;
        }

        if (elemLocIsNumber) {
            text.add("LDR " + reg3 + ", =" + elemLocNum);
        } else {
            text.add("LDR " + reg3 + ", [sp]");
        }
        loadFromArrayElem(reg2, reg3, arrayElemType);
        if (arrayElemType.equals("CHAR")
                        || arrayElemType.equals("BOOL")) {
            text.add("STRB " + reg1 + ", [" + reg2 + "]");
        } else {
            text.add("STR " + reg1 + ", [" + reg2 + "]");
        }

        // Clear the registers before releasing them
        text.add("LDR " + reg2 + ", =0");
        text.add("LDR " + reg3 + ", =0");

        releaseReg();
        releaseReg();
    }

    /*
     * Deallocates memory for the given scope. Note that can only deallocate at
     * most 1024 in one operation.
     */
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

    /*
     * Allocates memory for the given scope. Note that can only allocate at most
     * 1024 in one operation.
     */
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

    private int spaceForType(String varType) {
        if (varType.equals("BOOL") || varType.equals("CHAR")) {
            return 1;
        }
        return 4;
    }

    private void adjustLabels() {
        HashMap<String, Integer> stHashMap = currentST.getStLabel()
                        .getSt();
        for (String key : stHashMap.keySet()) {
            if (!key.contains(".p")) {
                // Position for variables
                stHashMap.put(key, spPosition - stHashMap.get(key));
            } else {
                // Position for param
                stHashMap.put(key, spPosition + stHashMap.get(key));
            }
        }
        currentST.setScopeSize(spPosition);
        spPosition = 0;
    }

    /* Write functions to traverse tree below here */

    /* Visit the main program */

    public Void visitProgram(WACCParser.ProgramContext ctx) {
        System.out.println("Current pass: " + PASS);
        if (PASS == 1) {
            currentST = new SymbolTableWrapper<String>();
            currentST.setScopeName("main");
            initReg();
        }

        if (DEBUG) {
            System.out.println("-Program");
        }

        // Resets
        nonFunctionBlockCount = 0;

        // Traverse all functions first
        List<FuncContext> funcList = ctx.func();
        for (FuncContext f : funcList) {
            if (PASS == 1) {
                String functionName = f.IDENT().toString();

                visit(f.type());

                String functionReturnType = stack.pop();
                FUNCTION newFunc = new FUNCTION(functionReturnType);

                currentST.addFunction(functionName, newFunc);

                stack.push(functionName);

                if (f.param_list() != null) {
                    visit(f.param_list());
                }
            }
        }

        for (FuncContext f : funcList) {
            visit(f);
        }

        int scopeSize = currentST.getScopeSize();

        // Go into the main program
        if (PASS == 2) {
            text.add("main:");
            text.add("PUSH {lr}");

            // Allocate memory
            allocateScopeMemory(scopeSize);
        }

        visit(ctx.stat());

        if (PASS == 1) {
            adjustLabels();
        }

        /*
         * TODO: [Program] Need to figure out where this LDR r0 =0 goes, will
         * have clash on if statements
         */
        if (PASS == 2) {
            // Deallocate memory
            deallocateScopeMemory(scopeSize);

            text.add("LDR r0, =0");
        }
        visit(ctx.END());

        printStack();
        currentST.printST();
        return null;
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
            spPosition += spaceForType(varType);
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
        String varType = stack.pop();
        String varName = stack.pop();
        if (PASS == 2) {
            // assignReg(); // Set the register after visiting expression
            text.add("ADD " + currentReg + ", sp, #"
                            + currentST.lookUpAllLabel(varName));
            text.add("MOV r0, " + currentReg);
            readHelper(varType);
        }
        return null;
    }

    // Creates a link to read functions
    private void readHelper(String varType) {
        switch (varType) {
        case "INT":
            addReadINT();
            text.add("BL p_read_int");
            break;
        case "CHAR":
            addReadCHAR();
            text.add("BL p_read_char");
            break;
        }
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
            System.out.println("-Return statement "
                            + currentFunctionName);
        }
        visit(ctx.expr());

        // Clearing the stack
        String exprType = stack.pop();
        String exprName = stack.pop();

        if (PASS == 2) {
            text.add("MOV r0, " + currentReg + "");

            int scopeSize = currentST.getScopeSize();
            
            // [Z - HOTFIX] Fix to properly deallocate memory
            SymbolTableWrapper<String> tempST = currentST;
            while (!tempST.getScopeName().equals(currentFunctionName)) {
                tempST = tempST.getEncSymTable();
                scopeSize += tempST.getScopeSize();
            }
            deallocateScopeMemory(scopeSize);
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
            // assignReg(); // Set the register after visiting expression
            text.add("MOV r0, " + currentReg + "");
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

        if (PASS == 2) {
            // assignReg(); // Set the register after visiting expression
            text.add("MOV r0, " + currentReg + "");
            printHelper(varType);
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

        if (PASS == 2) {
            // TODO: [Print] - figure out what this is for
            // assignReg(); // Set the register after visiting expression
            text.add("MOV r0, " + currentReg + "");
            printHelper(varType);
            text.add("BL p_print_ln");
            addPrintLN();
        }

        return null;
    }

    // Creates link to print functions in main code
    private void printHelper(String type) {
        // Printing for array
        if (Utils.isAnArray(type)) {
            addPrintReference();
            text.add("BL p_print_reference");
            return;
        }

        // Printing for primitive
        switch (type) {
        case "INT":
            addPrintINT();
            text.add("BL p_print_int");
            break;
        case "CHAR[]":
            addPrintSTRING();
            text.add("BL p_print_string");
            break;
        case "BOOL":
            addPrintBOOL();
            text.add("BL p_print_bool");
            break;
        case "CHAR":
            text.add("BL putchar");
        }
    }

    public Void visitIfstatement(WACCParser.IfstatementContext ctx) {
        if (DEBUG) {
            System.out.println("-If statement");
        }

        visit(ctx.expr());

        // Clear the stack
        stack.pop();
        stack.pop();

        String elseBlock = "L" + nonFunctionBlockCount++;
        String postFiBlock = "L" + nonFunctionBlockCount++;

        if (PASS == 2) {
            // Check condition; branch if false
            text.add("CMP " + currentReg + ", #0");
            text.add("BEQ " + elseBlock);
        }

        int scopeSize = 0;

        for (int i = 0; i < 2; i++) {
            // 0: then block; 1: else block
            if (PASS == 1) {
                if (i == 0) {
                    newScope(elseBlock);
                } else {
                    newScope(postFiBlock);
                }
            }

            if (PASS == 2) {
                if (i == 0) {
                    currentST = storeScopes.get(elseBlock);
                } else {
                    currentST = storeScopes.get(postFiBlock);
                }
                scopeSize = currentST.getScopeSize();
                allocateScopeMemory(scopeSize);
            }

            visit(ctx.stat(i));

            /*
             * if (PASS == 2) { text.add("f_" + functionName + ":");
             * text.add("PUSH {lr}"); currentST = storeScopes.get(functionName);
             * allocateScopeMemory(currentST.getScopeSize()); }
             * 
             * if (PASS == 1) { // Update the positions of variables in the
             * memory adjustLabels(); storeScopes.put(functionName, currentST);
             * }
             */
            if (PASS == 1) {
                adjustLabels();
                if (i == 0) {
                    storeScopes.put(elseBlock, currentST);
                } else {
                    storeScopes.put(postFiBlock, currentST);
                }
            }

            freeScope();

            if (PASS == 2) {
                deallocateScopeMemory(scopeSize);
                if (i == 0) {
                    text.add("B " + postFiBlock);
                    text.add(elseBlock + ":");
                } else {
                    text.add(postFiBlock + ":");
                }
            }
        }
        return null;
    }

    public Void visitWhilestatement(
                    WACCParser.WhilestatementContext ctx) {
        if (DEBUG) {
            System.out.println("-While statement");
        }

        // TODO: [While] - Fixed nested while loops

        String checkCondAndAfterWhile = "L" + nonFunctionBlockCount++;
        String loopBodyLabel = "L" + nonFunctionBlockCount++;

        if (PASS == 1) {
            newScope(loopBodyLabel);
        }

        int scopeSize = 0;

        if (PASS == 2) {
            // Branch to checking condition and code following while loop
            text.add("B " + checkCondAndAfterWhile);

            // Loop code
            text.add(loopBodyLabel + ":");
            currentST = storeScopes.get(loopBodyLabel);
            scopeSize = currentST.getScopeSize();
            allocateScopeMemory(scopeSize);
        }

        visit(ctx.stat());

        if (PASS == 1) {
            adjustLabels();
            storeScopes.put(loopBodyLabel, currentST);
        }

        freeScope();

        if (PASS == 2) {
            deallocateScopeMemory(scopeSize);
            // checking condition and code following loop
            text.add(checkCondAndAfterWhile + ":");
        }

        visit(ctx.expr());

        // Clearing stack
        stack.pop();
        stack.pop();

        if (PASS == 2) {
            text.add("CMP " + currentReg + ", #1");
            text.add("BEQ " + loopBodyLabel);
        }

        return null;
    }

    public Void visitBeginendstatement(
                    WACCParser.BeginendstatementContext ctx) {
        if (DEBUG) {
            System.out.println("-Begin end statement ");
        }
        //every time begin is visited, new scope is created
        int scopeSize = 0;
//        newScope("beginEndStat" + scopeSize);
//        allocateScopeMemory();
//        visit(ctx.stat());
//        freeScope();
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
        String elemLoc = null;
        String arrayName = null;

        if (Utils.isArrayElem(varName)) {
            arrayName = stack.pop();
            elemLoc = stack.pop();
        }

        // TODO: [Z - Array] This code result in duplicate loads to currentReg
        // when doing
        // operations like a[i] = i
        visit(ctx.expr());
        String exprType = stack.pop();
        String exprName = stack.pop();

        if (PASS == 2) {

            // To handle arrays
            if (Utils.isArrayElem(varName)) {
                storeToArrayElem(arrayName, varType, elemLoc);
            } else if (exprName.equals(varName)) {
                // TODO: [Z - HOTFIX] for param reassignment

                int offset = currentST.lookUpAllLabel(varName);

                // Overwrite the param's offset
                currentST.addLabel(varName + ".p", offset);
                storeToMemory(varName, varType);
            } else {
                storeToMemory(varName, varType);
            }
        }
        return null;
    }

    public Void visitAssignrhsarraylit(
                    WACCParser.AssignrhsarraylitContext ctx) {
        if (DEBUG) {
            System.out.println("-Assign RHS arraylit");
        }
        visit(ctx.arrayLiter());
        return null;
    }

    public Void visitArrayLiter(WACCParser.ArrayLiterContext ctx) {
        if (DEBUG) {
            System.out.println("-Assign ArrayLiter");
        }
        List<ExprContext> exprs = ctx.expr();

        String arrayType = stack.pop();
        String arrayName = stack.pop();

        // Not sure if this is needed
        String modArrayType = Utils.stripArrayTypeBracket(arrayType);
        int typeSpace = spaceForType(modArrayType);

        int numElems = exprs.size();
        int memoryReq = numElems * typeSpace + 4;
        String arrayReg = currentReg;

        // Allocate memory
        if (PASS == 2) {
            text.add("LDR r0, =" + memoryReq);
            text.add("BL malloc");

            // Assign register for the location of the array
            text.add("MOV " + arrayReg + ", r0");
        }

        // Lock the register
        lockReg();

        String elemReg = currentReg;

        int memoryPos = 0;
        for (ExprContext ectx : exprs) {
            visit(ectx);
            String exprType = stack.pop();
            String exprName = stack.pop();
            memoryPos += typeSpace;

            if (PASS == 2) {
                storeArrayLitToMemory(exprName, exprType, arrayReg,
                                memoryPos);
            }
        }

        // Release the register
        releaseReg();

        if (PASS == 2) {
            // Store the array
            text.add("LDR " + elemReg + ", =" + numElems);
            text.add("STR " + elemReg + ", [" + arrayReg + "]");
            storeToMemory(arrayName, arrayType);
        }

        // TODO: [Array] Not added to prevent stack pollution
        // stack.push(arrayName);
        // stack.push(arrayType);
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
    
    // TODO: [Z - HOTFIX] To make the argument list work correctly
    int argListAdjustment = 0;

    public Void visitArg_list(WACCParser.Arg_listContext ctx) {
        if (DEBUG) {
            System.out.println("-ArgList call ");
        }

        // List of args
        List<ExprContext> args = ctx.expr();
        int argsMem = 0;
        argListAdjustment = 0;
        for (int i = args.size() - 1; i >= 0; i--) {
            visit(args.get(i));
            String exprType = stack.pop();
            String exprName = stack.pop();

            if (PASS == 2) {
                if (exprType.equals("CHAR")
                                || exprType.equals("BOOL")) {
                    text.add("STRB " + currentReg + ", [sp, #-1]!");
                    argListAdjustment += 1;
                } else {
                    text.add("STR " + currentReg + ", [sp, #-4]!");
                    argListAdjustment += 4;
                }
            }
            argsMem += spaceForType(exprType);
        }
        argListAdjustment = 0;
        stack.push(Integer.toString(argsMem));
        return null;
    }

    public Void visitAssignrhscall(WACCParser.AssignrhscallContext ctx) {
        String funcName = ctx.IDENT().toString();

        if (DEBUG) {
            System.out.println("-Assign RHS call " + funcName);
        }
        // TODO: [Z] Assign RHS call Traversal removed at this point
        String varType = stack.pop();
        String varName = stack.pop();

        IDENTIFIER obj = currentST.lookUpAllFunction(funcName);

        if (PASS == 2) {
        }

        stack.push(funcName);
        String argsMem = "0";
        if (ctx.arg_list() != null) {
            visit(ctx.arg_list());
            argsMem = stack.pop(); // Retrieve arg memory
        }
        stack.pop(); // Remove function name

        if (PASS == 2) {
            text.add("BL f_" + funcName);
            if (!argsMem.equals("0")) {
                text.add("ADD sp, sp, #" + argsMem);
            }
            text.add("MOV " + currentReg + ", r0");
            // TODO: This is where the error probably is
            storeToMemory(varName, varType);
            // if (varType.equals("BOOL") || varType.equals("CHAR")) {
            // text.add("STRB " + currentReg + ", [sp]");
            // } else {
            // text.add("STR " + currentReg + ", [sp]");
            // }
        }

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

        String varName = ctx.arrayElem().getText();
        String arrayName = ctx.arrayElem().IDENT().toString();
        //
        // // Check if array element variable name is declared
        // int numberOfExprs = ctx.arrayElem().expr().size();
        //
        // for (int i = 0; i < numberOfExprs; i++) {
        // checkArrayElementVariableName(ctx.arrayElem().expr(i)
        // .getText(), ctx.arrayElem().expr(i));
        // }
        //
        int numberOfExprs = ctx.arrayElem().expr().size();
        String elemLoc = null;
        for (int i = 0; i < numberOfExprs; i++) {
            stack.push(varName);
            visit(ctx.arrayElem().expr(i));
            stack.pop();
            elemLoc = stack.pop();
        }
        stack.push(elemLoc);
        stack.push(arrayName);
        stack.push(varName);
        stack.push(arrayName);
        String typeNeeded = checkDefinedVariable(ctx);
        typeNeeded = Utils.stripArrayTypeBracket(typeNeeded);

        stack.pop(); // arrayName is not needed in stack
        stack.push(typeNeeded);
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
        boolean fromArray = false;
        if (!stack.empty()) {
            // Check if we are coming from an array
            String varName = stack.peek();
            if (Utils.isArrayElem(varName)) {
                fromArray = true;
                stack.pop(); // Remove array name
            }
        }
        int value = Integer.parseInt(ctx.getText());

        stack.push(Integer.toString(value));
        stack.push("INT");

        if (PASS == 2) {
            // Only add instruction if we are not coming from array
            if (!fromArray) {
                text.add("LDR " + currentReg + ", =" + value);
            }
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
                text.add("MOV " + currentReg + ", #1");
            } else {
                text.add("MOV " + currentReg + ", #0");
            }
        }
        return null;
    }

    public Void visitCharliteral(WACCParser.CharliteralContext ctx) {
        if (DEBUG) {
            System.out.println("-Char literal");
        }

        String message = ctx.getText();

        // Null character
        if (message.equals("'\\0'")) {
            message = "0";
        }
        message = message.replace("\\", ""); // Handle escaped characters

        stack.push(message);
        stack.push("CHAR");

        if (PASS == 2) {
            text.add("MOV " + currentReg + ", #" + message);
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

        if (PASS == 2) {
            text.add("LDR " + currentReg + ", =msg_" + messageCount);
            addMessageToData(message);
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

        String varName = ctx.IDENT().toString();
        stack.push(varName);

        String varType = checkDefinedVariable(ctx);
        stack.push(varType);

        if (PASS == 2) {
            loadFromMemory(varName, varType);
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

        String arrayName = ctx.IDENT().toString();
        int arrayLocation = currentST.lookUpAllLabel(arrayName);

        int numberOfExprs = ctx.expr().size();
        int elemLocation = 0;

        String arrayReg = currentReg;

        // Lock register
        lockReg();

        String elemReg = currentReg;

        IDENTIFIER object = currentST.lookUpAllIdentifier(arrayName);
        if (object == null) {
            object = currentST.lookUpAllParam(arrayName);
        }

        String arrayElemType = object.getType();
        int brackets = ctx.LBRACK().size();

        arrayElemType = arrayElemType.substring(0,
                        arrayElemType.length() - brackets * 2);

        if (PASS == 2) {
            text.add("ADD " + arrayReg + ", sp, #" + arrayLocation);
        }

        for (int i = 0; i < numberOfExprs; i++) {
            visit(ctx.expr(i));
            stack.pop(); // Remove expr type
            stack.pop(); // Remove expr name

            if (PASS == 2) {
                loadFromArrayElem(arrayReg, elemReg, arrayElemType);
            }
        }
        if (PASS == 2) {
            if (arrayElemType.equals("CHAR")
                            || arrayElemType.equals("BOOL")) {
                text.add("LDRSB " + arrayReg + ", [" + arrayReg + "]");
            } else {
                text.add("LDR " + arrayReg + ", [" + arrayReg + "]");
            }
        }

        // Release register
        releaseReg();

        stack.push(arrayName);
        stack.push(arrayElemType);

        return null;
    }

    public Void visitUnaryoperator(WACCParser.UnaryoperatorContext ctx) {
        if (DEBUG) {
            System.out.println("-Unary operator");
        }
        visit(ctx.expr());

        String varType = stack.pop();
        String varName = stack.peek();

        String unaryOp = ctx.UNARYOP().toString();

        // TODO: [ZZ - CLEANING] May be possible to remove this function
        switch (unaryOp) {
        case "!":
            stack.push(BOOL);
            checkType(ctx, varName, varType);
            break;
        case "-":
            stack.push(INT);
            checkType(ctx, varName, varType);
            break;
        case "len":
            if (!Utils.isAnArray(varType)
                            && !(varType.equals(STRING))) {
                // if not array, raise the exception
                stack.push("T[]");
                checkType(ctx, varName, varType);
            }
            varType = INT;
            break;
        case "ord":
            stack.push(CHAR);
            checkType(ctx, varName, varType);
            varType = INT;
            break;
        case "chr":
            stack.push(INT);
            checkType(ctx, varName, varType);
            varType = CHAR;
            break;
        }

        if (PASS == 2) {
            // assignReg();
            String reg = currentReg;
            unaryOpHelper(unaryOp, reg);
        }

        stack.push(varType);
        return null;
    }

    private void unaryOpHelper(String unaryOp, String reg) {
        switch (unaryOp) {
        case "!":
            text.add("EOR " + reg + ", " + reg + ", #1");
            break;
        case "-":
            text.add("RSBS " + reg + ", " + reg + ", #0");
            text.add("BLVS p_throw_overflow_error");
            addOverflowError();
            break;
        case "len":
            // TODO: [Unary op] Implement length
            text.add("LDR " + reg + ", [" + reg + "]");
            break;
        case "ord":
            break;
        case "chr":
            break;
        }
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
        String functionName = stack.pop();
        FUNCTION curFunc = currentST.lookUpAllFunction(functionName);

        if (curFunc == null) {
            // semantically never reach
            Utils.semanticError(ctx, "Something went wrong");
        }

        curFunc.setParamSize(ctx.param().size());

        for (int i = 0; i < ctx.param().size(); i++) {
            stack.push(functionName);
            visit(ctx.param(i));
        }

        return null;
    }

    public Void visitParam(WACCParser.ParamContext ctx) {
        if (DEBUG) {
            System.out.println("-Param");
        }
        String functionName = stack.pop();
        FUNCTION curFunc = currentST.lookUpAllFunction(functionName);

        if (curFunc == null) {
            // semantically never reach
            Utils.semanticError(ctx, "Something went wrong");
        }

        visit(ctx.type());
        String paramReturnType = stack.pop();
        String paramName = ctx.IDENT().toString();

        if (PASS == 1) {
            PARAM newParam = new PARAM(paramReturnType, paramName);
            curFunc.addParam(newParam);
        }
        return null;
    }

    public Void visitFunc(WACCParser.FuncContext ctx) {
        String functionName = ctx.IDENT().toString();
        if (DEBUG) {
            System.out.println("-Function: " + functionName);
        }

        FUNCTION func = currentST.lookUpAllFunction(functionName);
        newScope(functionName);

        // Add params in new scope
        if (PASS == 1) {
            int paramLoc = 4;
            for (int i = 0; i < func.getParamSize(); i++) {
                PARAM param = func.getParam(i);

                String paramType = param.getType();

                if (paramType.startsWith("Pair")
                                && !paramType.endsWith("[]")) {
                    PAIR p = new PAIR(paramType);
                    currentST.addParam(param.getName(), p);
                } else if (Utils.isAnArray(paramType)) {
                    ARRAY a = new ARRAY(paramType);
                    currentST.addParam(param.getName(), a);
                } else {
                    currentST.addParam(param.getName(), param);
                }
                currentST.addLabel(param.getName() + ".p", paramLoc);
                paramLoc += spaceForType(paramType);
            }
        }

        if (PASS == 2) {
            text.add("f_" + functionName + ":");
            text.add("PUSH {lr}");
            currentST = storeScopes.get(functionName);
            allocateScopeMemory(currentST.getScopeSize());
        }

        currentFunctionName = functionName;
        visit(ctx.stat());

        if (PASS == 1) {
            // Update the positions of variables in the memory
            adjustLabels();
            storeScopes.put(functionName, currentST);
        }

        freeScope();
        currentFunctionName = "";
        visit(ctx.END());
        return null;
    }
}
