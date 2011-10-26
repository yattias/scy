/*  $Id$
 *  
 *  File	term_set.pl
 *  Part of	IC - educational data mining
 *  Author	Anjo Anjewierden, a.a.anjewierden@utwente.nl
 *  Purpose	Term set specification
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

:- module(term_set_specification_ic,
	  [ term_set_create/2,			% Set x Properties
	    term_set_delete/1,			% Set

	    term_set/1,		   		% Set
	    term_set_properties/2,		% Set -> Properties
	    term_set_property/2,		% Set -> Property
	    term_set_propchk/2,			% Set x Property
	    term_set_add_property/2,		% Set x Property

	    term_set_parse/3,			% Source -> Set <- Options
	    term_set_manage/2,			% Set x Options

	    term_set_term/2,			% Set -> Term
	    term_set_term_string/3,		% Set x Term -> String
	    term_set_term_string_properties/4,	% Set x Term x String -> Properties

	    term_set_normalise_string/3,	% String -> Normalised <- Options
	    term_set_string_similarity/3,	% Term1 x Term -> Similarity
	    
	    term_set_match_string/4,		% Set x String -> Term <- Options
	    term_set_matches/4,			% Set x String -> Terms <- Options
	    
	    term_set_export_matches/3,		% Set x File <- Options
	    term_set_unused_strings/1		% Set
	  ]).

:- use_module(load).

:- use_module(library(lists), [append/3, delete/3, list_to_set/2, member/2,
			       subtract/3]).
:- use_module(library(sgml), [load_structure/3]).
:- use_module(library(option), [option/2, option/3, select_option/4, merge_options/3]).

:- use_module(normalise_string, [normalise_string/3, normalise_string_manage/1]).

:- use_module(dac(atom_to_csv), [atom_to_csv/2]).
:- use_module(sdm(model), [sdm_element_anonymous/3, sdm_element/4]).
:- use_module(sort(compound_terms), [compound_terms_sort/3]).
:- use_module(atom(edit_distance), [edit_distance/3, damerau_edit_distance/3]).
:- use_module(utilities(xml), [xml_memberchk/2, xml_avs_to_terms/2, source_to_xml/3]).


/**	<module> Term sets

A term set defines a set of terms and the corresponding syntactic strings learners
might use for these terms.  The basic functionality is to take a name provided
by a learner and map it to one of the terms in the set.

@author	Anjo Anjewierden
*/


%%	term_set_parse(+Source:term, -TermSet:atom, +Options:list) is det.
%
%	Source contains the term set.  Options are:
%
%	* term_set(Name)
%	  Override the identifier of the term set, normally the one in the
%	  file is used.

term_set_parse(Source, Set, Options) :-
	source_to_xml(Source, XML,
		       [ dialect(xml),
			 max_errors(-1),
			 syntax_errors(quiet),
			 space(remove)
		       ]),
	(   nonvar(Set)
	->  true
	;   (   option(term_set(Set), Options)
	    ->  true
	    ;   xml_memberchk(element(term_set,As,_), XML),
		memberchk(name=Set, As)
	    )
	),
	term_set_module(Set, Module),
	dynamic(Module:string_term/4),
	dynamic(Module:string_term_cache/5),
	option(verbose(Bool), Options, false),
	parse(XML, Bool, Set, Module, [word_order(false)]),
	(   Source = file(File)
	->  absolute_file_name(File, AbsFile),
	    time_file(AbsFile, TimeStamp),
	    term_set_add_property(Set, file(AbsFile)),
	    term_set_add_property(Set, last_modified(TimeStamp))
	;   true
	).

parse([], _, _, _, _).
parse([H|T], Verbose, Set, Module, Options) :-
	(   Verbose == true
	->  format('~w~n', [H])
	;   true
	),
	parse_h(H, Verbose, Set, Module, Options),
	parse(T, Verbose, Set, Module, Options).

parse_h(element(T,As,C), Verbose, Set, Module, Options) :- !,
	parse_t(T,As,C, Verbose, Set, Module, Options).
parse_h(_, _, _, _, _).

parse_t(term_set,As,C, Verbose, Set, Module, Options0) :- !,
	xml_avs_to_terms(As, Props0),
	semi_colon_in_type_props(Props0, Props),
	term_set_create(Set, Props),
	merge_options(Props, Options0, Options),
	parse(C, Verbose, Set, Module, Options).
parse_t(term,As,C, Verbose, Set, Module, Options) :- !,
	memberchk(name=Term, As),
	nb_setval(term_set_current, Term),
	assert_term_set_term(Set, Term),
	xml_avs_to_terms(As, TermOptions),
	merge_options(TermOptions, Options, AllOptions),
	parse(C, Verbose, Set, Module, AllOptions).
parse_t(string, As, [String], _Verbose, Set, Module, Options) :- !,
	xml_avs_to_terms(As, StringOptions),
	nb_getval(term_set_current, Term),
	merge_options(StringOptions, Options, AllOptions),
	add_term_string(Term, String, Set,Module, AllOptions).
parse_t(sub_string, As, [Sub], _Verbose, Set, Module, Options) :- !,
	xml_avs_to_terms(As, SubOptions),
	nb_getval(term_set_current, Term),
	merge_options(SubOptions, Options, AllOptions),
	add_term_string(Term, Sub, Set,Module, [method(sub_string)|AllOptions]).
parse_t(C, _, _, Verbose, _, _) :-				% Ignore everything else
	(   Verbose == true
	->  format('*** ignoring ~w ***~n', [C])
	;   true
	).


semi_colon_in_type_props([], []).
semi_colon_in_type_props([H|T], Result) :-
	semi_colon_in_type_prop(H, H2),
	semi_colon_in_type_props(T, More),
	append(H2, More, Result).

semi_colon_in_type_prop(type(Type), RealType) :- !,
	(   sub_atom(Type, _,_,_, ';')
	->  atomic_list_concat(Types0, ';', Type),
	    findall(type(T), ( member(Type0,Types0),
			 normalize_space(atom(T), Type0)), RealType)
	;   normalize_space(atom(Type1), Type),
	    RealType = [type(Type1)]
	).
semi_colon_in_type_prop(Prop, [Prop]).


add_term_string(Term, String, Set,Module, Options) :-
	option(word_order(WO), Options, false),
	assert_term_set_term_string_properties(Set, Term, String, Options),
	(   option(method(sub_string), Options)
	->  atomic_list_concat(Words, ' ', String),
	    atomic_list_concat(Words, String1),
	    assert_string_term(Module, String1, Term, String, Options)
	;   term_set_normalise_string(String, Norm, Options),
	    (   WO == true
	    ->  atomic_list_concat(Words, ' ', String),
		atomic_list_concat(Words, String1),
		assert_string_term(Module,String1,Term,String,Options)
	    ;   term_set_normalise_string(String, Norm, Options),
		atomic_list_concat(Words, ' ', Norm),
		forall(permutation(Words,Perm),
		       ( atomic_list_concat(Perm, '', T),
			 assert_string_term(Module, T, Term, String, Options)))
	    )
	).


assert_string_term(Module, String, Term, BaseString, Options) :-
	assert(Module:string_term(String,Term,BaseString,Options)).



/*------------------------------------------------------------
 *  Remove unwanted codes (perhaps should be replaced by a space)
 *------------------------------------------------------------*/

unwanted_code(47).		% /

remove_unwanted_chars(Term0, Term) :-
	atom_codes(Term0, Codes0),
	remove_unwanted_codes(Codes0, Codes),
	atom_codes(Term, Codes).

remove_unwanted_codes([], []).
remove_unwanted_codes([Code|Codes], More) :-
	unwanted_code(Code), !,
	remove_unwanted_codes(Codes, More).
remove_unwanted_codes([Code|Codes], [Code|More]) :-
	remove_unwanted_codes(Codes, More).



%%	term_set_manage(+Set:atom, +Options:list) is det.
%
%	Options are:
%
%	* cache(clear)
%	  Clear the cache of string to term matches computed
%	  previously.  Mainly for maintenance reasons.
%
%	* language(Language)
%	  Language used for stop words in string normalisation (term_set_normalise_string/3).

term_set_manage(Set, Options) :-
	term_set_manage2(Options, Set).

term_set_manage2([], _).
term_set_manage2([Option|Options], Set) :-
	term_set_manage_option(Option, Set),
	term_set_manage2(Options, Set).

term_set_manage_option(cache(clear), Set) :-
	term_set_module(Set, Module),
	retractall(Module:string_term_cache(_,_,_,_,_)).
term_set_manage_option(language(Language), _Set) :-
	normalise_string_manage([language(Language)]).


/*------------------------------------------------------------
 *  Normalise strings
 *------------------------------------------------------------*/

%%	term_set_normalise_string(+String:atom, -Normalised:atom, +Options:list) is det.
%
%	Normalised is the normalised String after applying Options.  Options
%	are:
%
%	* downcase(Bool)
%	  If Bool is =true= (default), then all upper case characters in String
%	  are replaced by lower case characters (using downcase/2).
%
%	* punctuation(Bool)
%	  If Bool is =true= (default), all punctuation characters are removed
%	  from String.  Punctuation characters are those for which char_type/2
%	  succeeds with =punct=.
%
%	* diacritics(Bool)
%	  If Bool is =true= (default), all diacritics are replaced by their
%	  more simple variants (see no_diacritics_atom/2).

term_set_normalise_string(String, Normalised, Options) :-
	normalise_string(String, Norm, Options),
	atomic_list_concat(Words, ' ', Norm),
	atomic_list_concat(Words, Normalised).


/*------------------------------------------------------------
 *  Storage
 *------------------------------------------------------------*/

%%	term_set_term(-Set:atom, -Term:atom) is nondet.
%
%	Succeeds if Term is part of Set.

:- dynamic
	term_set_term/2,			% Set x Term
	term_set_term_string_properties/4.	% Set x Term x String x Properties
%	string_term/4		% Set: String -> Term x BaseString x Options
%	string_term_cache/5	% Set: String -> Term x BaseString x Score x Options

term_set_term_string(Set, Term, String) :-
	term_set_term_string_properties(Set, Term, String, _Properties).

assert_term_set_term(Set, Term) :-
	(   term_set_term(Set, Term)
	;   assert(term_set_term(Set,Term))
	), !.

assert_term_set_term_string_properties(Set, Term, String, Properties) :-
	assert_term_set_term(Set, Term),
	assert(term_set_term_string_properties(Set,Term,String,Properties)).


/*------------------------------------------------------------
 *  Unused strings
 *------------------------------------------------------------*/

%%	term_set_unused_strings(+Set:atom) is det.
%
%	Compares the terms given for the concept set and those actually used
%	in the current set of models.  Unused terms are reported on the terminal.

term_set_unused_strings(Set) :-
	term_set_module(Set, Module),
	findall(tc(T,C), ( Module:string_term(_, C, T, Opts),
			   \+ memberchk(accept(false), Opts)), TCs),
	list_to_set(TCs, Unique),
	term_set_module(Set, Module),
	term_set_unused_strings(Unique, Module).

term_set_unused_strings([], _).
term_set_unused_strings([tc(T,C)|TCs], Module) :-
	Module:string_term_cache(_, C, T, _, _), !,
	term_set_unused_strings(TCs, Module).
term_set_unused_strings([tc(T,C)|TCs], Module) :-
	format('~w~40|~t~w~n', [T,C]),
	term_set_unused_strings(TCs, Module).


/*------------------------------------------------------------
 *  Existence
 *------------------------------------------------------------*/

term_set_create(Set, Properties) :-
	term_set_delete(Set),
	assert(term_set_properties(Set,Properties)),
	(   memberchk(language(Language), Properties)
	->  term_set_manage(Set, [language(Language)])
	;   true
	).


term_set_delete(Set) :-
	term_set(Set), !,
	term_set_module(Set, Module),
	retractall(Module:string_term(_,_,_,_)),
	retractall(Module:string_term_cache(_,_,_,_,_)),
	retractall(term_set_properties(Set,_)),
	retractall(term_set_term(Set,_)).
term_set_delete(_).


%%	term_set(+Set:atom) is semidet.
%%	term_set(-Set:atom) is nondet.
%
%	Succeeds if Set is the name of a concept set.

term_set(Set) :-
	term_set_properties(Set, _).


%%	term_set_properties(-Set:atom, -Properties:list) is nondet.
%
%	Succeeds if Set has Properties

:- dynamic
	term_set_properties/2.		% Set x Properties


%%	term_set_property(-Set:atom, -Property:term) is nondet.
%
%	Succeeds if Set has Property.

term_set_property(Set, Prop) :-
	term_set_properties(Set, Props),
	member(Prop, Props).


%%	term_set_propchk(-Set:atom, -Property:term) is semidet.
%
%	Succeeds once if Set has Property.

term_set_propchk(Set, Prop) :-
	term_set_properties(Set, Props),
	memberchk(Prop, Props).


term_set_add_property(Set, Prop) :-
	retract(term_set_properties(Set,Props)),
	assert(term_set_properties(Set,[Prop|Props])).


term_set_module(Set, Module) :-
	atom_concat(Set, '_term_set', Module).


/*------------------------------------------------------------
 *  Export matches
 *------------------------------------------------------------*/

%%	term_set_export_matches(+Set:name, +File:name, +Options:list) is det.
%
%	Export the matches of Set on all applicable Gls's to File.
%	The output is in .csv format and contains rows for: gls, type, name and
%	term.
%
%	* prefix(Label)
%	  If provided, Label is printed as the first column for all rows.
%	  This makes it possible to label the outputs of various versions of
%	  the term set and compare them (by concatenating the outputs).
%
%	@tbd Only works for SDM at this moment.

term_set_export_matches(CS, File, Options) :-
	open(File, write, Stream),
	(   option(prefix(Prefix), Options)
	->  format(Stream, 'prefix,', []),
	    nb_setval(cs_export_prefix, true(Prefix))
	;   nb_setval(cs_export_prefix, false)
	),
	format(Stream,'gls,type,name,term~n', []),
	findall(M, gls_propchk(M, term_set(CS)), Ms),
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
 *  String similarity
 *------------------------------------------------------------*/

:- dynamic
	tss/2.		% String1-String2 -> Similarity

term_set_string_similarity(String1, String2, Similarity) :-
	atomic_list_concat([String1,String2], '-', BothStrings),
	retractall(tss(_,_)),		% TBD - remove this line
	(   tss(BothStrings, KnownSimilarity)
	->  Similarity = KnownSimilarity
	;   atom_length(String1, Len1),
	    atom_length(String2, Len2),
	    damerau_edit_distance(String1, String2, Distance),
	    (   (Len1 < 5; Len2 < 5)
	    ->  (   Distance == 0
		->  ComputedSimilarity = 1.0
		;   ComputedSimilarity = 0.0
		)
	    ;   ComputedSimilarity is (max(Len1,Len2) - Distance) / max(Len1,Len2)
	    ),
	    assert(tss(BothStrings,ComputedSimilarity)),
	    Similarity = ComputedSimilarity
	).


term_set_string_term(Set, String, Term, Base, Options) :-
	term_set_module(Set, Module),
	Module:string_term(String, Term, Base, Options).


/*------------------------------------------------------------
 *  All matches
 *------------------------------------------------------------*/

term_set_matches(Set, String0, Terms, Options) :-
	option(threshold(Threshold), Options, 0.5),
	pre_string_terms(Set, PreTerms),
	term_set_normalise_string(String0, String, []),
	atomic_list_concat(Words, ' ', String),
	atomic_list_concat(Words, '', String1),
	matches_above_threshold(PreTerms, String1, Threshold, STSs),
	compound_terms_sort(STSs, Terms, [arg(3), functor(sts), order(descending)]).


/*------------------------------------------------------------
 *  Matching a given string against the term set
 *------------------------------------------------------------*/

term_set_match_string(Set, String0, Term, Options) :-
	String0 \== '',
	select_option(threshold(Threshold), Options, RestOptions, 0.5),
	pre_string_terms(Set, PreTerms),
	term_set_normalise_string(String0, String, RestOptions),
	match_sts(PreTerms, String, STSs),
	compound_terms_sort(STSs, Sorted, [arg(3), functor(sts), order(descending)]),
	memberchk(sts(_,Term,Similarity), Sorted),
	Similarity > Threshold.

pre_string_terms(Set, PreTerms) :-
	term_set_module(Set, Module),
	findall(stb(String,Term,Base),
		( Module:string_term(String,Term,Base,Options),
		  \+ memberchk(when(post), Options),
		  \+ memberchk(method(sub_string), Options)), PreTerms).

match_sts([], _, []).
match_sts([stb(Match,Term,Base)|STs], String, [sts(Base,Term,Similarity)|STSs]) :-
	term_set_string_similarity(Match, String, Similarity),
	match_sts(STs, String, STSs).

matches_above_threshold([], _, _, []).
matches_above_threshold([stb(Match,Term,Base)|STs], String, Threshold,
			[sts(Base,Term,Similarity)|STSs]) :-
	term_set_string_similarity(Match, String, Similarity),
	Similarity > Threshold, !,
	matches_above_threshold(STs, String, Threshold, STSs).
matches_above_threshold([_|STs], String, Threshold, STSs) :-
	matches_above_threshold(STs, String, Threshold, STSs).

