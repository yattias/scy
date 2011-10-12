/*  $Id$
 *  
 *  File	normalise_string.pl
 *  Part of	IC - educational data mining
 *  Author	Anjo Anjewierden, a.a.anjewierden@utwente.nl
 *  Purpose	Normalise strings for term set matching
 *  Works with	SWI-Prolog (www.swi-prolog.org)
 *  Status      
 *  
 *  Notice	Copyright (c) 2011  University of Twente
 *  
 *  History	04/07/11  (Created)
 *  		05/07/11  (Last modified)
 */

/*------------------------------------------------------------
 *  Directives
 *------------------------------------------------------------*/

:- module(normalise_string_specification_ic,
	  [ normalise_string_manage/1,		% Options
	    normalise_string/3			% String -> Normalised <- Options
	  ]).

:- use_module(load).

:- use_module(atom(plaincase), [no_diacritics_atom/2]).


normalise_string(String, Normalised, Options) :-
	ns_options(Options, Down, Diacritics, Punct),
	normalise_string(String, Down,Diacritics,Punct, Normalised).

normalise_string(String, Down,Diacritics,Punct, Normalised) :-
	ns_downcase(Down, String, String1),
	ns_punctuation(Punct, String1, String2),
	ns_diacritics(Diacritics, String2, String3),
	ns_space(String3, Normalised).

ns_options([], Down, Diacritics, Punct) :-
	(var(Down) -> Down = true; true),
	(var(Diacritics) -> Diacritics = true; true),
	(var(Punct) -> Punct = true; true).
ns_options([downcase(Down)|Options], _, Diacritics, Punct) :- !,
	ns_options(Options, Down, Diacritics, Punct).
ns_options([diacritics(Diacritics)|Options], Down, _, Punct) :- !,
	ns_options(Options, Down, Diacritics, Punct).
ns_options([punctuation(Punct)|Options], Down, Diacritics, _) :- !,
	ns_options(Options, Down, Diacritics, Punct).
ns_options([_|Options], Down, Stop, Punct) :-
	ns_options(Options, Down, Stop, Punct).


/*------------------------------------------------------------
 *  Downcase
 *------------------------------------------------------------*/

:- dynamic
	dc/2.		% String -> Down

ns_downcase(true, String, Normalised) :-
	(   dc(String, Normalised)
	;   downcase_atom(String, Normalised),
	    assert(dc(String,Normalised))
	), !.
ns_downcase(false, String, String).


/*------------------------------------------------------------
 *  Diacritics
 *------------------------------------------------------------*/

:- dynamic
	di/2.		% String -> Diacritics

ns_diacritics(true, String, Normalised) :-
	(   di(String, Normalised)
	;   no_diacritics_atom(String, Normalised),
	    assert(di(String,Normalised))
	), !.
ns_diacritics(false, String, String).


/*------------------------------------------------------------
 *  Punctuation
 *------------------------------------------------------------*/

:- dynamic
	pc/2.		% String -> WithoutPunctuation

ns_punctuation(true, String, Normalised) :-
	(   pc(String, Normalised)
	;   atom_codes(String, Codes),
	    punctuation2(Codes, NewCodes),
	    atom_codes(Normalised, NewCodes),
	    assert(pc(String,Normalised))
	), !.
ns_punctuation(false, String, String).

punctuation2([], []).
punctuation2([Code|Codes], [NewCode|NewCodes]) :-
	(   code_type(Code, punct)
	->  NewCode = 32
	;   code_type(Code, space)
	->  NewCode = 32
	;   NewCode = Code
	),
	punctuation2(Codes, NewCodes).


/*------------------------------------------------------------
 *  Space
 *------------------------------------------------------------*/

:- dynamic
	sp/2.		% String -> Normalised

ns_space(String, Normalised) :-
	(   sp(String, Normalised)
	;   normalize_space(atom(Normalised), String),
	    assert(sp(String,Normalised))
	), !.


/*------------------------------------------------------------
 *  Manage normalise string packages
 *------------------------------------------------------------*/

%%	normalise_string_manage(+Options:list) is det.
%
%	Global options for normalise string management.  Options are:
%
%	* cache(clear)
%	  Clear the cache of normalised strings.

normalise_string_manage([]).
normalise_string_manage([Option|Options]) :-
	normalise_string_option(Option), !,
	normalise_string_manage(Options).
normalise_string_manage([Option|Options]) :-
	format('normalise_string_manage/1: unknown option ~q~n', [Option]),
	normalise_string_manage(Options).

normalise_string_option(cache(clear)) :-
	retractall(ns_cache(_,_,_)).
