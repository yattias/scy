/*  $Id$
 *  
 *  File	load.pl
 *  Part of	Common UvA Tool Environment (CUTE)
 *  Author	Anjo Anjewierden, a.a.anjewierden@utwente.nl
 *  Purpose	Defining search paths and other global parameters
 *  Works with	SWI-Prolog (www.swi-prolog.org)
 *  
 *  Notice	Copyright (c) 2007-2011  University of Twente
 *		Copyright (c) 2001-2007  University of Amsterdam
 *  
 *  History	23/08/01  (Created)
 *  		13/04/11  (Last modified)
 */ 

/*------------------------------------------------------------
 *  Directives
 *------------------------------------------------------------*/

:- module(load_params,
	  []).

:- ensure_loaded(user:portray).

:- multifile
	user:file_search_path/2.

:- dynamic
	user:file_search_path/2.

:-
        prolog_load_context(directory, CurrentDir),
        file_directory_name(CurrentDir, Parent),
	file_directory_name(Parent, ParentParent),
	concat_atom([ParentParent], Cute),
	concat_atom([ParentParent,'/packages'], Packages),
	concat_atom([ParentParent,'/dictionaries'], Dictionaries),
	concat_atom([ParentParent,'/research'], Research),
	concat_atom([ParentParent,'/tools'], Tools),
	concat_atom([ParentParent,'/data'], Data),
	concat_atom([ParentParent,'/applications'], Applications),
        assertz(user:file_search_path(cute,Cute)),
        assertz(user:file_search_path(packages,Packages)),
        assertz(user:file_search_path(dictionaries,Dictionaries)),
        assertz(user:file_search_path(research,Research)),
        assertz(user:file_search_path(tools,Tools)),
        assertz(user:file_search_path(data,Data)),
        assertz(user:file_search_path(applications,Applications)).


	% PCE library
user:file_search_path(semweb, library(semweb)).		% Semantic Web library
user:file_search_path(http, library(http)).		% HTTP library
user:file_search_path(doc, pce('prolog/lib/doc')).	% Doc library

	% Tools
user:file_search_path(analog, tools(analog)).
user:file_search_path(tree_tagger, tools(tree_tagger)).

user:file_search_path(foreign, tools('tree_tagger/bin')).

	% Packages
user:file_search_path(atom, packages(atom)).
user:file_search_path(chats, packages(chats)).
user:file_search_path(corpus, packages(corpus)).
user:file_search_path(dac, packages(dac)).
user:file_search_path(data_set, packages(data_set)).
user:file_search_path(date, packages(date)).
user:file_search_path(dictionaries_dutch, dictionaries(dutch)).
user:file_search_path(dictionaries_english, dictionaries(english)).
user:file_search_path(dicts, packages(dicts)).
user:file_search_path(frequency, packages(frequency)).
user:file_search_path(gazetteer, nlp(gazetteer)).
user:file_search_path(graph, packages(graph)).
user:file_search_path(lexical, packages(lexical)).
user:file_search_path(list, packages(list)).
user:file_search_path(math, packages(math)).
user:file_search_path(nlp, packages(nlp)).
user:file_search_path(ontology, packages(ontology)).
user:file_search_path(opml, packages(opml)).
user:file_search_path(regex, packages(regex)).
user:file_search_path(rss, packages(rss)).
user:file_search_path(sigmund, packages(sigmund)).
user:file_search_path(sort, packages(sort)).
user:file_search_path(source, packages(source)).
user:file_search_path(sqlspaces, packages(sqlspaces)).
user:file_search_path(stats, packages(stats)).
user:file_search_path(stop_word, nlp(stop_word)).
user:file_search_path(t20plugin, packages('triple20/Plugins')).
user:file_search_path(termdoc, packages(termdoc)).
user:file_search_path(tokenise, packages(tokenise)).
user:file_search_path(toko, packages(toko)).
user:file_search_path(triple20, packages('triple20/src')).
user:file_search_path(triple20_base, packages('triple20/Ontologies/Base')).
user:file_search_path(utilities, packages(utilities)).
user:file_search_path(vector, packages(vector)).
user:file_search_path(visualisation, packages(visualisation)).
user:file_search_path(vizbiz, packages(vizbiz)).
user:file_search_path(xml_view, packages(xml_view)).
