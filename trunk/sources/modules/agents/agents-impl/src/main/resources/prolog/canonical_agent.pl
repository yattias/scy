/*  $Id$
 *  
 *  File	canonical_agent.pl
 *  Author	Stefan Weinbrenner, weinbrenner@collide.info
 *  Purpose	Read SCY action log and calculate the IncChange value
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

  | UUID |
  | feedback |
  | Date |
  | same_model_run |
  | Learner |
  | Tool |
  | Mission |
  | <same_model_run runs="#runs" unique="#unique"/> |

The pedagogical relevance is, of course, that learners should not run the
same model many times (as they get no new information from doing so).  For
example if runs=50 and unique=5 then the learner has only considered 5 different
models in 50 runs.

@author	Stefan Weinbrenner
*/


%%	same_model_run_agent(-TS:handle) is det.
%
%	Starts the agent by connecting to the TupleSpaces and registering that
%	agent is interested in =run_model= actions from the SCY Dynamics tool.

canonical_agent :-
	agent_connect(InTS, _, [user('canonical agent')]),
	agent_register(InTS,
		       [ tuple_type(action),
			 action_type('value_changed'),
			 callback(change_variables_callback)
		       ]).


%%	same_model_run_callback(+Command:atom, +SeqId:int, +Before:list, +After:list) is det.
%
%	This function is called when a =run_model= action is written in the
%	TupleSpace.  We extract the run_model Action itself and from the action
%	the Model being run.  Then the evaluation is performed and a feedback
%	tuple is put in the TupleSpace.
	
	
	
ignore_vars(['Mtot']).
	
change_variables_callback(write, _SeqId, [], [Tuple]) :-
	ignore_vars(IgVars),
	tspl_tuple_field(Tuple, 2, Time),
	tspl_tuple_field(Tuple, 4, Learner),
	tspl_tuple_field(Tuple, 5, Tool),
	tspl_tuple_field(Tuple, 7, Session),
	log_key_value(Tuple, name, VarName),
	not(member(VarName, IgVars)),
	log_key_value(Tuple, newValue, NewValueStr),
	term_to_atom(NewValue, NewValueStr),
	log_key_value(Tuple, oldValue, OldValueStr),
	term_to_atom(OldValue, OldValueStr),
	Diff is abs(OldValue - NewValue),
	change_variables_evaluation(Learner, Tool, Session, VarName, Diff, Time, IncChange),
	change_variables_feedback(Learner, Tool, Session, IncChange), !,
	thread_self(Thread),
	thread_detach(Thread).

change_variables_callback(Arg1, Arg2, Arg3, Arg4) :-
	thread_self(Thread),
	thread_detach(Thread).


%%	same_model_run_evaluation(+Model:atom, +Learner:atom, -NoRuns:int, -NoUnique:int) is det.
%
%	Model is the model of the current run, it is added to the model run
%	history for this learner.  Then count the total number of runs
%	(NoRuns) and the number of unique runs (NoUnique).  The hard
%	work is performed by sdm_model_identical/3, which is in the SDM library.

:- dynamic
	inc_change/5.	% Learner x Model

vc_timeout_min(1).

change_variables_evaluation(Learner, Tool, Session, VarName, Diff, _, IncChange) :-
	write('next run for '), writeln(VarName),
	retract(inc_change(Learner, Tool, Session, VarNameOld, DiffOld)),
	assert(inc_change(Learner, Tool, Session, VarName, Diff)),
	VarName == VarNameOld,
	(   Diff == DiffOld
	->  IncChange = 100
	;   IncChange = 0
	).

change_variables_evaluation(Learner, Tool, Session, VarName, Diff, _, _) :-
	write('asserting first for '), writeln(VarName),
	assert(inc_change(Learner, Tool, Session, VarName, Diff)), 
	!, 
	fail.



%%	same_model_run_feedback(+NoRuns:int, +NoUnique:int, +Tuple:term) is det.
%
%	Creates a feedback tuple using the original action Tuple as template
%	for some of the fields.

change_variables_feedback(Learner, Tool, Session, IncChange) :-
	% Create fields for the feedback 
	get_time(Time),
	tspl_actual_field(string, inc_change, F0),
	tspl_actual_field(string, Learner, F1),
	tspl_actual_field(string, Tool, F2),
	tspl_actual_field(string, Session, F3),
	tspl_actual_field(double, IncChange, F4),
	tspl_actual_field(long, Time, F5),
	tspl_tuple([F0,F1,F2,F3,F4,F5], Response),
	out_ts(TS),
	tspl_write(TS, Response).


/*************************************************************
  ALL CODE THAT FOLLOWS IS SHARED BY ALL PROLOG BASED AGENTS
 ************************************************************/


/*------------------------------------------------------------
 *  Fields of an action tuple
 *------------------------------------------------------------*/

field(tuple_type, 0).	% =action= for learner actions and =feedback= for feedback to the tool
field(id, 1).		% uuid
field(date, 2).		% date and time in DC format
field(action_type, 3).	% type of the action (depends on tool)
field(user, 4).		% user name (should be unique!)
field(tool, 5).		% tool name
field(mission, 6).	% mission name
field(session, 7).	% session id
field(datatype, 8).	% datatype of the data
field(data, 9).		% the data itself (arbitrary)


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
	tspl_connect_to_ts(InSpace, Host, Port, User, '', InTS), !,
	tspl_connect_to_ts(OutSpace, Host, Port, User, '', OutTS), !,
	assert(in_ts(InTS)),
	assert(out_ts(OutTS)).

/*------------------------------------------------------------
 *  Access key-value pairs
 *------------------------------------------------------------*/

test(Tuple) :-
	in_ts(TS),
	tspl_actual_field(string, action, F0),
	tspl_wildcard_field(F1),
	tspl_tuple([F0, F1], T),
	tspl_read(TS, T, Tuple).

log_key_value(Tuple, Key, Value) :-
	string_concat(Key, '=', KeyEq),
	tspl_tuple_field(Tuple, Num, FieldVal),
	Num > 9,
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
	(   memberchk(session(Action), Options)
	->  tspl_actual_field(string, Action, F7)
	;   tspl_formal_field(string, F7)
	),
	(   memberchk(datatype(Action), Options)
	->  tspl_actual_field(string, Action, F8)
	;   tspl_formal_field(string, F8)
	),
	(   memberchk(data(Action), Options)
	->  tspl_actual_field(string, Action, F9)
	;   tspl_formal_field(string, F9)
	),
	tspl_wildcard_field(F10),
	tspl_tuple([F0,F1,F2,F3,F4,F5,F6,F7,F8,F9,F10], Query),
	memberchk(callback(Call), Options),
	tspl_event_register(TS, write, Query, Call, _).
