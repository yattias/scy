/*  $Id$
 *
 *  File	fetch_elo.pl
 *  Part of	IC - educational data mining
 *  Author	Anjo Anjewierden, a.a.anjewierden@utwente.nl
 *  Purpose	Fetch an ELO over the TupleSpace
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

:- module(elo_fetch_agents,
	  [ ts_elo_fetch/3,		% TS x URI -> EloXML
	    server_elo_fetch/3		% Server x URI -> EloXML
	  ]).

:- use_module(load).

:- use_module(library('dialog/pretty_print')).

:- use_module(sqlspaces(tspl), [tspl_write/2, tspl_wait_to_read/3, tspl_wait_to_read/4, tspl_disconnect/1]).
:- use_module(agents(agent_api), [agent_connect/3, agent_query/3, agent_tuple/3]).
:- use_module(utilities(xml), [atom_to_xml/3]).


ts_elo_fetch(TS, URI, XML) :-
	agent_query(request_elo, Query1, [id(Id), elo_uri(URI)]),
	tspl_write(TS, Query1),
	agent_query(read_elo, Query2, [id(Id)]),
	tspl_wait_to_read(TS, Query2, Tuple),
	agent_tuple(read_elo, Tuple, [elo_xml(Atom)]),
	atom_to_xml(Atom, XML, [space(remove)]).


server_elo_fetch(Server, URI, XML) :-
	agent_connect(Server, command, TS),
	ts_elo_fetch(TS, URI, XML),
	tspl_disconnect(TS).

