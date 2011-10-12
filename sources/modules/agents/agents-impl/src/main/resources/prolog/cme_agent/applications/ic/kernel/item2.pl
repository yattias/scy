/*  $Id$
 *  
 *  File	item2.pl
 *  Part of	IC - educational data mining
 *  Author	Anjo Anjewierden, a.a.anjewierden@utwente.nl
 *  Purpose	API for item2s
 *  Works with	SWI-Prolog (www.swi-prolog.org)
 *  Status	Checked, documented    
 *  
 *  Notice	Copyright (c) 2008, 2009, 2010, 2011  University of Twente
 *  
 *  History	08/01/08  (Created)
 *  		07/02/11  (Last modified)
 */ 

/*------------------------------------------------------------
 *  Directives
 *------------------------------------------------------------*/

:- module(item2_kernel_ic,
	  [	
		% Creation
	    item2_create/4,		% TimeStamp x Type x Properties -> Item
	    item2_delete/1,		% Item

		% Import/Export
	    item2_export/1,		% Stream
	    item2_import_term/1,	% Item x TimeStamp x Type x Properties

		% Assessors
%	    item2/4,			% Item -> TimeStamp x Type x Properties
	    item2/1,			% Item
	    item2_time_stamp/2,		% Item -> TimeStamp
	    item2_type/2,		% Item -> Type
	    item2_properties/2,		% Item -> Properties
	    item2_property/2,		% Item x Property
	    item2_propchk/2,		% Item x Property
	    item2_propertychk/2,	% Item x Property	-- deprecated
	    item2_delete_property/2,	% Item x Property
	    item2_add_property/2,	% Item x Property
	    item2_add_properties/2,	% Item x Properties

		% Common properties
	    item2_nth/2,		% Item -> Nth
	    item2_assignment/2,		% Item -> Assignment
	    item2_set_assignment/2,	% Item x Assignment
	    item2_session/2,		% Item -> Session
	    item2_coding/2,		% Item -> Code
	    item2_coding/3,		% Item -> CodeType x Code

		% Ordering
	    item2_set_order/1,		% Items
	    item2_clear_order/1,	% Items
	    item2_next/2,		% Item -> Next
	    item2_prev/2,		% Item -> Prev

		% Date and time
	    item2_time/2,		% Item -> time(H,M,S)
	    item2_date/2,		% Item -> date(Y,M,D)
	    item2s_sub_sessions/3,	% Items -> SubSessions <- Options // time_gap(Days)

		% Models (mostly for System Dynamics)
	    item2_model/2,		% Item -> Model
	    item2p_model/2,		% Item -> Model

		% Window layout
	    item2_window_layout/2,	% Item -> WindowLayout
	    item2p_window_layout/2,	% Item -> WindowLayout

		% Phases
	    item2p_phase/2,		% Item -> Phase
	    item2s_in_phases/3,		% Items x Phases -> PhasesItems

	    item2_variable_value/3,	% Item x Variable -> Value

	    item2_chat_sender_text/3,	% Item -> Sender x Text
	    item2_chat_text/2,		% Item -> Text
	    item2_correct_chat/2,	% Item x NewText
	    item2_sender/2,		% Item -> Sender
	    item2_sender_code/2,	% Item -> Code
	    item2_sender_code/3,	% Item -> Code

	    item2_sequence_first_answer/2,	% Items -> Answer
	    item2_first_answer_sequence/3,	% Items -> Items x Answer

	    item2s_time_window/4,	% Items -> Inside x Time1 x Time2
	    item2s_time_bounds/3	% Items -> StartTimeStamp x EndTimeStamp
	  ]).

:- use_module(load).

:- use_module(library(broadcast), [listen/2]).
:- use_module(library(debug), [debug/3]).		% kernel
:- use_module(library(lists), [append/3, member/2, last/2]).
:- use_module(library(option), [option/3]).


/** <module> Storage and access to individual actions

This module provides low-level storage and access to individual actions.  In
accordance with data mining terminology these representations are called items
and the predicates that deal with them start with *item2* (the 2 is added to
avoid name clashes).  See item2_create/4 for a description of the general
format.

Although the representation is supposed to be generic, there are some access
predicates that are specific to certain types of learning environments (e.g.,
related to chat messages or models).

In some situations the invididual item2 does not contain all information
for the context of an action.  For these case, predicates starting with *item2p* are
provided.  The *p* denotes previous and relevant information is computed by
accessing previous item(s).  For example, to find out the window layout
for a given item2, item2p_window_layout/2 first looks whether the given item2
contains a window layout, and if not it uses item2_prev/2 to find the previous
item until a window layout is found.  For the *item2p* predicates to work it
is necessary to store the ordering of the items with item2_set_order/1.

@author		Anjo Anjewierden
@license	GPL
*/

/*------------------------------------------------------------
 *  Storage
 *------------------------------------------------------------*/

%%	item2(+Item:int, -TimeStamp:int, -Type:atom, -Properties:list) is semidet.
%
%	Item is the identifier of an item.  See item2_create/4 for a
%	description of Item.


%%	item2(-Item:int) is nondet.
%
%	Succeeds if Item is a known item.

item2(Item) :-
	item2(Item, _, _, _).


%%	item2_create(+TimeStamp:int, +Type:atom, +Properties:list, -Item:int) is det.
%
%	Creates and stores a new item, returning a unique Item.  The
%	argumentsare:
%
%	* TimeStamp: Time in Prolog time stamp format.  See item2_time_stamp/2.
%	* Type: Action type.  See item2_type/2.
%	* Properties: Arguments for the item.  See item2_properties/2.

item2_create(TimeStamp, Type, Properties, Item) :-
	flag(item2_index, Item, Item+1),
	assert(item2(Item,TimeStamp,Type,Properties)).


%%	item2_delete(+Item:int) is det.
%
%	Deletes the Item.  If Item is a variable all item2s are deleted.

item2_delete(Item) :-
  format('deleting item ~w ***************************~n', [Item]),
  trace,
	retractall(item2(Item,_,_,_)).


/*------------------------------------------------------------
 *  Import and export
 *------------------------------------------------------------*/

%%	item2_import_term(+Term:term) is det.
%
%	Import Item with the given TimeStamp, Type, and Properties.  This
%	predicate is used for files created using item2_export/2.  Effectively,
%	this makes fast loading of pre-processed log files possible.

item2_import_term(item2(Item, TimeStamp, Type, Properties)) :-
	assertz(item2(Item,TimeStamp,Type,Properties)).


%%	item2_export(+Stream:stream) is det.
%
%	Output all item2's on Stream.

item2_export(Stream) :-
	forall(item2(Item, TimeStamp, Type, Properties),
	       format(Stream, 'item2(~q, ~q, ~q, ~q).~n',
		      [Item,TimeStamp,Type,Properties])).


%%	item2_time_stamp(+Item:int, -Time:real) is det.
%
%	Item has Time as the timestamp.

item2_time_stamp(Item, TimeStamp) :-
	item2(Item, TimeStamp, _, _).


%%	item2_type(+Item:int, -Type:atom) is det.
%
%	Item has Type as the action type.

item2_type(Item, Type) :-
	item2(Item, _, Type, _).


%%	item2_properties(+Item:int, -Properties:list) is det.
%
%	Item has list of Properties.  Each property is a term and the
%	functor/arity is unique for most properties.

item2_properties(Item, Props) :-
	item2(Item, _, _, Props).


%%	item2_property(+Item:int, -Property:term) is nondet.
%%	item2_propchk(+Item:int, -Property:term) is semidet.
%%	item2_propertychk(+Item:int, -Property:term) is semidet.
%
%	Succeeds if Item has Property.  item2_propertychk/2 is deprecated.

item2_property(Item, Property) :-
	item2_properties(Item, Props),
	member(Property, Props).

item2_propchk(Item, Property) :-
	item2_properties(Item, Props),
	memberchk(Property, Props).

item2_propertychk(Item, Property) :-
	item2_properties(Item, Props),
	memberchk(Property, Props).


%%	item2_add_property(+Item:int, +Property:term) is det.
%
%	Property is added to Item.

item2_add_property(Item, Prop) :-
	retract(item2(Item, Time, Type, OldProps)),
	assert(item2(Item, Time, Type, [Prop|OldProps])).


item2_add_properties(Item, Props) :-
	retract(item2(Item, Time, Type, OldProps)),
	append(Props, OldProps, NewProps),
	assert(item2(Item,Time,Type,NewProps)).


%%	item2_delete_property(+Item:int, +Property:term) is det.
%
%	Property is deleted from Item.

item2_delete_property(Item, Prop) :-
	retract(item2(Item, Time, Type, OldProps)),
	delete_prop(OldProps, Prop, NewProps),
	assert(item2(Item, Time, Type, NewProps)).

delete_prop([], _, []).
delete_prop([Old|OldProps], Prop, Props) :-
	\+ \+ Old = Prop, !,
	delete_prop(OldProps, Prop, Props).
delete_prop([Old|OldProps], Prop, [Old|Props]) :-
	delete_prop(OldProps, Prop, Props).


%%	item2_nth(+Item:int, -Nth:int) is semidet.
%
%	Nth entry in the original log file.  Hardly ever used anymore.

item2_nth(Item, Nth) :-
	item2_property(Item, nth(Nth)).


%%	item2_assignment(+Item:int, -Assignment:atom) is semidet.
%
%	If the log file is broken up in "assignments" (for example SimQuest)
%	then returns Assignment this Item is part of.

item2_assignment(Item, Assignment) :-
	item2_property(Item, assignment(Assignment)).


%%	item2_set_assignment(+Item:int, +Assignment:atom) is semidet.
%
%	Sets Assignment of Item.  Sometimes necessary to normalise assignments
%	over different versions of the learning environment (when names of
%	assignments have been modified).

item2_set_assignment(Item, Assignment) :-
	item2_delete_property(Item, assignment(_)),
	item2_add_property(Item, assignment(Assignment)).


%%	item2_time(+Item:int, -Time:term) is det.
%
%	Time is the =time(H,M,S)= action was recorded.  Seconds are expressed
%	as whole seconds.  Use item2_time_stamp/2 for the full date.

item2_time(Item, time(H,Mn,S)) :-
	item2(Item, TimeStamp, _, _),
	stamp_date_time(TimeStamp, date(_,_,_,H,Mn,S0,_,_,_), 0),
	S is round(S0).


%%	item2_date(+Item:int, -Date:term) is det.
%
%	Date is the =date(Y,M,D)= action was recorded.  Use item2_time_stamp/2 for the full date.

item2_date(Item, date(Y,M,D)) :-
	item2(Item, TimeStamp, _, _),
	stamp_date_time(TimeStamp, date(Y,M,D,_,_,_,_,_,_), 0).


%%	item2_coding(+Item:int, -Code:atom) is nondet.
%%	item2_coding(+Item:int, +Type:any, -Code:atom) is nondet.
%
%	Code is a (manually) assigned coding for Item.  In general, multiple
%	codings are possible, so there are different Type's of coding.

item2_coding(Item, Code) :-
	item2_properties(Item, Props),
	member(coding(_,Code), Props).

item2_coding(Item, Type, Code) :-
	item2_properties(Item, Props),
	member(coding(Type,Code), Props).


%%	item2_chat_sender_text(+Item:int, -Sender:atom, -Text:atom) is semidet.
%
%	Succeeds if Item is a chat with Sender and Text.

item2_chat_sender_text(Item, Sender, Text) :-
	item2_type(Item, chat),
	item2_property(Item, sender(Sender)),
	item2_property(Item, text(Text)).


%%	item2_chat_text(+Item:int, -Text:atom) is semidet.
%
%	Succeeds if Item is a chat with Text.

item2_chat_text(Item, Text) :-
	item2_type(Item, chat),
	item2_property(Item, text(Text)).


%%	item2_correct_chat(+Item:int, +Text:atom) is semidet.
%
%	Changes to text of chat Item to Text.  The original text is available
%	in the original_text(OldText) property of Item. 

item2_correct_chat(Item, Text) :-
	item2_property(Item, text(OldText)),
	item2_delete_property(Item, text(_)),
	item2_add_properties(Item, [text(Text), original_text(OldText)]).


%%	item2_sender(+Item:int, -Sender:atom) is semidet.
%
%	Succeeds if chat Item has Sender.

item2_sender(Item, Sender) :-
	item2_type(Item, chat),
	item2_property(Item, sender(Sender)).


%%	item2_sender_code(+Item:int, -Code:atom) is semidet.
%
%	Succeeds if chat Item has a sender Code.  The Code, a capital letter
%	starting with *A*, is generated for each session.

item2_sender_code(Item, Code) :-
	item2_type(Item, chat),
	item2_session(Item, Session),
	item2_sender(Item, Sender),
	(   sender(Session, Sender, Code0)
	;   generate_sender_code(Session, Sender, Code0)
	), !,
	Code = Code0.

item2_sender_code(Item, Sender, Code) :-
	item2_type(Item, chat),
	item2_session(Item, Session),
	item2_sender(Item, Sender),
	(   sender(Session, Sender, Code0)
	->  true
	;   generate_sender_code(Session, Sender, Code0)
	),
	Code = Code0.

generate_sender_code(Session, Sender, Code) :-
	findall(C, sender(Session,_,C), Cs),
	(   Cs == []
	->  Code = 'A',
	    assertz(sender(Session,Sender,Code))
	;   last(Cs, Code0),
	    atom_codes(Code0, [AC0]),
	    succ(AC0, AC),
	    atom_codes(Code, [AC]),
	    assertz(sender(Session,Sender,Code))
	).


/*------------------------------------------------------------
 *  Models
 *------------------------------------------------------------*/

%%	item2_model(+Item:int, -Model:atom) is semidet.
%
%	If Item contains a reference to a model this Model is returned.
%	Otherwise the predicate fails.  Details about the model can be
%	obtained by an application specific predicate such as sdm/2. 

item2_model(Item, Model) :-
	item2_property(Item, model(Model)).


%%	item2p_model(+Item:int, -Model:atom) is semitdet.
%
%	Model is the model during Item (see item2_model/2).  If Item itself
%	does not contain a model, the previous item is checked until a model
%	is found.

item2p_model(Item, Model) :-
	item2_model(Item, Model0), !,
	Model = Model0.
item2p_model(Item, Model) :-
	item2_prev(Item, Prev),
	item2p_model(Prev, Model).


/*------------------------------------------------------------
 *  Window layout
 *------------------------------------------------------------*/

%%	item2_window_layout(+Item:int, -WindowLayout:handle) is semidet.
%
%	If Item contains a reference to a WindowLayout this is returned.
%	Otherwise the predicate fails.  Details about the window layout can be
%	obtained through window_layout/2. 

item2_window_layout(Item, Handle) :-
	item2_property(Item, window_layout(Handle)).


%%	item2p_window_layout(+Item:int, -WindowLayout:handle) is semidet.
%
%	WindowLayout is the window layout during Item (see
%	item2_window_layout/2).  If Item itself does not contain a window layout, the
%	previous item is checked until a window layout is found.

item2p_window_layout(Item, Handle) :-
	item2_window_layout(Item, Handle0), !,
	Handle = Handle0.
item2p_window_layout(Item, Handle) :-
	item2_prev(Item, Prev),
	item2p_window_layout(Prev, Handle).


/*------------------------------------------------------------
 *  Phases
 *------------------------------------------------------------*/

%%	item2p_phase(+Item:int, -Phase:int) is det.
%
%	Phase is the phase the learner is in.  It is assumed that the learner
%	is initially in phase 1 and can optionally progress to phase 2, 3
%	etc.  If no phase is found, then 1 is returned.

item2p_phase(Item, Phase) :-
	item2_type(Item, phase_change),
	item2_properties(Item, Props),
	memberchk(new_phase(Phase0), Props), !,
	Phase = Phase0.
item2p_phase(Item, Phase) :-
	item2_prev(Item, Prev), !,
	item2p_phase(Prev, Phase).
item2p_phase(_, 1).


%%	item2s_in_phases(+Items:list, +Phases:list, -Result:list) is det.
%
%	Result are all items in Items that belong to one of Phases.  As soon
%	as an item from a phase other than Phases is encountered the remaining
%	items are not included in Result.

item2s_in_phases([Item|Items], Phases, [Item|More]) :-
	item2_type(Item, Type),
	(   Type == phase_change
	->  item2_property(Item, new_phase(New)),
	    \+ memberchk(New, Phases)
	;   true
	), !,
	item2s_in_phases(Items, Phases, More).
item2s_in_phases(_, _, []).


item2_variable_value(Item, Variable, Value) :-
	item2_properties(Item, Args),
	memberchk(model(Model), Args),
	memberchk(variable(Variable,Value), Model).


item2_session(Item, Session) :-
	item2_property(Item, session(Session)).



/*------------------------------------------------------------
 *  Ordering
 *------------------------------------------------------------*/

%%	item2_next(+Item:int, -Next:int) is semidet.
%
%	Succeeds if Next is the item immediately after Item.


%%	item2_prev(+Item:int, -Prev:int) is semidet.
%
%	Succeeds if Prev is the item immediately before Item.


%%	item2_set_order(+Items:list) is det.
%
%	Remembers the order of Items.  The next and previous item can then be
%	accessed with item2_next/2 and item2_prev/2 respectively.

item2_set_order([]).
item2_set_order([Item|Items]) :-
	set_order(Items, Item).

set_order([], _).
set_order([Item|Items], Prev) :-
	assert(item2_next(Prev,Item)),
	assert(item2_prev(Item,Prev)),
	set_order(Items, Item).


%%	item2_clear_order(+Items:list) is det.
%
%	Forgets the order of Items.

item2_clear_order([]).
item2_clear_order([Item|Items]) :-
	retractall(item2_next(Item,_)),
	retractall(item2_prev(Item,_)),
	item2_clear_order(Items).


/*------------------------------------------------------------
 *  Item2 sequences
 *------------------------------------------------------------*/

item2_first_answer_sequence([Item|_], [Item], Answer) :-
	item2_type(Item, answer), !,
	item2_properties(Item, Args),
	(   memberchk(answer(_,Answer), Args)
	->  true
	;   Answer = no_answer
	).
item2_first_answer_sequence([Item|Items], [Item|More], Answer) :-
	item2_first_answer_sequence(Items, More, Answer).
	

item2_sequence_first_answer([], no_answer) :- !.
item2_sequence_first_answer([Item|_], State) :-
	item2_type(Item, answer), !,
	item2_properties(Item, Args),
	(   memberchk(answer(_,State), Args)
	->  true
	;   State = no_answer
	).
item2_sequence_first_answer([_|Items], Bool) :-
	item2_sequence_first_answer(Items, Bool).



/*------------------------------------------------------------
 *  Time window
 *------------------------------------------------------------*/

item2s_time_window([], [], _, _).
item2s_time_window([I|Items], [I|More], Time1, Time2) :-
	item2_time(I, Time),
	Time @>= Time1,
	Time @=< Time2, !,
	item2s_time_window(Items, More, Time1, Time2).
item2s_time_window([_|Items], More, Time1, Time2) :-
	item2s_time_window(Items, More, Time1, Time2).


item2s_time_bounds([], 0, 0).
item2s_time_bounds([First|Items], Start, End) :-
	item2_time_stamp(First, TS),
	item2s_time_bounds(Items, TS,TS, Start,End).

item2s_time_bounds([], Start,End, Start,End).
item2s_time_bounds([Item|Items], Low,High, Start, End) :-
	item2_time_stamp(Item, TS),
	(   TS < Low
	->  item2s_time_bounds(Items, TS,High, Start,End)
	;   TS > High
	->  item2s_time_bounds(Items, Low,TS, Start,End)
	;   item2s_time_bounds(Items, Low,High, Start,End)
	).


%%	item2s_sub_sessions(+Items:list, -SubSessions:list, +Options:list) is det.
%
%	Find the SubSessions of Items based on a time gap.  SubSessions
%	are returned as a list of Items.  Options are:
%
%	* time_gap(Days)
%	  Minimum time gap in a Days.  Default is 0.5.

item2s_sub_sessions(Items, SubSessions, Options) :-
	option(time_gap(Gap), Options, 0.5),
	Secs is Gap * 60 * 60 * 24,
	sub_sessions(Items, Secs, SubSessions).

sub_sessions([], _, []).
sub_sessions([Item|Items], Secs, [[Item|Sub]|Subs]) :-
	item2_time_stamp(Item, Stamp),
	sub_sessions(Items, Stamp, Secs, Sub, Rest),
	sub_sessions(Rest, Secs, Subs).

sub_sessions([], _, _, [], []).
sub_sessions([Item|Items], Prev, Gap, [], [Item|Items]) :-
	item2_time_stamp(Item, Stamp),
	Diff is Stamp - Prev,
	Diff > Gap, !.
sub_sessions([Item|Items], _Prev,Gap, [Item|Sub], Rest) :-
	item2_time_stamp(Item, Stamp),
	sub_sessions(Items, Stamp,Gap, Sub, Rest).



/*------------------------------------------------------------
 *  Cache
 *------------------------------------------------------------*/

:- dynamic
	item2/4,		% Item -> TimeStamp x Type x Properties
	item2_next/2,		% Item -> Next
	item2_prev/2,		% Item -> Previous
	sender/3.		% Session x Sender -> { A, B, ... }

clear_cache_all :-
	flag(item2_index, _, 0),
	retractall(item2(_,_,_,_)),
	retractall(item2_next(_,_)),
	retractall(item2_prev(_,_)),
	retractall(sender(_,_,_)).


:- initialization
	listen(clear_cache_all, clear_cache_all).

