/*  $Id$
 *  
 *  File	model.pl
 *  Part of	IC - educational data mining
 *  Author	Anjo Anjewierden, a.a.anjewierden@utwente.nl
 *  Purpose	Representation of concept maps (SCY Mapper)
 *  Works with	SWI-Prolog (www.swi-prolog.org)
 *  
 *  Notice	Copyright (c) 2010  University of Twente
 *  
 *  History	18/11/10  (Created)
 *  		18/11/10  (Last modified)
 */

/*------------------------------------------------------------
 *  Directives
 *------------------------------------------------------------*/

:- module(model_cmap_ic,
	  [ cmap_create/4,		% Map <- Nodes x Edges <- Options
	    cmap_delete/1,		% Map
	    cmap/1,			% Map
	    cmap_nodes/2,		% Map -> Nodes
	    cmap_edges/2,		% Map -> Edges
	    cmap_xml/2,			% Map -> XML

	    cmap_export/1,		% Stream
	    cmap_import_term/1		% Term
	  ]).


:- use_module(load).

:- use_module(library(option), [option/2]).

:- use_module(gls(model), [gls_create/2, gls_delete/1, cmap_to_gls/2]).


/**	<module> Representation of concept maps

@author	Anjo Anjewierden
*/


cmap_create(Map, Nodes, Edges, Options) :-
	gls_create(Map, [notation(cmap)]),
	assert(cmap(Map,Nodes,Edges)),
	(   option(xml(XML), Options)
	->  assert(cmap_xml(Map,XML))
	;   true
	),
	cmap_to_gls(Map, []).

cmap_delete(Map) :-
	nonvar(Map),
	gls_delete(Map),
	clear_cache(Map).

cmap(Map) :-
	cmap(Map, _, _).

cmap_nodes(Map, Nodes) :-
	cmap(Map, Nodes, _).

cmap_edges(Map, Edges) :-
	cmap(Map, _, Edges).


/*------------------------------------------------------------
 *  Export / import
 *------------------------------------------------------------*/

%%	cmap_export(+Stream:stream) is det.
%
%	Export all concept map related data to Stream.  Normally called by
%	ic_export/1.  

cmap_export(Stream) :-
	forall(cmap(Map,Nodes,Edges),
	       format(Stream, 'cmap(~q,~q,~q).~n', [Map,Nodes,Edges])),
	forall(cmap_xml(Map,XML),
	       format(Stream, 'cmap_xml(~q,~q).~n', [Map,XML])).


%%	cmap_import_term(+Term:term) is det.
%
%	Import the cmap related term.  Normally called by ic_import/2.

cmap_import_term(cmap(Map,Nodes,Edges)) :-
	cmap_create(Map, Nodes, Edges, []).
cmap_import_term(cmap_xml(Map,XML)) :-
	assertz(cmap_xml(Map,XML)).


/*------------------------------------------------------------
 *  Storage
 *------------------------------------------------------------*/

:- dynamic
	cmap/3,		% Map x Nodes x Edges
	cmap_xml/2.	% Map -> XML

clear_cache(Map) :-
	retractall(cmap(Map,_,_)),
	retractall(cmap_xml(Map,_)).

clear_cache_all :-
	retractall(cmap(_,_,_)),
	retractall(cmap_xml(_,_)).


/*------------------------------------------------------------
 *  Initialization
 *------------------------------------------------------------*/

:- initialization
	listen(clear_cache_all, clear_cache_all).
