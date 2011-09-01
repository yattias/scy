/*  $Id$
 *
 *  File	cme_agent.pl
 *  Part of	IC - educational data mining
 *  Author	Anjo Anjewierden, a.a.anjewierden@utwente.nl
 *  Purpose	Agent for evaluation concept maps
 *  Works with	SWI-Prolog (www.swi-prolog.org)
 *
 *  Notice	Copyright (c) 2011  University of Twente
 *
 *  History	08/06/11  (Created)
 *  		10/06/11  (Last modified)
 */

/*------------------------------------------------------------
 *  Directives
 *------------------------------------------------------------*/

:- module(cme_agent_ic,
	  [ cme_agent_start/1,	% Options		// [server(Server)]
	    cme_tuple_space/3	% Space -> TS x Server
	  ]).

:- use_module(load).

:- use_module(library(option), [option/2]).
:- use_module(library('dialog/pretty_print'), [pretty_print/1]).

:- use_module(find_tuple, [ts_find_cmap_saved_tuple/3, ts_find_cmap_tuple/3]).
:- use_module(evaluate, [cmap_evaluate/4]).

:- use_module(sqlspaces(tspl), [tspl_read/3, tspl_write/2, tspl_event_register/5,
				tspl_tuple_field/3, tspl_wildcard_field/1,
				tspl_actual_field/3, tspl_tuple/2, tspl_disconnect/1]).

:- use_module(agents(elo_fetch), [ts_elo_fetch/3]).
:- use_module(agents(agent_api), [agent_connect/3, agent_query/3, agent_tuple/3]).
:- use_module(cmap(parse), [cmap_parse/4]).
:- use_module(cmap(model), [cmap_create/4, cmap_delete/1]).
:- use_module(utilities(xml), [atom_to_xml/3, source_to_xml/3]).
:- use_module(specification(reference_model), [reference_model/1, reference_model_parse/3]).
:- use_module(specification(term_set), [term_set/1, term_set_parse/3]).
:- use_module(specification(rule_set), [rule_set/1, rule_set_parse/3]).


/*------------------------------------------------------------
 *  Initialization
 *------------------------------------------------------------*/

:- dynamic
	cme_tuple_space/3.		% Space x TS x Server

cme_agent_start(Options) :-
	forall(cme_tuple_space(_,TS,_), tspl_disconnect(TS)),
	action_monitor(Options),
	command_monitor(Options).

action_monitor(Options) :-
	option(server(Server), Options),
	agent_connect(Server, actions, TS),
	assert(cme_tuple_space(actions,TS,Server)),	
agent_query(action, Query,
			[ tuple_type(action),
			  tool(scymapper)
			]),
	Call = cme_action_callback,
	tspl_event_register(TS, write, Query, Call, _).

command_monitor(Options) :-
	option(server(Server), Options),
	agent_connect(Server, command, TS),
	assert(cme_tuple_space(command,TS,Server)),
	tspl_actual_field(string, concept_map_evaluation, F0),
	tspl_wildcard_field(WC),
	tspl_tuple([F0,WC], Query),
	Call = cme_command_callback,
	tspl_event_register(TS, write, Query, Call, _).


/*------------------------------------------------------------
 *  Commands in command space
 *------------------------------------------------------------*/

/*	In command space:
concept_map_evaluation, association, term_set, Name, XMLasString
concept_map_evaluation, association, reference_model, Name, XMLasString
concept_map_evaluation, association, rule_set, Name, XMLasString

concept_map_evaluation, evaluate_elo, Id, TermSet, RefMod, Method, EloURI	
concept_map_evaluation, evaluate_xml, Id, TermSet, RefMod, Method, CMapXML
*/

tspl:cme_command_callback(write, _SeqId, [], [Tuple]) :-
%  format('cme_command_callback ~w~n', [SeqId]),
%  pretty_print(Tuple),
	tspl_tuple_field(Tuple, 1, Action),
	command_callback(Action, Tuple),
	thread_self(Thread),
	thread_detach(Thread).

command_callback(association, Tuple) :- !,
	tspl_tuple_field(Tuple, 2, What),
	tspl_tuple_field(Tuple, 3, Name),
	tspl_tuple_field(Tuple, 4, Value),
	association_command(What, Name, Value).
command_callback(evaluate_elo, Tuple) :- !,
	tspl_tuple_field(Tuple, 2, Id),
	tspl_tuple_field(Tuple, 3, TermSet),
	tspl_tuple_field(Tuple, 4, RefMod),
	tspl_tuple_field(Tuple, 5, Method),
	tspl_tuple_field(Tuple, 6, URI),
	cme_tuple_space(command, TS, _),
	ts_elo_fetch(TS, URI, XML),
	evaluate_command(Id, TermSet, RefMod, Method, XML).
command_callback(evaluate_xml, Tuple) :- !,
	tspl_tuple_field(Tuple, 2, Id),
	tspl_tuple_field(Tuple, 3, TermSet),
	tspl_tuple_field(Tuple, 4, RefMod),
	tspl_tuple_field(Tuple, 5, Method),
	tspl_tuple_field(Tuple, 6, MapAtom),
	atom_to_xml(MapAtom, XML, []),
	evaluate_command(Id, TermSet, RefMod, Method, XML).
command_callback(_, _).		% Ignoring other commands

association_command(term_set, Name, Value) :-
	format('CME: parsing term_set ~w~n', [Name]),
	term_set_parse(atom(Value), _, [term_set(Name)]),
	format('  done~n', []).
association_command(reference_model, Name, Value) :-
	format('CME: parsing reference_model ~w~n', [Name]),
	reference_model_parse(atom(Value), _, [reference_model(Name)]),
	format('  done~n', []).
association_command(rule_set, Name, Value) :-
	format('CME: parsing rule_set ~w~n', [Name]),
	rule_set_parse(atom(Value), _, [rule_set(Name)]),
	format('  done~n', []).


/*------------------------------------------------------------
 *  Reply after an evaluate
 *------------------------------------------------------------*/

evaluate_command(Id, TermSet, RefMod, Method, XML) :-
	cmap_parse(XML, Nodes, Edges, []),
  format('  PARSED~n', []),
	cmap_create(Map, Nodes, Edges, [cmap_xml(XML)]),
  format('  CREATE~n', []),
	(   \+ term_set(TermSet)
	->  format(atom(Status), 'term set ~w not found', [TermSet])
	;   \+ reference_model(RefMod)
	->  format(atom(Status), 'reference model ~w not found', [RefMod])
	;   cmap_evaluate(Map, Method, Value,
			  [ term_set(TermSet),
			    reference_model(RefMod)
			  ])
	->  Status = succeed
	;   Status = 'unexpected failure during concept map evaluation',
	    Value = -1
	),
  format('  EVALED~n', []),
	cmap_delete(Map), !,
  format('  DELETE~n', []),
	evaluate_command_reply(Id, Status, Value).
evaluate_command(Id, _TermSet, _RefMod, _Method, _XML) :-
	Status = 'unexpected failure during parsing',
	evaluate_command_reply(Id, Status, -1).

evaluate_command_reply(Id, Status, Value) :-
  format('  ECR 1~n', []),
	tspl_actual_field(string, concept_map_evaluation, F0),
  format('  ECR 2~n', []),
	tspl_actual_field(string, Id, F1),
  format('  ECR 3~n', []),
	tspl_actual_field(string, Status, F2),
  format('  ECR 4~n', []),
	tspl_actual_field(float, Value, F3),
  format('  ECR 5~n', []),
	tspl_tuple([F0,F1,F2,F3], Tuple),
  format('  ECR 6~n', []),
	cme_tuple_space(command, TS, _),
  format('  ECR 7 ~w~n', [TS]),
	tspl_write(TS, Tuple),
  format('  ECR 8~n', []).
	

/*------------------------------------------------------------
 *  Callback on a SCYMapper action
 *------------------------------------------------------------*/

tspl:cme_action_callback(write, _SeqId, [], [Tuple]) :-
	agent_tuple(action, Tuple,
		    [ id(Id),
		      user(User),
		      action(Action),
		      attributes(Atts)
		    ]),
	format('~w by ~w with ~w~n', [Action,User,Id]),
	(   memberchk(model(Model), Atts)
	->  atom_to_xml(Model, XML, [dialect(xml), space(remove)]),
	    cmap_parse(XML, Nodes, Links, []),
	    length(Nodes, LenN),
	    length(Links, LenL),
	    format('  ~w nodes and ~w links~n', [LenN,LenL])
	;   true
	),
	thread_self(Thread),
	thread_delete(Thread).

