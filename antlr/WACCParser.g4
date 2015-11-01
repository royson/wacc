parser grammar WACCParser;

options {
  tokenVocab=WACCLexer;
}

arrayElem : IDENT (LBRACK expr RBRACK)+;

// Need to resolve the left recursive rule problem
// type              : BASETYPE | arraytype | pairtype;
// arraytype         : type '['']';
// pairtype          : PAIR '(' pairelementype ',' pairelementype ')';
// pairelementype    : BASETYPE | arraytype | PAIR;

//program: BEGIN (func)* (stat) END

//func: type IDENT LPAREN param_list RPAREN IS stat END

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

// EOF indicates that the program must consume to the end of the input.
prog: (expr)*  EOF ;
