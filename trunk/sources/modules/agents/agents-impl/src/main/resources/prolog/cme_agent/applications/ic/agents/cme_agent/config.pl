/*  $Id$
 *
 *  File	config.pl
 *  Part of	IC - educational data mining
 *  Author	Anjo Anjewierden, a.a.anjewierden@utwente.nl
 *  Purpose	Configurations for the evaluation
 *  Works with	SWI-Prolog (www.swi-prolog.org)
 *
 *  Notice	Copyright (c) 2011  University of Twente
 *
 *  History	07/10/11  (Created)
 *  		07/10/11  (Last modified)
 */

/*------------------------------------------------------------
 *  Directives
 *------------------------------------------------------------*/

:- module(config_cme_agent_ic,
	  [ anchor_properties/3,	% Anchor x Language -> Properties
	    method_anchor_evaluation/5	% Method x Language x Anchor x Evaluation -> Fs
	  ]).

:- use_module(load).

:- use_module(sqlspaces(tspl), [tspl_actual_field/3, tspl_wildcard_field/1]).


/*------------------------------------------------------------
 *  Anchor properties
 *------------------------------------------------------------*/

%%      anchor_properties(+AnchorId:atom, +Language:atom, -Properties:list) is semidet.
%
%	Properties for evaluating an ELO with AnchorId in Language.
%	Language is the two letter international code (English =en=, Greek
%	=el=, Estonian =et=, French =fr=).

anchor_properties(energyFactSheet, Language, Props) :-	% Pizza mission
	(   Language == el
	->  Props = [ term_set(efs),
		      reference_model(efs),
		      rule_set(''),
		      method(drag_and_drop)
		    ]
	; % Language == en
	    Props = [ term_set(efs),
		      reference_model(efs),
		      rule_set(''),
		      method(drag_and_drop)
		    ]
	).
anchor_properties(chimConceptMap, Language, Props) :-	% Forensic science mission
	(   Language == gr
	->  Props = [ term_set(forensic_fr),
		      reference_model(forensic),
		      rule_set(''),
		      method(open_ended)
		    ]
	; % Language == en
	    Props = [ term_set(forensic_en),
		      reference_model(forensic),
		      rule_set(''),
		      method(open_ended)
		    ]
	).
anchor_properties(conceptMap, Language, Props) :-	% ECO mission
	(   Language == et
	->  Props = [ term_set(eco_et),
		      reference_model(eco),
		      rule_set(''),
		      method(open_ended)
		    ]
	; % Language == en
	    Props = [ term_set(eco_en),
		      reference_model(eco),
		      rule_set(''),
		      method(open_ended)
		    ]
	).
	


/*------------------------------------------------------------
 *  Anchor method evaluation
 *------------------------------------------------------------*/

feedback_message(all_dropped, Lang, Props, Msg) :-
	memberchk(correct(Correct), Props),
	memberchk(wrong(Wrong), Props),
	(   Lang = _				% TBD -- ignored
	->  format(atom(Msg), 'You have ~w concepts correct and ~w are wrong',
		[Correct, Wrong])
	;   feedback_message(all_dropped, en, Props, Msg)
	).
	
feedback_message(not_all_dropped, Lang, Props, Msg) :-
	memberchk(not_dropped(NotDropped), Props),
	(   Lang = _
	->  format(atom(Msg), 'There are still %d concepts that have to be moved to an empty spot', [NotDropped])
	;   feedback_message(all_dropped, en, Props, Msg)
	).


method_anchor_evaluation(drag_and_drop, Language, _Anchor, Evaluation, Msg) :-
	memberchk(not_dropped(NotDropped), Evaluation),
%	memberchk(dropped(Drop), Evaluation),		// Not used
	memberchk(correct_drop(Correct), Evaluation),
	memberchk(wrong_drop(Wrong), Evaluation),
	length(Correct, CorrectLen),
	length(Wrong, WrongLen),
	(   NotDropped == 0
	->  feedback_message(all_dropped, Language,
			     [ correct(CorrectLen),
			       wrong(WrongLen)
			     ], Msg)
	;   feedback_message(not_all_dropped, Language,
			     [ not_dropped(NotDropped)
			     ], Msg)
	).


