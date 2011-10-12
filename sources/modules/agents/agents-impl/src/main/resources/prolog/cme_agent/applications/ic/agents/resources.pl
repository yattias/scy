/*  $Id$
 *  
 *  File	resources.pl
 *  Part of	IC - educational data mining
 *  Author	Anjo Anjewierden, a.a.anjewierden@utwente.nl
 *  Purpose	Resources for the agents
 *  Works with	SWI-Prolog (www.swi-prolog.org)
 *  Status	clean, checked
 *  
 *  Notice	Copyright (c) 2011  University of Twente
 *  
 *  History	07/10/11  (Created)
 *  		07/10/11  (Last modified)
 */ 

/*------------------------------------------------------------
 *  Directives
 *------------------------------------------------------------*/

:- module(resources_agents_ic,
	  [ agent_resource_required/2,	% Type x Domain
	    agent_resource/3		% Type x Domain -> File
	  ]).

:- use_module(load).

:- use_module(specification(reference_model), [reference_model/1,
						reference_model_parse/3]). 
:- use_module(specification(rule_set), [rule_set/1, rule_set_parse/3]). 
:- use_module(specification(term_set), [term_set/1, term_set_parse/3]). 


/*------------------------------------------------------------
 *  Load a resource
 *------------------------------------------------------------*/

agent_resource_required(reference_model, Domain) :-
	(   reference_model(Domain)
	->  true
	;   agent_resource(reference_model, Domain, Source),
	    reference_model_parse(file(Source), Domain, [])
	).
agent_resource_required(term_set, Domain) :-
	(   term_set(Domain)
	->  true
	;   agent_resource(term_set, Domain, Source),
	    term_set_parse(file(Source), Domain, [])
	).
agent_resource_required(rule_set, Domain) :-
	(   rule_set(Domain)
	->  true
	;   agent_resource(rule_set, Domain, Source),
	    rule_set_parse(file(Source), Domain, [])
	).


/*------------------------------------------------------------
 *  Resources
 *------------------------------------------------------------*/

%%	agent_resource(+Type:atom, +Domain:atom, -Source:term) is semidet.
%
%	Returns Source for the resource of Type (currently one of
%	=term_set=, =reference_model=, or =rule_set=) for Domain. 

agent_resource(reference_model, co2house, agents('resources/co2house_rm.xml')).
agent_resource(term_set,        co2house, agents('resources/co2house_ts.xml')).

agent_resource(reference_model, efs,      agents('resources/efs_rm.xml')).
agent_resource(term_set,        efs,      agents('resources/efs_ts.xml')).

agent_resource(reference_model, eco,      agents('resources/eco_rm.xml')).
agent_resource(term_set,        eco_en,   agents('resources/eco_ts_en.xml')).
agent_resource(term_set,        eco_et,   agents('resources/eco_ts_et.xml')).
agent_resource(rule_set,        eco,      agents('resources/eco_rs.xml')).

agent_resource(reference_model, forensic, agents('resources/eco_rm.xml')).
agent_resource(term_set,        forensic_en, agents('resources/forensic_ts_en.xml')).
agent_resource(term_set,        forensic_fr, agents('resources/forensic_ts_fr.xml')).


/*------------------------------------------------------------
 *  Initialization
 *------------------------------------------------------------*/

:- use_module(source(files), [source_file_register/1]).

:-
	forall(agent_resource(_,_,File), source_file_register(File)).
