/*  $Id$
 *  
 *  File	concept_set.pl
 *  Part of	IC - educational data mining
 *  Author	Anjo Anjewierden, a.a.anjewierden@utwente.nl
 *  Purpose	Concept set specification
 *  Works with	SWI-Prolog (www.swi-prolog.org)
 *  Status      check, clean, pldoc, public
 *  
 *  Notice	Copyright (c) 2009, 2010, 2011  University of Twente
 *  
 *  History	03/07/09  (Created)
 *  		14/01/11  (Last modified)
 */

/*------------------------------------------------------------
 *  Directives
 *------------------------------------------------------------*/

:- module(concept_set_specification_ic,
	  [ concept_set/1,			% Set
	    concept_set_property/2,		% Set -> Property

	    concept_set_parse/3,		% File -> Set <- Options

	    concept_set_concept/2,		% Set -> Concept

	    concept_set_normalise_term/2,	% Term -> Normalised
	    concept_set_term_similarity/3,	% Term1 x Term -> Similarity
	    
	    concept_set_match_concept/3,	% Name -> Concept <- Options
	    
	    concept_set_export_matches/3,	% Set x File <- Options
	    concept_set_unused_terms/1,		% Set
	    concept_set_consistency/1		% Set
	  ]).

:- use_module(load).

:- use_module(library(lists), [append/3, delete/3, list_to_set/2, member/2,
			       subtract/3]).
:- use_module(library(sgml), [load_structure/3]).
:- use_module(library(option), [option/2, option/3, merge_options/3]).
:- use_module(library('dialog/pretty_print'), [pretty_print/1]).

:- use_module(dac(atom_to_csv), [atom_to_csv/2]).
:- use_module(sdm(model), [sdm_element_anonymous/3, sdm_element/4]).
:- use_module(sort(compound_terms), [compound_terms_sort/3]).
:- use_module(stop_word(stop_word), [stop_word/2]).
:- use_module(atom(edit_distance), [edit_distance/3]).
:- use_module(utilities(xml), [xml_memberchk/2, xml_avs_to_terms/2]).


/**	<module> Concept sets

A concept set defines a set of concepts and the corresponding terms learners
might use for these.  The basic functionality is to take a name provided
by a learner and map it to a concept in the concept set.

@author	Anjo Anjewierden
*/


%%	concept_set_parse(+File:atom, -ConceptSet:atom, +Options:list) is det.
%
%	File contains the concept set.  Options are:
%
%	* concept_set(Set)
%	  Override the identifier of the concept set, normally the one in the
%	  file is used.

concept_set_parse(File, Set, Options) :-
	absolute_file_name(File, AbsFile),
	open(AbsFile, read, Stream),
	load_structure(Stream, XML,
		       [ dialect(xml),
			 max_errors(-1),
			 syntax_errors(quiet),
			 space(remove)
		       ]),
	close(Stream),
	(   option(concept_set(Set), Options)
	->  true
	;   xml_memberchk(element(concept_set,As,_), XML),
	    memberchk(name=Set, As)
	),
	clear_cache(Set),
	parse(XML, Set),
	time_file(AbsFile, TimeStamp),
	add_concept_set_property(Set, file(AbsFile)),
	add_concept_set_property(Set, last_modified(TimeStamp)).

parse([], _).
parse([H|T], Set) :-
	parse_h(H, Set),
	parse(T, Set).

parse_h(element(T,As,C), Set) :- !,
	parse_t(T,As,C, Set).
parse_h(_, _).

parse_t(concept_set,As,C, Set) :- !,
	xml_avs_to_terms(As, Props0),
	semi_colon_in_type_props(Props0, Props),
	assert(concept_set(Set,Props)),
	parse(C, Set).
parse_t(concept,As,C, Set) :- !,
	memberchk(name=Concept, As),
	nb_setval(match_concepts_concept, Concept),
	assert_concept_set_concept(Set, Concept),
	parse(C, Set).
parse_t(term, As, [Term], Set) :- !,
	concept_set(Set, SetOptions),
	xml_avs_to_terms(As, Options),
	nb_getval(match_concepts_concept, Concept),
	add_concept_term(Concept, Term, Set, SetOptions, Options).
parse_t(sub_string, As, [Sub], Set) :- !,
	concept_set(Set, SetOptions),
	xml_avs_to_terms(As, Options),
	nb_getval(match_concepts_concept, Concept),
	add_concept_term(Concept, Sub, Set, SetOptions, [method(sub_string)|Options]).
parse_t(_, _, _, _).				% Ignore everything else


semi_colon_in_type_props([], []).
semi_colon_in_type_props([H|T], [H2|More]) :-
	semi_colon_in_type_prop(H, H2),
	semi_colon_in_type_props(T, More).

semi_colon_in_type_prop(type(Type), RealType) :- !,
	(   sub_atom(Type, _,_,_, ';')
	->  atomic_list_concat(Types0, ';', Type),
	    findall(T, ( member(Type0,Types0),
			 normalize_space(atom(T), Type0)), RealType)
	;   RealType = Type
	).
semi_colon_in_type_prop(Prop, Prop).



add_concept_term(Concept, Term, Set, SetOptions, TermOptions) :-
	merge_options(TermOptions, SetOptions, Options),
	option(word_order(WO), Options, any),
	(   option(method(sub_string), Options)
	->  assert(term_concept(Set,Term,Concept,Term,Options))
	;   concept_set_normalise_term(Term, Norm, Options),
	    (   WO == fixed
	    ->  assert(term_concept(Set,Term,Term,Concept,Options))
	    ;   concept_set_normalise_term(Term, Norm, Options),
		atomic_list_concat(Words, ' ', Norm),
		forall(permutation(Words,Perm),
		       ( atomic_list_concat(Perm, ' ', T),
			 assert(term_concept(Set,T,Concept,Term,Options))))
	    )
	).


unwanted_chars('/').

remove_unwanted_chars(Term0, Term) :-
	atom_codes(Term0, Codes0),
	unwanted_chars(Chars),
	atom_codes(Chars, Unwanted),
	remove_unwanted_codes(Unwanted, Codes0, Codes),
	atom_codes(Term, Codes).

remove_unwanted_codes([], Codes, Codes).
remove_unwanted_codes([Code|Codes], Codes0, Result) :-
	delete(Codes0, Code, Codes1),
	remove_unwanted_codes(Codes, Codes1, Result).

concept_set_normalise_term(Term, Norm) :-
	remove_unwanted_chars(Term, Term0),
	atomic_list_concat(Parts, '_', Term0),
	atomic_list_concat(Parts, ' ', Term1),
	downcase_atom(Term1, Term2),
	normalize_space(atom(Norm), Term2).

concept_set_normalise_term(Term, Norm, Options) :-
	option(stop_word(ignore), Options), !,
	option(language(Language), Options),
	concept_set_normalise_term(Term, Term1),
	atomic_list_concat(Parts, ' ', Term1),
	findall(P, (member(P,Parts),
		    \+ stop_word(P,Language)), Ps),
	atomic_list_concat(Ps, ' ', Norm).
concept_set_normalise_term(Term, Norm, _) :-
	concept_set_normalise_term(Term, Norm).


%%	concept_set_match_concept(+Term:atom, -Concept:atom, +Options) is semidet.
%
%	Options are:
%
%	* concept_set(Set)
%	  Which Set to use.  This option is required.
%
%	* exclude(Concepts)
%	  Exclude the concepts in the list of Concepts from a possible match.
%	  This can be used to prevent that a single concept maps on multiple
%	  terms in a given model.
%
%	* type(Type)
%	  Type of the element corresponding to Term.  Type can be an atom or a list of types.
%
%	* cache(clear)
%	  Clear the cache of term to concept mappings computed
%	  previously.  Mainly for maintenance reasons.

concept_set_match_concept(Term, Concept, Options) :-
	option(concept_set(Set), Options),
	(   option(cache(clear), Options)
	->  retractall(term_concept_cache(_,_,_,_,Set,_))
	;   true
	),
	option(exclude(Exclude), Options, []),
	find_tcdos(Set, Exclude, Term, TCDOs),
	findall(tcdo(T,C,pre(X),Opts), member(tcdo(T,C,pre(X),Opts), TCDOs), Pres0),
	findall(tcdo(T,C,post(X),Opts), member(tcdo(T,C,post(X),Opts), TCDOs), Posts0),
	compound_terms_sort(Pres0, Pres, [arg(3), order(descending)]),
	compound_terms_sort(Posts0, Posts, [arg(3), order(descending)]),
	append(Pres, Posts, Sorted),
	select_best(Sorted, Matched,Value, Concept,Opts, Options),
	ignore(option(score(Value), Options)),
	assert(term_concept_cache(Term,Concept,Matched,Value,Set,Opts)).


find_tcdos(Set, Exclude, Term, TCDOs) :-
	findall(C, concept_set_concept(Set,C), Concepts0),
	subtract(Concepts0, Exclude, Concepts1),
	remove_accept_false(Concepts1, Set,Term, Concepts),
	findall(tcdo(T,C,Score,Opts),
		( member(C, Concepts),
		  term_concept(Set, T, C, _, Opts),
		  concept_set_normalise_term(Term, Norm, Opts),
		  term_options_score(Norm, T, Opts, Score)), TCDOs).


remove_accept_false([], _,_, []).
remove_accept_false([C|Concepts], Set,Term, More) :-
	term_concept(Set, T, C, _, Options),
	concept_set_normalise_term(Term, Norm, Options),
	option(method(sub_string), Options),
	option(accept(false), Options),
	sub_atom(Norm, _,_,_, T), !,
	remove_accept_false(Concepts, Set,Term, More).
remove_accept_false([C|Concepts], Set,Term, [C|More]) :-
	remove_accept_false(Concepts, Set,Term, More).


term_options_score(Term, Known, Options, Score) :-
	option(method(sub_string), Options), !,
	(   sub_atom(Term, _,_,_, Known)
	->  atom_length(Known, Len),
	    Score0 is 0.51 + Len / 100		% 0.51 is larger than the threshold of 0.5
	;   Score0 = 0.0
	),
	when_score(Options, post, Score0, Score).
term_options_score(Term, Known, Options, Score) :-
	option(method(words), Options), !,
	words(Term, Words1),
	words(Known, Words2),
	match_words(Words2, Words1, Score0),
	when_score(Options, pre, Score0, Score).
term_options_score(Term, Known, Options, Score) :-
	concept_set_term_similarity(Term, Known, Score0),
	when_score(Options, pre, Score0, Score).


when_score(Options, Default, Score0, Score) :-
	option(when(When), Options, Default),
	(   When == pre
	->  Score = pre(Score0)
	;   Score = post(Score0)
	).


words(Term, Words) :-
	atomic_list_concat(Words, ' ', Term).
	    
	
match_words(KnownWords, TermWords, Score) :-
	match_words(KnownWords, TermWords, 1, Score).
match_words(_, _, 0).

match_words([], _, Score, Score).
match_words([Known|KnownWords], TermWords, Score0, Score) :-
	(   Score0 < 0				% Already negative, don't search further
	->  Score = Score0
	;   best_term_word(TermWords, Known, Best, Score1),
	    Score2 is Score0 * Score1,		% If both negative, then score becomes positive
	    delete(TermWords, Best, TermRest),
	    match_words(KnownWords, TermRest, Score2, Score)
	).

best_term_word([Term|TermWords], Known, Best, Score) :-
	concept_set_term_similarity(Term, Known, Value),
	best_term_word(TermWords, Known, Term,Value, Best,Score).

best_term_word([], _, Best,Score, Best,Score).
best_term_word([Term|TermWords], Known, Best0,Score0, Best,Score) :-
	concept_set_term_similarity(Term, Known, Value),
	(   Value > Score0
	->  best_term_word(TermWords, Known, Term,Value, Best,Score)
	;   best_term_word(TermWords, Known, Best0,Score0, Best,Score)
	).


concept_set_term_similarity(Term1, Term2, Value) :-
	edit_distance(Term1, Term2, Dist),
	(   Dist == 0
	->  atom_length(Term1, Len1),
	    Value is 1 + Len1 / 100		% Prefer longer matches
	;   atom_length(Term1, Len1),
	    atom_length(Term2, Len2),
	    (   (Len1 < 4; Len2 < 4)
	    ->  Value is (Len1+Len2-4*Dist) / (Len1+Len2)
	    ;   Value is (Len1+Len2-3*Dist) / (Len1+Len2)
	    )
	).


select_best([tcdo(Term,Concept,Score,Opts)|_], Term,Value, Concept,Opts, Options) :-
	check_type(Options, Opts),
	arg(1, Score, Value),		% pre(Value) or post(Value)
	Value >= 0.5, !.
select_best([_|More], Term,Score, Concept,Opts, Options) :-
	select_best(More, Term,Score, Concept,Opts, Options).


check_type(OptionsCaller, OptionsTerm) :-
	option(type(Type0), OptionsCaller),
	option(type(Type1), OptionsTerm), !,
	(   atom(Type1)
	->  Type0 == Type1
	;   is_list(Type1),
	    memberchk(Type0, Type1)
	).
check_type(_, _).


/*------------------------------------------------------------
 *  Export matches
 *------------------------------------------------------------*/

%%	concept_set_export_matches(+ConceptSet:name, +File:name, +Options:list) is det.
%
%	Export the matches of ConceptSet on all applicable SDM models to File.
%	The output is in .csv format and contains rows for: model, type, name and
%	concept.
%
%	* prefix(Label)
%	  If provided, Label is printed as the first column for all rows.
%	  This makes it possible to label the outputs of various versions of
%	  the concept set and compare them (by concatenating the outputs).

concept_set_export_matches(CS, File, Options) :-
	open(File, write, Stream),
	(   option(prefix(Prefix), Options)
	->  format(Stream, 'prefix,', []),
	    nb_setval(cs_export_prefix, true(Prefix))
	;   nb_setval(cs_export_prefix, false)
	),
	format(Stream,'model,type,name,concept~n', []),
	findall(M, sdm_propchk(M, concept_set(CS)), Ms),
	export_matches(Ms, Stream),
	close(Stream).

export_matches([], _).
export_matches([M|Ms], Stream) :-
	findall(S, sdm_element(M,S,_,_), Ss),
	export_matches(Ss, M, Stream),
	export_matches(Ms, Stream).

export_matches([], _, _).
export_matches([S|Ss], Model, Stream) :-
	sdm_element_anonymous(Model, S, _), !,
	export_matches(Ss, Model, Stream).
export_matches([S|Ss], Model, Stream) :-
	sdm_element(Model, S, Type, As),
	option(name(Name), As, '*'),
	option(concept(Concept), As, '** no match **'),
	atom_to_csv(Name, Name1),
	atom_to_csv(Concept, Concept1),
	(   nb_getval(cs_export_prefix, true(Prefix))
	->  format(Stream, '~w,', [Prefix])
	;   true
	),
	format(Stream, '~w,~w,~w,~w~n', [Model,Type,Name1,Concept1]),
	export_matches(Ss, Model, Stream).


/*------------------------------------------------------------
 *  Storage
 *------------------------------------------------------------*/

%%	concept_set(+Set:atom) is semidet.
%%	concept_set(-Set:atom) is nondet.
%
%	Succeeds if Set is the name of a concept set.

concept_set(Set) :-
	concept_set(Set, _).


%%	concept_set_property(+Set:atom, +Property:term) is semidet.
%	concept_set_property(+Set:atom, -Property:term) is nondet.
%
%	Succeeds if Set has Property.

concept_set_property(Set, Property) :-
	nonvar(Property), !,
	concept_set(Set, Props),
	memberchk(Property, Props).
concept_set_property(Set, Property) :-
	concept_set(Set, Props),
	member(Property, Props).


%%	concept_set_concept(-Set:atom, -Concept:atom) is nondet.
%
%	Succeeds if Concept is part of Set.


/*------------------------------------------------------------
 *  Consistency
 *------------------------------------------------------------*/

%%	concept_set_consistency(+Set:atom) is det.
%
%	Checks the consistency of the Set and prints results on the terminal.
%	Currently input terms that would match on several concepts are reported.

concept_set_consistency(Set) :-
	findall(tbc(Term,Base,Concept,Opts),
		term_concept(Set, Term, Concept, Base, Opts), TBCs),
	sort(TBCs, Sorted),
	consistent(Sorted).

consistent([]).
consistent([_]) :- !.
consistent([tbc(Term,Base1,Concept1,Opts1), tbc(Term,Base2,Concept2,Opts2)|TBCs]) :- !,
	option(type(Type1), Opts1, ''),
	option(type(Type2), Opts2, ''),
	format('~n', []),
	format('~w~40|~t ~w (~w) ~w~n', [Term,Base1,Concept1,Type1]),
	format('  ~40|~t ~w (~w) ~w~n', [     Base2,Concept2,Type2]),
	consistent([tbc(Term,Base2,Concept2,Opts2)|TBCs]).
consistent([_|TBCs]) :-
	consistent(TBCs).


/*------------------------------------------------------------
 *  Unused terms
 *------------------------------------------------------------*/

%%	concept_set_unused_terms(+Set:atom) is det.
%
%	Compares the terms given for the concept set and those actually used
%	in the current set of models.  Unused terms are reported on the terminal.

concept_set_unused_terms(Set) :-
	findall(tc(T,C), ( term_concept(Set, _, C, T, Opts),
			   \+ memberchk(accept(false), Opts)), TCs),
	list_to_set(TCs, Unique),
	concept_set_unused_terms(Unique, Set).

concept_set_unused_terms([], _).
concept_set_unused_terms([tc(T,C)|TCs], Set) :-
	term_concept_cache(_, C, T, _, Set, _), !,
	concept_set_unused_terms(TCs, Set).
concept_set_unused_terms([tc(T,C)|TCs], Set) :-
	format('~w~40|~t~w~n', [T,C]),
	concept_set_unused_terms(TCs, Set).



:- dynamic
	concept_set/2,			% Set x Attributes
	concept_set_concept/2,		% Set x Concept

	term_concept/5,			% Set x Term -> Concept <- Base x Options
	sub_string_concept/4,		% Set x Sub -> Concept <- Options
	term_concept_cache/6.		% Term -> Concept X Match x Value x Set <- Options


assert_concept_set_concept(Set, Concept) :-
	(   concept_set_concept(Set, Concept)
	;   assert(concept_set_concept(Set,Concept))
	), !.

add_concept_set_property(Set, Prop) :-
	retract(concept_set(Set,Props)),
	assert(concept_set(Set,[Prop|Props])).

clear_cache_all :-
	clear_cache(_).

clear_cache(Set) :-
	retractall(concept_set(Set,_)),
	retractall(concept_set_concept(Set,_)),
	retractall(sub_string_concept(_,_,_,_)),
	retractall(term_concept(Set,_,_,_,_)),
	retractall(term_concept_cache(_,_,_,_,Set,_)).

:- initialization
	listen(clear_cache_all, clear_cache_all).
