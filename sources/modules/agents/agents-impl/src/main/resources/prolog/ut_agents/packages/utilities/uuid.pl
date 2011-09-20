/*  $Id$
 *  
 *  File	uuid.pl
 *  Part of	Prolog library
 *  Author	Anjo Anjewierden, a.a.anjewierden@utwente.nl
 *  Purpose	UUID generation
 *  Works with	SWI-Prolog (www.swi-prolog.pl)
 *  
 *  Notice	Copyright (c) 2009  University of Twente
 *  
 *  History	27/05/09  (Created)
 *  		27/05/09  (Last modified)
 */ 

/*------------------------------------------------------------
 *  Directives
 *------------------------------------------------------------*/

:- module(uuid_utilities,
	  [ uuid/1			% UUID -- TBD in reality MD5
	  ]).

:- use_module(load).

:- use_module(library(pce)).

%:- use_module(semweb(rdf_db)).
%:- use_module(utilities(list)).
:- use_module(sqlspaces(tspl)).


%%	uuid(-UUID:atom) is det.
%
%	UUID is a unique atom.
%
%	Currently uses MD5 algorithm (see uuid_generate_random in the C library).

uuid(Id) :-
	tspl:uid(Id).		% Temporarily because rdf library not on SCY server

end_of_file.

uuid(Id) :-
	get_time(S),
	X is S * 10000,
	X2 is X * X,
	Hint is random(100000),
	sformat(Str, '~w-~0f', [Hint,X2]),
	string_to_atom(Str, Atom),
	rdf_atom_md5(Atom, 1, Tmp),
	convert_to_version_4(Tmp, Id).


convert_to_version_4(In, Out) :-
	sub_atom(In,  0,8,_, Part1),
	sub_atom(In,  8,4,_, Part2),
	sub_atom(In, 12,4,_, Part3all),
	sub_atom(In, 16,4,_, Part4all),
	sub_atom(In, 20,4,_, Part5),
	sub_atom(Part3all, 1,3,_, Part3tail),
	sub_atom(Part4all, 1,3,_, Part4tail),
	atom_concat('4', Part3tail, Part3),
	sub_atom(Part4all, 0,1,_, Part4head),
	(   (Part4head == '8'; Part4head == '9'; Part4head == 'a'; Part4head == 'b')
	->  Part4 = Part4all
	;   select_random_list([8,9,a,b], 1, [Part4random]),
	    atom_concat(Part4random, Part4tail, Part4)
	),
	concat_atom([Part1,Part2,Part3,Part4,Part5], '-', Out).
