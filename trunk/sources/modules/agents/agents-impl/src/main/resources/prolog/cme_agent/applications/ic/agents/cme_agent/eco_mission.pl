/*  $Id$
 *
 *  File	eco_mission.pl
 *  Part of	IC - educational data mining
 *  Author	Anjo Anjewierden, a.a.anjewierden@utwente.nl
 *  Purpose	Code specific to the ECO mission
 *  Works with	SWI-Prolog (www.swi-prolog.org)
 *
 *  Notice	Copyright (c) 2011  University of Twente
 *
 *  History	14/10/11  (Created)
 *  		14/10/11  (Last modified)
 */

/*------------------------------------------------------------
 *  Directives
 *------------------------------------------------------------*/

:- module(eco_mission_cme_agent_ic,
	  [ eco_mission_contains_topic/2,	% Anchor -> Topic
	    eco_mission_find_peer/5,		% User x Topic x Correct -> Peer x Elo
	    eco_mission_is_topic/1,		% Topic
	    eco_mission_topic_label/3,		% Topic x Language -> Label
	    eco_mission_set_topic/2,		% User x Topic
	    eco_mission_topic/2,		% User -> Topic

	    eco_mission_feedback/3,		% Language x Evaluation -> Feedback

	    eco_mission_evaluation/3		% Map -> Evaluation <- Options
	  ]).

:- use_module(load).

:- use_module(gls(model), [gls_apply_term_set/3, gls_node_term/3]).


/*------------------------------------------------------------
 *  Topics
 *------------------------------------------------------------*/

%%	contains_eco_mission_topic(+Anchor:atom, -Topic:atom) is semidet.
%
%	Succeeds if Anchor is the anchor of an ELO that contains Topic in the
%	ECO mission.

eco_mission_contains_topic(Anchor, Topic) :-
	sub_atom(Anchor, _,2,0, Topic),
	eco_mission_is_topic(Topic).


%%	is_eco_mission_topic(-Topic:atom) is nondet.
%
%	Succeeds if Topic is an ECO mission topic.

eco_mission_is_topic('B1').
eco_mission_is_topic('B2').
eco_mission_is_topic('B3').
eco_mission_is_topic('C1').


eco_mission_topic_label(Topic, Language, Label) :-
	nonvar(Topic),
	nonvar(Language),
	eco_mission_is_topic(Topic),
	topic_label(Topic, Language, Label), !.
eco_mission_topic_label(Topic, _, Label) :-
	eco_mission_topic_label(Topic, en, Label).

topic_label('B1', en, 'primary production').
topic_label('B2', en, 'photosynthesis').
topic_label('B3', en, 'thropic levels').
topic_label('C1', en, 'pH').

topic_label('B1', et, 'esmane produktsioon').
topic_label('B2', et, 'fotosüntees').
topic_label('B3', et, 'troofilised tasemed').
topic_label('C1', et, 'pH').



/*------------------------------------------------------------
 *  Topics per user
 *------------------------------------------------------------*/

:- dynamic
	eco_mission_topic/2,		% User x Topic
	eco_mission_topic_evaluation/4,	% User x Topic -> Evaluation x URI
	eco_mission_topic_status/3.	% User x Topic -> Status


eco_mission_set_topic(User, Topic) :-
	retractall(eco_mission_topic(User,_)),
	assert(eco_mission_topic(User,Topic)).

eco_mission_set_topic_evaluation(User, Topic, Evaluation, Elo) :-
	retractall(eco_mission_topic_evaluation(User,Topic,_,_)),
	assert(eco_mission_topic_evaluation(User,Topic,Evaluation,Elo)).

eco_mission_set_topic_status(User, Topic, Status) :-
	retractall(eco_mission_topic_status(User,Topic,_)),
	assert(eco_mission_topic_status(User,Topic,Status)).


/*------------------------------------------------------------
 *  Evaluation
 *------------------------------------------------------------*/

:- use_module(specification(rule_set), [rule_set_rule/2]).

eco_mission_evaluation(Map, Evaluation, Options) :-
	memberchk(user(User), Options),
	(   \+ eco_mission_topic(User, Topic)
	->  Evaluation = [topic(not_available)]
	;   eco_mission_topic(User, Topic),
	    TopicEval = topic(Topic),
	    memberchk(rule_set(RS), Options),
	    rule_set_rule(RS, rule(Topic,Props,_)),
	    rule_present_nodes(Props, AtLeast, Nodes),
	    AtLeastEval = at_least(AtLeast),
	    length(Nodes, LenNodes),
	    NoNodesEval = no_nodes(LenNodes),
	    memberchk(term_set(TS), Options),
	    gls_apply_term_set(Map, TS, []),
	    memberchk(reference_model(RM), Options),
	    correct_nodes(Nodes, Map, RM, Correct),
	    length(Correct, NoCorrect),
	    (   eco_mission_topic_evaluation(User, Topic, OldCorrect, _)
	    ->  (   OldCorrect > NoCorrect
		->  eco_mission_set_topic_status(User,Topic,worse)
		;   OldCorrect == NoCorrect
		->  eco_mission_topic_status(User,Topic,OldStatus),
		    (   OldStatus == same
		    ->  eco_mission_set_topic_status(User,Topic,find_peer)
		    ;   eco_mission_set_topic_status(User,Topic,same)
		    )
		;   eco_mission_set_topic_status(User, Topic, better)
		)
	    ;   eco_mission_set_topic_status(User, Topic, initial)
	    ),
	    option(elo(Elo), Options),
	    eco_mission_set_topic_evaluation(User, Topic, NoCorrect, Elo),
	    eco_mission_topic_status(User, Topic, Status),
	    debug(cme(dev), 'User = ~w; Topic = ~w; Status = ~w~n', [User,Topic,Status]),
	    Evaluation = [ TopicEval,
			   AtLeastEval,
			   NoNodesEval,
			   no_correct(NoCorrect),
			   user(User),
			   status(Status)
			 ]
	).


rule_present_nodes(Props, AtLeast, Nodes) :-
	memberchk(true(Count,Subs), Props), !,
	memberchk(at_least(AtLeast), Count),
	present_nodes(Subs, Nodes).


present_nodes([], []).
present_nodes([node(Props)|Nodes], [Node|More]) :-
	memberchk(status(present), Props), !,
	memberchk(term(Node), Props),
	present_nodes(Nodes, More).


correct_nodes([], _, _, []).
correct_nodes([Term|Terms], Map, RM, [Node|Nodes]) :-
	gls_node_term(Map, Node, Term), !,
  debug(cme(dev), '  found ~w in ~w~n', [Term,Map]),
	correct_nodes(Terms, Map, RM, Nodes).
correct_nodes([Term|Terms], Map, RM, Nodes) :-
  debug(cme(dev), '  did not find ~w in ~w~n', [Term,Map]),
	correct_nodes(Terms, Map, RM, Nodes).


/*------------------------------------------------------------
 *  Find a better peer
 *------------------------------------------------------------*/

:- dynamic
	previous_peer/3.	% User x Topic x Peer

eco_mission_find_peer(User, Topic, Correct, Peer, Elo) :-
	findall(pe(P,E), ( eco_mission_topic_evaluation(P,Topic,C,E),
			   \+ previous_peer(User,Topic,P),
			   C > Correct), PEs),
	select_random_element(PEs, PE),
	PE = pe(Peer,Elo).

select_random_element(List, Element) :-
	length(List, Len),
	random(0, Len, Nth),
	nth0(Nth, List, Element).
	
	


/*------------------------------------------------------------
 *  Feedback
 *------------------------------------------------------------*/

eco_mission_feedback(Language, Evaluation, Feedback) :-
	memberchk(topic(Topic), Evaluation),
	feedback_message(topic(Topic), Language, Evaluation, Feedback1),
	(   memberchk(no_correct(NoNodes), Evaluation)
	->  feedback_message(no_correct(NoNodes), Language, Evaluation, Feedback2)
	;   Feedback2 = ''
	), !,
	atomic_list_concat([Feedback1,Feedback2], '   ', Feedback).


feedback_message(topic(Topic), Lang, _Props, Msg) :-
	m(topic(Topic), Lang, Fmt),
	(   Topic == not_available
	->  format(atom(Msg), Fmt, [])
	;   eco_mission_topic_label(Topic, Lang, Label),
	    format(atom(Msg), Fmt, [Label])
	).
feedback_message(no_correct(Correct), Lang, Props, Msg) :-
	memberchk(at_least(AtLeast), Props),
	(   Correct == 0
	->  m(none_correct, Lang, Fmt),
	    format(atom(Msg), Fmt, [AtLeast])
	;   Correct >= AtLeast
	->  m(all_correct, Lang, Fmt),
	    format(atom(Msg), Fmt, [])
	;   Still is AtLeast - Correct,
	    option(status(Status), Props, initial),
	    (   Status == initial,
		Still == 1
	    ->  m(some_correct(one_missing), Lang, Fmt)
	    ;   m(some_correct(Status), Lang, Fmt)
	    ),
	    format(atom(Msg), Fmt, [Still])
	).


m(none_correct, et, 'Sinu mõistekaardil ei ole ühtegi mõistet mis seostuvad läbitud uurimusliku töö teemaga.') :- !.
m(none_correct, _, 'You do not have any concepts related to this topic in your map.').

m(all_correct, et, 'Sinu mõistekaart seonduvalt läbitud uurimusliku töö teemaga on valmis. Palju õnne!').
m(all_correct, _, 'Your concept map for this topic is complete.  Congratulations').

m(some_correct(initial), et, 'Tehtud on hea algus kuid puuduvad veel ~w olulist mõistet.').
m(some_correct(initial), _, 'That is a good start but you still miss ~w concepts.').

m(some_correct(one_missing), et, 'Tehtud on hea algus kuid puudub veel vähemalt üks oluline mõiste.').
m(some_correct(one_missing), _, 'That is a good start but you still miss one concept.').

m(some_correct(better), et, 'Mõistekaart on täiuslikum kuid ~w mõistet on ikka puudus.').
m(some_correct(better), _, 'The concept map has improved.  ~w concepts are still missing.').

m(some_correct(worse), et, 'Mõistekaardi eelmisel versioonil oli rohkem korrektseid mõisteid.').
m(some_correct(worse), _, 'The previous concept map had more correct concepts.').

m(some_correct(same), et, 'Sinu mõistekaart ei ole täiuslikum kui varem. Kui Sa soovid et ma aitaks leida hea näite Su klassikaaslaste mõistekaartide seast siis klõpsa uuesti salvestamisnuppu.').
m(some_correct(same), _, 'Your concept map has not improved.  If you want me to look for a good concept map from a class mate then click the save button again').

m(some_correct(find_peer), et, 'Ma ei leidnud ühtegi klassikaaslast kellel oleks Sinuga võrreldes täiuslikum mõistekaart.').
m(some_correct(find_peer), _, 'I did not find any class mate with a better concept map.').

m(topic(not_available), et, 'Ma ei suutnud leida millise uurimusliku teemaga Sa viimati tegelesid. ').
m(topic(not_available), _, 'I could not determine the topic you are working on.') :- !.

m(topic(_), et, 'Hindamine teemal ~w.').
m(topic(_), _, 'Evaluation for topic ~w.').
	
