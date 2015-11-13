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

expr: INTLITERAL            #integerliteral
    | BOOLEANLITERAL        #booleanliteral
    | CHARLITERAL           #charliteral
    | STRINGLITERAL         #stringliteral
    | PAIRLITERAL           #pairliteral
    | IDENT                 #identifier
    | arrayElem             #arrayelement
    | UNARYOP expr          #unaryoperator
    | expr BINARYOP expr    #binaryoperator
    | LPAREN expr RPAREN    #brackets
    ;

arrayElem : IDENT (LBRACK expr RBRACK)+;

program: (BEGIN (func)* (stat) END)* EOF;

func: type IDENT LPAREN param_list? RPAREN IS stat END;

param_list: param (COMMA param)*;

param: type IDENT;

stat: SKIP                              #skipstatement
    | type IDENT ASSIGN assignRHS       #varinit
    | assignLHS ASSIGN assignRHS        #assignment
    | READ assignLHS                    #readstatement
    | FREE expr                         #freestatement
    | RETURN expr                       #returnstatement
    | EXIT expr                         #exitstatement
    | PRINT expr                        #printstatement
    | PRINTLN expr                      #printlnstatement
    | IF expr THEN stat ELSE stat FI    #ifstatement
    | WHILE expr DO stat DONE           #whilestatement
    | BEGIN stat END                    #beginendstatement
    | stat SEMI stat                    #statementblock
    ;

assignLHS   : IDENT		#assignlhsident
            | arrayElem		#assignlhsarrayelem		
            | pairElem		#assignlhspairelem
            ;

assignRHS   : expr                                  #assignrhsexpr
            | arrayLiter                            #assignrhsarraylit
            | NEWPAIR LPAREN expr COMMA expr RPAREN #assignrhsnewpair
            | pairElem                              #assignrhspairelem
            | CALL IDENT LPAREN arg_list? RPAREN    #assignrhscall
            ;

arg_list: expr (COMMA expr)*;

pairElem: FST expr
        | SND expr
        ;

arrayLiter: LBRACK (expr (COMMA expr)*)? RBRACK;
