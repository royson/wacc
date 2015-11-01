lexer grammar WACCLexer;

// Keywords
BEGIN: 'begin';
END: 'end';
EXIT: 'exit';

//operators
ASSIGN          : '=';
MUL             : '*';
DIV             : '/';
MOD             : '%';
ADD             : '+';
SUB             : '-';
GT              : '>';
GE              : '>=';
LT              : '<';
LE              : '<=';
EQUAL           : '==';
NOTEQUAL        : '!=';
AND             : '&&';
OR              : '||';

//brackets
OPEN_PARENTHESES : '(' ;
CLOSE_PARENTHESES : ')' ;

//numbers
fragment DIGIT : '0'..'9' ; 
INTEGER: DIGIT+ ;

//comments and whitespace
WS  :  [ \t\r\n\u000C]+ -> skip ;


LINE_COMMENT :   '#' ~[\r\n]* -> skip;





