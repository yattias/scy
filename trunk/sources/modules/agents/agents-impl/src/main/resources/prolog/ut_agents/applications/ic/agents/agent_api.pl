/*  $Id$
 *
 *  File	agent_api.pl
 *  Part of	IC - educational data mining
 *  Author	Anjo Anjewierden, a.a.anjewierden@utwente.nl
 *  Purpose	Generic predicates for agent communication
 *  Works with	SWI-Prolog (www.swi-prolog.org)
 *
 *  Notice	Copyright (c) 2011  University of Twente
 *
 *  History	06/06/11  (Created)
 *  		06/06/11  (Last modified)
 */

/*------------------------------------------------------------
 *  Directives
 *------------------------------------------------------------*/

:- module(agent_api,
	  [ agent_server/3,			% Server x Host x Port
	    agent_connect/3,			% Server x Space -> TS
	    agent_query/3,			% Type -> Query <- Options
	    agent_tuple/3			% Type x Tuple -> Options
	  ]).

:- use_module(load).

:- use_module(library('dialog/pretty_print'), [pretty_print/1]).

:- use_module(utilities(uuid), [uuid/1]).

:- use_module(sqlspaces(tspl), [tspl_connect_to_ts/3, tspl_formal_field/2,
				tspl_tuple_field/3, tspl_wildcard_field/1,
				tspl_tuple/2, tspl_actual_field/3]).

:- dynamic
	agent:server/3.

:- multifile
	agent:server/3.

agent:server(local, 'localhost', 2525).
agent:server(twente2, 'scyserver2.gw.utwente.nl', 2525).
agent:server(twente3, 'scyserver3.gw.utwente.nl', 2525).
agent:server(collide, 'scy.collide.info', 2525).


%%	agent_server(+Server:atom, -Host:atom, -Port:int) is semidet.
%
%	Succeeds if Server is the symbolic name of a (SCY) server with the given
%	Host and Port.

agent_server(Server, Host, Port) :-
	agent:server(Server, Host, Port).


%%	agent_connect(+Server:atom, +Space:atom, -TS:ts_handle) is semidet.
%
%	Connect to tuple space Space on Server and return handle TS.
%	Space is normally =actions= or =notification=.

agent_connect(Server, _Space, _TS) :-
	\+ agent_server(Server, _, _), !,
	format('Server ~w not found~n', [Server]),
	format('The following servers are available~n', []),
	forall(agent_server(S,Host,Port),
	       format('   ~w (at ~w:~w)~n', [S,Host,Port])),
	fail.
agent_connect(Server, Space, TS) :-
	agent_server(Server, Host, Port),
	tspl_connect_to_ts(Space, TS, [host(Host), port(Port), user('cme'), password('')]).

	

%%	agent_query(+Type:atom, -Query:term, +Options:list) is semidet.
%
%	Creates a tuple Query.  When Type is =action=, Options are:
%
%	* action(Action)
%	* user(User)
%	* tool(Tool)
%	* mission(Mission)
%	* session(Session)
%	* elo(Elo)

agent_query(action, Query, Options) :- !,
	tspl_actual_field(string, action, F0),	% "action"
	tspl_formal_field(string, F1),		% Id
	tspl_formal_field(long,   F2),		% Millis
	(   memberchk(action(Action), Options)	% Action
	->  tspl_actual_field(string, Action, F3)
	;   tspl_formal_field(string, F3)
	),
	(   memberchk(user(User), Options)	% User
	->  tspl_actual_field(string, User, F4)
	;   tspl_formal_field(string, F4)
	),
	(   memberchk(tool(Tool), Options)	% Tool
	->  tspl_actual_field(string, Tool, F5)
	;   tspl_formal_field(string, F5)
	),
	(   memberchk(mission(Mission), Options)% Mission
	->  tspl_actual_field(string, Mission, F6)
	;   tspl_formal_field(string, F6)
	),
	(   memberchk(session(Session), Options)% Session
	->  tspl_actual_field(string, Session, F7)
	;   tspl_formal_field(string, F7)
	),
	(   memberchk(elo(Elo), Options)	% ELO
	->  tspl_actual_field(string, Elo, F8)
	;   tspl_formal_field(string, F8)
	),
	tspl_wildcard_field(Args),
	tspl_tuple([F0,F1,F2,F3,F4,F5,F6,F7,F8,Args], Query).
agent_query(request_elo, Query, Options) :- !,
	uuid(Id),
	tspl_actual_field(string, Id, F0),
	memberchk(id(Id), Options),
	tspl_actual_field(string, 'roolo-agent', F1),
	tspl_actual_field(string, elo, F2),
	memberchk(elo_uri(URI), Options),
	tspl_actual_field(string, URI, F3),
	tspl_tuple([F0,F1,F2,F3], Query).
agent_query(read_elo, Query, Options) :- !,
	memberchk(id(Id), Options),
	tspl_actual_field(string, Id, F0),
	tspl_actual_field(string, 'roolo-response', F1),
	tspl_formal_field(string, F2),
	tspl_tuple([F0,F1,F2], Query).
agent_query(wildcard, Query, _Options) :- !,
	tspl_wildcard_field(WC),
	tspl_tuple([WC], Query).
agent_query(Type, _Query, _Options) :-
	format('agent_query: unknown type ~w~n', [Type]),
	fail.


%%	agent_tuple(+What:atom, +Tuple:term, +Options:list) is semidet.
%
%	Extracts fields from Tuple according to Options.
%
%	* id(Id)
%	* millis(Millis)
%	* action(Action)
%	* user(User)
%	* tool(Tool)
%	* mission(Mission)
%	* session(Session)
%	* elo(Elo)
%	* attributes(Atts)

agent_tuple(action, Tuple, Options) :- !,
	(   var(Options)
	->  Options = [ id(_), millis(_), action(_), user(_), tool(_), mission(_),
			session(_), elo(_), attributes(_)
		      ]
	;   true
	),
	action_extract(Options, Tuple).
agent_tuple(read_elo, Tuple, Options) :- !,
	(   var(Options)
	->  Options = [id(_), elo_xml(_)]
	;   true
	),
	read_elo_extract(Options, Tuple).
agent_tuple(Type, _Tuple, _Options) :-
	format('agent_tuple: unknown type ~w~n', [Type]),
	trace,
	fail.


/*------------------------------------------------------------
 *  Extract action
 *------------------------------------------------------------*/

action_extract([], _).
action_extract([H|T], Tuple) :-
	action_extract_h(H, Tuple), !,
	action_extract(T, Tuple).
action_extract([H|T], Tuple) :-
	format(' ***** action extract failed for ******~n', [H]),
	pretty_print(H),
	action_extract(T, Tuple).

action_extract_h(id(Id), Tuple) :-
	tspl_tuple_field(Tuple, 1, Id).
action_extract_h(millis(Millis), Tuple) :-
	tspl_tuple_field(Tuple, 2, Millis).
action_extract_h(action(Action), Tuple) :-
	tspl_tuple_field(Tuple, 3, Action).
action_extract_h(user(User), Tuple) :-
	tspl_tuple_field(Tuple, 4, User).
action_extract_h(tool(Tool), Tuple) :-
	tspl_tuple_field(Tuple, 5, Tool).
action_extract_h(mission(Mission), Tuple) :-
	tspl_tuple_field(Tuple, 6, Mission).
action_extract_h(session(Session), Tuple) :-
	tspl_tuple_field(Tuple, 7, Session).
action_extract_h(elo(Elo), Tuple) :-
	tspl_tuple_field(Tuple, 8, Elo).
action_extract_h(attributes(AVs), Tuple) :-
	tuple_attribute_values(Tuple, 9, AVs).


/*------------------------------------------------------------
 *  Read ELO
 *------------------------------------------------------------*/

read_elo_extract([], _).
read_elo_extract([H|T], Tuple) :-
	read_elo_extract_h(H, Tuple),
	read_elo_extract(T, Tuple).

read_elo_extract_h(id(Id), Tuple) :-
	tspl_tuple_field(Tuple, 0, Id).
read_elo_extract_h(elo_xml(XML), Tuple) :-
	tspl_tuple_field(Tuple, 2, XML).



tuple_attribute_values(Tuple, N, AVs) :-
	tuple_avs(N, Tuple, AVs).

tuple_avs(N, Tuple, [AttValue|AVs]) :-
	tspl_tuple_field(Tuple, N, AV), !,
	sub_atom(AV, B,_,A, '='), !,
	sub_atom(AV, 0,B,_, Att),
	sub_atom(AV, _,A,0, Value),
	functor(AttValue, Att, 1),
	arg(1, AttValue, Value),
	Next is N + 1,
	tuple_avs(Next, Tuple, AVs).
tuple_avs(_, _, []).
