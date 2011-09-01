/*  $Id$
 *  
 *  File	seq.pl
 *  Part of	IC - educational data mining
 *  Author	Anjo Anjewierden, a.a.anjewierden@utwente.nl
 *  Purpose	Sequences of items
 *  Works with	SWI-Prolog (www.swi-prolog.org)
 *  
 *  Notice	Copyright (c) 2008, 2009, 2010  University of Twente
 *  
 *  History	19/02/08  (Created)
 *  		24/11/10  (Last modified)
 */ 

/*------------------------------------------------------------
 *  Directives
 *------------------------------------------------------------*/

:- module(seq_kernel_ic,
	  [ seq_create/3,		% Seq x Members x Atts
	    seq_delete/1,		% Seq

	    seq_export/1,		% Stream
	    seq_import_term/1,		% Term
	    
	    seq/1,			% Seq
	    seq_members/2,		% Seq -> Members
	    seq_property/2,		% Seq -> Prop(Value)
	    seq_append/2,	   	% Seq x Member

	    seq_sub_items/3,		% Seq -> Items x Options
	    seq_sub/4			% Seq x Item -> Sub x Options
	  ]).

:- use_module(load).

:- use_module(library(option), [option/2]).
:- use_module(library(gensym), [gensym/2]).
:- use_module(library(lists), [append/3, last/2, member/2, nth1/3]).

/*
  seq(session(Session), ...)
  seq(session_assignment(Session,Assignment,StartNth), ...)
*/

seq_create(Seq, Members, Props) :-
	retractall(seq(Seq,_,_)),
	assert(seq(Seq,Members,Props)).

seq_delete(Seq) :-
	retractall(seq(Seq,_,_)),
	retractall(seq_document(Seq,_)).

seq_append(Seq, Member) :-
	(   retract(seq(Seq,Members,Props))
	->  append(Members, [Member], New),
	    assert(seq(Seq,New,Props))
	;   seq_create(Seq, [Member], [])
	), !.

seq_member(Seq, M) :-
	seq_members(Seq, Members),
	member(M, Members).

seq_members(Seq, Members) :-
	ground(Seq), 
	seq(Seq, Members, _), !.
seq_members(Seq, Members) :-
	seq(Seq, Members, _).

seq(Seq) :-
	seq(Seq, _, _).

seq_property(Seq, Prop) :-
	seq(Seq, _, Props),
	member(Prop, Props).

seq_export(Stream) :-
	forall(seq(Seq, Members, Props),
	       format(Stream, 'seq(~q, ~q, ~q).~n',
		      [Seq,Members,Props])).

seq_import_term(seq(Seq,Members,Props)) :-
	seq_create(Seq, Members, Props).


/*------------------------------------------------------------
 *  Storage
 *------------------------------------------------------------*/

:- dynamic
	seq/3.			% Seq x Members x Props

:- initialization
	listen(clear_cache_all, clear_cache_all).

clear_cache_all :-
	retractall(seq(_,_,_)).


/*------------------------------------------------------------
 *  Create a sub sequence
 *------------------------------------------------------------*/

seq_sub_items(Seq, Items, Options) :-
	option(first(A), Options),
	option(last(B), Options),
	seq_members(Seq, Items0),
	nth1(From, Items0, A),
	nth1(To, Items0, B),
	findall(I, (between(From,To,N), nth1(N,Items0,I)), Items).

seq_sub(Seq, Item, Sub, Options) :-
	option(before(B), Options),
	option(after(A), Options),
	seq_members(Seq, Items0),
	seq_sub_options(Options, Items0, Items),
	length(Items, Len),
	nth1(Nth, Items, Item),
	From is max(Nth-B,1),
	To is min(Nth+A,Len),
	findall(I, (between(From,To,N), nth1(N,Items,I)), SubItems),
	gensym(sub, Sub),
	seq_create(Sub, SubItems, []).


seq_sub_options([], In, Out) :- !,
	In = Out.
seq_sub_options([Option|Options], In, Out) :-
	seq_sub_option(Option, In, Tmp),
	seq_sub_options(Options, Tmp, Out).

seq_sub_option(before(_), In, In).
seq_sub_option(after(_), In, In).
seq_sub_option(ignore_type(Type), In, Out) :-
	findall(I, (member(I,In), \+ item2_type(I,Type)), Out).


seq_first_last(Seq, First, Last) :-
	findall(Nth-I, (seq_member(Seq,I), item2_nth(I,Nth)), Ms),
	keysort(Ms, Sorted),
	Sorted = [_-First|_],
	last(Sorted, _-Last).
