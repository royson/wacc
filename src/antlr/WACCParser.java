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
		INTEGER=38, RBRACK=31, EXIT=4, UNARYOP=24, LBRACK=30, RBRACE=29, WHILE=16, 
		LINE_COMMENT=41, LBRACE=28, ELSE=15, DO=17, SEMI=32, PRINT=7, LPAREN=26, 
		IF=12, FI=13, SKIP=11, StringLiteral=36, FREE=5, RPAREN=27, WS=40, THEN=14, 
		COMMA=33, BINARYOP=25, READ=9, IS=6, DONE=18, PRINTLN=8, BEGIN=1, RETURN=10, 
		ASSIGN=23, PAIRELEM=22, IDENT=39, PAIR=20, PairLiteral=37, CALL=2, DOT=34, 
		END=3, BASETYPE=19, NEWPAIR=21, BooleanLiteral=35;
	public static final String[] tokenNames = {
		"<INVALID>", "'begin'", "'call'", "'end'", "'exit'", "'free'", "'is'", 
		"'print'", "'println'", "'read'", "'return'", "'skip'", "'if'", "'fi'", 
		"'then'", "'else'", "'while'", "'do'", "'done'", "BASETYPE", "'pair'", 
		"'newpair'", "PAIRELEM", "'='", "UNARYOP", "BINARYOP", "'('", "')'", "'{'", 
		"'}'", "'['", "']'", "';'", "','", "'.'", "BooleanLiteral", "StringLiteral", 
		"'null'", "INTEGER", "IDENT", "WS", "LINE_COMMENT"
	};
	public static final int
		RULE_binaryOper = 0, RULE_expr = 1, RULE_prog = 2;
	public static final String[] ruleNames = {
		"binaryOper", "expr", "prog"
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
	public static class BinaryOperContext extends ParserRuleContext {
		public TerminalNode BINARYOP() { return getToken(WACCParser.BINARYOP, 0); }
		public BinaryOperContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_binaryOper; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WACCParserVisitor ) return ((WACCParserVisitor<? extends T>)visitor).visitBinaryOper(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BinaryOperContext binaryOper() throws RecognitionException {
		BinaryOperContext _localctx = new BinaryOperContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_binaryOper);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(6); match(BINARYOP);
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
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode RPAREN() { return getToken(WACCParser.RPAREN, 0); }
		public TerminalNode LPAREN() { return getToken(WACCParser.LPAREN, 0); }
		public TerminalNode INTEGER() { return getToken(WACCParser.INTEGER, 0); }
		public BinaryOperContext binaryOper() {
			return getRuleContext(BinaryOperContext.class,0);
		}
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
			setState(14);
			switch (_input.LA(1)) {
			case INTEGER:
				{
				setState(9); match(INTEGER);
				}
				break;
			case LPAREN:
				{
				setState(10); match(LPAREN);
				setState(11); expr(0);
				setState(12); match(RPAREN);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(22);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new ExprContext(_parentctx, _parentState);
					pushNewRecursionContext(_localctx, _startState, RULE_expr);
					setState(16);
					if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
					setState(17); binaryOper();
					setState(18); expr(4);
					}
					} 
				}
				setState(24);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
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
			setState(28);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==LPAREN || _la==INTEGER) {
				{
				{
				setState(25); expr(0);
				}
				}
				setState(30);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(31); match(EOF);
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
		case 0: return precpred(_ctx, 3);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3+$\4\2\t\2\4\3\t\3"+
		"\4\4\t\4\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\3\5\3\21\n\3\3\3\3\3\3\3\3\3\7"+
		"\3\27\n\3\f\3\16\3\32\13\3\3\4\7\4\35\n\4\f\4\16\4 \13\4\3\4\3\4\3\4\2"+
		"\3\4\5\2\4\6\2\2#\2\b\3\2\2\2\4\20\3\2\2\2\6\36\3\2\2\2\b\t\7\33\2\2\t"+
		"\3\3\2\2\2\n\13\b\3\1\2\13\21\7(\2\2\f\r\7\34\2\2\r\16\5\4\3\2\16\17\7"+
		"\35\2\2\17\21\3\2\2\2\20\n\3\2\2\2\20\f\3\2\2\2\21\30\3\2\2\2\22\23\f"+
		"\5\2\2\23\24\5\2\2\2\24\25\5\4\3\6\25\27\3\2\2\2\26\22\3\2\2\2\27\32\3"+
		"\2\2\2\30\26\3\2\2\2\30\31\3\2\2\2\31\5\3\2\2\2\32\30\3\2\2\2\33\35\5"+
		"\4\3\2\34\33\3\2\2\2\35 \3\2\2\2\36\34\3\2\2\2\36\37\3\2\2\2\37!\3\2\2"+
		"\2 \36\3\2\2\2!\"\7\2\2\3\"\7\3\2\2\2\5\20\30\36";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}