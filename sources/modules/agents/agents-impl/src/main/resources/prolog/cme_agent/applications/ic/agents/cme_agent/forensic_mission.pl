/*  $Id$
 *
 *  File	forensic_mission.pl
 *  Part of	IC - educational data mining
 *  Author	Anjo Anjewierden, a.a.anjewierden@utwente.nl
 *  Purpose	Specific for the forensic mission
 *  Works with	SWI-Prolog (www.swi-prolog.org)
 *
 *  Notice	Copyright (c) 2011  University of Twente
 *
 *  History	07/10/11  (Created)
 *  		18/10/11  (Last modified)
 */

/*------------------------------------------------------------
 *  Directives
 *------------------------------------------------------------*/

:- module(forensic_mission_cme_agent_ic,
	  [ forensic_mission_feedback/3	% Language x Evaluation -> Feedback
	  ]).

:- use_module(load).

:- use_module(gls(model), [gls_node_label/3]).

forensic_mission_feedback(Language, Evaluation, Feedback) :-
	memberchk(empty(Empty), Evaluation),
	memberchk(not_understood(NotUnderstood), Evaluation),
%	memberchk(correct(Correct), Evaluation),
%	memberchk(perfect(Perfect), Evaluation),
	memberchk(dislocated(Dislocated), Evaluation),
	(   Empty \== []
	->  feedback_message(empty, Language, Evaluation, Feedback1)
	;   Feedback1 = ''
	),
	(   NotUnderstood \== []
	->  feedback_message(not_understood, Language, Evaluation, Feedback2)
	;   Feedback2 = ''
	),
	(   Dislocated == [], Empty == [], NotUnderstood == []
	->  feedback_message(congratulations, Language, Evaluation, Feedback3)
	;   Feedback3 = ''
	),
	(   Dislocated \== []
	->  feedback_message(dislocated, Language, Evaluation, Feedback4)
	;   Feedback4 = ''
	),
	atomic_list_concat([Feedback1,Feedback2,Feedback3,Feedback4], '   ', Feedback).


feedback_message(empty, Lang, Props, Msg) :-
	memberchk(empty(Empty), Props),
	length(Empty, LenEmpty),
	(   LenEmpty == 1
	->  m(empty(single), Lang, Fmt)
	;   m(empty(multiple), Lang, Fmt)
	), !,
	format(atom(Msg), Fmt, [LenEmpty]).
feedback_message(not_understood, Lang, Props, Msg) :-
	memberchk(not_understood(Nodes), Props),
	memberchk(gls(G), Props),
	m(conjunction, Lang, Conj),
	nodes_to_labels(Nodes, G, Conj, Labels),
	length(Nodes, Len),
	(   Len == 1
	->  m(not_understood(single), Lang, Fmt)
	;   m(not_understood(multiple), Lang, Fmt)
	), !,
	format(atom(Msg), Fmt, [Labels]).
feedback_message(dislocated, Lang, Props, Msg) :-
	memberchk(dislocated(Nodes), Props),
	memberchk(gls(G), Props),
	m(conjunction, Lang, Conj),
	nodes_to_labels(Nodes, G, Conj, Labels),
	length(Nodes, Len),
	(   Len == 1
	->  m(dislocated(single), Lang, Fmt)
	;   m(dislocated(multiple), Lang, Fmt)
	), !,
	format(atom(Msg), Fmt, [Labels]).
feedback_message(congratulations, Lang, _Props, Msg) :-
	m(congratulations, Lang, Fmt),
	format(atom(Msg), Fmt, []).


m(congratulations, fr, 'Bravo! Votre carte conceptuelle est parfaite.').
m(congratulations, _, 'Congratulations: your concept map is perfect!').

m(empty(single), fr, 'Il vous manque 1 concept.').
m(empty(single), _, 'You still have to fill one empty concept.').

m(empty(multiple), fr, 'Il vous manque ~w concepts.').
m(empty(multiple), _, 'You still have to fill ~w empty concepts.').

m(not_understood(single), fr, 'Le concept ~w ne fait pas partie de la liste des concepts attendus. Afin de le remplacer par un autre concept, relisez le manuel de laboratoire ou bien cherchez un synonyme.').
m(not_understood(single), _, 'Concept ~w is not one of the expected concepts.  Check the handbook and change it to the appropriate term or think of a different name for the concept you have in mind.').

m(not_understood(multiple), fr, 'Les concepts ~w ne font pas partie de la liste des concepts attendus. Afin de les remplacer par d\'autres concepts, relisez le manuel de laboratoire ou bien cherchez des synonymes.').
m(not_understood(multiple), _, 'The concepts ~w are not expected concepts.  Check the handbook and change them to the appropriate terms or think of different names for the concepts you have in mind.').

m(dislocated(single), fr, 'Le concept ~w n\'est pas bien placé. Relisez le manuel de laboratoire afin de lui trouver une autre place.').
m(dislocated(single), _, 'The concept ~w is not in the right place. Check the handbook again to find another place for this concept.').

m(dislocated(multiple), fr, 'Les concepts ~w ne sont pas bien placés. Relisez le manuel de laboratoire afin de leur trouver d\'autres places.').
m(dislocated(multiple), _, 'The concepts ~w are not in the right place. Check the handbook to find another place for these concepts.').

m(conjunction, fr, et).
m(conjunction, _, and).


/*------------------------------------------------------------
 *  Utilities
 *------------------------------------------------------------*/

nodes_to_labels(Nodes, G, Conj, Labels) :-
	quote_nodes(Nodes, G, Quoted),
	format(atom(Connect), ' ~w ', [Conj]),
	atomic_list_concat(Quoted, Connect, Labels).


quote_nodes([], _, []).
quote_nodes([Node|Nodes], G, [Quote|Quotes]) :-
	gls_node_label(G, Node, Label),
	format(atom(Quote), '"~w"', [Label]),
	quote_nodes(Nodes, G, Quotes).
