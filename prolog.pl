% 性別
male(andy). male(bob). male(cecil). male(dennis). male(edward). male(felix).
male(martin). male(oscar). male(quinn).

female(gigi). female(helen). female(iris). female(jane). female(kate).
female(liz). female(nancy). female(pattie). female(rebecca).

% 婚姻事實
married(bob, helen). married(helen, bob).
married(dennis, pattie). married(pattie, dennis).
married(gigi, martin). married(martin, gigi).

% 父母事實
parent_fact(andy, bob).
parent_fact(bob, cecil).
parent_fact(cecil, dennis).
parent_fact(dennis, edward).
parent_fact(edward, felix).

parent_fact(gigi, helen).
parent_fact(helen, iris).
parent_fact(iris, jane).
parent_fact(jane, kate).
parent_fact(kate, liz).

parent_fact(martin, nancy).
parent_fact(nancy, oscar).
parent_fact(oscar, pattie).
parent_fact(pattie, quinn).
parent_fact(quinn, rebecca).

% Relation #1: 父母關係推論
parent(X, Y) :- parent_fact(X, Y).
parent(Y, Z) :- married(X, Y), parent_fact(X, Z).

% Relation #2
sibling(Y, Z) :- parent(X, Y), parent(X, Z), Y \= Z.

% Relation #3
brother(X, Y) :- sibling(X, Y), male(X), male(Y).

% Relation #4
sister(X, Y) :- sibling(X, Y), female(X), female(Y).

% Relation #5
cousin(Y, Z) :-
    parent(W, Y),
    parent(X, Z),
    sibling(W, X),
    Y \= Z.

% relation 判斷
relation(X, Y, Relation) :-
    (brother(X, Y) -> Relation = 'brothers';
     sister(X, Y) -> Relation = 'sisters';
     sibling(X, Y) -> Relation = 'siblings';
     cousin(X, Y) -> Relation = 'cousins';
     parent(X, Y) -> Relation = 'parent';
     parent(Y, X) -> Relation = 'child';
     married(X, Y) -> Relation = 'married';
     Relation = 'no direct relation').

check_relation(X, Y) :-
    relation(X, Y, R),
    format('~w and ~w are ~w.~n', [X, Y, R]).

main :-
    check_relation(bob, helen),
    check_relation(cecil, dennis),
    check_relation(kate, liz),
    check_relation(dennis, pattie),
    check_relation(andy, bob),
    check_relation(pattie, quinn),
    halt.

:- main.
