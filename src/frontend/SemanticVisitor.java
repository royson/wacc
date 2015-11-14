package frontend;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;

import semantics.FUNCTION;
import semantics.IDENTIFIER;
import semantics.PAIR;
import semantics.PARAM;
import semantics.SymbolTable;
import semantics.VARIABLE;
import antlr.WACCParser;
import antlr.WACCParser.ExprContext;
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
    
    private void checkBinaryOpType(ParserRuleContext ctx, String type){
      if(!(Arrays.asList(primitiveTypes).contains(type))
      	||type.equals(STRING)){
          semanticError(ctx,"Incompatible type " +type);
      }
    }
    
    private void newScope(){
       SymbolTable<String, IDENTIFIER> st 
       		= new SymbolTable<String, IDENTIFIER>(currentST);
       currentST = st;
    }
    
    private void freeScope(){
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
        System.out.println(ctx.depth() + " " + ctx.getChildCount()
                        + " " + ctx);
    }
    
    private String checkBinaryOpArgument(ParserRuleContext ctx, 
    	String binaryOp, String binaryExpr, String binaryType){
      String returnType = "";
      
      //check if rhsType match with operator's requirement
      switch(binaryOp){
      	case "*":
      	case "/":
      	case "%":
      	case " + ":
      	case " - ":
      	  stack.push(INT);
      	  checkType(ctx, binaryExpr, binaryType);
      	  returnType = binaryType;
      	  break;
      	case ">":
      	case ">=":
      	case "<": 
      	case "<=":
      	  if(!binaryType.equals(INT) && !binaryType.equals(CHAR)){
      		stack.push(INT);
      		checkType(ctx, binaryExpr, binaryType);
      	  }
      	  returnType = BOOL;
      	  break;
      	case "&&":
      	case "||":
      	  stack.push(BOOL);
      	  checkType(ctx, binaryExpr, binaryType);
      	case "==":
      	case "!=":
      	  returnType = BOOL;
      	  break;
      }
      
      return returnType;
    }
    
    private void checkParameters(ParserRuleContext ctx,
    	String funcName,int paramSize,int funcSize){
      		if(paramSize != funcSize){
      	  		String errorMessage 
      	  			= "Incorrect number of parameters for "+funcName+
      	  			" (expected: "+funcSize+", actual: "+paramSize+
      	  			")";
      	  			semanticError(ctx,errorMessage);
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
            String errorMessage = "Incompatible type at " + value.replaceAll("\\s","");
            errorMessage += " (expected: " + compareType;
            errorMessage += ", actual: " + type + ")";
            semanticError(ctx, errorMessage);
        }
    }
    
    private void checkMultipleTypes(ParserRuleContext ctx, String value,
        String type) {
      	//types are separated by commas
        if (stack.isEmpty()) {
          	if (DEBUG) {
          	  	System.out.println("Something ridiculous that we will know");
          		}
          	return;
        }
        String compareTypes = stack.pop();
        boolean semErr = true;
        List<String> typesList = Arrays.asList(compareTypes.split("\\s*,\\s*"));
        for(String compareType : typesList ){
          if (type.equals(compareType)) {
        	semErr = false;
          }
        }
        if(semErr){
          String errorMessage = "Incompatible type at " + value;
          errorMessage += " (expected: " + Arrays.toString(typesList.toArray());
          errorMessage += ", actual: " + type + ")";
          semanticError(ctx, errorMessage);
        }
    }

    private String checkDefinedVariable(ParserRuleContext ctx) {
        String curIdentToCheck = stack.peek();
        IDENTIFIER object = currentST.lookupAll(curIdentToCheck);

        // Variable is not declared
        if (object == null || object instanceof FUNCTION) {
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
        
        if(stack.isEmpty()){
          //returning from main program
          semanticError(ctx, "Cannot return from the global scope.");
        }else{
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
        //exit code must be 0-256
        stack.push(INT);
        checkType(ctx.expr(),exprCode,exprType);
        
        return null;
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
        String condition = ctx.expr().getText();
        checkType(ctx.expr(),condition,ifExprType);
  
        //giving a new scope for each stat
        for(int x = 0; x < 2; x++){
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
        
        //Check if the condition is a BOOL
        stack.push(BOOL);
        String condition = ctx.expr().getText();
        checkType(ctx.expr(),condition,whileExprType);
        
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
        
        //clear unused name from stack
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
    
    public Void visitArrayLiter(WACCParser.ArrayLiterContext ctx){
      if (DEBUG) {
        System.out.print("-Assign ArrayLiter ");
        contextDepth(ctx);
      }      
      
      List<ExprContext> exprs = ctx.expr();
      
      for(ExprContext ectx : exprs){
    	//Populate the stack with names and types
    	visit(ectx);
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
    
    public Void visitArg_list(WACCParser.Arg_listContext ctx){
      if (DEBUG) {
        System.out.print("-ArgList call ");
        contextDepth(ctx);
      }
      
      String funcName = stack.pop();
      IDENTIFIER obj = currentST.lookupAll(funcName);
      if(!(obj instanceof FUNCTION)){
    	  semanticError(ctx,"Something went wrong.");
      }
      
      FUNCTION func = (FUNCTION) obj;
      
      //TODO
      
      //List of args
      List<ExprContext> args = ctx.expr();
      
      checkParameters(ctx.getParent(),funcName,ctx.expr().size(),func.getParamSize());
      
      if(!(ctx.expr().isEmpty())){
    	int i = 0;
    	  for(ExprContext ectx : args){
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
        
        IDENTIFIER obj = currentST.lookupAll(func);
        
        if(obj == null){
          stack.push(func);
          checkDefinedVariable(ctx);
        }
        else if(!(obj instanceof FUNCTION)){
          semanticError(ctx,"\""+func+"\" is not a function");
        }else{
          checkType(ctx,ctx.getText(),obj.getType());
          
          //Remove unwanted variable name
          stack.pop();
          
          //check the arguments
          stack.push(func);
          visit(ctx.arg_list());
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
        
        String arrayBrackets = "";
        List<TerminalNode> brackets = ctx.LBRACK();

        int i = 0;
		while (i < brackets.size()) {
		  arrayBrackets += "[]";
		  i++;
		}
        
        String curType = ctx.BASETYPE().toString().toUpperCase()
        				 + arrayBrackets;
        
        curType = renameStringToCharArray(curType);
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
        };
        // Visit LHS
        visit(ctx.expr(0));
        
        String lhsType = stack.pop();
        
        // Visit RHS
        visit(ctx.expr(1));
        
        String rhsType = stack.pop();
        String rhsExpr = stack.pop();
        
        //check for invalid types
        checkBinaryOpType(ctx, lhsType);
        checkBinaryOpType(ctx, rhsType);
        
        //check arguments for binary operation
        String lhsExpr = stack.peek();
        
        String binaryOp = ctx.BINARYOP().toString();
        String returnType = checkBinaryOpArgument(ctx.expr(0),
        	binaryOp ,lhsExpr, lhsType);
        returnType = checkBinaryOpArgument(ctx.expr(1), binaryOp,
        	rhsExpr, rhsType);

        //check if both argument types are the same
        //mainly for '>' '>=' '<=' '<' cases
        stack.push(rhsType);
        checkType(ctx, lhsExpr, lhsType);
        
        //Push the new expression into the stack
        //remove unused expression
        stack.pop();
        String newExpr 
        	= (lhsExpr+binaryOp+rhsExpr).replaceAll("\\s","");
        stack.push(newExpr);
        stack.push(returnType);
        
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
        IDENTIFIER obj = currentST
            .lookupAll(functionName);

        if(!(obj instanceof FUNCTION)){
            //semantically never reach
            semanticError(ctx,"Something went wrong");
        }
        
        FUNCTION curFunc = (FUNCTION) obj;

        curFunc.setParamSize(ctx.param().size());

        return visitChildren(ctx);
    }

    public Void visitParam(WACCParser.ParamContext ctx) {
        if (DEBUG) {
            System.out.print("-Param ");
            contextDepth(ctx);
        }
        String functionName = stack.peek();
        IDENTIFIER obj = currentST
                        .lookupAll(functionName);
        
        if(!(obj instanceof FUNCTION)){
          //semantically never reach
          semanticError(ctx,"Something went wrong");
        }
        
        FUNCTION curFunc = (FUNCTION) obj;

        String paramName = ctx.IDENT().toString();

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

        newScope();
        // Grabs the params and store it in the function object
        if (ctx.param_list() != null) {
            visit(ctx.param_list());
        }
        
        visit(ctx.stat());
        freeScope();
        
        return null;
    }
}
