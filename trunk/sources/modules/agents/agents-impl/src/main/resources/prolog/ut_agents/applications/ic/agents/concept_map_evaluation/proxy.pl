/*  $Id$
 *
 *  File	proxy.pl
 *  Part of	IC - educational data mining
 *  Author	Anjo Anjewierden, a.a.anjewierden@utwente.nl
 *  Purpose	Adding Tuples
 *  Works with	SWI-Prolog (www.swi-prolog.org)
 *
 *  Notice	Copyright (c) 2011  University of Twente
 *
 *  History	08/06/11  (Created)
 *  		09/06/11  (Last modified)
 */

/*------------------------------------------------------------
 *  Directives
 *------------------------------------------------------------*/

:- module(proxy_cme_agent_ic,
	  [ proxy/0,
	    read/0,
	    write_term_set/0,
	    write_reference_model/0
	  ]).

:- use_module(load).

:- use_module(library('dialog/pretty_print'), [pretty_print/1]).

:- use_module(cme_agent, [cme_agent_start/1]).

:- use_module(library(option), [option/2]).

:- use_module(sqlspaces(tspl), [tspl_write/2, tspl_read/3,
				tspl_event_register/5,
				tspl_tuple_field/3, tspl_wildcard_field/1,
				tspl_actual_field/3, tspl_tuple/2, tspl_disconnect/1]).
:- use_module(agents(agent_api), [agent_connect/3, agent_query/3, agent_tuple/3]).

:- use_module(cmap(parse), [cmap_parse/4]).
:- use_module(utilities(xml), [xml_to_atom/3, source_to_xml/3]).


/*------------------------------------------------------------
 *  Protocol
 *------------------------------------------------------------*/

/*	In command space:
  
concept_map_evaluation, association, term_set, Name, XML
concept_map_evaluation, association, reference_model, Name, XML
concept_map_evaluation, association, rule_set, Name, XML
*/

s(twente2).

proxy :-
	s(Server),
	agent_connect(Server, command, TS),
	tspl_actual_field(string, concept_map_evaluation, F0),
	tspl_wildcard_field(WC),
	tspl_tuple([F0,WC], Query),
	tspl_event_register(TS, write, Query, proxy_callback, _),
	write_term_set.

read :-
	s(Server),
	agent_connect(Server, command, TS),
	tspl_actual_field(string, concept_map_evaluation, F0),
	tspl_wildcard_field(WC),
	tspl_tuple([F0,WC], Query),
	tspl_read(TS, Query, Tuple),
	pretty_print(Tuple),
	tspl_disconnect(TS).


write_term_set :-
	s(Server),
	source_to_xml(file('co2house_ts.xml'), XML, []),
	xml_to_atom(XML, Atom, [layout(true)]),
	agent_connect(Server, command, TS),
	tspl_actual_field(string, concept_map_evaluation, F0),
	tspl_actual_field(string, association, F1),
	tspl_actual_field(string, term_set, F2),
	tspl_actual_field(string, co2house, F3),
	tspl_actual_field(string, Atom, F4),
	tspl_tuple([F0,F1,F2,F3,F4], Query),
	tspl_write(TS, Query),
	tspl_disconnect(TS).

write_reference_model :-
	s(Server),
	source_to_xml(file('co2house_rm.xml'), XML, []),
	xml_to_atom(XML, Atom, [layout(true)]),
	agent_connect(Server, command, TS),
	tspl_actual_field(string, concept_map_evaluation, F0),
	tspl_actual_field(string, association, F1),
	tspl_actual_field(string, reference_model, F2),
	tspl_actual_field(string, co2house, F3),
	tspl_actual_field(string, Atom, F4),
	tspl_tuple([F0,F1,F2,F3,F4], Query),
	tspl_write(TS, Query),
	tspl_disconnect(TS).


tspl:proxy_callback(A, B, C, D) :-
	format('~w ~w ~w ~w~n', [A,B,C,D]).
