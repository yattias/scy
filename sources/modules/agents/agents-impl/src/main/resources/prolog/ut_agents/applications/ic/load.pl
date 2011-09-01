/*  $Id$
 *  
 *  File	load.pl
 *  Part of	IC - behavioural data mining
 *  Author	Anjo Anjewierden, a.a.anjewierden@utwente.nl
 *  Purpose	Loading
 *  Works with	SWI-Prolog (www.swi-prolog.org)
 *  
 *  Notice	Copyright (c) 2007, 2008, 2009, 2010  University of Twente
 *  
 *  History	08/02/07  (Created)
 *  		07/05/10  (Last modified)
 */ 

/*------------------------------------------------------------
 *  Directives
 *------------------------------------------------------------*/

:- module(load_ic, []).

%:- ignore(send(@pce,load_defaults,'~/.xpce/Defaults')).

:- use_module('../../packages/params/load').

:- use_module(library(pce)).

:- use_module(autoload).


/*------------------------------------------------------------
 *  Parts
 *------------------------------------------------------------*/

user:file_search_path(ic,		applications(ic)).

user:file_search_path(kernel,		ic(kernel)).
user:file_search_path(methods,		ic(methods)).

user:file_search_path(brick,		ic(brick)).

user:file_search_path(specification,	ic(specification)).

user:file_search_path(cmap,		ic('format/cmap')).
user:file_search_path(gls,		ic('format/gls')).
user:file_search_path(colab,		ic('format/colab')).
user:file_search_path(gaze_tracker,	ic('format/gaze_tracker')).
user:file_search_path(scy,		ic('format/scy')).
user:file_search_path(scy_sdm,		ic('format/scy_sdm')).
user:file_search_path(scy_ontology,	ic('format/scy_ontology')).
user:file_search_path(sdm,		ic('format/sdm')).
user:file_search_path(simquest,		ic('format/simquest')).
user:file_search_path(zap,		ic('format/zap')).

user:file_search_path(win,		ic(win)).
user:file_search_path(pce,		ic(pce)).

user:file_search_path(agents,		ic(agents)).
user:file_search_path(analysis,		ic(analysis)).
user:file_search_path(coding,		ic(coding)).
user:file_search_path(edm,		ic(edm)).
user:file_search_path(gra,		ic(gra)).
user:file_search_path(mod,		ic(mod)).
user:file_search_path(seq,		ic(seq)).
user:file_search_path(systematic_behaviour, ic(systematic_behaviour)).
user:file_search_path(study,		ic(study)).


/*------------------------------------------------------------
 *  Global objects
 *------------------------------------------------------------*/

:- pce_global(@finder, new(finder)).

:- pce_global(@horizontal_format, make_horizontal_format).

make_horizontal_format(F) :-
	new(F, format(horizontal,1000,@on)),
	send(F, row_sep, 5).

:- pce_global(@vertical_center_format, make_vertical_center_format).

make_vertical_center_format(F) :-
	new(F, format(horizontal,1,@on)),
	send(F, row_sep, 5),
	send(F, adjustment, vector(center)).

:- pce_image_directory(icons).

define_types :-
	pce_define_type(item2, int),
	pce_define_type(sdm_id, name),
	pce_define_type(cmap_id, name),
	pce_define_type(gls_id, name).

:- initialization
	define_types.
