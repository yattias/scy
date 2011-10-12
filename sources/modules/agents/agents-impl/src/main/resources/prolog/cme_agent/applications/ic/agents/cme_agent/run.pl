/*  $Id$
 *  
 *  File	run.pl
 *  Part of	IC - educational data mining
 *  Author	Anjo Anjewierden, a.a.anjewierden@utwente.nl
 *  Purpose	Running CME agent
 *  Works with	SWI-Prolog (www.swi-prolog.org)
 *  Status	
 *  
 *  Notice	Copyright (c) 2011  University of Twente
 *  
 *  History	05/10/11  (Created)
 *  		07/10/11  (Last modified)
 */ 

/*------------------------------------------------------------
 *  Directives
 *------------------------------------------------------------*/

:- module(run_cme_agent_ic,
	  [ cme_run/0,
	    cme_svn_files/0
	  ]).

:- use_module(load).

:- use_module(cme_agent, [cme_agent_start/1]).

:- use_module(source(files), [source_files_required/2, source_files_copy/3]).


cme_run :-
	cme_agent_start([server(collide)]).


cme_svn_files :-
	Dst = '/home/anjo/svn_cme_agent',
	(   exists_directory(Dst)
	->  format(atom(Remove), 'rm -r ~w', [Dst]),
	    shell(Remove)
	;   true
	),
	format(atom(Create), 'mkdir ~w', [Dst]),
	shell(Create),
	source_files_required(Files, [pce_autoload(false)]),
	source_files_copy(Files, Dst, [ root('/home/anjo/cute2')]).




