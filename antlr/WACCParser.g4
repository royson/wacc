parser grammar WACCParser;

options {
  tokenVocab=WACCLexer;
}

// Need to resolve the left recursive rule problem
// type            : BASETYPE | arraytype | pairtype;
 type              : BASETYPE (LBRACK RBRACK)* | pairtype (LBRACK RBRACK)*;
 arraytype         : type LBRACK RBRACK;
 pairtype          : PAIR LPAREN pairelementype COMMA pairelementype RPAREN;
 pairelementype    : BASETYPE | arraytype | PAIR;
 
 expr: INTLITERAL
| BOOLEANLITERAL
| CHARLITERAL
| STRINGLITERAL
| PAIRLITERAL
| IDENT
| arrayElem
| UNARYOP expr
| expr BINARYOP expr
| LPAREN expr RPAREN
;

arrayElem : IDENT (LBRACK expr RBRACK)+;

program: BEGIN (func)* (stat) END;

func: type IDENT LPAREN param_list? RPAREN IS stat END;

param_list: param (COMMA param)*;

param: type IDENT;

stat: SKIP
| type IDENT ASSIGN assignRHS
| assignLHS ASSIGN assignRHS
| READ assignLHS
| FREE expr
| RETURN expr
| EXIT expr
| PRINT expr
| PRINTLN expr
| IF expr THEN stat ELSE stat FI
| WHILE expr DO stat DONE
| BEGIN stat END
| stat SEMI stat
;

assignLHS: IDENT
| arrayElem
| pairElem
;

assignRHS: expr
| arrayLiter
| NEWPAIR LPAREN expr COMMA expr RPAREN
| pairElem
| CALL IDENT LPAREN arg_list? RPAREN
;

arg_list: expr (COMMA expr)*;

pairElem: FST expr
| SND expr
;

arrayLiter: LBRACK (expr (COMMA expr)*)? RBRACK;


// EOF indicates that the program must consume to the end of the input.
prog: (expr)*  EOF ;