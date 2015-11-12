package frontend;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.TerminalNode;

import semantics.*;
import antlr.WACCParser;
import antlr.WACCParserBaseVisitor;

public class MyVisitor extends WACCParserBaseVisitor<Void> {

    private SymbolTable<String, IDENTIFIER> top_st;
    // TODO: Preload initial symbol table with globally visible identifiers

    private SymbolTable<String, IDENTIFIER> current_st;
    

    private int syntaxErrorCount = 0;
    private boolean DEBUG = true;

    private Void syntaxError() {
        // TODO: (Print out the error messages. Refer to referred compiler for
        // the messages)
        syntaxErrorCount += 1;

        return null;
    }

    private void contextDepth(ParserRuleContext ctx) {
        System.out.println(ctx.depth());
    }

    public int getSyntaxErrorCount() {
        return syntaxErrorCount;
    }

    /* Write functions to traverse tree below here */

    /* Visit the main program */

    public Void visitProgram(WACCParser.ProgramContext ctx) {
        if (DEBUG) {
            System.out.print("-Program ");
            contextDepth(ctx);
        }
        return visitChildren(ctx);
    }

    /* Functions to visit statements */

    public Void visitVarinit(WACCParser.VarinitContext ctx) {
        if (DEBUG) {
            System.out.print("-Variable init statement ");
            contextDepth(ctx);
        }
        return visitChildren(ctx);
    }

    public Void visitAssignment(WACCParser.AssignmentContext ctx) {
        if (DEBUG) {
            System.out.println("-Assignment statement");
            contextDepth(ctx);
        }
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

    public Void visitAssignRHS(WACCParser.AssignRHSContext ctx) {
        if (DEBUG) {
            System.out.print("-Assign RHS ");
            contextDepth(ctx);
        }
        return visitChildren(ctx);
    }

    public Void visitAssignLHS(WACCParser.AssignLHSContext ctx) {
        if (DEBUG) {
            System.out.println("-Assign LHS");
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
        return visitChildren(ctx);
    }

    public Void visitTerminal(TerminalNode node) {
        if (DEBUG) {
            System.out.println(node.toString());
        }
        return null;
    }

    /* Visit expressions */
    // public Void visitExpr(WACCParser.ExprContext ctx) {
    // if (DEBUG) {
    // System.out.println("-Expression");
    // contextDepth(ctx);
    // }
    // return visitChildren(ctx);
    // }

    public Void visitIntegerliteral(
                    WACCParser.IntegerliteralContext ctx) {
        if (DEBUG) {
            System.out.print("-Int literal ");
            contextDepth(ctx);
        }
        try {
            @SuppressWarnings("unused")
            Integer value = Integer.parseInt(ctx.getChild(0)
                            .getText());
        } catch (NumberFormatException e) {
            syntaxError();
        }
        return null;
    }

    public Void visitBooleanliteral(
                    WACCParser.BooleanliteralContext ctx) {
        if (DEBUG) {
            System.out.print("-Boolean literal ");
            contextDepth(ctx);
        }
        return visitChildren(ctx);
    }

    public Void visitCharliteral(WACCParser.CharliteralContext ctx) {
        if (DEBUG) {
            System.out.print("-Char literal ");
            contextDepth(ctx);
        }
        return visitChildren(ctx);
    }

    public Void visitStringliteral(WACCParser.StringliteralContext ctx) {
        if (DEBUG) {
            System.out.print("-String literal ");
            contextDepth(ctx);
        }
        return visitChildren(ctx);
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
        return visitChildren(ctx);
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
        // TODO

        return null;
    }
}
