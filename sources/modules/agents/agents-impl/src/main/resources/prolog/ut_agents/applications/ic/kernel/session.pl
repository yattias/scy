/*  $Id$
 *  
 *  File	session.pl
 *  Part of	IC - educational data mining
 *  Author	Anjo Anjewierden, a.a.anjewierden@utwente.nl
 *  Purpose	ADT for sessions
 *  Works with	SWI-Prolog (www.swi-prolog.org)
 *  
 *  Notice	Copyright (c) 2011  University of Twente
 *  
 *  History	19/04/08  (Created)
 *  		08/04/11  (Last modified)
 */ 

/*------------------------------------------------------------
 *  Directives
 *------------------------------------------------------------*/

:- module(session_kernel_ic,
	  [ session/1,				% Session
	    
	    session_create/2,			% Session x Properties
	    session_delete/1,			% Session
	    session_ensure/1,			% Session
	    
	    session_export/1,			% Stream
	    session_import_term/1,		% Term
	    
	    session_properties/2,		% Session -> Properties
	    session_property/2,			% Session x Property
	    session_propchk/2,			% Session x Property
	    session_add_properties/2,		% Session x Properties
	    session_set_properties/2,		% Session x Properties
	    
	    session_learner/2,			% Session x Learner
	    session_learner_property/3		% Session x Learner x Property
	  ]).

:- use_module(load).

	% session(create | delete | update, Session)
:- use_module(library(broadcast), [broadcast/1]).
:- use_module(library(lists), [append/3, member/2]).


/*------------------------------------------------------------
 *  Sessions
 *------------------------------------------------------------*/

%%	session(+Session:atom) is semidet.
%%	session(-Session:atom) is nondet.
%
%	Succeeds if Session is a session.  Sessions normally correspond with
%	learners working during a certain period of time.

session(Session) :-
	session(Session, _).


%%	session_create(+Session:atom, +Properties:list) is det.
%
%	Create a new Session with the given Properties.
%	Broadcasts =|session(create,Session)|=.

session_create(Session, Properties) :-
	nonvar(Session),
	session_delete(Session),
	assert(session(Session,Properties)),
	broadcast(session(create,Session)).

%%	session_delete(+Session:atom) is det.
%
%	Delete Session.
%	Broadcasts =|session(delete,Session)|=.

session_delete(Session) :-
	nonvar(Session),
	(   retract(session(Session,_))
	->  broadcast(session(delete,Session))
	;   true
	).
	

%%	session_properties(+Session:atom, -Properties:list) is semidet.
%%	session_properties(-Session:atom, -Properties:list) is nondet.
%
%	Succeeds if Session is a session with Properties.  Some common
%	properties are learner(Learner), test(When,Type,Score), log_file(File)
%	and so forth.

session_properties(Session, Props) :-
	session(Session, Props).


%%	session_export(+Stream:stream) is det.
%
%	Writes all session to Stream, normally called by ic_export/2.

session_export(Stream) :-
	forall(session(Session, Props),
	       format(Stream, 'session(~q, ~q).~n',
		      [Session,Props])).

session_import_term(session(Session,Props)) :-
	session_create(Session, Props).


%%	session_ensure(+Session:atom) is det.
%
%	Makes sure that Session exists, may call session_create/2.

session_ensure(Session) :-
	nonvar(Session),
	(   session(Session, _)
	;   session_create(Session, [])
	), !.


%%	session_set_properties(+Session:atom, +Properties:list) is det.
%
%	Sets the Properties for the Session, may call session_create/2.
%	Broadcasts =|session(update,Session,OldProperties)|=.

session_set_properties(Session, Properties) :-
	nonvar(Session),
	(   retract(session(Session,Old))
	->  assert(session(Session,Properties)),
	    broadcast(session(update,Session,Old))
	;   session_create(Session, Properties)
	).


%%	session_add_properties(+Session:atom, +Properties:list) is det.
%
%	Add the Properties for the Session, may call session_create/2.
%	Broadcasts =|session(update,Session,OldProperties)|=.

session_add_properties(Session, Properties) :-
	nonvar(Session),
	(   retract(session(Session,Old))
	->  append(Old, Properties, New),
	    assert(session(Session,New)),
	    broadcast(session(update,Session,Old))
	;   session_create(Session, Properties)
	).


%%	session_property(?Session:atom, ?Property:term) is semidet.
%
%	Succeeds if Property belongs to Session.

session_property(Session, Property) :-
	session(Session, Properties),
	member(Property, Properties).


%%	session_property(?Session:atom, ?Property:term) is semidet.
%
%	Succeeds if Property belongs to Session.

session_propchk(Session, Property) :-
	session(Session, Properties),
	memberchk(Property, Properties).



%%	session_learner_property(+Session:atom, +Learner:atom, -Property:term) is semidet.
%
%	Succeeds if Property belongs to Learner in Session.  This assumes that
%	learner_info(Learner,LearnerProps) is a property of Session.

session_learner_property(Session, Learner, Property) :-
	session(Session, Properties),
	member(learner_info(Learner,LearnerProps), Properties),
	member(Property, LearnerProps).


%%	session_learner(+Session:atom, -Learner:atom) is nondet.
%
%	Succeeds if Learner participated in Session.

session_learner(Session, Learner) :-
	session_propchk(Session, learner(Learner)).
session_learner(Session, Learner) :-
	session_property(Session, learner_info(Learner,_)).


/*------------------------------------------------------------
 *  Storage
 *------------------------------------------------------------*/

:- dynamic
	session/2.				% Session x Properties

:- initialization
	listen(clear_cache_all, clear_cache_all).

clear_cache_all :-
	retractall(session(_,_)).
