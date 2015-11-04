// Generated from ./WACCParser.g4 by ANTLR 4.4
package antlr;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link WACCParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface WACCParserVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link WACCParser#pairelementype}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPairelementype(@NotNull WACCParser.PairelementypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link WACCParser#param}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParam(@NotNull WACCParser.ParamContext ctx);
	/**
	 * Visit a parse tree produced by {@link WACCParser#pairElem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPairElem(@NotNull WACCParser.PairElemContext ctx);
	/**
	 * Visit a parse tree produced by {@link WACCParser#arrayElem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayElem(@NotNull WACCParser.ArrayElemContext ctx);
	/**
	 * Visit a parse tree produced by {@link WACCParser#assignRHS}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignRHS(@NotNull WACCParser.AssignRHSContext ctx);
	/**
	 * Visit a parse tree produced by {@link WACCParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr(@NotNull WACCParser.ExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link WACCParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType(@NotNull WACCParser.TypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link WACCParser#stat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStat(@NotNull WACCParser.StatContext ctx);
	/**
	 * Visit a parse tree produced by {@link WACCParser#param_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParam_list(@NotNull WACCParser.Param_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link WACCParser#assignLHS}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignLHS(@NotNull WACCParser.AssignLHSContext ctx);
	/**
	 * Visit a parse tree produced by {@link WACCParser#prog}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProg(@NotNull WACCParser.ProgContext ctx);
	/**
	 * Visit a parse tree produced by {@link WACCParser#arg_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArg_list(@NotNull WACCParser.Arg_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link WACCParser#arraytype}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArraytype(@NotNull WACCParser.ArraytypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link WACCParser#pairtype}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPairtype(@NotNull WACCParser.PairtypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link WACCParser#program}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgram(@NotNull WACCParser.ProgramContext ctx);
	/**
	 * Visit a parse tree produced by {@link WACCParser#arrayLiter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayLiter(@NotNull WACCParser.ArrayLiterContext ctx);
	/**
	 * Visit a parse tree produced by {@link WACCParser#func}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunc(@NotNull WACCParser.FuncContext ctx);
}