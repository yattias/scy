:- consult('tspl_sqls_mt.pl').
:- consult('hungarian.pl').
:- dynamic(con/1).
:- dynamic(ts/2).
:- dynamic(ts_port/1).
:- dynamic(sc/2).
:- dynamic(compare_list/6).
:- dynamic(compare_elo/5).
:- dynamic(elo/4).

ts_port(2525).
ts_host('localhost').
%Fakten
related_diff(0.5).

  
:- assert(count(0)).
  
start_timer :-
	retract(count(C)),
	CNew is C + 1,
	write('Start Timer '),
	writeln(CNew),
	get_time(T),
	assert(time(T)),
	assert(count(CNew)).
	  
stop_timer :-
	retract(time(T1)),
	get_time(T2),
	TDiff is T2 - T1,
	count(C),
	write('Stop Timer '),
	write(C),
	write(' at '),
	write(TDiff),
	writeln(' sec!').


connect_to_ts :-
ts_host(Host),
ts_port(Port),
tspl_connect_to_ts(command, Host, Port, TS),
	assert(con(TS)).
wait_for_work :-
connect_to_ts,
con(TS),
repeat,
	%A Simple Heartbeat
	%tspl_actual_field(string,'ping', P0),
	%tspl_formal_field(string,PingID),
	%tspl_tuple([P0,PingID], Ping),
	%tspl_wait_to_take(TS, Ping, PingReceived),
	%writeln('----------Ping received----------'),
	%tspl_tuple_field(PingReceived,1,ID),
	%tspl_actual_field(string, 'pong', P2),
	%tspl_actual_field(string,ID,P3),
	%tspl_tuple([P2,P3],Pong),
	%tspl_write(TS,Pong),
	%writeln('----------Pong written----------'),
	tspl_actual_field(string,'startComparing', F0),
	tspl_formal_field(integer, F1),
	tspl_formal_field(string, F2),
	tspl_tuple([F0,F1,F2], T),
	tspl_wait_to_take(TS, T, Tuple),
	writeln('---------New Query-----------'),
	writeln('Command to Compare two Scenarios arrived...'),
	tspl_tuple_field(Tuple,1,Version),
	tspl_tuple_field(Tuple,2,GID),
	fetch_elos(TS, GID),
	fetch_lists(TS, GID),
	writeln('Similarity will be computed...'),
	findall(ScenId, (compare_list(GID, ScenId, _, _, _, _, _) ; compare_elo(GID, ScenId, _, _, _, _)), Scenarios),
	list_to_set(Scenarios, ScenIDs),
	forall(member(ScenId, ScenIDs), (simi_elos(GID, ScenId, TS), simi_global(GID, ScenId, Version,TS))),
	%hungarian(GID, Result),
	
	
	tspl_actual_field(string, 'comparingFinished', AT1),
	tspl_actual_field(string, GID, AT2),
	tspl_tuple([AT1,AT2],AT),
	tspl_write(TS,AT),
	writeln('Results has been written into CommandSpace...'),
	writeln('---------Finished Comparison-----------'),
		%tspl_tuple_id(Tuple, ID),
		retractall(sc(_,_)),
	fail.

fetch_relations(TS, Relation) :-
	tspl_formal_field(string, F0),
	tspl_formal_field(string, F1),
	tspl_actual_field(string, Relation, F2),
	tspl_formal_field(string, F3),
	tspl_tuple([F0, F1, F2, F3], SubclassTemplate),
	tspl_read_all(TS, SubclassTemplate, SCTuples),
	findall(sc(A, B), (member(SCTuple, SCTuples), tspl_tuple_field(SCTuple, 1, A), tspl_tuple_field(SCTuple, 3, B)), SCs),
	forall(member(SC, SCs), assert(SC)).
	
fetch_lists(TS, GID) :-
	tspl_actual_field(string, GID, F0),
	tspl_formal_field(string, F1),
	tspl_formal_field(string, F2),
	tspl_formal_field(integer, F3),
	tspl_formal_field(integer, F4),
	tspl_wildcard_field(F5),
	tspl_tuple([F0, F1, F2, F3, F4, F5], Template),
	tspl_take_all(TS, Template, ListTuples),
	findall(compare_list(GID, ScenId, I, J, Rel, List, UID), (member(LTuple, ListTuples), tspl_tuple_field(LTuple, 1, ScenId), tspl_tuple_field(LTuple, 2, Rel), tspl_tuple_field(LTuple, 3, I), tspl_tuple_field(LTuple, 4, J), fields_to_list(LTuple, List), uid(UID)), CLs),
	forall(member(CL, CLs), assert(CL)).
	
	
fetch_elos(TS, GID) :-
	tspl_actual_field(string, GID, F0),
	tspl_formal_field(string, F1),
	tspl_actual_field(string,'EloComparison', F2),
	tspl_formal_field(integer, F3),
	tspl_formal_field(integer, F4),
	tspl_wildcard_field(F5),
	tspl_tuple([F0, F1, F2, F3, F4, F5], Template),
	tspl_take_all(TS, Template, ListTuples),
	findall(compare_elo(GID, ScenId, I, J, List, UID), (member(LTuple, ListTuples), tspl_tuple_field(LTuple, 1, ScenId), tspl_tuple_field(LTuple, 3, I), tspl_tuple_field(LTuple, 4, J), LTuple = element(tuple, _, [_,_,_,_,_|RestFields]), fields_to_elos(RestFields, List), uid(UID)), ELOs),
	forall(member(ELO, ELOs), assert(ELO)).
	

fields_to_elos([], []).
fields_to_elos([A0,TF0,FR0,LR0|Rest], [elo(A1,TF1,FR1,LR1)|RestElos]) :-
	A0 = element(field, _, A),
	TF0 = element(field, _, TF),
	FR0 = element(field, _, FR),
	LR0 = element(field, _, LR),
	(A \= [] -> nth0(0, A, A1);true),
	(TF \= [] -> nth0(0, TF, TF1);true),
	(FR \= [] -> nth0(0, FR, FR1);true),
	(LR \= [] -> nth0(0, LR, LR1);true),
	fields_to_elos(Rest, RestElos).
	
fields_to_list(element(tuple, _, Fields), Fs) :-
	findall(F, member(element(field, _, [F]), Fields), [_,_,_,_,_|Fs]).
	
	
% setdiff(S1,S2,D)
%

setdiff([],_,[]).
setdiff([X|Xs],Ys,Rest) :-
  member(X,Ys), !,
  select(X, Ys, NYs),
  setdiff(Xs,NYs,Rest).
setdiff([X|Xs],Ys,[X|Rest]) :-
  setdiff(Xs,Ys,Rest).

calculateDifference(_,I,I,1,_).

calculateDifference(Version,I1,I2,RelatedDiff,Relation) :-
	I1 \== I2,
	related_diff(RelatedDiff ),
	relatedInstances(I1,I2,Version,Relation),
	!.
	
relatedInstances(I1,I2,Version,Relation):-
get_ontology_ts(Version,TS),
get_subclasses(TS,I1,SC1,Relation),
get_subclasses(TS,I2,SC2,Relation),
intersection(SC1,SC2,I),
I \== [],
!.

calculateDifference(_,_,_,0) :- !.

calculateMaxFromRow(Version,A,Bs,Max,Relation):-
	findall(V,(member(X,Bs),calculateDifference(Version,A,X,V,Relation)), Vs),
	max_list(Vs,Max).

% Elos1 = [elo(bla, adsf,sdfg,sdfg), elo(sdg,weer,shd,bxv)]
% Elos2 = [elo(bla, sdadsf,shdfg,sedfg), elo(sxdg,wer,sd,bv)]

	
simi_elos(GID, ScenId, _):-
	\+ compare_elo(GID, ScenId, _, _, _, _),
	!.
simi_elos(GID, ScenId, _) :-
	repeat,
	compare_elo(GID, ScenId, I, J, Elos1, UID1),
	compare_elo(GID, ScenId, I, J, Elos2, UID2),
	UID1 \== UID2,
	simi_elo(Elos1, Elos2, Delta),
	IntegerResult is Delta, 
	round(IntegerResult, RoundedIntegerDelta),
	assert(matrix(id(GID, ScenId, 'elos'),I,J,RoundedIntegerDelta)),
	retractall(compare_elo(GID,ScenId,I,J,_,_)),
	\+ compare_elo(GID, ScenId, _,_,_,_),
	!.

simi_elo([], [], 0).
simi_elo(Elos1, Elos2,ReturnDelta) :-
	uid(ID),
	findall(matrix(ID, X, Y, D), (nth0(X, Elos1, Elo1), nth0(Y, Elos2, Elo2), compare_two_elos(Elo1,Elo2,D)), Matrices),
	length(Matrices, ML),
	(ML == 0 ->
		ReturnDelta is 100;
		(
		
		length(Elos1,LengthElos1),
	    length(Elos2,LengthElos2),
	    (LengthElos1 > LengthElos2 ->
			   Divi is LengthElos1;
			     	(Divi is LengthElos2)
	     ),
	     %write('Anzahl der meisten ELOS: '), writeln(Divi),
		
			forall(member(M, Matrices), assert(M)),
			hungarian(ID, Delta),
			ReturnDelta is Delta / Divi,
			%write('Aehnlichkeit ist :'),writeln(Delta),
			
			retractall(matrix(ID, _, _, _))
		)
	),!.
		
	
compare_two_elos(elo(A1,TF1,FR1,LR1), elo(A2,TF2,FR2,LR2) , Result) :-
	(A1=A2 -> ADelta is 0; ADelta is 25),
	(TF1=TF2 -> TFDelta is 0; TFDelta is 25),
	(FR1=FR2 -> FRDelta is 0; FRDelta is 25),
	(LR1=LR2 -> LRDelta is 0; LRDelta is 25),
	Result is ADelta + TFDelta + FRDelta + LRDelta.
	
simi_global(GID, ScenId, Version, TS) :-
	findall(Rel, compare_list(GID, ScenId,_,_,Rel,_, _), RelList),
	list_to_set(RelList, Rels),
	get_ontology_ts(Version,OntTS),
	forall(member(Rel, Rels), fetch_relations(OntTS, Rel)),
	repeat,
	compare_list(GID, ScenId, I, J, Rel, List1, UID1),
	compare_list(GID, ScenId, I, J, Rel, List2, UID2),
	UID1 \== UID2,
	simi(List1, List2, Version, Delta, Rel),
	IntegerResult is Delta* 100, 
	round(IntegerResult, RoundedIntegerDelta),
	assert(matrix(id(GID,ScenId,Rel),I,J,RoundedIntegerDelta)),
	retractall(compare_list(GID,ScenId, I, J, Rel, _, _)),
	\+ compare_list(GID, ScenId, _,_,_,_,_),
	!,
	%print_all_mats,
	findall(
		solution(Rel1, Freq, Value),
		(
			member(Rel1, ['elos'|Rels]),
			get_matrix_dim1(id(GID, ScenId, Rel1), MaxX, MaxY),
			Freq is (MaxX+MaxY),
			%min_list([MaxX, MaxY], Freq),
			hungarian(id(GID, ScenId, Rel1), Value)
		),
		Solutions
	),
	findall(V, member(solution(_, V, _), Solutions), Values),
	sumlist(Values, ValuesSum),
	(
		ValuesSum == 0 -> 
			DeltaLass is 0;
			    Freq == 0 ->
				   DeltaLass is 1;
			(findall(V, (member(solution(_, Freq, Value), Solutions), V is Freq / ValuesSum * Value / 100), HungarianValues), sumlist(HungarianValues, DeltaLass))
	),
	get_matrix_dim1(id(GID, ScenId, _), MaxX, MaxY),
	LasCount is max(MaxX, MaxY),			
	(
		LasCount == 0 ->
			ResultDelta is DeltaLass;
			ResultDelta is DeltaLass / LasCount
	),		
	write('The Distance between the Scenarios is: '), writeln(ResultDelta),
	retractall(matrix(id(GID,ScenId,_), _, _, _)),
	tspl_actual_field(string, GID, AT1),
	tspl_actual_field(string, ScenId, AT2),
	tspl_actual_field(float, ResultDelta, AT3),
      tspl_tuple([AT1,AT2,AT3],DeltaTuple),
	tspl_write(TS,DeltaTuple).

simi([],[],_,0,_) :- !.
simi([],_,_,1,_) :- !.
simi(_,[],_,1,_) :- !.
simi(S1,S2,Version,Delta,Relation) :-
  length(S1,L1),
  length(S2,L2),
  Length is (L1+L2),
  findall(MaxS1, (member(S1M, S1), calculateMaxFromRow(Version,S1M,S2,MaxS1,Relation)), MaxS1s),
  sumlist(MaxS1s, S1Diff),
  findall(MaxS2, (member(S2M, S2), calculateMaxFromRow(Version,S2M,S1,MaxS2,Relation)), MaxS2s),
  sumlist(MaxS2s, S2Diff),
  (
	(Length == 0, Delta is 0) ;
	(Length \= 0, Delta is (L1 - S1Diff + L2 - S2Diff) / Length)
  ),
 %writeln('----------------------------------------------------------------------------------'),
  %write('Similarity of '), write(S1), write(' and '), write(S2), write(': '), write(Delta),nl,
   %writeln('----------------------------------------------------------------------------------'),
	!.

% Übergeben von Oberklasse an get_subclasses
get_subclasses(_, ID, Subclasses,_) :-
	%tspl_formal_field(string, F0),
	%tspl_actual_field(string, ID, F1),
	%tspl_actual_field(string, Relation, F2),
	%tspl_formal_field(string, F3),
	%tspl_tuple([F0, F1, F2, F3], SubclassTemplate),
	%tspl_read_all(TS, SubclassTemplate, SCTuples),
	%findall(Subclass, (member(SCTuple, SCTuples), tspl_tuple_field(SCTuple, 3, Subclass)), Subclasses).
	findall(SC, sc(ID, SC), Subclasses).
	
get_ontology_ts(SpaceId, TS) :-
	ts(SpaceId, TS),
	!.

get_ontology_ts(SpaceId, TS) :-
	con(TSC),
	ts_host(TsHost),
	ts_port(TsPort),
	tspl_get_all_spaces(TSC, Spaces),
	member(space(SpaceName, SpaceId), Spaces),
	tspl_connect_to_ts(SpaceName, TsHost, TsPort, TS),
	assert(ts(SpaceId, TS)),
	!.
  
  test:-
  assert(matrix('test',0,0,50)),
  assert(matrix('test',1,0,13)),
  assert(matrix('test',2,0,13)),
  assert(matrix('test',3,0,13)),
  assert(matrix('test',4,0,13)),
  assert(matrix('test',5,0,13)),
  assert(matrix('test',6,0,13)),
  hungarian('test',R),
  writeln(R).
 
  
  test(X) :-
	simi(['http://www.scy.eu/scyontology#scy_experimental_design_tool', 'http://www.scy.eu/scyontology#scy_data_visualization_tool'], ['http://www.scy.eu/scyontology#scy_experimental_design_tool', 'http://www.scy.eu/scyontology#simquestviewer'], 5, X).
 
  
 
