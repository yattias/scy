/*  $Id$
 *  
 *  File	load.pl
 *  Part of	IC - educational data mining
 *  Author	Anjo Anjewierden, a.a.anjewierden@utwente.nl
 *  Purpose	Loading the agents
 *  Works with	SWI-Prolog (www.swi-prolog.org)
 *  
 *  Notice	Copyright (c) 2009, 2010, 2011  University of Twente
 *  
 *  History	07/01/09  (Created)
 *  		07/10/11  (Last modified)
 */ 

/*------------------------------------------------------------
 *  Directives
 *------------------------------------------------------------*/

:- module(load_agents_ic,
	  []).

:- use_module('../load').

user:file_search_path(cme_agent,	agents(cme_agent)).
