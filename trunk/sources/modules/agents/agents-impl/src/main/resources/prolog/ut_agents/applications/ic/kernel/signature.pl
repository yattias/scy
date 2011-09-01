/*  $Id$
 *  
 *  File	signature.pl
 *  Part of	IC - behavioural data mining
 *  Author	Anjo Anjewierden, a.a.anjewierden@utwente.nl
 *  Purpose	Signatures for Co-Lab models
 *  Works with	SWI-Prolog (www.swi-prolog.org)
 *  
 *  Notice	Copyright (c) 2009  University of Twente
 *  
 *  History	23/01/09  (Created)
 *  		20/05/10  (Last modified)
 */

/*------------------------------------------------------------
 *  Directives
 *------------------------------------------------------------*/

:- module(signature_kernel_ic,
	  [ item2_signature/2,		% Item x Signature
	    item2_signature_raw/2,	% Item x Signature (raw; w/0 id conversion)
	    item2_set_signature/2,	% Item x Signature

		% Signature lists
	    signature_list/4,		% Context x View x Sigs x Items
	    signature_list_add/4	% Context x View x Sigs x Items
	  ]).

:- use_module(load).

:- use_module(library(lists), [select/3]).
:- use_module(library('dialog/pretty_print'), [pretty_print/1]).

:- use_module(item2, [item2_assignment/2, item2_properties/2, item2/1,
		      item2_sender_code/2, item2_type/2, item2_chat_sender_text/3]).
:- use_module(sdm(model), [sdm_element_by_name/5,
			   sdm_element_by_name/3, sdm_element/4]).


item2_signature(Item, Sig) :-
	nonvar(Item), !,
	(   item2_signature_c(Item, Sig)
	->  true
	;   item2_signature_init(Item, Sig),
	    item2_set_signature(Item, Sig)
	).
item2_signature(Item, Sig) :-
      % var(Item),	
	item2(Item),
	item2_signature(Item, Sig).
	

item2_set_signature(Item, Sig) :-
	nonvar(Item),
	retractall(item2_signature_c(Item,_)),
	assert(item2_signature_c(Item,Sig)).


item2_signature_init(Item, Sig) :-
	nb_getval(log_file_format, simquest5), !,	% Does not have structural
	item2_type(Item, Type),
	signature_simquest5(Type, Item, Sig0),
	Sig = Sig0.
item2_signature_init(Item, Sig) :-
	nb_getval(log_file_format, simquest6), !,
	item2_type(Item, Type),
	(   signature_simquest6(Type, Item, Sig0)
	->  Sig = Sig0
	;   signature_simquest5(Type, Item, Sig0),
	    Sig = Sig0
	).
item2_signature_init(Item, Sig) :-
	item2_type(Item, Type),
	item2_properties(Item, Struct),
	(   nb_getval(log_file_format, scy)
	->  signature_scy(Type, Struct, Sig0),
	    Sig = Sig0
	;   nb_getval(log_file_format, simquest5)
	->  signature_simquest5(Type, Struct, Sig0),
	    Sig = Sig0
	;   signature_by_type(Type, Struct, Sig0),
	    symbol_to_id_conversion(Sig0, Sig)
	).

item2_signature_raw(Item, R) :-			% TBD -- without symbol -> id conversion
	item2_type(Item, Type),
	item2_properties(Item, Struct),
	signature_by_type(Type, Struct, R).


signature_by_type(Type, Struct, R) :-
	(   Type == change_specification
	->  signature(Type, Struct, R)
	;   signature(Type, Args, R),
	    same_args(Args, Struct)
	), !.

same_args([], _).
same_args([A|As], Ss) :-
	select(A, Ss, Rest),
	same_args(As, Rest).


/*------------------------------------------------------------
 *  Signature lists
 *------------------------------------------------------------*/

:- dynamic
	signature_list/4.		% Context x View x Signatures x Items

%%	signature_list(-Context:term, -View:term, -Signatures:list, -Items:list) is nondet
%
%	Succeeds if Context under View has Signatures corresponding to Items.


%%	signature_list_add(+Context:term, +View:term, +Signatures:list, +Items:list) is det
%
%	Create a signature list for the Context (e.g., session(x)),
%	under View (e.g., model(black_sphere)) containing Signatures and the
%	corresponding items.

signature_list_add(Context, View, Signatures, Items) :-
	retractall(signature_list(Context,View,_,_)),
	assert(signature_list(Context,View,Signatures,Items)).


/*------------------------------------------------------------
 *  Cache
 *------------------------------------------------------------*/

:- dynamic
	item2_signature_c/2.		% Item x Signature


clear_cache :-
	retractall(item2_signature_c(_,_)).
	

:- initialization
	clear_cache.


/*------------------------------------------------------------
 *  Convert symbolic names to ids
 *------------------------------------------------------------*/

symbol_to_id_conversion(add_flow(Symbol,Model,Type,Stock),
			add_flow(Id,Model,Type,StockId)) :- !,
	sdm_element_by_name(Model, Symbol, Id),
	(   Stock == none
	->  StockId = none
	;   sdm_element_by_name(Model, Stock, StockId)
	).
symbol_to_id_conversion(add_dataset(Symbol,Model),
			add_dataset(Id,Model)) :- !,
	sdm_element_by_name(Model, Symbol, Id).
symbol_to_id_conversion(add_auxiliary(Symbol,Model),
			add_auxiliary(Id,Model)) :- !,
	sdm_element_by_name(Model, Symbol, Id).
symbol_to_id_conversion(add_constant(Symbol,Model),
			add_constant(Id,Model)) :- !,
	sdm_element_by_name(Model, Symbol, Id).
symbol_to_id_conversion(add_stock(Symbol,Model),
			add_stock(Id,Model)) :- !,
	sdm_element_by_name(Model, Symbol, Id).
symbol_to_id_conversion(add_relation(Symbol,Model,Start,End),
			add_relation(Id,Model,StartId,EndId)) :- !,
	sdm_element_by_name(Model, Symbol, Id),
	sdm_element_by_name(Model, Start, StartId),
	sdm_element_by_name(Model, End, EndId).
symbol_to_id_conversion(rename_element(Model, Old, Label),
			rename_element(Id, Model, Old, Label)) :- !,
	sdm_element_by_name(Model, Label, Id).
symbol_to_id_conversion(Term, Term) :-
	format('symbol_to_id_converstion/2: ~w~n', [Term]).
			

/*------------------------------------------------------------
 *  SCY format (as used in Yvonne's experiment)
 *------------------------------------------------------------*/

signature_scy(view_help, Atts,
      view_help(Doc)) :- !,
	memberchk(document(Doc), Atts).
signature_scy(simulator_set_value, Atts,
      simulator_set_value(Variable, OldValue, NewValue)) :- !,
	memberchk(variable(Variable), Atts),
	memberchk(old_value(OldValue), Atts),
	memberchk(new_value(NewValue), Atts).
signature_scy(run_simulation, Atts, Sig) :- !,
	memberchk(action(Action), Atts),
	(   Action == start
	->  memberchk(values(Values), Atts),
	    Sig = start_simulation(Values)
	;   Action == stop
	->  memberchk(seconds(Secs), Atts),
	    Sig = stop_simulation(Secs)
	).
signature_scy(phase_change, Atts, phase_change(Old,New)) :- !,
	memberchk(old_phase(Old), Atts),
	memberchk(new_phase(New), Atts).
signature_scy(inspect_graph, Atts, inspect_graph(Sim,Mod)) :- !,
	memberchk(simulation_variables(Sim), Atts),
	memberchk(model_variables(Mod), Atts).
signature_scy(inspect_table, Atts, inspect_table(Sim,Mod)) :- !,
	memberchk(simulation_variables(Sim), Atts),
	memberchk(model_variables(Mod), Atts).
signature_scy(open_window, Atts, Sig) :- !,
	memberchk(window(Window), Atts),
	memberchk(area(Area), Atts),
	Sig = open_window(Window,Area).
signature_scy(show_window, Atts, Sig) :- !,
	memberchk(window(Window), Atts),
	memberchk(area(Area), Atts),
	Sig = show_window(Window,Area).
signature_scy(hide_window, Atts, Sig) :- !,
	memberchk(window(Window), Atts),
	memberchk(area(Area), Atts),
	Sig = hide_window(Window,Area).
signature_scy(activate_window, Atts, Sig) :- !,
	memberchk(window(Window), Atts),
	memberchk(area(Area), Atts),
	Sig = activate_window(Window,Area).
signature_scy(resize_window, Atts, Sig) :- !,
	memberchk(window(Window), Atts),
	memberchk(area(Area), Atts),
	Sig = resize_window(Window,Area).

signature_scy(change_specification, Atts,
      change_specification(Id, Model, Expr, Unit)) :- !,
	memberchk(id(Id), Atts),
	memberchk(expression(Expr), Atts),
	memberchk(unit(Unit), Atts),
	memberchk(model(Model), Atts).
signature_scy(rename_element, Atts,
      rename_element(Id, Model, Expr, Unit)) :- !,
	memberchk(id(Id), Atts),
	memberchk(expression(Expr), Atts),
	memberchk(unit(Unit), Atts),
	memberchk(model(Model), Atts).
signature_scy(add_relation, Atts,
      add_relation(Id,Model,From,To)) :- !,
	memberchk(id(Id), Atts),
	memberchk(from(From), Atts),
	memberchk(to(To), Atts),
	memberchk(model(Model), Atts).
signature_scy(delete_relation, Atts,
      delete_relation(Id,Model,From,To)) :- !,
	memberchk(id(Id), Atts),
	memberchk(from(From), Atts),
	memberchk(to(To), Atts),
	memberchk(model(Model), Atts).
signature_scy(add_constant, Atts,
      add_constant(Id,Model)) :- !,
	memberchk(id(Id), Atts),
	memberchk(model(Model), Atts).
signature_scy(delete_constant, Atts,
      delete_constant(Id,Model)) :- !,
	memberchk(id(Id), Atts),
	memberchk(model(Model), Atts).
signature_scy(add_stock, Atts,
      add_stock(Id,Model)) :- !,
	memberchk(id(Id), Atts),
	memberchk(model(Model), Atts).
signature_scy(delete_stock, Atts,
      delete_stock(Id,Model)) :- !,
	memberchk(id(Id), Atts),
	memberchk(model(Model), Atts).
signature_scy(add_dataset, Atts,
      add_dataset(Id,Model)) :- !,
	memberchk(id(Id), Atts),
	memberchk(model(Model), Atts).
signature_scy(delete_dataset, Atts,
      delete_dataset(Id,Model)) :- !,
	memberchk(id(Id), Atts),
	memberchk(model(Model), Atts).
signature_scy(add_flow, Atts,
      add_flow(Id,Model,Type,Stock)) :- !,
	memberchk(id(Id), Atts),
	memberchk(type(Type), Atts),
	memberchk(stock(Stock), Atts),
	memberchk(model(Model), Atts).
signature_scy(delete_flow, Atts,
      delete_flow(Id,Model)) :- !,
	memberchk(id(Id), Atts),
	memberchk(model(Model), Atts).
signature_scy(add_auxiliary, Atts,
      add_auxiliary(Id,Model)) :- !,
	memberchk(id(Id), Atts),
	memberchk(model(Model), Atts).
signature_scy(delete_auxiliary, Atts,
      delete_auxiliary(Id,Model)) :- !,
	memberchk(id(Id), Atts),
	memberchk(model(Model), Atts).
signature_scy(inspect_graph, Atts, inspect_graph(Variables)) :- !,
	memberchk(variables(Variables), Atts).
signature_scy(inspect_table, Atts, inspect_table(Variables)) :- !,
	memberchk(variables(Variables), Atts).
signature_scy(simulator_status, Atts, simulator_status(Sender, Status)) :- !,
	memberchk(sender(Sender), Atts),
	memberchk(status(Status), Atts).
signature_scy(simulator_action, Atts, simulator_action(Sender, Action)) :- !,
	memberchk(sender(Sender), Atts),
	memberchk(action(Action), Atts).
signature_scy(simulator_values, Atts, simulator_values(Sender, Values)) :- !,
	memberchk(sender(Sender), Atts),
	memberchk(values(Values), Atts).
signature_scy(simulator_message, Atts, simulator_message(Sender, Message)) :- !,
	memberchk(sender(Sender), Atts),
	memberchk(message(Message), Atts).
signature_scy(resize_window, Atts, resize_window(Win,Area)) :- !,
	memberchk(window(Win), Atts),
	memberchk(area(Area), Atts).
signature_scy(deactivate_window, Atts, deactivate_window(Win,Area)) :- !,
	memberchk(window(Win), Atts),
	memberchk(area(Area), Atts).
signature_scy(activate_window, Atts, activate_window(Win,Area)) :- !,
	memberchk(window(Win), Atts),
	memberchk(area(Area), Atts).
signature_scy(open_window, Atts, open_window(Win,Area)) :- !,
	memberchk(window(Win), Atts),
	memberchk(area(Area), Atts).
signature_scy(hide_window, Atts, hide_window(Win,Area)) :- !,
	memberchk(window(Win), Atts),
	memberchk(area(Area), Atts).
signature_scy(run_model, Atts, run_model(Sender)) :- !,
	memberchk(sender(Sender), Atts).
signature_scy(run_model_error, Atts, run_model_error(Model)) :- !,
	memberchk(model(Model), Atts).
signature_scy(element_relation_feedback, Atts, Sig) :- !,
	memberchk(model(Model), Atts),
	(   memberchk(elements(EC,EW,EA), Atts),
	    memberchk(relations(RC,RW), Atts),
	    memberchk(direction(DC,DW), Atts)
	->  Sig = element_relation_feedback(Model,elements(EC,EW,EA),relations(RC,RW),direction(DC,DW))
	;   Sig = element_relation_feedback(Model)
	).
signature_scy(evaluate_model, Atts, evaluate_model(Model,Method)) :- !,
	memberchk(model(Model), Atts),
	memberchk(method(Method), Atts).
signature_scy(one_step_run, Atts, one_step_run(Sender)) :- !,
	memberchk(sender(Sender), Atts).
signature_scy(reset_model, Atts, reset_model(Sender)) :- !,
	memberchk(sender(Sender), Atts).
signature_scy(open_model, Atts, open_model(Model)) :- !,
	memberchk(model(Model), Atts).
signature_scy(stop_model, Atts, stop_model(Sender)) :- !,
	memberchk(sender(Sender), Atts).
signature_scy(exit_application, _Atts, exit_application) :- !.
signature_scy(Type, Atts, Struct) :-
	format('signature_scy/3 ~w: ~w~n', [Type,Atts]),
	trace,
	signature_scy(Type, Atts, Struct),
	fail.


/*------------------------------------------------------------
 *  SimQuest 6 format (as used in Mieke Hageman's experiment)
 *------------------------------------------------------------*/

signature_simquest6(select_concept_map, Item, select_concept_map(ConceptMap, Concept)) :- !,
	item2_properties(Item, Args),
	memberchk(concept_map(ConceptMap), Args),
	memberchk(concept(Concept), Args).
signature_simquest6(status_concept_map, Item, status_concept_map(ConceptMap, Values)) :- !,
	item2_properties(Item, Args),
	memberchk(concept_map(ConceptMap), Args),
	memberchk(concept_values(Values), Args).
signature_simquest6(select_topic, Item, select_topic(ConceptMap)) :- !,
	item2_properties(Item, Args),
	memberchk(concept_map(ConceptMap), Args).
signature_simquest6(open_assignment, Item, open_assignment(Assignment)) :- !,
	item2_assignment(Item, Assignment).
signature_simquest6(start_simulation, Item, start_situation(Assignment,VVs)) :- !,
	item2_properties(Item, Args),
	memberchk(variable_values(VVs), Args),
	item2_assignment(Item, Assignment).
signature_simquest6(compute_simulation, Item, compute_situation(VVs)) :- !,
	item2_properties(Item, Args),
	memberchk(variable_values(VVs), Args).
signature_simquest6(run_simulation, Item, run_situation(VVs)) :- !,
	item2_properties(Item, Args),
	memberchk(variable_values(VVs), Args).


/*------------------------------------------------------------
 *  SimQuest 5 format (as used in Nadira's experiment)
 *------------------------------------------------------------*/

signature_simquest5(answer, Item, answer(Ass,Answer,Kind)) :- !,
	item2_assignment(Item, Ass),
	item2_properties(Item, Args),
	memberchk(answer(Answer,Kind), Args).
signature_simquest5(open_answer, Item, answer(Ass,Answer,Kind)) :- !,
	item2_assignment(Item, Ass),
	item2_properties(Item, Args),
	memberchk(answer(Answer,Kind), Args).
signature_simquest5(chat, Item, chat(Sender,Text,Codings)) :- !,
	item2_sender_code(Item, Sender),
	item2_chat_sender_text(Item, _, Text),
	findall(coding(Type,Value), item2_coding(Item,Type,Value), Codings).
signature_simquest5(start_simulation, Item, start_simulation(Ass,VVs)) :- !,
	item2_assignment(Item, Ass),
	findall(vv(Var,Value), item2_variable_value(Item,Var,Value), VVs).
signature_simquest5(stop_simulation, Item, stop_simulation(Ass,VVs)) :- !,
	item2_assignment(Item, Ass),
	findall(vv(Var,Value), item2_variable_value(Item,Var,Value), VVs).
signature_simquest5(simulation, Item, simulation(Ass,VVs)) :- !,
	item2_assignment(Item, Ass),
	findall(vv(Var,Value), item2_variable_value(Item,Var,Value), VVs).
signature_simquest5(start_situation, Item, start_situation(Ass,VVs)) :- !,
	item2_assignment(Item, Ass),
	findall(vv(Var,Value), item2_variable_value(Item,Var,Value), VVs).
signature_simquest5(stop_situation, Item, stop_situation(Ass,VVs)) :- !,
	item2_assignment(Item, Ass),
	findall(vv(Var,Value), item2_variable_value(Item,Var,Value), VVs).
signature_simquest5(succeed, Item, succeed(Ass)) :- !,
	item2_assignment(Item, Ass).
signature_simquest5(fail, Item, fail(Ass)) :- !,
	item2_assignment(Item, Ass).
signature_simquest5(changed_result, Item, changed_result(Ass,Args)) :- !,
	item2_assignment(Item, Ass),
	item2_properties(Item, Args).
signature_simquest5(changed_a_result, Item, changed_a_result(Ass,Args)) :- !,
	item2_assignment(Item, Ass),
	item2_properties(Item, Args).
signature_simquest5(changed_test, Item, changed_test(Ass,Args)) :- !,
	item2_assignment(Item, Ass),
	item2_properties(Item, Args).
signature_simquest5(changed_test_a, Item, changed_test_a(Ass,Args)) :- !,
	item2_assignment(Item, Ass),
	item2_properties(Item, Args).
signature_simquest5(add_hypothesis, Item, add_hypothesis(Ass,Args)) :- !,
	item2_assignment(Item, Ass),
	item2_properties(Item, Args).
signature_simquest5(remove_hypothesis, Item, remove_hypothesis(Ass,Args)) :- !,
	item2_assignment(Item, Ass),
	item2_properties(Item, Args).
signature_simquest5(start, _Item, start) :- !.
signature_simquest5(Type, _Atts, _Sig) :-
	format('signature_simquest5/3 ~w~n', [Type]),
	fail.


/*------------------------------------------------------------
 *  Original Co-Lab format
 *------------------------------------------------------------*/

signature(document_changed, [structure_changes(Changes)],
      document_changed(Changes)).

signature(container_changed, [data(Container)],
      container_changed(Container)).
signature(container_added, [data(Container)],
      container_added(Container)).
signature(container_cleared, [data(Container)],
      container_cleared(Container)).
signature(container_deleted, [data(Container)],
      container_deleted(Container)).
signature(containers_loaded, [data(Containers)],
      containers_loaded(Containers)).

signature(inspect_graph, [variables(Vars)],
      inspect_graph(Vars)).
signature(inspect_table, [variables(Vars)],
      inspect_table(Vars)). 

signature(window_event, [action(resize), window(Win), area(area(X,Y,W,H))],
      resize_window(Win, area(X,Y,W,H))).
signature(window_event, [action(deactivate), window(Win), area(area(X,Y,W,H))],
      deactivate_window(Win, area(X,Y,W,H))).
signature(window_event, [action(activate), window(Win), area(area(X,Y,W,H))],
      activate_window(Win, area(X,Y,W,H))).
signature(window_event, [action(open), window(Win), area(area(X,Y,W,H))],
      open_window(Win, area(X,Y,W,H))).
signature(window_event, [action(hide), window(Win), area(area(X,Y,W,H))],
      hide_window(Win, area(X,Y,W,H))).

signature(phenomenon_event, [sender(Sender), type(values), values(Values)],
      simulator_values(Sender, Values)).
signature(phenomenon_event, [sender(Sender), type(info), message(Message)],
      simulator_message(Sender, Message)).
signature(phenomenon_event, [sender(Sender), type(action), action(Action)],
      simulator_action(Sender, Action)).
signature(phenomenon_event, [sender(Sender), type(status), status(Status)],
      simulator_status(Sender, Status)).

signature(selected_variables, [sender(graph), variables(Vars)],
      inspect_graph(Vars)).
signature(selected_variables, [sender(table), variables(Vars)],
      inspect_table(Vars)).

signature(user_value_change, [variable(Var), value(Value)],
      simulator_set_value(Var,Value)).

signature(exit_application, [],
      exit_application).

signature(reset_model, [sender(Sender)],
      reset_model(Sender)).

signature(open_model, [model(Model)],
      open_model(Model)).

signature(run_model, [sender(Sender)],
      run_model(Sender)).

signature(run_model_error, [model(Model)],
      run_model_error(Model)).

signature(element_relation_feedback, [model(Model)],
      element_relation_feedback(Model)).

signature(evaluate_model, [model(Model), method(Method)],
      evaluate_model(Model,Method)).

signature(end_run_model, [sender(Sender)],
      end_run_model(Sender)).

signature(stop_model, [sender(Sender)],
      stop_model(Sender)).
signature(one_step_run, [symbol(Symbol),model(Model)],
      one_step_run_model(Symbol,Model)).

signature(add_flow, [symbol(Symbol),model(Model),start(Start)],
      add_flow(Symbol,Model,out,Start)).
signature(add_flow, [symbol(Symbol),model(Model),end(End)],
      add_flow(Symbol,Model,in,End)).
signature(add_flow, [symbol(Symbol),model(Model)],
      add_flow(Symbol,Model,flow,none)).
signature(add_dataset, [symbol(Symbol),model(Model)],
      add_dataset(Symbol,Model)).
signature(add_constant, [symbol(Symbol),model(Model)],
      add_constant(Symbol,Model)).
signature(add_stock, [symbol(Symbol),model(Model)],
      add_stock(Symbol,Model)).
signature(add_auxiliary, [symbol(Symbol),model(Model)],
      add_auxiliary(Symbol,Model)).
signature(add_relation, [symbol(Symbol),model(Model),start(Start),end(End)],
      add_relation(Symbol,Model,Start,End)).

signature(delete_flow, [symbol(Symbol),model(Model)],
      delete_flow(Symbol,Model)).
signature(delete_dataset, [symbol(Symbol),model(Model)],
      delete_dataset(Symbol,Model)).
signature(delete_constant, [symbol(Symbol),model(Model)],
      delete_constant(Symbol,Model)).
signature(delete_stock, [symbol(Symbol),model(Model)],
      delete_stock(Symbol,Model)).
signature(delete_auxiliary, [symbol(Symbol),model(Model)],
      delete_auxiliary(Symbol,Model)).
signature(delete_relation, [symbol(Symbol),model(Model),start(Start),end(End)],
      delete_relation(Symbol,Model,Start,End)).


signature(rename_element, [model(Model), label(Label), old_label(Old)],
      rename_element(Model, Old, Label)).

signature(change_specification, [symbol(Symbol), model(Model)],
      change_specification(Id, Model, Expr, Unit)) :-
	(   sdm_element_by_name(Model, Symbol, Id, Type, Atts)
	->  true
	;   sdm_element(Model, Symbol, Type, Atts),
	    Id = Symbol
	),
	(   Type == stock
	->  (   memberchk(initial(Expr), Atts)
	    ->  true
	    ;   Expr = ''
	    )
	;   (   memberchk(expression(Expr), Atts)
	    ->  true
	    ;   Expr = ''
	    )
	),
	(   memberchk(unit(Unit), Atts)
	->  true
	;   Unit = ''
	).


