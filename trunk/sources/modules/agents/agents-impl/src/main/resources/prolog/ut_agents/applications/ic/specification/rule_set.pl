/*  $Id$
 *  
 *  File	rule_set.pl
 *  Part of	IC - educational data mining
 *  Author	Anjo Anjewierden, a.a.anjewierden@utwente.nl
 *  Purpose	Parse an XML specification of rules
 *  Works with	SWI-Prolog (www.swi-prolog.org)
 *  Status      check, clean, public
 *  
 *  Notice	Copyright (c) 2009, 2010  University of Twente
 *  
 *  History	16/09/09  (Created)
 *  		14/06/11  (Last modified)
 */

/*------------------------------------------------------------
 *  Directives
 *------------------------------------------------------------*/

:- module(rule_set_specification_ic,
	  [ rule_set_parse/2,		% Source x Options	// deprecated
	    rule_set_parse/3,		% Source -> RuleSet <- Options
	    rule_set/1,			% RuleSet
	    rule_set/2,			% RuleSet -> Rules
	    rule_set_rule/2		% RuleSet -> rule(Name,Specification,Options)
	  ]).

:- use_module(load).

:- use_module(library(lists), [delete/3, member/2]).
:- use_module(library(broadcast), [broadcast/1, listen/2]).
:- use_module(library(option), [option/2]).
:- use_module(library('dialog/pretty_print'), [pretty_print/1]).

:- use_module(utilities(xml), [source_to_xml/3]).


rule_set_parse(Source, Options) :-
	format('*** rule_set_parse/2 deprecated, use .../3~n', []),
	rule_set_parse(Source, _, Options).
		       
rule_set_parse(Source, RuleSet, Options) :-
	source_to_xml(Source, XML,
		      [dialect(xml), space(remove)]),
	option(rule_set(RuleSet), Options),
	clear_cache(RuleSet),
	parse(XML, [rule_set(Rules)]),
	assert(rule_set(RuleSet,Rules)),
	forall(rule_set_rule(RuleSet, rule(Name,_,_)),
	       broadcast(rule(create,RuleSet,Name))).


rule_set_rule(RuleSet, Rule) :-
	rule_set(RuleSet, Rules),
	member(rule(Name,Specification,Options), Rules),
	Rule = rule(Name,Specification,Options).


/*------------------------------------------------------------
 *  Parser
 *------------------------------------------------------------*/

parse([], []).
parse([H|T], [R|Rest]) :-
	parse_h(H, R), !,
	parse(T, Rest).
parse([H|T], Rest) :-
	format('***** failed to parse~n', []),
	pretty_print(H),
	format('*****~n~n', []),
	parse(T, Rest).


parse_h(element(T,As,C), R) :-
	convert_avs(As, Args),
	parse_t(T, Args, C, R).


parse_t(rule_set, _Args, C, rule_set(C2)) :-
	parse(C, C2).
parse_t(rules, _Args, C, rule_set(C2)) :-			% TBD -- deprecated
	parse(C, C2).
parse_t(rule, Args, C, rule(Name,C2,As2)) :-
	delete(Args, name(Name), As2),
	parse(C, C2).
parse_t(or, _Args, C, or(C2)) :-
	parse(C, C2).
parse_t(and, _Args, C, and(C2)) :-
	parse(C, C2).
parse_t(nodes, Args, _C, nodes(Args)).
parse_t(edges, Args, _C, edges(Args)).
parse_t(node, Args, _C, node(Args)).
parse_t(edge, Args, _C, edge(Args)).
parse_t(t_edge, Args, _C, t_edge(Args)).
parse_t(compare, Args, _C, compare(Args)).
parse_t(count, Args, _C, count(Args)).
parse_t(true, Args, C, true(Args,C2)) :-
	parse(C, C2).


convert_avs([], []).
convert_avs([Att=Val|AVs], [Term|More]) :-
	convert_value(Val, Value),
	functor(Term, Att, 1),
	arg(1, Term, Value),
	convert_avs(AVs, More).


convert_value(Value, NormValue) :-
	normalize_space(atom(Atom), Value),
	convert_value2(Atom, NormValue).


convert_value2(Value, NormValue) :-
	catch(atom_number(Value,NormValue), Error, true),
	var(Error), !.
convert_value2(Value, or(Norms)) :-
	sub_atom(Value, _,_,_, ';'), !,
	atomic_list_concat(Parts, ';', Value),
	findall(N, (member(P,Parts), convert_value(P,N)), Norms).
convert_value2(Value, and(Norms)) :-
	sub_atom(Value, _,_,_, ','), !,
	atomic_list_concat(Parts, ',', Value),
	findall(N, (member(P,Parts), convert_value(P,N)), Norms).
convert_value2(Value, Value).


/*------------------------------------------------------------
 *  Storage
 *------------------------------------------------------------*/

:- dynamic
	rule_set/2.		% RuleSet -> Rules

rule_set(RS) :-
	rule_set(RS, _).

clear_cache :-
	clear_cache(_).

clear_cache(RuleSet) :-
	forall(rule_set_rule(RuleSet, rule(Name,_,_)),
	       broadcast(rule(delete,RuleSet,Name))),
	retractall(rule_set(RuleSet,_)).


:- initialization
	listen(clear_cache_all, clear_cache).
