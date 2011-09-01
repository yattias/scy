/*  $Id$
 *  
 *  File	load.pl
 *  Part of	Natural Language Processing
 *  Author	Anjo Anjewierden, anjo@science.uva.nl
 *  Purpose	Loading stop words for all languages
 *  Works with	SWI-Prolog (www.swi-prolog.org)
 *  
 *  Notice	Copyright (c) 2001  University of Amsterdam
 *  
 *  History	18/05/01  (Created)
 *  		18/05/01  (Last modified)
 */ 

/*------------------------------------------------------------
 *  Directives
 *------------------------------------------------------------*/

:- module(load_stop_word,
	  []).

:- use_module('../../params/load').

:- use_module(danish).
:- use_module(dutch).
:- use_module(english).
:- use_module(finnish).
:- use_module(french).
:- use_module(german).
:- use_module(italian).
:- use_module(portuguese).
:- use_module(spanish).
:- use_module(swedish).
