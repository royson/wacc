package frontend;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
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

public class SemanticVisitor extends WACCParserBaseVisitor<Void> {

    private SymbolTableWrapper<String> currentST;
    private Stack<String> stack = new Stack<String>();

    private final static String INT = "INT";
    private final static String BOOL = "BOOL";
    private final static String CHAR = "CHAR";
    private final static String STRING = "CHAR[]";

    private String[] primitiveTypes = { INT, BOOL, CHAR, STRING };

    private boolean DEBUG = true;

    /* Helper functions */
    
    private boolean isAnArray(String s){
      //STRING is not an array
      if(s.equals(STRING)) return false;
      else return (s.endsWith("[]"));
    }
    
    private String stripArrayTypeBracket(String arrayType){
      //This function returns the types stored in an array
      //"INT[]" --> "INT"; "INT[][]" --> "INT[]"
      return arrayType.substring(0, arrayType.length()-2);
    }
    
    //This function checks x in a[x] where arrayElemName is x;
    private void checkArrayElementVariableName(
    	String arrayElemName, ParserRuleContext ctx){
      
      String typeOfArrayElemName = getPrimitiveType(arrayElemName);
      if(typeOfArrayElemName == null){
    	  //ArrayElemName is an object
    	  stack.push(arrayElemName);
    	  
    	  String arrayElemType 
    	  		= checkDefinedVariable(ctx);
    	  stack.push(INT);
    	  checkType(ctx,arrayElemName,arrayElemType);
    	  
    	  //arrayElem is not needed in stack
    	  stack.pop();
      }else if(!typeOfArrayElemName.equals(INT)){
    	  stack.push(INT);
    	  checkType(ctx,arrayElemName,typeOfArrayElemName);
      }else{
    	//Pass validation
      }
    }
    
    private String getPrimitiveType(String s){
      String int_regex = "-?\\d+";
      String char_regex = "\'.\'";
      String bool_regex = "true|false";
      String string_regex = "\".*\"";
      if(s.matches(int_regex)) return INT;
      else if(s.matches(char_regex)) return CHAR;
      else if(s.matches(bool_regex)) return BOOL;
      else if(s.matches(string_regex)) return STRING;
      else return null;
    }

    private void printStack() {
        System.out.println("-----PRINTING STACK-----");
        System.out.println(Arrays.toString(stack.toArray()));
        System.out.println("------------------------");
    }

    private String renameStringToCharArray(String s) {
        return s.equals("STRING") ? STRING : s;
    }

    private void visitBinaryoperator(ParserRuleContext ctx,
                    String binaryOp, ParserRuleContext lhs,
                    ParserRuleContext rhs) {
        if (DEBUG) {
            System.out.print("-Binary operator ");
            contextDepth(ctx);
        }
        // Visit LHS
        visit(lhs);

        String lhsType = stack.pop();

        // Visit RHS
        visit(rhs);

        String rhsType = stack.pop();
        String rhsExpr = stack.pop();

        // check arguments for binary operation
        String lhsExpr = stack.peek();
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
            semanticError(ctx, "Incompatible type " + type);
        }
    }

    private void newScope() {
        SymbolTableWrapper<String> st = new SymbolTableWrapper<String>(
                        currentST);
        currentST = st;
    }

    private void freeScope() {
        currentST = currentST.getEncSymTable();
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
        // System.out.println(ctx.depth() + " " + ctx.getChildCount()
        // + " " + ctx);
        System.out.println("");
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

    private void checkParameters(ParserRuleContext ctx,
                    String funcName, int paramSize, int funcSize) {
        if (paramSize != funcSize) {
            String errorMessage = "Incorrect number of parameters for "
                            + funcName
                            + " (expected: "
                            + funcSize
                            + ", actual: " + paramSize + ")";
            semanticError(ctx, errorMessage);
        }
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
            String errorMessage = "Incompatible type at "
                            + value.replaceAll("\\s", "");
            errorMessage += " (expected: " + compareType;
            errorMessage += ", actual: " + type + ")";
            semanticError(ctx, errorMessage);
        }
    }

    private String checkDefinedVariable(ParserRuleContext ctx) {
        String curIdentToCheck = stack.peek();

        IDENTIFIER object = currentST
                        .lookUpAllIdentifier(curIdentToCheck);

        // Variable is not declared

        // TODO: Make this not look so manual
        if (object == null) {
            PARAM object1 = currentST.lookUpParam(curIdentToCheck);
            if (object1 == null) {
                String errorMessage = "Variable " + curIdentToCheck
                                + " is not defined in this scope";
                semanticError(ctx, errorMessage);
            }
            return object1.getType();
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
        currentST = new SymbolTableWrapper<String>();

        List<FuncContext> funcList = ctx.func();
        for (FuncContext f : funcList) {
            String functionName = f.IDENT().toString();

            FUNCTION object = currentST
                            .lookUpAllFunction(functionName);
            if (object != null) {
                semanticError(ctx,
                                "\""
                                                + functionName
                                                + "\" is already defined in this scope");
            }

            visit(f.type());

            String functionReturnType = stack.pop();
            FUNCTION newFunc = new FUNCTION(functionReturnType);

            currentST.addFunction(functionName, newFunc);

            stack.push(functionName);

            if (f.param_list() != null) {
                visit(f.param_list());
            }

            // clear functionName from the stack
            stack.pop();

        }

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
        IDENTIFIER object = currentST.lookUpIdentifier(varName);
        if (object != null) {
            semanticError(ctx, "\"" + varName
                            + "\" is already defined in this scope");
        }
        stack.push(varName);
        visit(ctx.type());

        String varType = stack.peek();

        //If variable is not a Pair or Array, create new var object.
        if (!varType.startsWith("Pair") && !(isAnArray(varType))) {
          currentST.addIdentifier(ctx.IDENT().toString(),
              new VARIABLE(varType));
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
        if (!readType.equals(INT) && !readType.equals(CHAR)) {
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

        if (currentST.getEncSymTable() == null) {
            // returning from main program
            semanticError(ctx, "Cannot return from the global scope.");
        } else {
            String functionName = stack.pop();
            FUNCTION curFunc = currentST
                            .lookUpAllFunction(functionName);

            visit(ctx.expr());

            String type = stack.pop();
            String varname = stack.pop();

            if (!curFunc.getType().equals(type)) {
                stack.push(curFunc.getType());
                checkType(ctx, varname, type);
            }
        }

        return null;
    }

    public Void visitExitstatement(WACCParser.ExitstatementContext ctx) {
        if (DEBUG) {
            System.out.println("-Exit statement");
            contextDepth(ctx);
        }

        visit(ctx.expr());

        String exprType = stack.pop();
        String exprCode = stack.pop();
        // exit code must be 0-256
        stack.push(INT);
        checkType(ctx.expr(), exprCode, exprType);

        return null;
    }

    public Void visitPrintstatement(
                    WACCParser.PrintstatementContext ctx) {
        if (DEBUG) {
            System.out.println("-Print statement");
            contextDepth(ctx);
        }

        visit(ctx.expr());
        //Clear the stack
        stack.pop();
        stack.pop();
        return null;
    }

    public Void visitPrintlnstatement(
                    WACCParser.PrintlnstatementContext ctx) {
        if (DEBUG) {
            System.out.println("-Print line statement");
            contextDepth(ctx);
        }

        visit(ctx.expr());
        //Clear the stack
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

        // Check if the condition is a BOOL
        stack.push(BOOL);
        String condition = ctx.expr().getText();
        checkType(ctx.expr(), condition, ifExprType);

        // giving a new scope for each stat
        for (int x = 0; x < 2; x++) {
            newScope();
            visit(ctx.stat(x));
            freeScope();
        }
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

        // Check if the condition is a BOOL
        stack.push(BOOL);
        String condition = ctx.expr().getText();
        checkType(ctx.expr(), condition, whileExprType);

        newScope();
        visit(ctx.stat());
        freeScope();

        return null;
    }

    public Void visitBeginendstatement(
                    WACCParser.BeginendstatementContext ctx) {
        if (DEBUG) {
            System.out.println("-Begin end statement");
            contextDepth(ctx);
        }

        newScope();
        visit(ctx.stat());
        freeScope();

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

        // clear unused name from stack
        stack.pop();

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

    public Void visitArrayLiter(WACCParser.ArrayLiterContext ctx) {
        if (DEBUG) {
            System.out.print("-Assign ArrayLiter ");
            contextDepth(ctx);
        }

        // TODO: Create an array [DONE]
        List<ExprContext> exprs = ctx.expr();
        
        String arrayType = stack.pop();
        String arrayName = stack.pop();
        
        IDENTIFIER obj = currentST.lookupAll(arrayName);
        ARRAY a;
        if(obj != null){
            //Redeclare Array
           if(!(obj instanceof ARRAY)){
        	 System.err.println("Something went wrong.");
           }
            a = (ARRAY) obj;
            a.clear();
        }else{
          a = new ARRAY(arrayType);
          currentST.add(arrayName, a);
        }

        String allowedElemType = stripArrayTypeBracket(arrayType);
        
        for (ExprContext ectx : exprs) {
            visit(ectx);
            String arrayElemType = stack.pop();
            String arrayElemName = stack.pop();
            
            stack.push(allowedElemType);
            checkType(ectx,arrayElemName,arrayElemType);
            a.addElem(arrayElemType);
        }

        return null;
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
        String sndVarName = stack.pop();
        String fstType = stack.pop();
        String fstVarName = stack.pop();
        PAIR comparePair = new PAIR(fstType, sndType);

        checkType(ctx, "Pair(" + fstVarName + "," + sndVarName + ")",
                        comparePair.toString());
        return null;
    }

    public Void visitAssignrhspairelem(
                    WACCParser.AssignrhspairelemContext ctx) {
        if (DEBUG) {
            System.out.print("-Assign RHS pairelem ");
            contextDepth(ctx);
        }
        visit(ctx.pairElem());
        return null;
    }

    public Void visitArg_list(WACCParser.Arg_listContext ctx) {
        if (DEBUG) {
            System.out.print("-ArgList call ");
            contextDepth(ctx);
        }

        String funcName = stack.pop();

        FUNCTION func = currentST.lookUpAllFunction(funcName);
        if (func == null) {
            semanticError(ctx, "Something went wrong.");
        }

        // List of args
        List<ExprContext> args = ctx.expr();

        checkParameters(ctx.getParent(), funcName, ctx.expr().size(),
                        func.getParamSize());

        if (!(ctx.expr().isEmpty())) {
            int i = 0;
            for (ExprContext ectx : args) {
                visit(ectx);
                String argType = stack.pop();
                String argName = stack.pop();

                PARAM p = func.getParam(i);
                stack.push(p.getType());
                checkType(ectx, argName, argType);

                i++;
            }
        }

        return null;
    }

    public Void visitAssignrhscall(WACCParser.AssignrhscallContext ctx) {
        if (DEBUG) {
            System.out.print("-Assign RHS call ");
            contextDepth(ctx);
        }

        String func = ctx.IDENT().toString();

        IDENTIFIER obj = currentST.lookUpAllFunction(func);

        if (obj == null) {
            stack.push(func);
            checkDefinedVariable(ctx);
        } else if (!(obj instanceof FUNCTION)) {
            semanticError(ctx, "\"" + func + "\" is not a function");
        } else {
            checkType(ctx, ctx.getText(), obj.getType());
            // Remove unwanted variable name
            stack.pop();

            // check the arguments
            stack.push(func);
            if (ctx.arg_list() != null) {
                visit(ctx.arg_list());
            }
        }
        return null;
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
        
     // TODO: Convert to array implementation [DONE]
        String varName = ctx.arrayElem().getText();
        String arrayName = ctx.arrayElem().IDENT().toString();
        
          //check if array element variable name is declared
          int numberOfExprs = ctx.arrayElem().expr().size();
          
          for(int i = 0 ; i < numberOfExprs; i++){
        	checkArrayElementVariableName(ctx.arrayElem().expr(i).getText(),
        		ctx.arrayElem().expr(i));
          }
         
            stack.push(varName);
            stack.push(arrayName);
            String typeNeeded = checkDefinedVariable(ctx);
            typeNeeded = stripArrayTypeBracket(typeNeeded);
            //OLD CODE
            //typeNeeded = typeNeeded.substring(0, typeNeeded.indexOf('['));
            
            stack.pop(); //arrayName is not needed in stack
            stack.push(typeNeeded);
          
          //OLD CODE.
//        printStack();
//        String varName = ctx.arrayElem().getText();
//        String arrName = varName.substring(0, varName.indexOf('['));
//        stack.push(varName);
//        
//        stack.push(arrName);
//        stack.push(checkDefinedVariable(ctx));
//        
//        String typeNeeded = stack.pop();
//        typeNeeded = typeNeeded.substring(0, typeNeeded.indexOf('['));
//        // remove arrName
//        stack.pop();
//        stack.push(typeNeeded);
//        printStack();

        return null;
    }

    public Void visitAssignlhspairelem(
                    WACCParser.AssignlhspairelemContext ctx) {
        if (DEBUG) {
            System.out.println("-Assign LHS pair elem");
            contextDepth(ctx);
        }
        visit(ctx.pairElem());
        return null;
    }

    public Void visitPairfstelem(WACCParser.PairfstelemContext ctx) {
        if (DEBUG) {
            System.out.println("-Assign Pair fst Elem");
            contextDepth(ctx);
        }
        visit(ctx.expr());
        visitPairElem(ctx, true);
        return null;
    }

    public Void visitPairsndelem(WACCParser.PairsndelemContext ctx) {
        if (DEBUG) {
            System.out.println("-Assign Pair snd Elem");
            contextDepth(ctx);
        }
        visit(ctx.expr());
        visitPairElem(ctx, false);
        return null;
    }

    private void visitPairElem(ParserRuleContext ctx, boolean fst) {

        // type of pair is not needed

        stack.pop();
        String pairName = stack.peek();
        IDENTIFIER obj = currentST.lookUpAllIdentifier(pairName);
        if (!(obj instanceof PAIR)) {
            semanticError(ctx, "Something went wrong.");
        }
        PAIR pair = (PAIR) obj;
        if (fst) {
            stack.push(pair.getFstType());
        } else {
            stack.push(pair.getSndType());
        }
    }

    /* Type */

    public Void visitTypebasetype(WACCParser.TypebasetypeContext ctx) {
        if (DEBUG) {
            System.out.print("-Type BASETYPE ");
            contextDepth(ctx);
        }

        String arrayBrackets = "";
        List<TerminalNode> brackets = ctx.LBRACK();

        int i = 0;
        while (i < brackets.size()) {
            arrayBrackets += "[]";
            i++;
        }

        String curType = renameStringToCharArray(ctx.BASETYPE()
                        .toString().toUpperCase());
        curType += arrayBrackets;

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
        visit(ctx.type());

        // Add brackets
        String arrayType = stack.pop();
        arrayType += "[]";
        stack.push(arrayType);

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

        currentST.addIdentifier(curVarName, newPair);
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
        visit(ctx.arraytype());

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

        visit(ctx.arrayElem());

        return null;
    }

    public Void visitArrayElem(WACCParser.ArrayElemContext ctx) {
        if (DEBUG) {
            System.out.print("-Arrayelem ");
            contextDepth(ctx);
        }
        

        int numberOfExprs = ctx.expr().size();
        
        for(int i = 0; i<numberOfExprs;i++){
          checkArrayElementVariableName(ctx.expr(i).getText(),
        	  ctx.expr(i));
        }
        
        String ident = ctx.IDENT().toString();
        IDENTIFIER object = currentST.lookUpIdentifier(ident);

        // TODO: Need to complete for array [OK]
        // if (!(object instanceof VARIABLE || object instanceof PAIR)) {
        // System.err.println("Something went wrong");
        // }

        String arrayElemType = object.getType();
        if (!(object instanceof PAIR)) {
            int brackets = ctx.LBRACK().size();
            arrayElemType = arrayElemType.substring(0,
                            arrayElemType.length() - brackets * 2);
        }

        stack.push(ident);
        stack.push(arrayElemType);
        return null;
    }

    public Void visitUnaryoperator(WACCParser.UnaryoperatorContext ctx) {
        if (DEBUG) {
            System.out.print("-Unary operator ");
            contextDepth(ctx);
        }
        visit(ctx.expr());
        String varType = stack.pop();
        String varName = stack.peek();

        String unaryOp = ctx.UNARYOP().toString();

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
            // TODO: If NOT ARRAY[DONE]
            if (!isAnArray(varType)&&!(varType.equals(STRING))) {
               //if not array, fails
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

        stack.push(varType);

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
        FUNCTION curFunc = currentST.lookUpAllFunction(functionName);

        if (curFunc == null) {
            // semantically never reach
            semanticError(ctx, "Something went wrong");
        }

        curFunc.setParamSize(ctx.param().size());

        return visitChildren(ctx);
    }

    public Void visitParam(WACCParser.ParamContext ctx) {
        if (DEBUG) {
            System.out.print("-Param ");
            contextDepth(ctx);
        }
        String functionName = stack.peek();
        FUNCTION curFunc = currentST.lookUpAllFunction(functionName);

        if (curFunc == null) {
            // semantically never reach
            semanticError(ctx, "Something went wrong");
        }

        visit(ctx.type());
        String paramReturnType = stack.pop();
        String paramName = ctx.IDENT().toString();
        PARAM newParam = new PARAM(paramReturnType, paramName);

        curFunc.addParam(newParam);

        return null;
    }

    public Void visitFunc(WACCParser.FuncContext ctx) {
        if (DEBUG) {
            System.out.print("-Function: " + ctx.IDENT().toString());
            contextDepth(ctx);
        }

        // Check for duplicate function
        String functionName = ctx.IDENT().toString();

        stack.push(functionName);
        FUNCTION func = currentST.lookUpAllFunction(functionName);
        newScope();

        // Add params in new scope
        for (int i = 0; i < func.getParamSize(); i++) {
            PARAM param = func.getParam(i);
            currentST.addParam(param.getName(), param);
        }

        visit(ctx.stat());
        freeScope();

        return null;
    }
}
