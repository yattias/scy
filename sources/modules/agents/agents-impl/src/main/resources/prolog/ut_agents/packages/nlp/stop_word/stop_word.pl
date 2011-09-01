/*  $Id$
 *  
 *  File	stop_word.pl
 *  Part of	Natural Language Processing
 *  Author	Anjo Anjewierden, anjo@science.uva.nl
 *  Purpose	Stop words for various languages
 *  Works with	SWI-Prolog (www.swi-prolog.org)
 *  
 *  Notice	Copyright (c) 2001, 2004  University of Amsterdam
 *  
 *  History	17/04/01  (Created)
 *  		19/12/04  (Last modified)
 */ 

/*------------------------------------------------------------
 *  Directives
 *------------------------------------------------------------*/

:- module(stop_word_stop_word,
	  [ stop_word/2,		% Word x Language
	    stop_word_language/1,	% Language
	    stop_word_languages/2	% Word -> Languages
	  ]).

:- use_module(load).


/*------------------------------------------------------------
 *  Languages for which stop words are available
 *------------------------------------------------------------*/

stop_word_language(danish).
stop_word_language(dutch).
stop_word_language(english).
stop_word_language(finnish).
stop_word_language(french).
stop_word_language(german).
stop_word_language(italian).
stop_word_language(portuguese).
stop_word_language(spanish).
stop_word_language(swedish).


/*------------------------------------------------------------
 *  Languages in which a given word is a stop word
 *------------------------------------------------------------*/

stop_word_languages(Word, Languages) :-
	findall(L, stop_word_language(L), Ls),
	stop_word_languages(Ls, Word, Languages).

stop_word_languages([], _, []).
stop_word_languages([L|Ls], Word, [L|More]) :-
	stop_word(Word, L), !,
	stop_word_languages(Ls, Word, More).
stop_word_languages([_|Ls], Word, Languages) :-
	stop_word_languages(Ls, Word, Languages).


/*------------------------------------------------------------
 *  Stop words
 *------------------------------------------------------------*/

stop_word(Word, Language) :-
	w(Language, Word).


/*------------------------------------------------------------
 *  Internal
 *------------------------------------------------------------*/

w(danish, W) :- !,
        stoplist_danish:stop(W).
w(dutch, W) :- !,
        stoplist_dutch:stop(W).
w(english, W) :- !,
        stoplist_english:stop(W).
w(finnish, W) :- !,
        stoplist_finnish:stop(W).
w(french, W) :- !,
        stoplist_french:stop(W).
w(german, W) :- !,
        stoplist_german:stop(W).
w(italian, W) :- !,
        stoplist_italian:stop(W).
w(portuguese, W) :- !,
        stoplist_portuguese:stop(W).
w(spanish, W) :- !,
        stoplist_spanish:stop(W).
w(swedish, W) :- !,
        stoplist_swedish:stop(W).
w(Language, _W) :-
	format('filter_stop_words(~w, _) - unknown language~n', [Language]).
