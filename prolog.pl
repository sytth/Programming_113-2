% 性別
male(andy). male(bob). male(cecil). male(dennis). male(edward). male(felix).
male(martin). male(oscar). male(quinn).
female(gigi). female(helen). female(iris). female(jane).
female(kate). female(liz). female(nancy). female(pattie). female(rebecca).

% 婚姻
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

% Relation #2: 兄弟姊妹
sibling(Y, Z) :- parent(X, Y), parent(X, Z), Y \= Z.

% Relation #3: 兄弟
brother(X, Y) :- sibling(X, Y), male(X), male(Y).

% Relation #4: 姊妹
sister(X, Y) :- sibling(X, Y), female(X), female(Y).

% Relation #5: 表親
cousin(Y, Z) :-
    parent(W, Y),
    parent(X, Z),
    sibling(W, X),
    Y \= Z.

% 關係判定
relation(brother, X, Y) :- brother(X, Y).
relation(sister, X, Y) :- sister(X, Y).
relation(sibling, X, Y) :- sibling(X, Y).
relation(cousin, X, Y) :- cousin(X, Y).
relation(parent, X, Y) :- parent(X, Y).
relation(child, X, Y) :- parent(Y, X).
relation(married, X, Y) :- married(X, Y).

% check 是否為該關係
check_relation(X, Y, Relation) :-
    relation(Relation, X, Y),
    format("Yes, ~w and ~w are ~w.~n", [X, Y, Relation]).

check_relation(X, Y, Relation) :-
    \+ relation(Relation, X, Y),
    format("No, ~w and ~w are not ~w.~n", [X, Y, Relation]).

main :-
    check_relation(bob, helen, married),
    check_relation(andy, bob, parent),
    check_relation(bob, andy, child),
    check_relation(cecil, dennis, brother),
    check_relation(iris, jane, sister),
    check_relation(liz, rebecca, cousin),
    check_relation(pattie, quinn, parent),
    check_relation(martin, iris, cousin),
    check_relation(gigi, liz, parent),
    halt.

:- main.
