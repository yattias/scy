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

:- use_module(ns_data, [ns_test_string/1]).





test_ns :-
	findall(S, ns_test_string(S), Strings),
	normalise_string_manage([cache(clear), language(dutch)]),
	Options = [],
	test_ns(Strings, Options).

test_ns([], _).
test_ns([String|Strings], Options) :-
	normalise_string(String, _, Options),
	test_ns(Strings, Options).
	

/*
normalise_string(String, Normalised, Options) :-
	(memberchk(downcase(Down), Options); Down = true),
	(memberchk(stop_word(Stop), Options); Stop = true),
	(memberchk(punctuation(Punct), Options); Punct = true),
	ns_downcase(Down, String, String1),
	ns_punctuation(Punct, String1, String2),
	ns_stop_word(Stop, String2, String3),
	ns_space(String3, Normalised).
*/
normalise_string(String, Normalised, Options) :-
	ns_options(Options, Down, Stop, Punct),
	normalise_string(String, Down,Stop,Punct, Normalised).

normalise_string(String, Down,Stop,Punct, Normalised) :-
	ns_downcase(Down, String, String1),
	ns_punctuation(Punct, String1, String2),
	ns_stop_word(Stop, String2, String3),
	ns_space(String3, Normalised).

ns_options([], Down, Stop, Punct) :-
	(var(Down) -> Down = true; true),
	(var(Stop) -> Stop = true; true),
	(var(Punct) -> Punct = true; true).
ns_options([downcase(Down)|Options], _, Stop, Punct) :- !,
	ns_options(Options, Down, Stop, Punct).
ns_options([stop_word(Stop)|Options], Down, _, Punct) :- !,
	ns_options(Options, Down, Stop, Punct).
ns_options([punctuation(Punct)|Options], Down, Stop, _) :- !,
	ns_options(Options, Down, Stop, Punct).
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
 *  Stop words
 *------------------------------------------------------------*/

:- dynamic
	sw/2.		% String -> WithoutStopWords

ns_stop_word(true, String, Normalised) :-
	(   sw(String, Normalised)
	;   ns_language(Language),
	    (   known_language(Language)
	    ->  atomic_list_concat(Words, ' ', String),
		stop_words(Words, Language, NewWords),
		atomic_list_concat(NewWords, ' ', Normalised)
	    ;   String = Normalised
	    ),
	    assert(sw(String,Normalised))
	), !.
ns_stop_word(false, String, String).
	
stop_words([], _, []).
stop_words([Word|Words], Language, More) :-
	Language:stop_word(Word), !,
	stop_words(Words, Language, More).
stop_words([Word|Words], Language, [Word|More]) :-
	stop_words(Words, Language, More).


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
%
%	* language(Language)
%	  Language for stop words.

normalise_string_manage([]).
normalise_string_manage([Option|Options]) :-
	normalise_string_option(Option), !,
	normalise_string_manage(Options).
normalise_string_manage([Option|Options]) :-
	format('normalise_string_manage/1: unknown option ~q~n', [Option]),
	normalise_string_manage(Options).

normalise_string_option(cache(clear)) :-
	retractall(ns_cache(_,_,_)).
normalise_string_option(language(Lang)) :-
	(   ns_language(Lang)
	->  true
	;   normalise_string_option(cache(clear)),
	    retractall(ns_language(_)),
	    assert(ns_language(Lang))
	).


/*------------------------------------------------------------
 *  Stop words
 *------------------------------------------------------------*/

known_language(dutch).

dutch:stop_word(de).
dutch:stop_word(van).
dutch:stop_word(het).
dutch:stop_word(in).
dutch:stop_word(te).
dutch:stop_word(op).
dutch:stop_word(die).
dutch:stop_word(met).
dutch:stop_word(dat).
dutch:stop_word(en).


:- dynamic
	ns_language/1.		% Language 	// for stop words

:- initialization
	retractall(ns_language(_)),
	assert(ns_language(none)).

