/*  $Id$
 *
 *  File	user_language.pl
 *  Part of	IC - educational data mining
 *  Author	Anjo Anjewierden, a.a.anjewierden@utwente.nl
 *  Purpose	Language for a user
 *  Works with	SWI-Prolog (www.swi-prolog.org)
 *
 *  Notice	Copyright (c) 2011  University of Twente
 *
 *  History	10/10/11  (Created)
 *  		10/10/11  (Last modified)
 */

/*------------------------------------------------------------
 *  Directives
 *------------------------------------------------------------*/

:- module(user_language_agents_ic,
	  [ scy_session_user_language/4	% TS x User -> Language <- Options
	  ]).

:- use_module(load).

:- use_module(library(option), [option/3]).

:- use_module(sqlspaces(tspl), [tspl_read/3, tspl_formal_field/2,
				tspl_tuple_field/3, tspl_actual_field/3,
				tspl_tuple/2]).



/*------------------------------------------------------------
 *  User language
 *------------------------------------------------------------*/

%%	scy_session_user_language(+TS:tuple_space_hdl, +User:atom, -Language:atom, +Options:list) is det.
%
%	Returns Language for User as a two-character atom in TS.  If no
%	language is found then first default(Language) is tried and otherwise
%	it default to =en=.

scy_session_user_language(TS, User, Language, Options) :-
	tspl_actual_field(string, language, F0),
	tspl_actual_field(string, User, F1),
	tspl_formal_field(string, F2),
	tspl_tuple([F0,F1,F2], Query),
	(   tspl_read(TS, Query, Tuple)
	->  tspl_tuple_field(Tuple, 2, Language)
	;   option(default(Language), Options, en)
	).
