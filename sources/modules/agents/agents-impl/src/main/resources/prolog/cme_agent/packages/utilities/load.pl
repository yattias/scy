/*  $Id$
 *  
 *  File	load.pl
 *  Part of	Utilities
 *  Author	Anjo Anjewierden, anjo@science.uva.nl
 *  Purpose	Loading
 *  Works with	SWI-Prolog (www.swi-prolog.org)
 *  
 *  Notice	Copyright (c) 2006  University of Amsterdam
 *  
 *  History	19/02/06  (Created)
 *  		26/02/06  (Last modified)
 */ 


/*------------------------------------------------------------
 *  Directives
 *------------------------------------------------------------*/

:- module(load_utilities,
	  []).

:- use_module('../params/load').


user:file_search_path(bin, utilities(bin)).
