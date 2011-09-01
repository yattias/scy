/*  $Id$
 *  
 *  File	list.pl
 *  Part of	SWI-Prolog library
 *  Author	Anjo Anjewierden, a.a.anjewierden@utwente.nl
 *        	Anjo Anjewierden, anjo@science.uva.nl
 *  Purpose	List utility predicates
 *  Works with	SWI-Prolog (www.swi-prolog.org)
 *  
 *  Notice	Copyright (c) 2007-2011  University of Twente
 *		Copyright (c) 2001-2006  University of Amsterdam
 *  
 *  History	19/05/01  (Created)
 *  		01/02/11  (Last modified)
 */ 

/*------------------------------------------------------------
 *  Directives
 *------------------------------------------------------------*/

:- module(list_utilities,
	 [ from_element_n/3,		% N x List -> OutList
	   first/2,			% List -> Element
	   comma_separated_words/2,	% Words -> Atom
	   average_list/2,		% List -> Average
	   quadratic_sum_list/2,	% List -> Sum
	   sum_list_arg/3,		% TermList x Arg -> Sum
	   select_random_list/3,	% List x N -> Selection
	   select_random_list/4,	% List x N -> Selection x Rest

	   scale_list/3,		% List x Unit -> NewList
	   split_list/4,		% List x Element -> Before x After

	   permutation/2,		% List -> Permutation

	   select_range_list/4,		% List x From x To -> Range

	   delete_unify/3,		% List x Pattern x Rest
	   sub_list_list/2,		% SubList x List
	   sub_list_list/3,		% SubList x List x Rest
	   sub_nth1_list/4,		% List x N1 x N2 -> SubList
	   slice_list/4,		% List x From x To -> List
	   head_list/2,			% Head x List
	   head_list/3,			% Head x List -> Matching
	   head_list/4,			% Head x List -> Matching x Rest

	   delete_last/2,		% List -> List
	   delete_range_list/4,		% List -> List x X x Y
	   delete_successive_duplicates/2% List x List
	 ]).

:- use_module(load).

:- use_module(library(lists), [nth0/3, nth1/3, sumlist/2]).
:- use_module(library(random), [random/3]).

:- use_module(list(first_n), [first_n_list/3]).



/**	<module> List predicates

A collection of predicates related to lists.

@author	  Anjo Anjewierden
*/


first([X|_], X).


/*------------------------------------------------------------
 *  List to comma separated atom
 *------------------------------------------------------------*/

comma_separated_words([], '').
comma_separated_words([C], C) :- !.
comma_separated_words([C|Cs], Keywords) :-
	comma_separated_words(Cs, Ks),
	atomic_list_concat([C, ', ', Ks], Keywords).


/*------------------------------------------------------------
 *  Sum list
 *------------------------------------------------------------*/

average_list([], 0) :- !.
average_list(List, Average) :-
	length(List, Len),
	sumlist(List, Sum),
	Average is Sum/Len.

quadratic_sum_list(List, Sum) :-
	quadratic_sum_list(List, 0, Sum).

quadratic_sum_list([], Sum, Sum).
quadratic_sum_list([A|As], Sum0, Sum) :-
	Sum1 is Sum0+A*A,
	quadratic_sum_list(As, Sum1, Sum).


sum_list_arg(List, Arg, Sum) :-
	sum_list_arg(List, Arg, 0, Sum).

sum_list_arg([], _, Sum, Sum).
sum_list_arg([Term|Terms], Arg, Sum0, Sum) :-
	arg(Arg, Term, N),
	Sum1 is Sum0+N,
	sum_list_arg(Terms, Arg, Sum1, Sum).


/*------------------------------------------------------------
 *  Select n random elements from a list
 *------------------------------------------------------------*/

%%	select_random_list(+List:list, +N:int, -Selection:list) is semidet.
%%	select_random_list(+List:list, +N:int, -Selection:list, -Rest:list) is semidet.
%
%	Selection is a randomly selected list of N elements from List.
%	select_random_list/4 returns the non-selected elements in Rest.

select_random_list(List, N, Selection) :-
	select_random_list2(N, List, Selection, _).

select_random_list(List, N, Selection, Rest) :-
	select_random_list2(N, List, Selection, Rest).

select_random_list2(0, Rest, [], Rest) :- !.
select_random_list2(N, List, [E|More], Rest) :-
	length(List, Len),
	Len >= N,
	random(0, Len, Nth),
	nth0(Nth, List, E),
	delete_nth0(0,Nth, List, NewList),
	Next is N-1,
	select_random_list2(Next, NewList, More, Rest).

delete_nth0(N,N, [_|Rest], Rest) :- !.
delete_nth0(N,Nth, [H|T], [H|Rest]) :-
	Next is N + 1,
	delete_nth0(Next,Nth, T, Rest).


/*------------------------------------------------------------
 *  Scale list
 *------------------------------------------------------------*/

scale_list(In, 1, In) :- !.
scale_list(In, 1.0, In) :- !.
scale_list(In, Scale, Out) :-
	scale_list2(In, Scale, Out).

scale_list2([], _, []).
scale_list2([X|Xs], Scale, [N|Ns]) :-
	N is X / Scale,
	scale_list2(Xs, Scale, Ns).


/*------------------------------------------------------------
 *  Split list
 *------------------------------------------------------------*/

split_list([], _, [], []).
split_list([H|T], H, [], T) :- !.
split_list([H|T], Target, [H|Before], After) :-
	split_list(T, Target, Before, After).


/*------------------------------------------------------------
 *  Permutation (O'Keefe, p. 38)
 *------------------------------------------------------------*/

permutation(Xs, Ys) :-
	permutation(Xs, Ys, Ys).

permutation([], [], []).
permutation([X|Xs], Ys1, [_|Bound]) :-
	permutation(Xs, Ys, Bound),
	insert(Ys, X, Ys1).

insert(L, X, [X|L]).
insert([H|T], X, [H|L]) :-
	insert(T, X, L).


/*------------------------------------------------------------
 *  Extract a range from a list
 *------------------------------------------------------------*/

select_range_list(List, From, To, SubList) :-
	skip_to_nth(List, From, Skipped),
	(   To < From
	->  SubList = Skipped
	;   N is To-From+1,
	    length(Skipped, Len),
	    (   N > Len
	    ->  SubList = Skipped
	    ;   first_n_list(Skipped, N, SubList)
	    )
	).

skip_to_nth(List, N, List) :-
	N < 1, !.
skip_to_nth([_|T], N0, List) :-
	N is N0-1,
	skip_to_nth(T, N, List).


/*------------------------------------------------------------
 *  Find a sub-list inside a list
 *------------------------------------------------------------*/

sub_list_list([H|T], [H|Rest]) :-
	sub_list_rest(T, Rest).
sub_list_list(Sub, [_|Rest]) :-
	sub_list_list(Sub, Rest).

sub_list_rest([], _).
sub_list_rest([H|T], [H|Rest]) :-
	sub_list_rest(T, Rest).


sub_list_list([H|T], [H|Rest], More) :-
	sub_list_rest(T, Rest, More).
sub_list_list(Sub, [_|Rest], More) :-
	sub_list_list(Sub, Rest, More).

sub_list_rest([], More, More).
sub_list_rest([H|T], [H|Rest], More) :-
	sub_list_rest(T, Rest, More).


/*------------------------------------------------------------
 *  Sub-list based on nth1
 *------------------------------------------------------------*/

sub_nth1_list(List, N, N, [Value]) :- !,
	nth1(N, List, Value).
sub_nth1_list(List, N1, N2, SubList) :-
	N1 >= 1,
	N2 >= N1,
	sub_nth1_list2(List, 1, N1, N2, SubList).

sub_nth1_list2([H|T], N, N, N2, [H|Rest]) :- !,
	succ(N, Next),
	sub_nth1_list_rest(T, Next, N2, Rest).
sub_nth1_list2([_|T], N, N1, N2, SubList) :-
	succ(N, Next),
	sub_nth1_list2(T, Next, N1, N2, SubList).


sub_nth1_list_rest([H|_], N, N, [H]) :- !.
sub_nth1_list_rest([H|T], N, N2, [H|Rest]) :-
	succ(N, Next),
	sub_nth1_list_rest(T, Next, N2, Rest).



/*------------------------------------------------------------
 *  Slice a list (select a range of consecutive elements)
 *------------------------------------------------------------*/

slice_list([H|T], H, To, [H|Result]) :-
	slice_list2(T, To, Result).
slice_list([_|T], From, To, Result) :-
	slice_list(T, From, To, Result).

slice_list2([H|_], H, [H]).
slice_list2([H|T], To, [H|Rest]) :-
	slice_list2(T, To, Rest).


/*------------------------------------------------------------
 *  One list is the head of another
 *------------------------------------------------------------*/

head_list([], _).
head_list([H|T], [H|More]) :-
	head_list(T, More).


head_list([], _, []).
head_list([H1|T], [H2|More], [H2|Rest]) :-
	\+ \+ H1 = H2,
	head_list(T, More, Rest).


head_list([], Post, [], Post).
head_list([H1|T], [H2|More], [H2|Rest], Post) :-
	\+ \+ H1 = H2,
	head_list(T, More, Rest, Post).


/*------------------------------------------------------------
 *  Delete all unifying elements from a list
 *------------------------------------------------------------*/

delete_unify([], _, []).
delete_unify([H|T], E, Rest) :-
	\+ \+ H = E, !,
        delete_unify(T, E, Rest).
delete_unify([H|T], E, [H|Rest]) :-
        delete_unify(T, E, Rest).


/*------------------------------------------------------------
 *  Delete last element of a list
 *------------------------------------------------------------*/

delete_last([_], []) :- !.
delete_last([H|T], [H|Rest]) :-
	delete_last(T, Rest).


from_element_n(1, List, List) :- !.
from_element_n(N, [_|T], Rest) :-
	Next is N-1,
	from_element_n(Next, T, Rest).


%%	delete_range_list(+In:list, -Out:list, +From:int, +In:int) is semidet.
%
%	Deletes the range of elements From through To (nth1) from In, returning Out. 

delete_range_list(In, Out, X,Y) :-
	Y >= X,
	delete_range_list(In, Out, 1,X,Y).

delete_range_list([], [], _, _,_).
delete_range_list([_|T], T2, N, N,Y) :- !,
	Next is N+1,
	delete_range_list2(T, T2, Next, Y).
delete_range_list([H|T], [H|T2], N, X,Y) :-
	Next is N+1,
	delete_range_list(T, T2, Next, X,Y).

delete_range_list2(Rest, Rest, N, Y) :-
	N > Y, !.
delete_range_list2([_|T], T2, N, Y) :-
	Next is N+1,
	delete_range_list2(T, T2, Next, Y).


%%	delete_successive_duplicates(+In:list, -Out:list) is det.
%
%	Removes successive duplicate elements.

delete_successive_duplicates([], []).
delete_successive_duplicates([H,H|T], Result) :- !,
	delete_successive_duplicates([H|T], Result).
delete_successive_duplicates([H|T], [H|Rest]) :-
	delete_successive_duplicates(T, Rest).

	
