/*  $Id$
 *
 *  File	demo.pl
 *  Part of	IC - educational data mining
 *  Author	Anjo Anjewierden, a.a.anjewierden@utwente.nl
 *  Purpose	Demo predicates for testing
 *  Works with	SWI-Prolog (www.swi-prolog.org)
 *
 *  Notice	Copyright (c) 2011  University of Twente
 *
 *  History	10/06/11  (Created)
 *  		10/06/11  (Last modified)
 */

/*------------------------------------------------------------
 *  Directives
 *------------------------------------------------------------*/

:- module(demo_cme_agent,
	  [ demo_set_up/0,
	    demo_post_read/0,
	    demo_eval/0,
	    demo_elo_fetch/0,
	    demo_elo_fetch/2	% TSactions x TScommand
	  ]).

:- use_module(load).

:- use_module(library('dialog/pretty_print')).

:- use_module(cme_agent, [cme_tuple_space/3, cme_agent_start/1]).
:- use_module(find_tuple, [ts_find_cmap_saved_tuple/3, ts_find_cmap_tuple/3]).
:- use_module(evaluate, [cmap_evaluate/4]).

:- use_module(sqlspaces(tspl), [tspl_read/3, tspl_write/2, tspl_tuple/2, tspl_disconnect/1, tspl_actual_field/3, tspl_formal_field/2, tspl_wait_to_read/4, tspl_wait_to_read/3]).
:- use_module(agents(agent_api), [agent_query/3, agent_tuple/3, agent_connect/3]).
:- use_module(agents(elo_fetch), [ts_elo_fetch/3]).
:- use_module(utilities(xml), [source_to_xml/3, xml_to_atom/3, atom_to_xml/3]).
:- use_module(cmap(parse), [cmap_parse/4]).
:- use_module(cmap(model), [cmap_create/4]).
:- use_module(utilities(uuid), [uuid/1]).


demo_set_up :-
	cme_agent_start([server(twente3)]),
	cme_tuple_space(command, _, Server),
	associate_term_set('co2house_ts.xml', Server),
	associate_reference_model('co2house_rm.xml', Server).

:- dynamic
	ts/2.

demo_post_read :-
	demo_post(Id),
	demo_read(Id).

demo_post(Id) :-
	ts(actions, TSa),
	ts(command, TSc),
	ts_find_cmap_tuple(TSa, _, MapAtom),
	uuid(Id),
	tspl_actual_field(string, concept_map_evaluation, F0),
	tspl_actual_field(string, evaluate_xml, F1),
	tspl_actual_field(string, Id, F2),
	tspl_actual_field(string, co2house, F3),
	tspl_actual_field(string, co2house, F4),
	tspl_actual_field(string, simple, F5),
	tspl_actual_field(string, MapAtom, F6),
	tspl_tuple([F0,F1,F2,F3,F4,F5,F6], Query),
	tspl_write(TSc, Query).

demo_read(Id) :-
	ts(command, TSc),
	tspl_actual_field(string, concept_map_evaluation, F0),
	tspl_actual_field(string, Id, F1),
	tspl_formal_field(string, F2),	
	tspl_formal_field(float, F3),
	tspl_tuple([F0,F1,F2,F3], Query),
	tspl_wait_to_read(TSc, Query, Tuple),
	pretty_print(Tuple).

demo_eval :-
	cme_tuple_space(actions, TS, _),
	ts_find_cmap_tuple(TS, _, MapAtom),
	atom_to_xml(MapAtom, XML, [space(remove)]),
%	pretty_print(XML),
	cmap_parse(XML, Nodes, Edges, []),
	length(Nodes, Ns),
	length(Edges, Es),
	format('~w nodes and ~w edges~n', [Ns,Es]),
	cmap_create(Map, Nodes, Edges, [cmap_xml(XML)]),
	cmap_evaluate(Map, simple, Result,
		      [ term_set(co2house),
			reference_model(co2house)
		      ]),
	format('map = ~w; result = ~w~n', [Map,Result]).
	


associate_term_set(Set, Server) :-
	source_to_xml(file(Set), XML, []),
	xml_to_atom(XML, Atom, [layout(false)]),
	agent_connect(Server, command, TS),
	tspl_actual_field(string, concept_map_evaluation, F0),
	tspl_actual_field(string, association, F1),
	tspl_actual_field(string, term_set, F2),
	tspl_actual_field(string, co2house, F3),
	tspl_actual_field(string, Atom, F4),
	tspl_tuple([F0,F1,F2,F3,F4], Query),
	tspl_write(TS, Query),
	tspl_disconnect(TS).

associate_reference_model(RefMod, Server) :-
	source_to_xml(file(RefMod), XML, []),
	xml_to_atom(XML, Atom, [layout(false)]),
	agent_connect(Server, command, TS),
	tspl_actual_field(string, concept_map_evaluation, F0),
	tspl_actual_field(string, association, F1),
	tspl_actual_field(string, reference_model, F2),
	tspl_actual_field(string, co2house, F3),
	tspl_actual_field(string, Atom, F4),
	tspl_tuple([F0,F1,F2,F3,F4], Query),
	tspl_write(TS, Query),
	tspl_disconnect(TS).


demo_elo_fetch :-
	cme_tuple_space(actions, TSa, _),
	cme_tuple_space(command, TSc, _),
	demo_elo_fetch(TSa, TSc).

demo_elo_fetch(TSa, TSc) :-
	ts_find_cmap_saved_tuple(TSa, _, URI),
	ts_elo_fetch(TSc, URI, XML),
	pretty_print(XML).
