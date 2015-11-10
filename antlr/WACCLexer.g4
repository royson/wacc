lexer grammar WACCLexer;

// Program
BEGIN           : 'begin';
END             : 'end';

// Function
IS              : 'is';

// Statement
SKIP            : 'skip';
ASSIGN          : '=';
READ            : 'read';
FREE            : 'free';
RETURN          : 'return';
EXIT            : 'exit';
PRINT           : 'print';
PRINTLN         : 'println';

IF              : 'if';
FI              : 'fi';
THEN            : 'then';
ELSE            : 'else';

WHILE           : 'while';
DO              : 'do';
DONE            : 'done';

// Assign RHS
NEWPAIR         : 'newpair';
CALL            : 'call';

// Pair elem
FST             : 'fst';
SND             : 'snd';

// Base type
BASETYPE        : 'int' | 'bool' | 'char' | 'string';

// Pair type
PAIR            : 'pair';

// Operators
UNARYOP         : '!' | '-' | 'len' | 'ord' | 'chr';
BINARYOP        : '*' | '/' | '%' | '+' | '-' | '>' | '>=' | '<' | '<=' | '==' | '!=' | '&&' | '||';

// Identifier
IDENT           : [_a-zA-Z][_a-zA-Z0-9]*;

// Int literal
INTLITERAL      : INTSIGN? DIGIT+;
fragment DIGIT  : ('0'..'9');
fragment INTSIGN
                : '+' | '‐';

// Boolean literal
BOOLEANLITERAL  : 'true' | 'false';

// Char literal
CHARLITERAL     : '\''STRINGCHARACTER'\'';

// String literal 
STRINGLITERAL   : '"' STRINGCHARACTERS? '"';

// Support for string literal
fragment STRINGCHARACTERS
                : STRINGCHARACTER+;
fragment STRINGCHARACTER
                : ~["\\] | ESCAPESEQUENCE;
fragment ESCAPESEQUENCE 
                : '\\' [0btnfr"'\\];

// Pair literal
PAIRLITERAL     : 'null';

// Separators
LPAREN          : '(';
RPAREN          : ')';
LBRACE          : '{';
RBRACE          : '}';
LBRACK          : '[';
RBRACK          : ']';
SEMI            : ';';
COMMA           : ',';

// Comments and whitespace
WS              : [ \t\r\n\u000C]+ -> skip;
LINE_COMMENT    : '#' ~[\r\n]* -> skip;
