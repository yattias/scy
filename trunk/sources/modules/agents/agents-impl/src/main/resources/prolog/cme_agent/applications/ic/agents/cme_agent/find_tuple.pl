/*  $Id$
 *
 *  File	find_tuple.pl
 *  Part of	IC - educational data mining
 *  Author	Anjo Anjewierden, a.a.anjewierden@utwente.nl
 *  Purpose	Find a tuple
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

:- module(find_tuple_agents,
	  [ ts_find_cmap_tuple/3,
	    ts_find_cmap_saved_tuple/3
	  ]).

:- use_module(load).

:- use_module(sqlspaces(tspl), [tspl_read/3]).
:- use_module(agents(agent_api), [agent_query/3, agent_tuple/3]).


ts_find_cmap_tuple(TS, Tuple, Model) :-
	agent_query(action, Query,
		    [ tool(conceptmap)
		    ]),
	repeat,
	tspl_read(TS, Query, Tuple),
	agent_tuple(action, Tuple, [attributes(Atts)]),
	memberchk(model(Model), Atts), !.


ts_find_cmap_saved_tuple(TS, Tuple, URI) :-
	agent_query(action, Query,
		    [ action(elo_saved),
		      tool(conceptmap)			% Should be scymapper
		    ]),
	repeat,
	tspl_read(TS, Query, Tuple),
	agent_tuple(action, Tuple, [elo(URI)]).
