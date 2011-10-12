/*  $Id$
 *
 *  File	cme_agent.pl
 *  Part of	IC - educational data mining
 *  Author	Anjo Anjewierden, a.a.anjewierden@utwente.nl
 *  Purpose	Concept map evaluation agent
 *  Works with	SWI-Prolog (www.swi-prolog.org)
 *
 *  Notice	Copyright (c) 2011  University of Twente
 *
 *  History	08/06/11  (Created)
 *  		07/10/11  (Last modified)
 */

/*------------------------------------------------------------
 *  Directives
 *------------------------------------------------------------*/

:- module(cme_agent_ic,
	  [ cme_agent_start/1,	% Options		// [server(Server)]
	    cme_tuple_space/3	% Space -> TS x Server
	  ]).

:- use_module(load).

:- use_module(library(option), [option/2, option/3]).
:- use_module(library(broadcast), [broadcast/1]).
:- use_module(library(debug), [debug/1, debug/3]).

:- use_module(find_tuple, [ts_find_cmap_saved_tuple/3, ts_find_cmap_tuple/3]).
:- use_module(evaluate, [cmap_evaluate/4]).
:- use_module(config, [anchor_properties/3]).
:- use_module(config, [method_anchor_evaluation/5]).

:- use_module(sqlspaces(tspl), [tspl_read/3, tspl_write/2,
				tspl_event_register/5, tspl_formal_field/2,
				tspl_read_all/3, tspl_wildcard_field/1,
				tspl_tuple_field/3, tspl_wildcard_field/1,
				tspl_actual_field/3, tspl_tuple/2,
				tspl_disconnect/1]).

:- use_module(agents(resources), [agent_resource_required/2]).
:- use_module(agents(elo_fetch), [ts_elo_fetch/3]).
:- use_module(agents(agent_api), [agent_connect/3, agent_query/3, agent_tuple/3]).
:- use_module(agents(user_language), [scy_session_user_language/4]).
:- use_module(cmap(parse), [cmap_parse/4]).
:- use_module(cmap(model), [cmap_create/4, cmap_delete/1]).
:- use_module(utilities(xml), [atom_to_xml/3, source_to_xml/3]).
:- use_module(specification(reference_model), [reference_model/1, reference_model_parse/3]).
:- use_module(specification(term_set), [term_set/1, term_set_parse/3]).
:- use_module(specification(rule_set), [rule_set/1, rule_set_parse/3]).
:- use_module(specification(drag_and_drop), [drag_and_drop_evaluation/3]).



/*------------------------------------------------------------
 *  Initialization
 *------------------------------------------------------------*/

:- dynamic
	cme_tuple_space/3.		% Space {command, actions} -> TS x Server

cme_agent_start(Options) :-
	forall(cme_tuple_space(_,TS,_), tspl_disconnect(TS)),
	action_monitor(Options),
	command_monitor(Options),
	session_monitor(Options).

action_monitor(Options) :-
	option(server(Server), Options),
	agent_connect(Server, actions, TS),
	assert(cme_tuple_space(actions,TS,Server)),
	(   exists_directory('/home/anjo')
	->  debug(cme(dev)),
	    debug(cme(dev), '**** running in simulation mode ****~n', []),
	    agent_query(action, Query,
			[ action(elo_saved),
			  tool(conceptmap),
			  user('anjo@scy.collide.info')
			])
	;   agent_query(action, Query,
			[ action(elo_saved),
			  tool(conceptmap)
			])
	),
	Call = cme_action_callback,
	tspl_event_register(TS, write, Query, Call, _).

command_monitor(Options) :-
	option(server(Server), Options),
	agent_connect(Server, command, TS),
	assert(cme_tuple_space(command,TS,Server)),
	tspl_actual_field(string, cme_agent, F0),
	tspl_wildcard_field(WC),
	tspl_tuple([F0,WC], Query),
	Call = cme_command_callback,
	tspl_event_register(TS, write, Query, Call, _).

session_monitor(Options) :-
	option(server(Server), Options),
	agent_connect(Server, session, TS),
	assert(cme_tuple_space(session,TS,Server)).


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
	evaluate_command(Id, TermSet, RefMod, Method, XML, Value),
	evaluate_elo_markup(URI, Value, Method, TermSet, RefMod, none).
command_callback(evaluate_xml, Tuple) :- !,
	tspl_tuple_field(Tuple, 2, Id),
	tspl_tuple_field(Tuple, 3, TermSet),
	tspl_tuple_field(Tuple, 4, RefMod),
	tspl_tuple_field(Tuple, 5, Method),
	tspl_tuple_field(Tuple, 6, MapAtom),
	atom_to_xml(MapAtom, XML, []),
	evaluate_command(Id, TermSet, RefMod, Method, XML, _Value).
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
 *  Mark-up ELO
 *------------------------------------------------------------*/

evaluate_elo_markup(URI, Value, Method, TermSet, RefMod, RuleSet) :-
	tspl:uid(Id),
	tspl_actual_field(string, Id, F0),
	tspl_actual_field(string, 'roolo-agent', F1),
	tspl_actual_field(string, 'cmmetadata', F2),
	tspl_actual_field(string, URI, F3),
	term_to_atom(Value, AtomValue),
	tspl_actual_field(string, AtomValue, F4),
	tspl_actual_field(string, Method, F5),
	tspl_actual_field(string, TermSet, F6),
	tspl_actual_field(string, RefMod, F7),
	tspl_actual_field(string, RuleSet, F8),
	tspl_tuple([F0,F1,F2,F3,F4,F5,F6,F7,F8], Tuple),
	cme_tuple_space(command, TS, _),
	tspl_write(TS, Tuple).



/*------------------------------------------------------------
 *  Reply after an evaluate
 *------------------------------------------------------------*/

evaluate_command(Id, TermSet, RefMod, Method, XML, Value) :-
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

tspl:cme_action_callback(A, B, C, D) :-
	cme_action_callback(A, B, C, D).

cme_action_callback(write, _SeqId, [], [Tuple]) :-
	agent_tuple(action, Tuple, [action(Action)]),
	(   cme_action(Action, Tuple)
	->  true
	;   debug(cme(dev), 'cme_action_callback failed for ~w~n', [Action])
	),
	thread_self(Thread),
	thread_detach(Thread).

cme_action(elo_saved, Tuple) :-
	agent_tuple(action, Tuple,
		    [ user(User),
%		      tool(Tool),
		      mission(Mission),
		      session(Session),
		      elo(Elo),
		      attributes(Attributes)
		    ]),
	cme_tuple_space(session, SessionSpace, _),
	scy_session_user_language(SessionSpace, User, Language, [default(gr)]),
	option(mission_anchor_id(Anchor), Attributes, 'energyFactSheet'),	% TBD -- default
	anchor_properties(Anchor, Language, AnchorProps),
	memberchk(term_set(TS), AnchorProps),
	memberchk(reference_model(RM), AnchorProps),
	memberchk(rule_set(RS), AnchorProps),
	memberchk(method(Method), AnchorProps),
	cme_tuple_space(command, CommandSpace, _),
	ts_elo_fetch(CommandSpace, Elo, XML),
	cmap_parse(XML, Nodes, Edges, []),
	cmap_create(Map, Nodes, Edges, [cmap_xml(XML)]),
	debug(cme(dev), 'created cmap ~w~n', [Map]),
	cme_evaluate(Method, Map, TS, RM, RS, Evaluation),
	broadcast(show_gls(Map)),
	debug(cme(dev), '  eval = ~w~n', [Evaluation]),
	tspl:uid(Id),
	tspl_actual_field(string, notification, F0),
	tspl_actual_field(string, Id, F1),
	tspl_actual_field(string, User, F2),
	tspl_actual_field(string, Elo, F3),
	tspl_actual_field(string, cme_agent, F4),
	tspl_actual_field(string, Mission, F5),
	tspl_actual_field(string, Session, F6),
	method_anchor_evaluation(Method, Language, Anchor, Evaluation, Msg),
	atom_concat('message=', Msg, MsgAtt),
	tspl_actual_field(string, MsgAtt, F7),
	tspl_tuple([F0,F1,F2,F3,F4,F5,F6,F7], Notification),
	cme_tuple_space(command, CommandSpace, _),
	tspl_write(CommandSpace, Notification),
	debug(cme(dev), '   notification ~w written', [F7]).


/*------------------------------------------------------------
 *  Evaluation methods
 *------------------------------------------------------------*/

cme_evaluate(drag_and_drop, Map, TS, RM, _, Evaluation) :-
	agent_resource_required(term_set, TS),
	agent_resource_required(reference_model, RM),
	drag_and_drop_evaluation(Map, Evaluation,
				 [term_set(TS), reference_model(RM)]).
