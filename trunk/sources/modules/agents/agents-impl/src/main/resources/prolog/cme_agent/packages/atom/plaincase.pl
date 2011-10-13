/*  $Id$
 *  
 *  File	plaincase.pl
 *  Part of	Atom predicates
 *  Author	Anjo Anjewierden, a.a.anjewierden@utwente.nl
 *  Purpose	Converting an atom to "plain" case
 *  Works with	SWI-Prolog (www.swi-prolog.org)
 *  
 *  Notice	Copyright (c) 2001-2006  University of Amsterdam
 *		Copyright (c) 2011       University of Twente
 *  
 *  History	19/10/01  (Created)
 *  		11/10/11  (Last modified)
 */ 

/*------------------------------------------------------------
 *  Directives
 *------------------------------------------------------------*/

:- module(plaincase_atom,
	  [ plaincase_atom/2,		% Atom -> Atom
	    is_diacritics/1,		% Atom
	    is_plaincase/1,		% Atom
	    no_diacritics_atom/2	% Atom -> Atom
	  ]).

:- use_module(load).

/*------------------------------------------------------------
 *  Register related files
 *------------------------------------------------------------*/

:- use_module(source(files), [source_file_register/1]).

:-
	source_file_register(atom('Makefile')),
	source_file_register(atom('plaincase.c')),
	source_file_register(atom('plaincase.h')).


/*------------------------------------------------------------
 *  Load the library
 *------------------------------------------------------------*/

:-
	use_foreign_library(bin(plaincase)).

