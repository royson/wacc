package frontend;

import semantics.IDENTIFIER;
import semantics.SymbolTable;
import antlr.WACCParser;
import antlr.WACCParserBaseVisitor;

public class MyVisitor extends WACCParserBaseVisitor<Void> {

  private SymbolTable<String, IDENTIFIER> top_st 
  	= new SymbolTable<String, IDENTIFIER>();
  //TODO: Preload initial symbol table with globally visible identifiers
  
  private int syntaxErrorCount = 0;

  private Void syntaxError() {
	// TODO: (Print out the error messages. Refer to referred compiler for
	// the messages)
	syntaxErrorCount += 1;

	return null;
  }

  public Void visitProgram(WACCParser.ProgramContext ctx) {
	return visitChildren(ctx);
  }

  public Void visitExpr(WACCParser.ExprContext ctx) {
	return null;
  }

  public Void visitStat(WACCParser.StatContext ctx) {
	// System.out.println("-Statement");
	return visitChildren(ctx);
  }

  public Void visitFunc(WACCParser.FuncContext ctx) {
	System.out.println("I found a function definition!");
	// TODO

	return null;
  }

  // Visiting literals
  public Void visitIntegerliteral(WACCParser.IntegerliteralContext ctx) {
	try {
	  @SuppressWarnings("unused")
	  Integer value = Integer.parseInt(ctx.getChild(0).getText());
	} catch (NumberFormatException e) {
	  syntaxError();
	}
	return null;
  }

  public int getSyntaxErrorCount() {
	return syntaxErrorCount;
  }
}
