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
		RBRACK=37, EXIT=9, UNARYOP=24, LBRACK=36, RBRACE=35, WHILE=16, LINE_COMMENT=41, 
		LBRACE=34, ELSE=15, BOOLEANLITERAL=28, DO=17, INTLITERAL=27, SEMI=38, 
		PRINT=10, LPAREN=32, IF=12, FI=13, SKIP=4, FREE=7, RPAREN=33, WS=40, THEN=14, 
		COMMA=39, BINARYOP=25, READ=6, IS=3, DONE=18, STRINGLITERAL=30, PRINTLN=11, 
		BEGIN=1, CHARLITERAL=29, RETURN=8, ASSIGN=5, PAIRELEM=21, IDENT=26, PAIR=23, 
		CALL=20, END=2, BASETYPE=22, PAIRLITERAL=31, NEWPAIR=19;
	public static final String[] tokenNames = {
		"<INVALID>", "'begin'", "'end'", "'is'", "'skip'", "'='", "'read'", "'free'", 
		"'return'", "'exit'", "'print'", "'println'", "'if'", "'fi'", "'then'", 
		"'else'", "'while'", "'do'", "'done'", "'newpair'", "'call'", "PAIRELEM", 
		"BASETYPE", "'pair'", "UNARYOP", "BINARYOP", "IDENT", "INTLITERAL", "BOOLEANLITERAL", 
		"CHARLITERAL", "STRINGLITERAL", "'null'", "'('", "')'", "'{'", "'}'", 
		"'['", "']'", "';'", "','", "WS", "LINE_COMMENT"
	};
	public static final int
		RULE_arrayElem = 0, RULE_expr = 1, RULE_prog = 2;
	public static final String[] ruleNames = {
		"arrayElem", "expr", "prog"
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
		enterRule(_localctx, 0, RULE_arrayElem);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(6); match(IDENT);
			setState(11); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(7); match(LBRACK);
					setState(8); expr(0);
					setState(9); match(RBRACK);
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(13); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,0,_ctx);
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
		int _startState = 2;
		enterRecursionRule(_localctx, 2, RULE_expr, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(29);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				{
				setState(16); match(UNARYOP);
				setState(17); expr(3);
				}
				break;
			case 2:
				{
				setState(18); match(INTLITERAL);
				}
				break;
			case 3:
				{
				setState(19); match(BOOLEANLITERAL);
				}
				break;
			case 4:
				{
				setState(20); match(CHARLITERAL);
				}
				break;
			case 5:
				{
				setState(21); match(STRINGLITERAL);
				}
				break;
			case 6:
				{
				setState(22); match(PAIRLITERAL);
				}
				break;
			case 7:
				{
				setState(23); match(IDENT);
				}
				break;
			case 8:
				{
				setState(24); arrayElem();
				}
				break;
			case 9:
				{
				setState(25); match(LPAREN);
				setState(26); expr(0);
				setState(27); match(RPAREN);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(36);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,2,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new ExprContext(_parentctx, _parentState);
					pushNewRecursionContext(_localctx, _startState, RULE_expr);
					setState(31);
					if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
					setState(32); match(BINARYOP);
					setState(33); expr(3);
					}
					} 
				}
				setState(38);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,2,_ctx);
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
		enterRule(_localctx, 4, RULE_prog);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(42);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << UNARYOP) | (1L << IDENT) | (1L << INTLITERAL) | (1L << BOOLEANLITERAL) | (1L << CHARLITERAL) | (1L << STRINGLITERAL) | (1L << PAIRLITERAL) | (1L << LPAREN))) != 0)) {
				{
				{
				setState(39); expr(0);
				}
				}
				setState(44);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(45); match(EOF);
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
		case 1: return expr_sempred((ExprContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean expr_sempred(ExprContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0: return precpred(_ctx, 2);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3+\62\4\2\t\2\4\3\t"+
		"\3\4\4\t\4\3\2\3\2\3\2\3\2\3\2\6\2\16\n\2\r\2\16\2\17\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3 \n\3\3\3\3\3\3\3\7\3%\n\3"+
		"\f\3\16\3(\13\3\3\4\7\4+\n\4\f\4\16\4.\13\4\3\4\3\4\3\4\2\3\4\5\2\4\6"+
		"\2\29\2\b\3\2\2\2\4\37\3\2\2\2\6,\3\2\2\2\b\r\7\34\2\2\t\n\7&\2\2\n\13"+
		"\5\4\3\2\13\f\7\'\2\2\f\16\3\2\2\2\r\t\3\2\2\2\16\17\3\2\2\2\17\r\3\2"+
		"\2\2\17\20\3\2\2\2\20\3\3\2\2\2\21\22\b\3\1\2\22\23\7\32\2\2\23 \5\4\3"+
		"\5\24 \7\35\2\2\25 \7\36\2\2\26 \7\37\2\2\27 \7 \2\2\30 \7!\2\2\31 \7"+
		"\34\2\2\32 \5\2\2\2\33\34\7\"\2\2\34\35\5\4\3\2\35\36\7#\2\2\36 \3\2\2"+
		"\2\37\21\3\2\2\2\37\24\3\2\2\2\37\25\3\2\2\2\37\26\3\2\2\2\37\27\3\2\2"+
		"\2\37\30\3\2\2\2\37\31\3\2\2\2\37\32\3\2\2\2\37\33\3\2\2\2 &\3\2\2\2!"+
		"\"\f\4\2\2\"#\7\33\2\2#%\5\4\3\5$!\3\2\2\2%(\3\2\2\2&$\3\2\2\2&\'\3\2"+
		"\2\2\'\5\3\2\2\2(&\3\2\2\2)+\5\4\3\2*)\3\2\2\2+.\3\2\2\2,*\3\2\2\2,-\3"+
		"\2\2\2-/\3\2\2\2.,\3\2\2\2/\60\7\2\2\3\60\7\3\2\2\2\6\17\37&,";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}