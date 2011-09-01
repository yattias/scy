/*  $Id$
 *  
 *  File	first_n.pl
 *  Part of	List predicates
 *  Author	Anjo Anjewierden, a.a.anjewierden@utwente.nl
 *        	Anjo Anjewierden, anjo@science.uva.nl
 *  Purpose	Select first N elements of a list
 *  Works with	SWI-Prolog (www.swi-prolog.org)
 *  
 *  Notice	Copyright (c) 2007-2011  University of Twente
 *		Copyright (c) 2001-2006  University of Amsterdam
 *  
 *  History	19/05/01  (Created)
 *  		20/02/11  (Last modified)
 */ 

/*------------------------------------------------------------
 *  Directives
 *------------------------------------------------------------*/

:- module(first_n_list,
	 [ first_n_list/3,		% List x N -> OutList
	   first_n_list/4		% List x N -> OutList x Rest
	 ]).

:- use_module(load).
	   

/*------------------------------------------------------------
 *  First N elements
 *------------------------------------------------------------*/

%%	first_n_list(+List:list, +N:int, -First:list) is semidet.
%
%	Succeeds when First are the first N elements of List.
%	When List has less than N elements, also unifies List and First.

first_n_list(List, N, First) :-
	first_n(N, List, First).

first_n(N, [H|T], [H|More]) :-
	N > 0, !,
	Next is N-1,
	first_n(Next, T, More).
first_n(_, _, []).

%%	first_n_list(+List:list, +N:int, -First:list, -Rest:list) is semidet.
%
%	As first_n_list/3, but also returns the Rest of List.

first_n_list(List, N, First, Rest) :-
	first_n(N, List, First, Rest).

first_n(N, [H|T], [H|More], Rest) :-
	N > 0, !,
	Next is N-1,
	first_n(Next, T, More, Rest).
first_n(_, Rest, [], Rest).
