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
	    anchor_evaluation_feedback/4% Anchor x Language x Evaluation -> Feedback
	  ]).

:- use_module(load).

:- use_module(pizza_mission, [pizza_mission_feedback/3]).
:- use_module(forensic_mission, [forensic_mission_feedback/3]).
:- use_module(eco_mission, [eco_mission_feedback/3]).


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
	(   Language == fr
	->  Props = [ term_set(forensic_fr),
		      reference_model(forensic),
		      rule_set(''),
		      method(fill_in)
		    ]
	; % Language == en
	    Props = [ term_set(forensic_en),
		      reference_model(forensic),
		      rule_set(''),
		      method(fill_in)
		    ]
	).
anchor_properties(conceptMap, Language, Props) :-	% ECO mission
	(   Language == et
	->  Props = [ term_set(eco_et),
		      reference_model(eco),
		      rule_set(eco),
		      method(open_ended)
		    ]
	; % Language == en
	    Props = [ term_set(eco_en),
		      reference_model(eco),
		      rule_set(eco),
		      method(open_ended)
		    ]
	).
	


/*------------------------------------------------------------
 *  Anchor specific feedback message
 *------------------------------------------------------------*/

anchor_evaluation_feedback(conceptMap, Language, Evaluation, Feedback) :-
	eco_mission_feedback(Language, Evaluation, Feedback).
anchor_evaluation_feedback(energyFactSheet, Language, Evaluation, Feedback) :-
	pizza_mission_feedback(Language, Evaluation, Feedback).
anchor_evaluation_feedback(chimConceptMap, Language, Evaluation, Feedback) :-
	forensic_mission_feedback(Language, Evaluation, Feedback).
