/*  $Id$
 *  
 *  File	votat_agent.pl
 *  Author	Stefan Weinbrenner, weinbrenner@collide.info
 *  Purpose	Read SCY action log and calculate the VOTAT value
 *  Works with	SWI-Prolog (www.swi-prolog.org)
 *  
 *  Notice	Copyright (c) 2009  Collide Research Group
 *  
 *  History	30/11/09  (Created)
 *  		30/11/09  (Last modified)
 */ 

/*------------------------------------------------------------
 *  Directives
 *------------------------------------------------------------*/

%:- module(same_model_run_agents_ic,
%	  [ same_model_run_agent/1
%	  ]).

%:- use_module(library(tspl)).	% TupleSpace interface to Prolog
:- consult('tspl_sqls_mt.pl').
/**	<module>Same model run agent

This module implements a pedagogical agent which counts how often a learner runs
a (system dynamics) model and how often a different model is run.  The output
is returned as a tuple with the following fields:

  | votat |
  | Learner |
  | Tool |
  | Mission |
  | VotatValue |

The pedagogical relevance is, of course, that learners should not run the
same model many times (as they get no new information from doing so).  For
example if runs=50 and unique=5 then the learner has only considered 5 different
models in 50 runs.

@author	Stefan Weinbrenner
*/

votat_agent :-
	agent_connect(InTS, _, [user('votat')]),
	agent_register(InTS,
		       [ tuple_type(action),
			 action_type('value_changed'),
			 callback(change_variables_callback)
		       ]),
	agent_register(InTS,
		       [ tuple_type(action),
			 action_type('variables_selected'),
			 callback(process_variables_callback)
		       ]).

process_variables_callback(write, _SeqId, [], [Tuple]) :-
	tspl_tuple_field(Tuple, 4, Learner),
	tspl_tuple_field(Tuple, 8, ELOURI),
	log_key_value(Tuple, variables, ValidVars),
	csv_to_list(ValidVars, ValidVarsList),
	retractall(valid_vars(Learner, ELOURI, _)),
	assert(valid_vars(Learner, ELOURI, ValidVarsList)),
	thread_self(Thread),
	thread_detach(Thread).

change_variables_callback(write, _SeqId, [], [Tuple]) :-
	tspl_tuple_field(Tuple, 2, Time),
	tspl_tuple_field(Tuple, 4, Learner),
	tspl_tuple_field(Tuple, 5, Tool),
	tspl_tuple_field(Tuple, 6, Mission),
	tspl_tuple_field(Tuple, 7, Session),
	tspl_tuple_field(Tuple, 8, ELOURI),
	log_key_value(Tuple, name, VarName),
	valid_vars(Learner, ELOURI, ValidVars),
	member(VarName, ValidVars),
	change_variables_evaluation(Learner, Tool, ELOURI, VarName, Time, Votat),
	change_variables_feedback(Learner, Tool, Mission, Session, ELOURI, Votat), !,
	thread_self(Thread),
	thread_detach(Thread).

change_variables_callback(_, _, _, _) :-
	thread_self(Thread),
	thread_detach(Thread).

csv_to_list(Text, [TextTrimmed]) :-
	not(sub_string(Text, _, 1, _, ',')),
	trim_text(Text, TextTrimmed).

csv_to_list(CSV, List) :-
	sub_string(CSV, Pos, 1, After, ','),
	sub_string(CSV, 0, Pos, _, FirstPart),
	Pos1 is Pos + 1,
	sub_string(CSV, Pos1, After, 0, SecondPart),
	csv_to_list(FirstPart, FirstList),
	csv_to_list(SecondPart, SecondList),
	append(FirstList, SecondList, List).

trim_text(Text, TextAtom) :-
	not(sub_string(Text, 0, 1, _, ' ')),
	not(sub_string(Text, _, 1, 0, ' ')),
	string_to_atom(Text, TextAtom).
	
trim_text(Text, TrimmedText) :-
	string_concat(' ', TrimmedTextAux, Text),
	trim_text(TrimmedTextAux, TrimmedText).
trim_text(Text, TrimmedText) :-
	string_concat(TrimmedTextAux, ' ', Text),
	trim_text(TrimmedTextAux, TrimmedText).



:- dynamic
	var_change/5.

vc_timeout_min(1).

% Calculate from last
% This is just another way of calculating a VOTAT value
% alternative implementation for the next predicate
change_variables_evaluation_last(Learner, Tool, ELOURI, VarName, Time, Votat) :-
	assert(var_change(Learner,Tool, ELOURI, Time, VarName)),
	remove_old_changes,
	findall(var_change(Learner, Tool, ELOURI, TimeX, VarNameX), var_change(Learner, Tool, ELOURI, TimeX, VarNameX), VCs),
	findall(var_change(Learner, Tool, ELOURI, TimeX, VarName), (member(var_change(Learner, Tool, ELOURI, TimeX, VarName), VCs)), SameVCs),
	length(VCs, VCsLength),
	length(SameVCs, SameVCsLength),
	Votat is SameVCsLength / VCsLength,
	write(SameVCsLength), write(' / '), write(VCsLength), write(' => '), writeln(Votat).


% Calculate from average
change_variables_evaluation(Learner, Tool, ELOURI, VarName, Time, Votat) :-
	assert(var_change(Learner,Tool, ELOURI, Time, VarName)),
	remove_old_changes,
	findall(VarNameX, var_change(Learner, Tool, ELOURI, _, VarNameX), VarNames),
	number_of_switches(VarNames, Switches),
	(Switches < 3
	->	
		(
			Votat is 1,
			write(Switches), writeln(' switches => 1')
		)
		;
		(
			most_often(_, VarNames, Count),
			length(VarNames, VarNamesLength),
			Votat is Count / VarNamesLength,
			write(Count), write(' / '), write(VarNamesLength), write(' => '), writeln(Votat)
		)
	).
	
number_of_switches([H|T], Number) :-
	next_switch(H, T, Number).

next_switch(_, [], 0).
next_switch(Last, [Last|T], Number) :-
	next_switch(Last, T, Number).
next_switch(Last, [H|T], NewNumber) :-
	Last \== H,
	next_switch(H, T, Number),
	NewNumber is Number + 1.
	

most_often(VarName, VarNames, MaxCount) :- 
	retractall(count(_, _)),
	forall(
	  member(VN, VarNames), 
	  (
	      (retract(count(VN, Count)),NewCount is Count + 1, assert(count(VN,NewCount)))
	      ;
	      assert(count(VN, 1))
	  )
	),
	findall(Counter, count(_, Counter), Counters),
	max_list(Counters, MaxCount),
	count(VarName, MaxCount),
	retractall(count(_, _)).

remove_old_changes :-
	vc_timeout_min(Timeout),
	get_time(CurrTime),
	findall(var_change(L, T, S, Time, V), (var_change(L, T, S, Time, V), Time < CurrTime * 1000 - Timeout * 60000), DelVCs),
	write('Expiring: '), writeln(DelVCs),
	forall(member(DelVC, DelVCs), retract(DelVC)).

change_variables_feedback(Learner, Tool, Mission, Session, ELOURI, Votat) :-
	% Create fields for the feedback 
	get_time(T1), T is T1 * 1000, sformat(Time, '~0f', [T]),
	tspl_actual_field(string, votat, F0),
	tspl_actual_field(string, Learner, F1),
	tspl_actual_field(string, Tool, F2),
	tspl_actual_field(string, Mission, F3),
	tspl_actual_field(string, Session, F4),
	tspl_actual_field(string, ELOURI, F5),
	tspl_actual_field(long, Time, F6),
	tspl_actual_field(double, Votat, F7),
	tspl_tuple([F0,F1,F2,F3,F4,F5,F6,F7], Response),
	out_ts(TS),
	tspl_write(TS, Response).


/*------------------------------------------------------------
 *  Connect to the TupleSpace
 *------------------------------------------------------------*/

in_space(actions).			% scydynamics_actionlog
out_space(command).			% scydynamics_actionlog
host('localhost').		% localhost
port(2525).
user('sqlspaces').

:- dynamic
	in_ts/1.			% TupleSpace
:- dynamic
	out_ts/1.			% TupleSpace

agent_connect(InTS, OutTS, Options) :-
	retractall(ts(_)),	% Assuming a single TupleSpace
	(memberchk(in_space(InSpace), Options); in_space(InSpace)),
	(memberchk(out_space(OutSpace), Options); out_space(OutSpace)),
	(memberchk(host(Host), Options); host(Host)),
	(memberchk(port(Port), Options); port(Port)),
	(memberchk(user(User), Options); user(User)),
	tspl_connect_to_ts(InSpace, InTS, [host(Host), port(Port), user(User), password('')]), !,
	tspl_connect_to_ts(OutSpace, OutTS, [host(Host), port(Port), user(User), password('')]), !,
	assert(in_ts(InTS)),
	assert(out_ts(OutTS)).

/*------------------------------------------------------------
 *  Access key-value pairs
 *------------------------------------------------------------*/

log_key_value(Tuple, Key, Value) :-
	string_concat(Key, '=', KeyEq),
	tspl_tuple_field(Tuple, Num, FieldVal),
	Num > 8,
	string_concat(KeyEq, ValueStr, FieldVal),
	string_to_atom(ValueStr, Value).

	
/*------------------------------------------------------------
 *  Register a callback
 *------------------------------------------------------------*/
 
agent_register(TS, Options) :-
	(   memberchk(tuple_type(TT), Options)
	->  tspl_actual_field(string, TT, F0)
	;   tspl_formal_field(string, F0)
	),
	(   memberchk(id(Id), Options)
	->  tspl_actual_field(string, Id, F1)
	;   tspl_formal_field(string, F1)
	),
	(   memberchk(date(Date), Options)
	->  tspl_actual_field(long, Date, F2)
	;   tspl_formal_field(long, F2)
	),
	(   memberchk(action_type(AT), Options)
	->  tspl_actual_field(string, AT, F3)
	;   tspl_formal_field(string, F3)
	),
	(   memberchk(user(User), Options)
	->  tspl_actual_field(string, User, F4)
	;   tspl_formal_field(string, F4)
	),
	(   memberchk(tool(Tool), Options)
	->  tspl_actual_field(string, Tool, F5)
	;   tspl_formal_field(string, F5)
	),
	(   memberchk(mission(Mission), Options)
	->  tspl_actual_field(string, Mission, F6)
	;   tspl_formal_field(string, F6)
	),
	(   memberchk(session(Session), Options)
	->  tspl_actual_field(string, Session, F7)
	;   tspl_formal_field(string, F7)
	),
	(   memberchk(elo(EloURI), Options)
	->  tspl_actual_field(string, EloURI, F8)
	;   tspl_formal_field(string, F8)
	),
	tspl_wildcard_field(F9),
	tspl_tuple([F0,F1,F2,F3,F4,F5,F6,F7,F8,F9], Query),
	memberchk(callback(Call), Options),
	tspl_event_register(TS, write, Query, Call, _).
