/*  $Id$
 *
 *  File	pizza_mission.pl
 *  Part of	IC - educational data mining
 *  Author	Anjo Anjewierden, a.a.anjewierden@utwente.nl
 *  Purpose	Specific for the Pizza mission (energy fact sheet)
 *  Works with	SWI-Prolog (www.swi-prolog.org)
 *
 *  Notice	Copyright (c) 2011  University of Twente
 *
 *  History	07/10/11  (Created)
 *  		14/10/11  (Last modified)
 */

/*------------------------------------------------------------
 *  Directives
 *------------------------------------------------------------*/

:- module(pizza_mission_cme_agent_ic,
	  [ pizza_mission_feedback/3	% Language x Evaluation -> Feedback
	  ]).

:- use_module(load).


/*------------------------------------------------------------
 *  Anchor method evaluation
 *------------------------------------------------------------*/

pizza_mission_feedback(Language, Evaluation, Feedback) :-
	memberchk(not_dropped(NotDropped), Evaluation),
	memberchk(correct_drop(Correct), Evaluation),
	memberchk(wrong_drop(Wrong), Evaluation),
	length(Correct, CorrectLen),
	length(Wrong, WrongLen),
	(   NotDropped == 0
	->  FeedbackType = all_dropped
	;   FeedbackType = not_all_dropped
	),
	Props = [correct(CorrectLen), wrong(WrongLen), not_dropped(NotDropped)],
	feedback_message(FeedbackType, Language, Props, Feedback).


feedback_message(all_dropped, Lang, Props, Msg) :-
	memberchk(correct(Correct), Props),
	memberchk(wrong(Wrong), Props),
	(   Wrong == 0
	->  m(congratulations, Lang, Fmt),
	    format(atom(Msg), M, [])
	;   m(correct_wrong, Lang, Fmt),
	    format(atom(Msg), M, [Correct, Wrong])
	).
	
feedback_message(not_all_dropped, Lang, Props, Msg) :-
	memberchk(not_dropped(NotDropped), Props),
	(   NotDropped == 1
	->  m(not_dropped(single), Lang, Fmt),
	    format(atom(Msg), M, [])
	;   m(not_dropped(multiple), Lang, Fmt),
	    format(atom(Msg), M, [NotDropped])
	).


m(congratulations, gr, 'EL: You have placed all concepts correctly.  Congratulations!').
m(congratulations, _, 'You have placed all concepts correctly.  Congratulations!').

m(correct_wrong, gr, 'EL: You have ~w concepts correct and ~w are wrong').
m(correct_wrong, _, 'You have ~w concepts correct and ~w are wrong').

m(not_dropped_single, gr, 'EL: There is still one concept that needs to be dragged to an empty spot').
m(not_dropped_single, _, 'There is still one concept that needs to be dragged to an empty spot').

m(not_dropped_multiple, gr, 'EL: There are still ~w concepts that need to be dragged to an empty spot').
m(not_dropped_multiple, _, 'There are still ~w concepts that need to be dragged to an empty spot').
