/*  $Id$
 *  
 *  File	edit_distance.pl
 *  Part of	Prolog library
 *  Author	Anjo Anjewierden, a.a.anjewierden@utwente.nl
 *  Purpose	Computes the edit distance of two strings
 *  Works with	SWI-Prolog (www.swi-prolog.org)
 *  
 *  Notice	Copyright (c) 2007  University of Twente
 *  
 *  History	03/04/07  (Created)
 *  		03/04/07  (Last modified)
 */ 

/*------------------------------------------------------------
 *  Directives
 *------------------------------------------------------------*/

:- module(edit_distance_atom,
	  [ edit_distance/3,		% Atom x Atom -> Distance
	    edit_distance_pl/3,		% Atom x Atom -> Distance
	    damerau_edit_distance/3	% Atom x Atom -> Distance
	  ]).

:- use_module(load).


%%	edit_distance(+Str1:atom, +Str2:atom, -Distance:int) is det.
%
%	Succeeds if the edit distance between Str1 and Str2 equals Distance.
%	The edit distance is the Levenshtein edit distance and is the minimum
%	number of deletions, insertions and substitutions necessary to make
%	Str1 and Str2 equal.  See http://en.wikipedia.org/Levenshtein_distance
%	for details.
%
%	edit_distance/3 is similar to dwim_match/2 from the SWI-Prolog
%	library.  The latter only succeeds when the edit distance is 0 or 1.
%	Unfortunately dwim_match/2 fails on very short strings.


%%	damerau_edit_distance(+Str1:atom, +Str2:atom, -Distance:int) is det.
%
%	Succeeds if the edit distance between Str1 and Str2 equals Distance.
%	The edit distance is the Damerau-Levenshtein distance and is the
%	minimum number of deletions, insertions, substitutions and
%	transpositions necessary to make Str1 and Str2 equal.  See
%	http://en.wikipedia.org/Damerau-Levenshtein_distance for details.




:- dynamic
	d/3.

	
set_d(I, J, Value) :-
	(   retract(d(I,J,_))
	->  assert(d(I,J,Value))
	;   assert(d(I,J,Value))
	), !.
set_d(I, J, Value) :-
	format('set_d(~w, ~w, ~w)~n', [I,J,Value]).


init_d(L1, L2) :-
	retractall(d(_,_,_)),
	init_d(0,L1, L2).

init_d(I,Max, _) :-
	I > Max, !.
init_d(I,Max, L2) :-
	init_d2(0,L2, I),
	Next is I + 1,
	init_d(Next,Max, L2).

init_d2(J,Max, _) :-
	J > Max, !.
init_d2(J,Max, I) :-
	(   J == 0
	->  assert(d(I,0,I))
	;   (   I == 0
	    ->  assert(d(0,J,J))
	    ;   assert(d(I,J,0))
	    )
	),
	Next is J + 1,
	init_d2(Next,Max, I).


edit_distance_pl(A1, A2, Dist) :-
	atom_length(A1, L1),
	atom_length(A2, L2),
	init_d(L1, L2),
	atom_codes(A1, C1),
	atom_codes(A2, C2),
	T1 =.. [c|C1],
	T2 =.. [c|C2],

	iterate(L2, L1, T1, T2),
	d(L1, L2, Dist).
	       

iterate(L2, L1, A1,A2) :-
	iterate(1,L2, L1, A1,A2).

iterate(J,L2, _, _,_) :-
	J > L2, !.
iterate(J,L2, L1, A1,A2) :-
	iterate2(1,L1, J, A1,A2),
	Next is J + 1,
	iterate(Next,L2, L1, A1,A2).

iterate2(I,L1, _, _,_) :-
	I > L1, !.
iterate2(I,L1, J, C1,C2) :-
	I1 is I - 1,
	J1 is J - 1,
	(   arg(I, C1, Code),
	    arg(J, C2, Code)
	->  d(I1, J1, Prev),
	    set_d(I, J, Prev)
	;   d(I1, J, V1),
	    d(I, J1, V2),
	    d(I1, J1, V3),
	    V is min(V1, min(V2,V3)) + 1,
	    set_d(I, J, V)
	),
	Next is I + 1,
	iterate2(Next,L1, J, C1,C2).
	



/*------------------------------------------------------------
 *  Load the library
 *------------------------------------------------------------*/

:-
	use_foreign_library(bin(edit_distance)).
