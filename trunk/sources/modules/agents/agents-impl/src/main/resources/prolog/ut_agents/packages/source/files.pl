/*  $Id$
 *  
 *  File	load.pl
 *  Part of	Utilities
 *  Author	Anjo Anjewierden, a.a.anjewierden@utwente.nl
 *  Purpose	Files required by currently loaded program
 *  Works with	SWI-Prolog (www.swi-prolog.org)
 *  
 *  Notice	Copyright (c) 2011  University of Twente
 *  
 *  History	14/04/11  (Created)
 *  		14/04/11  (Last modified)
 */ 


/*------------------------------------------------------------
 *  Directives
 *------------------------------------------------------------*/

:- module(files_source,
	  [ source_files_required/2,	% Files <- Options
	    source_files_copy/3,	% Files x Root <- Options
	    source_file_register/1,	% File
	    source_file_deregister/1	% File
	  ]).

:- use_module(load).

:- use_module(library(lists), [append/3]).
:- use_module(library(option), [option/2, option/3]).


/*------------------------------------------------------------
 *  Predicates
 *------------------------------------------------------------*/

%%	source_files_requires(-Files:list, +Options:list) is det.
%
%	Files is a list of the currently loaded source files including those
%	specified with pce_autoload/2.  The names in Files are absolute file
%	names.  Options are:
%
%	* pce_autoload(Bool
%	  If =true= (default) then files specified by pce_autoload/2 are
%	  included in the list of Files.
%
%	* system(System)
%	  System is the prefix for system files part of the SWI-Prolog
%	  distribution.  The default prefix is =/usr/=.

source_files_required(Files, Options) :-
	option(system(System), Options, '/usr/'),
	option(pce_autoload(Autoload), Options, true),
	findall(F, (source_file(F); src_file(F)), Fs),
	(   Autoload == true
	->  findall(A, ( pce_autoload:autoload(_,S),
			 autoload_file(S, A)), As)
	;   As = []
	),
	append(Fs, As, Files0),
	remove_system_files(Files0, System, Files1),
	sort(Files1, Files).


autoload_file(Rel, Abs) :-
	absolute_file_name(Rel, AbsFile),
	(   sub_atom(AbsFile, _,_,0, '.pl')
	->  Abs = AbsFile
	;   atom_concat(AbsFile, '.pl', Abs)
	).

remove_system_files([], _, []).
remove_system_files([F|Fs], Prefix, Files) :-
	sub_atom(F, 0,_,_, Prefix), !,
	remove_system_files(Fs, Prefix, Files).
remove_system_files([File|Fs], Prefix, [File|Files]) :-
	remove_system_files(Fs, Prefix, Files).


%%	source_files_copy(+Files:list, +Dst:atom, +Options:list) is det.
%
%	Copies all Files to Dst.  Options are:
%
%	* root(Root)
%	  Prefix is the Root of the source files, e.g.,
%	  =/home/fred/mytool/=.  This option must be specified.

source_files_copy(Files, Dst0, Options) :-
  trace,
	option(root(Root0), Options),
	ends_on_slash(Dst0, Dst),
	ends_on_slash(Root0, Root),
	copy_files(Files, Dst, Root).


ends_on_slash(Dir, Dir) :-
	sub_atom(Dir, _,_,0, '/'), !.
ends_on_slash(Dir0, Dir) :-
	atomic_concat(Dir0, '/', Dir).


copy_files([], _, _).
copy_files([File|Files], Dst, Root) :-
	(   access_file(File, read)
	->  copy_file(File, Dst, Root)
	;   format('  source_files_copy/2: file ~w is not readable~n', [File])
	),
	copy_files(Files, Dst, Root).

copy_file(File, Dst, Root) :-
	sub_atom(File, 0,_,A, Root),
	sub_atom(File, _,A,0, Tail),
	atomic_concat(Dst, Tail, Path),
	create_path(Path),
	open(File, read, In),
	open(Path, write, Out),
	copy_stream_data(In, Out),
	close(Out),
	close(In).


create_path(Path) :-
	forall(sub_atom(Path, B,_,_, '/'),
	       ensure_sub_path(Path, B)).

ensure_sub_path(_, 0) :- !.		% Is / directory
ensure_sub_path(Path, B) :-
	sub_atom(Path, 0,B,_, Sub),
	(   exists_directory(Sub)
	->  true
	;   make_directory(Sub)
	).


%%	source_file_register(+File:atom) is det.
%
%	Register File as a file part of the current sources.  It will
%	then be copied with source_files_copy/3.

source_file_register(File) :-
	absolute_file_name(File, AbsFile),
	source_file_deregister(AbsFile),
	assertz(src_file(AbsFile)).


%%	source_file_deregister(+File:atom) is det.
%
%	Deregister File as a file part of the current sources.  See
%	source_file_register/1. 

source_file_deregister(File) :-
	absolute_file_name(File, AbsFile),
	retractall(src_file(AbsFile)).


:- dynamic
	src_file/1.		% File
