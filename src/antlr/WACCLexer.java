// Generated from ./WACCLexer.g4 by ANTLR 4.4
package antlr;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class WACCLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.4", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		BEGIN=1, END=2, EXIT=3, ASSIGN=4, MUL=5, DIV=6, MOD=7, ADD=8, SUB=9, GT=10, 
		GE=11, LT=12, LE=13, EQUAL=14, NOTEQUAL=15, AND=16, OR=17, OPEN_PARENTHESES=18, 
		CLOSE_PARENTHESES=19, INTEGER=20, WS=21, LINE_COMMENT=22;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] tokenNames = {
		"'\\u0000'", "'\\u0001'", "'\\u0002'", "'\\u0003'", "'\\u0004'", "'\\u0005'", 
		"'\\u0006'", "'\\u0007'", "'\b'", "'\t'", "'\n'", "'\\u000B'", "'\f'", 
		"'\r'", "'\\u000E'", "'\\u000F'", "'\\u0010'", "'\\u0011'", "'\\u0012'", 
		"'\\u0013'", "'\\u0014'", "'\\u0015'", "'\\u0016'"
	};
	public static final String[] ruleNames = {
		"BEGIN", "END", "EXIT", "ASSIGN", "MUL", "DIV", "MOD", "ADD", "SUB", "GT", 
		"GE", "LT", "LE", "EQUAL", "NOTEQUAL", "AND", "OR", "OPEN_PARENTHESES", 
		"CLOSE_PARENTHESES", "DIGIT", "INTEGER", "WS", "LINE_COMMENT"
	};


	public WACCLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "WACCLexer.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\30}\b\1\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\3\2\3\2\3"+
		"\2\3\2\3\2\3\2\3\3\3\3\3\3\3\3\3\4\3\4\3\4\3\4\3\4\3\5\3\5\3\6\3\6\3\7"+
		"\3\7\3\b\3\b\3\t\3\t\3\n\3\n\3\13\3\13\3\f\3\f\3\f\3\r\3\r\3\16\3\16\3"+
		"\16\3\17\3\17\3\17\3\20\3\20\3\20\3\21\3\21\3\21\3\22\3\22\3\22\3\23\3"+
		"\23\3\24\3\24\3\25\3\25\3\26\6\26j\n\26\r\26\16\26k\3\27\6\27o\n\27\r"+
		"\27\16\27p\3\27\3\27\3\30\3\30\7\30w\n\30\f\30\16\30z\13\30\3\30\3\30"+
		"\2\2\31\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17"+
		"\35\20\37\21!\22#\23%\24\'\25)\2+\26-\27/\30\3\2\4\5\2\13\f\16\17\"\""+
		"\4\2\f\f\17\17~\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3"+
		"\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2"+
		"\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3"+
		"\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2"+
		"\2\2\3\61\3\2\2\2\5\67\3\2\2\2\7;\3\2\2\2\t@\3\2\2\2\13B\3\2\2\2\rD\3"+
		"\2\2\2\17F\3\2\2\2\21H\3\2\2\2\23J\3\2\2\2\25L\3\2\2\2\27N\3\2\2\2\31"+
		"Q\3\2\2\2\33S\3\2\2\2\35V\3\2\2\2\37Y\3\2\2\2!\\\3\2\2\2#_\3\2\2\2%b\3"+
		"\2\2\2\'d\3\2\2\2)f\3\2\2\2+i\3\2\2\2-n\3\2\2\2/t\3\2\2\2\61\62\7d\2\2"+
		"\62\63\7g\2\2\63\64\7i\2\2\64\65\7k\2\2\65\66\7p\2\2\66\4\3\2\2\2\678"+
		"\7g\2\289\7p\2\29:\7f\2\2:\6\3\2\2\2;<\7g\2\2<=\7z\2\2=>\7k\2\2>?\7v\2"+
		"\2?\b\3\2\2\2@A\7?\2\2A\n\3\2\2\2BC\7,\2\2C\f\3\2\2\2DE\7\61\2\2E\16\3"+
		"\2\2\2FG\7\'\2\2G\20\3\2\2\2HI\7-\2\2I\22\3\2\2\2JK\7/\2\2K\24\3\2\2\2"+
		"LM\7@\2\2M\26\3\2\2\2NO\7@\2\2OP\7?\2\2P\30\3\2\2\2QR\7>\2\2R\32\3\2\2"+
		"\2ST\7>\2\2TU\7?\2\2U\34\3\2\2\2VW\7?\2\2WX\7?\2\2X\36\3\2\2\2YZ\7#\2"+
		"\2Z[\7?\2\2[ \3\2\2\2\\]\7(\2\2]^\7(\2\2^\"\3\2\2\2_`\7~\2\2`a\7~\2\2"+
		"a$\3\2\2\2bc\7*\2\2c&\3\2\2\2de\7+\2\2e(\3\2\2\2fg\4\62;\2g*\3\2\2\2h"+
		"j\5)\25\2ih\3\2\2\2jk\3\2\2\2ki\3\2\2\2kl\3\2\2\2l,\3\2\2\2mo\t\2\2\2"+
		"nm\3\2\2\2op\3\2\2\2pn\3\2\2\2pq\3\2\2\2qr\3\2\2\2rs\b\27\2\2s.\3\2\2"+
		"\2tx\7%\2\2uw\n\3\2\2vu\3\2\2\2wz\3\2\2\2xv\3\2\2\2xy\3\2\2\2y{\3\2\2"+
		"\2zx\3\2\2\2{|\b\30\2\2|\60\3\2\2\2\6\2kpx\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}