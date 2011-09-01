/*  $Id$
 *
 *  File	compound_terms.pl
 *  Part of	SWI-Prolog library
 *  Author	Anjo Anjewierden, a.a.anjewierden@utwente.nl
 *  Purpose	Sorting utilities
 *  Works with	SWI-Prolog (www.swi-prolog.org)
 *  Status	check, clean, pldoc, public    
 *
 *  Notice	Copyright (c) 2007-2011  University of Twente
 *              Copyright (c) 2000-2005  University of Amsterdam
 *
 *  History	06/10/00  (Created)
 *  		19/02/11  (Last modified)
 */

/*------------------------------------------------------------
 *  Directives
 *------------------------------------------------------------*/

:- module(compound_terms_sort,
	  [ compound_terms_sort/3	% List -> Sorted x Options
	  ]).

:- use_module(load).

:- use_module(library(option), [option/3]).
:- use_module(library(lists), [reverse/2]).


%%	compound_terms_sort(+List:list, -Sorted:list, +Options:list) is det.
%
%	Sorted is the result of sorting the List of (compound) terms according
%	to Options.  Duplicates are not removed.  Options are:
%
%	* arg(Key)
%	Terms are sorted by the Key'th argument.  Default is 0.
%
%	* order(Order)
%	Order is the sort order, one of =ascending= (default), =descending= or
%	=lexicographical=.  In the later case collation_key/2 is used to
%	create the sort key.
%
%	* arg2((Key)
%	Secondary argument for sorting.
%
%	* order2(Order2)
%	Secondary order.

compound_terms_sort(List, Sorted, Options) :-
	option(arg(Arg), Options, 0),
	option(order(Order), Options, ascending),
	option(arg2(Arg2), Options, 0),
	option(order2(Order2), Options, ascending),
	(   Arg == 0
	->  (   Order == ascending
	    ->  msort(List, Sorted)
	    ;   Order == descending
	    ->  msort(List, Tmp),
	        reverse(Tmp, Sorted)
	    )
	;   Arg2 == 0
	->  (   Order == lexicographical
	    ->  terms_to_collation_keys(List, Arg, Keys),
	        keysort(Keys, SortedKeys),
	        keys_to_terms(SortedKeys, Sorted)
	    ;   terms_to_keys(List, Arg, Keys),
	  	keysort(Keys, SortedKeys),
	        (   Order == ascending
		->  keys_to_terms(SortedKeys, Sorted)
		;   keys_to_terms(SortedKeys, Tmp),
		    reverse(Tmp, Sorted)
		)
	    )
	;   terms_to_keys(List, Arg, Order, Arg2, Order2, Keys),
	    keysort(Keys, SortedKeys),
	    (   Order == ascending
	    ->  keys_to_terms(SortedKeys, Sorted)
	    ;   keys_to_terms(SortedKeys, Tmp),
	        reverse(Tmp, Sorted)
	    )
	).


terms_to_collation_keys([], _, []).
terms_to_collation_keys([Term|Terms], Arg, [Key-Term|KTs]) :-
	arg(Arg, Term, Atom),
	collation_key(Atom, Key),
	terms_to_collation_keys(Terms, Arg, KTs).


terms_to_keys([], _, []).
terms_to_keys([Term|Terms], Arg, [Key-Term|KTs]) :-
	arg(Arg, Term, Key),
	terms_to_keys(Terms, Arg, KTs).

terms_to_keys([], _, _, _, _, []).
terms_to_keys([Term|Terms], Arg1, Order1, Arg2, Order2, [Key-Term|KTs]) :-
	term_to_key(Order1, Order2, Term, Arg1, Arg2, Key),
	terms_to_keys(Terms, Arg1, Order1, Arg2, Order2, KTs).

term_to_key(lexicographical, lexicographical, Term, Arg1, Arg2, k(K1,K2)) :- !,
	arg(Arg1, Term, V1),
	arg(Arg2, Term, V2),
	collation_key(V1, K1),
	collation_key(V2, K2).
term_to_key(lexicographical, _, Term, Arg1, Arg2, k(K1,K2)) :- !,
	arg(Arg1, Term, V1),
	arg(Arg2, Term, K2),
	collation_key(V1, K1).
term_to_key(_, lexicographical, Term, Arg1, Arg2, k(K1,K2)) :- !,
	arg(Arg1, Term, K1),
	arg(Arg2, Term, V2),
	collation_key(V2, K2).
term_to_key(_, _, Term, Arg1, Arg2, k(K1,K2)) :- !,
	arg(Arg1, Term, K1),
	arg(Arg2, Term, K2).

keys_to_terms([], []).
keys_to_terms([_-Term|KTs], [Term|Terms]) :-
	keys_to_terms(KTs, Terms).

