lexer grammar WACCLexer;

// Keywords
BEGIN           : 'begin';
CALL            : 'call';
END             : 'end';
EXIT            : 'exit';
FREE            : 'free';
IS              : 'is';
PRINT           : 'print';
PRINTLN         : 'println';
READ            : 'read';
RETURN          : 'return';
SKIP            : 'skip';

IF              : 'if';
FI              : 'fi';
THEN            : 'then';
ELSE            : 'else';

WHILE           : 'while';
DO              : 'do';
DONE            : 'done';

// Base types
BASETYPE        : 'int' | 'bool' | 'char' | 'string';

// Pair types
PAIR            : 'pair';
NEWPAIR         : 'newpair';
PAIRELEM        : 'fst' | 'snd';

// Operators
ASSIGN       : '=';
UNARYOP      : '!' | '-' | 'len' | 'ord' | 'chr';
BINARYOP     : '*' | '/' | '%' | '+' | '-' | '>' | '>=' | '<' | '<=' | '==' | '!=' | '&&' | '||';

// Separators
LPAREN          : '(';
RPAREN          : ')';
LBRACE          : '{';
RBRACE          : '}';
LBRACK          : '[';
RBRACK          : ']';
SEMI            : ';';
COMMA           : ',';
DOT             : '.';

// Literals
BooleanLiteral  : 'true' | 'false';

StringLiteral   :   '"'StringCharacters?'"' | '\''StringCharacters?'\'';
fragment StringCharacters
                :   StringCharacter+;
fragment StringCharacter
                :   ~["\\] | EscapeSequence;

// §3.10.6 Escape Sequences for Character and String Literals
fragment
EscapeSequence
    :   '\\' [0btnfr"'\\]
    ;

PairLiteral     : 'null';

// Numbers
fragment DIGIT  : '0'..'9'; 
INTEGER         : DIGIT+;

// Identifier
IDENT           : [_a-zA-Z][_a-zA-Z0-9]*;

// Comments and whitespace
WS              : [ \t\r\n\u000C]+ -> skip;
LINE_COMMENT    : '#' ~[\r\n]* -> skip;





