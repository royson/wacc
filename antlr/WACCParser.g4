parser grammar WACCParser;

options {
  tokenVocab=WACCLexer;
}

binaryOper : BINARYOP;

// Need to resolve the left recursive rule problem
// type              : BASETYPE | arraytype | pairtype;
// arraytype         : type '['']';
// pairtype          : PAIR '(' pairelementype ',' pairelementype ')';
// pairelementype    : BASETYPE | arraytype | PAIR;

expr: expr binaryOper expr
| INTEGER
| LPAREN expr RPAREN
;

// EOF indicates that the program must consume to the end of the input.
prog: (expr)*  EOF ;
