// Generated from ./WACCParser.g4 by ANTLR 4.4
package antlr;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class WACCParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.4", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		RBRACK=38, EXIT=9, UNARYOP=25, LBRACK=37, RBRACE=36, WHILE=16, FST=21, 
		LINE_COMMENT=42, SND=22, LBRACE=35, ELSE=15, BOOLEANLITERAL=29, DO=17, 
		INTLITERAL=28, SEMI=39, PRINT=10, LPAREN=33, IF=12, FI=13, SKIP=4, FREE=7, 
		RPAREN=34, WS=41, THEN=14, COMMA=40, BINARYOP=26, READ=6, IS=3, DONE=18, 
		STRINGLITERAL=31, PRINTLN=11, BEGIN=1, CHARLITERAL=30, RETURN=8, ASSIGN=5, 
		IDENT=27, PAIR=24, CALL=20, END=2, BASETYPE=23, PAIRLITERAL=32, NEWPAIR=19;
	public static final String[] tokenNames = {
		"<INVALID>", "'begin'", "'end'", "'is'", "'skip'", "'='", "'read'", "'free'", 
		"'return'", "'exit'", "'print'", "'println'", "'if'", "'fi'", "'then'", 
		"'else'", "'while'", "'do'", "'done'", "'newpair'", "'call'", "'fst'", 
		"'snd'", "BASETYPE", "'pair'", "UNARYOP", "BINARYOP", "IDENT", "INTLITERAL", 
		"BOOLEANLITERAL", "CHARLITERAL", "STRINGLITERAL", "'null'", "'('", "')'", 
		"'{'", "'}'", "'['", "']'", "';'", "','", "WS", "LINE_COMMENT"
	};
	public static final int
		RULE_type = 0, RULE_arraytype = 1, RULE_pairtype = 2, RULE_pairelementype = 3, 
		RULE_expr = 4, RULE_arrayElem = 5, RULE_program = 6, RULE_func = 7, RULE_param_list = 8, 
		RULE_param = 9, RULE_stat = 10, RULE_assignLHS = 11, RULE_assignRHS = 12, 
		RULE_arg_list = 13, RULE_pairElem = 14, RULE_arrayLiter = 15, RULE_prog = 16;
	public static final String[] ruleNames = {
		"type", "arraytype", "pairtype", "pairelementype", "expr", "arrayElem", 
		"program", "func", "param_list", "param", "stat", "assignLHS", "assignRHS", 
		"arg_list", "pairElem", "arrayLiter", "prog"
	};

	@Override
	public String getGrammarFileName() { return "WACCParser.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public WACCParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class TypeContext extends ParserRuleContext {
		public TerminalNode BASETYPE() { return getToken(WACCParser.BASETYPE, 0); }
		public List<TerminalNode> RBRACK() { return getTokens(WACCParser.RBRACK); }
		public TerminalNode LBRACK(int i) {
			return getToken(WACCParser.LBRACK, i);
		}
		public TerminalNode RBRACK(int i) {
			return getToken(WACCParser.RBRACK, i);
		}
		public PairtypeContext pairtype() {
			return getRuleContext(PairtypeContext.class,0);
		}
		public List<TerminalNode> LBRACK() { return getTokens(WACCParser.LBRACK); }
		public TypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_type; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WACCParserVisitor ) return ((WACCParserVisitor<? extends T>)visitor).visitType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeContext type() throws RecognitionException {
		TypeContext _localctx = new TypeContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_type);
		try {
			int _alt;
			setState(50);
			switch (_input.LA(1)) {
			case BASETYPE:
				enterOuterAlt(_localctx, 1);
				{
				setState(34); match(BASETYPE);
				setState(39);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,0,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(35); match(LBRACK);
						setState(36); match(RBRACK);
						}
						} 
					}
					setState(41);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,0,_ctx);
				}
				}
				break;
			case PAIR:
				enterOuterAlt(_localctx, 2);
				{
				setState(42); pairtype();
				setState(47);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(43); match(LBRACK);
						setState(44); match(RBRACK);
						}
						} 
					}
					setState(49);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ArraytypeContext extends ParserRuleContext {
		public TerminalNode RBRACK() { return getToken(WACCParser.RBRACK, 0); }
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public TerminalNode LBRACK() { return getToken(WACCParser.LBRACK, 0); }
		public ArraytypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arraytype; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WACCParserVisitor ) return ((WACCParserVisitor<? extends T>)visitor).visitArraytype(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArraytypeContext arraytype() throws RecognitionException {
		ArraytypeContext _localctx = new ArraytypeContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_arraytype);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(52); type();
			setState(53); match(LBRACK);
			setState(54); match(RBRACK);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PairtypeContext extends ParserRuleContext {
		public List<PairelementypeContext> pairelementype() {
			return getRuleContexts(PairelementypeContext.class);
		}
		public TerminalNode COMMA() { return getToken(WACCParser.COMMA, 0); }
		public TerminalNode RPAREN() { return getToken(WACCParser.RPAREN, 0); }
		public TerminalNode PAIR() { return getToken(WACCParser.PAIR, 0); }
		public PairelementypeContext pairelementype(int i) {
			return getRuleContext(PairelementypeContext.class,i);
		}
		public TerminalNode LPAREN() { return getToken(WACCParser.LPAREN, 0); }
		public PairtypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pairtype; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WACCParserVisitor ) return ((WACCParserVisitor<? extends T>)visitor).visitPairtype(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PairtypeContext pairtype() throws RecognitionException {
		PairtypeContext _localctx = new PairtypeContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_pairtype);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(56); match(PAIR);
			setState(57); match(LPAREN);
			setState(58); pairelementype();
			setState(59); match(COMMA);
			setState(60); pairelementype();
			setState(61); match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PairelementypeContext extends ParserRuleContext {
		public ArraytypeContext arraytype() {
			return getRuleContext(ArraytypeContext.class,0);
		}
		public TerminalNode BASETYPE() { return getToken(WACCParser.BASETYPE, 0); }
		public TerminalNode PAIR() { return getToken(WACCParser.PAIR, 0); }
		public PairelementypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pairelementype; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WACCParserVisitor ) return ((WACCParserVisitor<? extends T>)visitor).visitPairelementype(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PairelementypeContext pairelementype() throws RecognitionException {
		PairelementypeContext _localctx = new PairelementypeContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_pairelementype);
		try {
			setState(66);
			switch ( getInterpreter().adaptivePredict(_input,3,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(63); match(BASETYPE);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(64); arraytype();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(65); match(PAIR);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExprContext extends ParserRuleContext {
		public TerminalNode INTLITERAL() { return getToken(WACCParser.INTLITERAL, 0); }
		public ArrayElemContext arrayElem() {
			return getRuleContext(ArrayElemContext.class,0);
		}
		public TerminalNode STRINGLITERAL() { return getToken(WACCParser.STRINGLITERAL, 0); }
		public TerminalNode LPAREN() { return getToken(WACCParser.LPAREN, 0); }
		public TerminalNode CHARLITERAL() { return getToken(WACCParser.CHARLITERAL, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode RPAREN() { return getToken(WACCParser.RPAREN, 0); }
		public TerminalNode UNARYOP() { return getToken(WACCParser.UNARYOP, 0); }
		public TerminalNode PAIRLITERAL() { return getToken(WACCParser.PAIRLITERAL, 0); }
		public TerminalNode BINARYOP() { return getToken(WACCParser.BINARYOP, 0); }
		public TerminalNode IDENT() { return getToken(WACCParser.IDENT, 0); }
		public TerminalNode BOOLEANLITERAL() { return getToken(WACCParser.BOOLEANLITERAL, 0); }
		public ExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expr; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WACCParserVisitor ) return ((WACCParserVisitor<? extends T>)visitor).visitExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExprContext expr() throws RecognitionException {
		return expr(0);
	}

	private ExprContext expr(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExprContext _localctx = new ExprContext(_ctx, _parentState);
		ExprContext _prevctx = _localctx;
		int _startState = 8;
		enterRecursionRule(_localctx, 8, RULE_expr, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(82);
			switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
			case 1:
				{
				setState(69); match(UNARYOP);
				setState(70); expr(3);
				}
				break;
			case 2:
				{
				setState(71); match(INTLITERAL);
				}
				break;
			case 3:
				{
				setState(72); match(BOOLEANLITERAL);
				}
				break;
			case 4:
				{
				setState(73); match(CHARLITERAL);
				}
				break;
			case 5:
				{
				setState(74); match(STRINGLITERAL);
				}
				break;
			case 6:
				{
				setState(75); match(PAIRLITERAL);
				}
				break;
			case 7:
				{
				setState(76); match(IDENT);
				}
				break;
			case 8:
				{
				setState(77); arrayElem();
				}
				break;
			case 9:
				{
				setState(78); match(LPAREN);
				setState(79); expr(0);
				setState(80); match(RPAREN);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(89);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,5,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new ExprContext(_parentctx, _parentState);
					pushNewRecursionContext(_localctx, _startState, RULE_expr);
					setState(84);
					if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
					setState(85); match(BINARYOP);
					setState(86); expr(3);
					}
					} 
				}
				setState(91);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,5,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class ArrayElemContext extends ParserRuleContext {
		public List<TerminalNode> RBRACK() { return getTokens(WACCParser.RBRACK); }
		public TerminalNode LBRACK(int i) {
			return getToken(WACCParser.LBRACK, i);
		}
		public TerminalNode RBRACK(int i) {
			return getToken(WACCParser.RBRACK, i);
		}
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode IDENT() { return getToken(WACCParser.IDENT, 0); }
		public List<TerminalNode> LBRACK() { return getTokens(WACCParser.LBRACK); }
		public ArrayElemContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arrayElem; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WACCParserVisitor ) return ((WACCParserVisitor<? extends T>)visitor).visitArrayElem(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArrayElemContext arrayElem() throws RecognitionException {
		ArrayElemContext _localctx = new ArrayElemContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_arrayElem);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(92); match(IDENT);
			setState(97); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(93); match(LBRACK);
					setState(94); expr(0);
					setState(95); match(RBRACK);
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(99); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,6,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ProgramContext extends ParserRuleContext {
		public List<FuncContext> func() {
			return getRuleContexts(FuncContext.class);
		}
		public StatContext stat() {
			return getRuleContext(StatContext.class,0);
		}
		public FuncContext func(int i) {
			return getRuleContext(FuncContext.class,i);
		}
		public TerminalNode BEGIN() { return getToken(WACCParser.BEGIN, 0); }
		public TerminalNode END() { return getToken(WACCParser.END, 0); }
		public ProgramContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_program; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WACCParserVisitor ) return ((WACCParserVisitor<? extends T>)visitor).visitProgram(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ProgramContext program() throws RecognitionException {
		ProgramContext _localctx = new ProgramContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_program);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(101); match(BEGIN);
			setState(105);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,7,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(102); func();
					}
					} 
				}
				setState(107);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,7,_ctx);
			}
			{
			setState(108); stat(0);
			}
			setState(109); match(END);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FuncContext extends ParserRuleContext {
		public Param_listContext param_list() {
			return getRuleContext(Param_listContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(WACCParser.RPAREN, 0); }
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public StatContext stat() {
			return getRuleContext(StatContext.class,0);
		}
		public TerminalNode IS() { return getToken(WACCParser.IS, 0); }
		public TerminalNode LPAREN() { return getToken(WACCParser.LPAREN, 0); }
		public TerminalNode IDENT() { return getToken(WACCParser.IDENT, 0); }
		public TerminalNode END() { return getToken(WACCParser.END, 0); }
		public FuncContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_func; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WACCParserVisitor ) return ((WACCParserVisitor<? extends T>)visitor).visitFunc(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FuncContext func() throws RecognitionException {
		FuncContext _localctx = new FuncContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_func);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(111); type();
			setState(112); match(IDENT);
			setState(113); match(LPAREN);
			setState(115);
			_la = _input.LA(1);
			if (_la==BASETYPE || _la==PAIR) {
				{
				setState(114); param_list();
				}
			}

			setState(117); match(RPAREN);
			setState(118); match(IS);
			setState(119); stat(0);
			setState(120); match(END);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Param_listContext extends ParserRuleContext {
		public List<ParamContext> param() {
			return getRuleContexts(ParamContext.class);
		}
		public List<TerminalNode> COMMA() { return getTokens(WACCParser.COMMA); }
		public ParamContext param(int i) {
			return getRuleContext(ParamContext.class,i);
		}
		public TerminalNode COMMA(int i) {
			return getToken(WACCParser.COMMA, i);
		}
		public Param_listContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_param_list; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WACCParserVisitor ) return ((WACCParserVisitor<? extends T>)visitor).visitParam_list(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Param_listContext param_list() throws RecognitionException {
		Param_listContext _localctx = new Param_listContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_param_list);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(122); param();
			setState(127);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(123); match(COMMA);
				setState(124); param();
				}
				}
				setState(129);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ParamContext extends ParserRuleContext {
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public TerminalNode IDENT() { return getToken(WACCParser.IDENT, 0); }
		public ParamContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_param; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WACCParserVisitor ) return ((WACCParserVisitor<? extends T>)visitor).visitParam(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParamContext param() throws RecognitionException {
		ParamContext _localctx = new ParamContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_param);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(130); type();
			setState(131); match(IDENT);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class StatContext extends ParserRuleContext {
		public TerminalNode SKIP() { return getToken(WACCParser.SKIP, 0); }
		public TerminalNode WHILE() { return getToken(WACCParser.WHILE, 0); }
		public TerminalNode IF() { return getToken(WACCParser.IF, 0); }
		public TerminalNode DO() { return getToken(WACCParser.DO, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode RETURN() { return getToken(WACCParser.RETURN, 0); }
		public TerminalNode FREE() { return getToken(WACCParser.FREE, 0); }
		public List<StatContext> stat() {
			return getRuleContexts(StatContext.class);
		}
		public TerminalNode EXIT() { return getToken(WACCParser.EXIT, 0); }
		public TerminalNode IDENT() { return getToken(WACCParser.IDENT, 0); }
		public TerminalNode PRINTLN() { return getToken(WACCParser.PRINTLN, 0); }
		public TerminalNode PRINT() { return getToken(WACCParser.PRINT, 0); }
		public TerminalNode THEN() { return getToken(WACCParser.THEN, 0); }
		public TerminalNode FI() { return getToken(WACCParser.FI, 0); }
		public TerminalNode DONE() { return getToken(WACCParser.DONE, 0); }
		public StatContext stat(int i) {
			return getRuleContext(StatContext.class,i);
		}
		public TerminalNode ELSE() { return getToken(WACCParser.ELSE, 0); }
		public TerminalNode ASSIGN() { return getToken(WACCParser.ASSIGN, 0); }
		public TerminalNode SEMI() { return getToken(WACCParser.SEMI, 0); }
		public TerminalNode READ() { return getToken(WACCParser.READ, 0); }
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public AssignLHSContext assignLHS() {
			return getRuleContext(AssignLHSContext.class,0);
		}
		public AssignRHSContext assignRHS() {
			return getRuleContext(AssignRHSContext.class,0);
		}
		public TerminalNode BEGIN() { return getToken(WACCParser.BEGIN, 0); }
		public TerminalNode END() { return getToken(WACCParser.END, 0); }
		public StatContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stat; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WACCParserVisitor ) return ((WACCParserVisitor<? extends T>)visitor).visitStat(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StatContext stat() throws RecognitionException {
		return stat(0);
	}

	private StatContext stat(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		StatContext _localctx = new StatContext(_ctx, _parentState);
		StatContext _prevctx = _localctx;
		int _startState = 20;
		enterRecursionRule(_localctx, 20, RULE_stat, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(174);
			switch (_input.LA(1)) {
			case SKIP:
				{
				setState(134); match(SKIP);
				}
				break;
			case BASETYPE:
			case PAIR:
				{
				setState(135); type();
				setState(136); match(IDENT);
				setState(137); match(ASSIGN);
				setState(138); assignRHS();
				}
				break;
			case FST:
			case SND:
			case IDENT:
				{
				setState(140); assignLHS();
				setState(141); match(ASSIGN);
				setState(142); assignRHS();
				}
				break;
			case READ:
				{
				setState(144); match(READ);
				setState(145); assignLHS();
				}
				break;
			case FREE:
				{
				setState(146); match(FREE);
				setState(147); expr(0);
				}
				break;
			case RETURN:
				{
				setState(148); match(RETURN);
				setState(149); expr(0);
				}
				break;
			case EXIT:
				{
				setState(150); match(EXIT);
				setState(151); expr(0);
				}
				break;
			case PRINT:
				{
				setState(152); match(PRINT);
				setState(153); expr(0);
				}
				break;
			case PRINTLN:
				{
				setState(154); match(PRINTLN);
				setState(155); expr(0);
				}
				break;
			case IF:
				{
				setState(156); match(IF);
				setState(157); expr(0);
				setState(158); match(THEN);
				setState(159); stat(0);
				setState(160); match(ELSE);
				setState(161); stat(0);
				setState(162); match(FI);
				}
				break;
			case WHILE:
				{
				setState(164); match(WHILE);
				setState(165); expr(0);
				setState(166); match(DO);
				setState(167); stat(0);
				setState(168); match(DONE);
				}
				break;
			case BEGIN:
				{
				setState(170); match(BEGIN);
				setState(171); stat(0);
				setState(172); match(END);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(181);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,11,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new StatContext(_parentctx, _parentState);
					pushNewRecursionContext(_localctx, _startState, RULE_stat);
					setState(176);
					if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
					setState(177); match(SEMI);
					setState(178); stat(2);
					}
					} 
				}
				setState(183);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,11,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class AssignLHSContext extends ParserRuleContext {
		public PairElemContext pairElem() {
			return getRuleContext(PairElemContext.class,0);
		}
		public ArrayElemContext arrayElem() {
			return getRuleContext(ArrayElemContext.class,0);
		}
		public TerminalNode IDENT() { return getToken(WACCParser.IDENT, 0); }
		public AssignLHSContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_assignLHS; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WACCParserVisitor ) return ((WACCParserVisitor<? extends T>)visitor).visitAssignLHS(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AssignLHSContext assignLHS() throws RecognitionException {
		AssignLHSContext _localctx = new AssignLHSContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_assignLHS);
		try {
			setState(187);
			switch ( getInterpreter().adaptivePredict(_input,12,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(184); match(IDENT);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(185); arrayElem();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(186); pairElem();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AssignRHSContext extends ParserRuleContext {
		public TerminalNode CALL() { return getToken(WACCParser.CALL, 0); }
		public ArrayLiterContext arrayLiter() {
			return getRuleContext(ArrayLiterContext.class,0);
		}
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public PairElemContext pairElem() {
			return getRuleContext(PairElemContext.class,0);
		}
		public Arg_listContext arg_list() {
			return getRuleContext(Arg_listContext.class,0);
		}
		public TerminalNode COMMA() { return getToken(WACCParser.COMMA, 0); }
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode RPAREN() { return getToken(WACCParser.RPAREN, 0); }
		public TerminalNode NEWPAIR() { return getToken(WACCParser.NEWPAIR, 0); }
		public TerminalNode LPAREN() { return getToken(WACCParser.LPAREN, 0); }
		public TerminalNode IDENT() { return getToken(WACCParser.IDENT, 0); }
		public AssignRHSContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_assignRHS; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WACCParserVisitor ) return ((WACCParserVisitor<? extends T>)visitor).visitAssignRHS(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AssignRHSContext assignRHS() throws RecognitionException {
		AssignRHSContext _localctx = new AssignRHSContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_assignRHS);
		int _la;
		try {
			setState(206);
			switch (_input.LA(1)) {
			case UNARYOP:
			case IDENT:
			case INTLITERAL:
			case BOOLEANLITERAL:
			case CHARLITERAL:
			case STRINGLITERAL:
			case PAIRLITERAL:
			case LPAREN:
				enterOuterAlt(_localctx, 1);
				{
				setState(189); expr(0);
				}
				break;
			case LBRACK:
				enterOuterAlt(_localctx, 2);
				{
				setState(190); arrayLiter();
				}
				break;
			case NEWPAIR:
				enterOuterAlt(_localctx, 3);
				{
				setState(191); match(NEWPAIR);
				setState(192); match(LPAREN);
				setState(193); expr(0);
				setState(194); match(COMMA);
				setState(195); expr(0);
				setState(196); match(RPAREN);
				}
				break;
			case FST:
			case SND:
				enterOuterAlt(_localctx, 4);
				{
				setState(198); pairElem();
				}
				break;
			case CALL:
				enterOuterAlt(_localctx, 5);
				{
				setState(199); match(CALL);
				setState(200); match(IDENT);
				setState(201); match(LPAREN);
				setState(203);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << UNARYOP) | (1L << IDENT) | (1L << INTLITERAL) | (1L << BOOLEANLITERAL) | (1L << CHARLITERAL) | (1L << STRINGLITERAL) | (1L << PAIRLITERAL) | (1L << LPAREN))) != 0)) {
					{
					setState(202); arg_list();
					}
				}

				setState(205); match(RPAREN);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Arg_listContext extends ParserRuleContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public List<TerminalNode> COMMA() { return getTokens(WACCParser.COMMA); }
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode COMMA(int i) {
			return getToken(WACCParser.COMMA, i);
		}
		public Arg_listContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arg_list; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WACCParserVisitor ) return ((WACCParserVisitor<? extends T>)visitor).visitArg_list(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Arg_listContext arg_list() throws RecognitionException {
		Arg_listContext _localctx = new Arg_listContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_arg_list);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(208); expr(0);
			setState(213);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(209); match(COMMA);
				setState(210); expr(0);
				}
				}
				setState(215);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PairElemContext extends ParserRuleContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode FST() { return getToken(WACCParser.FST, 0); }
		public TerminalNode SND() { return getToken(WACCParser.SND, 0); }
		public PairElemContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pairElem; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WACCParserVisitor ) return ((WACCParserVisitor<? extends T>)visitor).visitPairElem(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PairElemContext pairElem() throws RecognitionException {
		PairElemContext _localctx = new PairElemContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_pairElem);
		try {
			setState(220);
			switch (_input.LA(1)) {
			case FST:
				enterOuterAlt(_localctx, 1);
				{
				setState(216); match(FST);
				setState(217); expr(0);
				}
				break;
			case SND:
				enterOuterAlt(_localctx, 2);
				{
				setState(218); match(SND);
				setState(219); expr(0);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ArrayLiterContext extends ParserRuleContext {
		public TerminalNode RBRACK() { return getToken(WACCParser.RBRACK, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public List<TerminalNode> COMMA() { return getTokens(WACCParser.COMMA); }
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode COMMA(int i) {
			return getToken(WACCParser.COMMA, i);
		}
		public TerminalNode LBRACK() { return getToken(WACCParser.LBRACK, 0); }
		public ArrayLiterContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arrayLiter; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WACCParserVisitor ) return ((WACCParserVisitor<? extends T>)visitor).visitArrayLiter(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArrayLiterContext arrayLiter() throws RecognitionException {
		ArrayLiterContext _localctx = new ArrayLiterContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_arrayLiter);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(222); match(LBRACK);
			setState(231);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << UNARYOP) | (1L << IDENT) | (1L << INTLITERAL) | (1L << BOOLEANLITERAL) | (1L << CHARLITERAL) | (1L << STRINGLITERAL) | (1L << PAIRLITERAL) | (1L << LPAREN))) != 0)) {
				{
				setState(223); expr(0);
				setState(228);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(224); match(COMMA);
					setState(225); expr(0);
					}
					}
					setState(230);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(233); match(RBRACK);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ProgContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(WACCParser.EOF, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public ProgContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_prog; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WACCParserVisitor ) return ((WACCParserVisitor<? extends T>)visitor).visitProg(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ProgContext prog() throws RecognitionException {
		ProgContext _localctx = new ProgContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_prog);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(238);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << UNARYOP) | (1L << IDENT) | (1L << INTLITERAL) | (1L << BOOLEANLITERAL) | (1L << CHARLITERAL) | (1L << STRINGLITERAL) | (1L << PAIRLITERAL) | (1L << LPAREN))) != 0)) {
				{
				{
				setState(235); expr(0);
				}
				}
				setState(240);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(241); match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 4: return expr_sempred((ExprContext)_localctx, predIndex);
		case 10: return stat_sempred((StatContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean expr_sempred(ExprContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0: return precpred(_ctx, 2);
		}
		return true;
	}
	private boolean stat_sempred(StatContext _localctx, int predIndex) {
		switch (predIndex) {
		case 1: return precpred(_ctx, 1);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3,\u00f6\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\3\2\3\2\3\2\7\2(\n\2\f\2\16\2+\13\2\3\2\3\2\3\2\7\2\60\n\2\f\2\16\2\63"+
		"\13\2\5\2\65\n\2\3\3\3\3\3\3\3\3\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\5\3\5\3"+
		"\5\5\5E\n\5\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\5"+
		"\6U\n\6\3\6\3\6\3\6\7\6Z\n\6\f\6\16\6]\13\6\3\7\3\7\3\7\3\7\3\7\6\7d\n"+
		"\7\r\7\16\7e\3\b\3\b\7\bj\n\b\f\b\16\bm\13\b\3\b\3\b\3\b\3\t\3\t\3\t\3"+
		"\t\5\tv\n\t\3\t\3\t\3\t\3\t\3\t\3\n\3\n\3\n\7\n\u0080\n\n\f\n\16\n\u0083"+
		"\13\n\3\13\3\13\3\13\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3"+
		"\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f"+
		"\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\5\f\u00b1\n\f\3\f\3\f\3\f"+
		"\7\f\u00b6\n\f\f\f\16\f\u00b9\13\f\3\r\3\r\3\r\5\r\u00be\n\r\3\16\3\16"+
		"\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\5\16\u00ce"+
		"\n\16\3\16\5\16\u00d1\n\16\3\17\3\17\3\17\7\17\u00d6\n\17\f\17\16\17\u00d9"+
		"\13\17\3\20\3\20\3\20\3\20\5\20\u00df\n\20\3\21\3\21\3\21\3\21\7\21\u00e5"+
		"\n\21\f\21\16\21\u00e8\13\21\5\21\u00ea\n\21\3\21\3\21\3\22\7\22\u00ef"+
		"\n\22\f\22\16\22\u00f2\13\22\3\22\3\22\3\22\2\4\n\26\23\2\4\6\b\n\f\16"+
		"\20\22\24\26\30\32\34\36 \"\2\2\u010e\2\64\3\2\2\2\4\66\3\2\2\2\6:\3\2"+
		"\2\2\bD\3\2\2\2\nT\3\2\2\2\f^\3\2\2\2\16g\3\2\2\2\20q\3\2\2\2\22|\3\2"+
		"\2\2\24\u0084\3\2\2\2\26\u00b0\3\2\2\2\30\u00bd\3\2\2\2\32\u00d0\3\2\2"+
		"\2\34\u00d2\3\2\2\2\36\u00de\3\2\2\2 \u00e0\3\2\2\2\"\u00f0\3\2\2\2$)"+
		"\7\31\2\2%&\7\'\2\2&(\7(\2\2\'%\3\2\2\2(+\3\2\2\2)\'\3\2\2\2)*\3\2\2\2"+
		"*\65\3\2\2\2+)\3\2\2\2,\61\5\6\4\2-.\7\'\2\2.\60\7(\2\2/-\3\2\2\2\60\63"+
		"\3\2\2\2\61/\3\2\2\2\61\62\3\2\2\2\62\65\3\2\2\2\63\61\3\2\2\2\64$\3\2"+
		"\2\2\64,\3\2\2\2\65\3\3\2\2\2\66\67\5\2\2\2\678\7\'\2\289\7(\2\29\5\3"+
		"\2\2\2:;\7\32\2\2;<\7#\2\2<=\5\b\5\2=>\7*\2\2>?\5\b\5\2?@\7$\2\2@\7\3"+
		"\2\2\2AE\7\31\2\2BE\5\4\3\2CE\7\32\2\2DA\3\2\2\2DB\3\2\2\2DC\3\2\2\2E"+
		"\t\3\2\2\2FG\b\6\1\2GH\7\33\2\2HU\5\n\6\5IU\7\36\2\2JU\7\37\2\2KU\7 \2"+
		"\2LU\7!\2\2MU\7\"\2\2NU\7\35\2\2OU\5\f\7\2PQ\7#\2\2QR\5\n\6\2RS\7$\2\2"+
		"SU\3\2\2\2TF\3\2\2\2TI\3\2\2\2TJ\3\2\2\2TK\3\2\2\2TL\3\2\2\2TM\3\2\2\2"+
		"TN\3\2\2\2TO\3\2\2\2TP\3\2\2\2U[\3\2\2\2VW\f\4\2\2WX\7\34\2\2XZ\5\n\6"+
		"\5YV\3\2\2\2Z]\3\2\2\2[Y\3\2\2\2[\\\3\2\2\2\\\13\3\2\2\2][\3\2\2\2^c\7"+
		"\35\2\2_`\7\'\2\2`a\5\n\6\2ab\7(\2\2bd\3\2\2\2c_\3\2\2\2de\3\2\2\2ec\3"+
		"\2\2\2ef\3\2\2\2f\r\3\2\2\2gk\7\3\2\2hj\5\20\t\2ih\3\2\2\2jm\3\2\2\2k"+
		"i\3\2\2\2kl\3\2\2\2ln\3\2\2\2mk\3\2\2\2no\5\26\f\2op\7\4\2\2p\17\3\2\2"+
		"\2qr\5\2\2\2rs\7\35\2\2su\7#\2\2tv\5\22\n\2ut\3\2\2\2uv\3\2\2\2vw\3\2"+
		"\2\2wx\7$\2\2xy\7\5\2\2yz\5\26\f\2z{\7\4\2\2{\21\3\2\2\2|\u0081\5\24\13"+
		"\2}~\7*\2\2~\u0080\5\24\13\2\177}\3\2\2\2\u0080\u0083\3\2\2\2\u0081\177"+
		"\3\2\2\2\u0081\u0082\3\2\2\2\u0082\23\3\2\2\2\u0083\u0081\3\2\2\2\u0084"+
		"\u0085\5\2\2\2\u0085\u0086\7\35\2\2\u0086\25\3\2\2\2\u0087\u0088\b\f\1"+
		"\2\u0088\u00b1\7\6\2\2\u0089\u008a\5\2\2\2\u008a\u008b\7\35\2\2\u008b"+
		"\u008c\7\7\2\2\u008c\u008d\5\32\16\2\u008d\u00b1\3\2\2\2\u008e\u008f\5"+
		"\30\r\2\u008f\u0090\7\7\2\2\u0090\u0091\5\32\16\2\u0091\u00b1\3\2\2\2"+
		"\u0092\u0093\7\b\2\2\u0093\u00b1\5\30\r\2\u0094\u0095\7\t\2\2\u0095\u00b1"+
		"\5\n\6\2\u0096\u0097\7\n\2\2\u0097\u00b1\5\n\6\2\u0098\u0099\7\13\2\2"+
		"\u0099\u00b1\5\n\6\2\u009a\u009b\7\f\2\2\u009b\u00b1\5\n\6\2\u009c\u009d"+
		"\7\r\2\2\u009d\u00b1\5\n\6\2\u009e\u009f\7\16\2\2\u009f\u00a0\5\n\6\2"+
		"\u00a0\u00a1\7\20\2\2\u00a1\u00a2\5\26\f\2\u00a2\u00a3\7\21\2\2\u00a3"+
		"\u00a4\5\26\f\2\u00a4\u00a5\7\17\2\2\u00a5\u00b1\3\2\2\2\u00a6\u00a7\7"+
		"\22\2\2\u00a7\u00a8\5\n\6\2\u00a8\u00a9\7\23\2\2\u00a9\u00aa\5\26\f\2"+
		"\u00aa\u00ab\7\24\2\2\u00ab\u00b1\3\2\2\2\u00ac\u00ad\7\3\2\2\u00ad\u00ae"+
		"\5\26\f\2\u00ae\u00af\7\4\2\2\u00af\u00b1\3\2\2\2\u00b0\u0087\3\2\2\2"+
		"\u00b0\u0089\3\2\2\2\u00b0\u008e\3\2\2\2\u00b0\u0092\3\2\2\2\u00b0\u0094"+
		"\3\2\2\2\u00b0\u0096\3\2\2\2\u00b0\u0098\3\2\2\2\u00b0\u009a\3\2\2\2\u00b0"+
		"\u009c\3\2\2\2\u00b0\u009e\3\2\2\2\u00b0\u00a6\3\2\2\2\u00b0\u00ac\3\2"+
		"\2\2\u00b1\u00b7\3\2\2\2\u00b2\u00b3\f\3\2\2\u00b3\u00b4\7)\2\2\u00b4"+
		"\u00b6\5\26\f\4\u00b5\u00b2\3\2\2\2\u00b6\u00b9\3\2\2\2\u00b7\u00b5\3"+
		"\2\2\2\u00b7\u00b8\3\2\2\2\u00b8\27\3\2\2\2\u00b9\u00b7\3\2\2\2\u00ba"+
		"\u00be\7\35\2\2\u00bb\u00be\5\f\7\2\u00bc\u00be\5\36\20\2\u00bd\u00ba"+
		"\3\2\2\2\u00bd\u00bb\3\2\2\2\u00bd\u00bc\3\2\2\2\u00be\31\3\2\2\2\u00bf"+
		"\u00d1\5\n\6\2\u00c0\u00d1\5 \21\2\u00c1\u00c2\7\25\2\2\u00c2\u00c3\7"+
		"#\2\2\u00c3\u00c4\5\n\6\2\u00c4\u00c5\7*\2\2\u00c5\u00c6\5\n\6\2\u00c6"+
		"\u00c7\7$\2\2\u00c7\u00d1\3\2\2\2\u00c8\u00d1\5\36\20\2\u00c9\u00ca\7"+
		"\26\2\2\u00ca\u00cb\7\35\2\2\u00cb\u00cd\7#\2\2\u00cc\u00ce\5\34\17\2"+
		"\u00cd\u00cc\3\2\2\2\u00cd\u00ce\3\2\2\2\u00ce\u00cf\3\2\2\2\u00cf\u00d1"+
		"\7$\2\2\u00d0\u00bf\3\2\2\2\u00d0\u00c0\3\2\2\2\u00d0\u00c1\3\2\2\2\u00d0"+
		"\u00c8\3\2\2\2\u00d0\u00c9\3\2\2\2\u00d1\33\3\2\2\2\u00d2\u00d7\5\n\6"+
		"\2\u00d3\u00d4\7*\2\2\u00d4\u00d6\5\n\6\2\u00d5\u00d3\3\2\2\2\u00d6\u00d9"+
		"\3\2\2\2\u00d7\u00d5\3\2\2\2\u00d7\u00d8\3\2\2\2\u00d8\35\3\2\2\2\u00d9"+
		"\u00d7\3\2\2\2\u00da\u00db\7\27\2\2\u00db\u00df\5\n\6\2\u00dc\u00dd\7"+
		"\30\2\2\u00dd\u00df\5\n\6\2\u00de\u00da\3\2\2\2\u00de\u00dc\3\2\2\2\u00df"+
		"\37\3\2\2\2\u00e0\u00e9\7\'\2\2\u00e1\u00e6\5\n\6\2\u00e2\u00e3\7*\2\2"+
		"\u00e3\u00e5\5\n\6\2\u00e4\u00e2\3\2\2\2\u00e5\u00e8\3\2\2\2\u00e6\u00e4"+
		"\3\2\2\2\u00e6\u00e7\3\2\2\2\u00e7\u00ea\3\2\2\2\u00e8\u00e6\3\2\2\2\u00e9"+
		"\u00e1\3\2\2\2\u00e9\u00ea\3\2\2\2\u00ea\u00eb\3\2\2\2\u00eb\u00ec\7("+
		"\2\2\u00ec!\3\2\2\2\u00ed\u00ef\5\n\6\2\u00ee\u00ed\3\2\2\2\u00ef\u00f2"+
		"\3\2\2\2\u00f0\u00ee\3\2\2\2\u00f0\u00f1\3\2\2\2\u00f1\u00f3\3\2\2\2\u00f2"+
		"\u00f0\3\2\2\2\u00f3\u00f4\7\2\2\3\u00f4#\3\2\2\2\26)\61\64DT[eku\u0081"+
		"\u00b0\u00b7\u00bd\u00cd\u00d0\u00d7\u00de\u00e6\u00e9\u00f0";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}