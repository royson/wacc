parser grammar WACCParser;

options {
  tokenVocab=WACCLexer;
}

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

program: (BEGIN (func)* (stat) END)? EOF;

func: type IDENT LPAREN param_list? RPAREN IS funcStat END;
funcStat: (stat SEMI)* 
(RETURN expr | EXIT expr | IF expr THEN funcStat ELSE funcStat FI);

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

assignLHS   : IDENT
            | arrayElem
            | pairElem
            ;

assignRHS   : expr
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
