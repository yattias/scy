:- dynamic(matrix/4).
:- dynamic(marked/2).

%matrix(a, 0, 0, 5).
%matrix(a, 0, 1, 3).
%matrix(a, 0, 2, 7).
%matrix(a, 1, 0, 1).
%matrix(a, 1, 1, 0).
%matrix(a, 1, 2, 2).
%matrix(a, 2, 0, 0).
%matrix(a, 2, 1, 2).
%matrix(a, 2, 2, 4).
 
quadriere_matrix(Mat) :-
  get_matrix_dim0(Mat, MaxX, MaxY),
  (MaxX > MaxY -> 
    (
      numlist(MaxY, MaxX, [_|YList]),
      numlist(0, MaxX, XList),
      forall((member(Y, YList), member(X, XList)), assert(matrix(Mat, X, Y, 100)))
    );
    true
  ),
  (MaxY > MaxX -> 
    (
      numlist(MaxX, MaxY, [_|XList]),
      numlist(0, MaxY, YList),
      forall((member(Y, YList), member(X, XList)), assert(matrix(Mat, X, Y, 100)))
    );
    true
  ).
    

kostentranformiere(Mat) :-
  reinige_matrix(Mat),
  findall(V, (matrix(Mat, X, Y, V), marked(row, X), \+ marked(col, Y)), Vs),
  sort(Vs, [MinValue|_]),
  forall(
    (
      matrix(Mat, X, Y, V), marked(row, X), \+ marked(col, Y)
    ),
    (
	  %write(X), write(' '), write(Y),write(' '), write(V),
      retract(matrix(Mat, X, Y, V)),
      NewV is V - MinValue,
      assert(matrix(Mat, X, Y, NewV))
	  %writeln(' done!')
    )
  ),
  forall(
    (
      matrix(Mat, X, Y, V), \+ marked(row, X), marked(col, Y)
    ),
    (
      retract(matrix(Mat, X, Y, V)),
      NewV is V + MinValue,
      assert(matrix(Mat, X, Y, NewV))
    )
  ),
  retractall(marked(_, _)).

  
reinige_matrix(Mat) :-
  forall(
    (
      matrix(Mat, X, Y, V),
      member(V, ['*', 'x'])
    ),
    (
      retract(matrix(Mat, X, Y, V)),
      assert(matrix(Mat, X, Y, 0))
    )
  ).
  
hungarian(Mat, L) :-
  uid_hungarian(ID),
  quadriere_matrix(Mat),
  reduzier_matrix(Mat, ID),
  %anzahl_debug(ID),
  suche_loesung(ID),
  loesung(Mat, ID, L),
  retractall(matrix(ID, _, _, _)),!.
  
loesung(Orig, Red, L) :-
  findall(V, (matrix(Red, X, Y, '*'), matrix(Orig, X, Y, V)), Vs),
  sumlist(Vs, L).

suche_loesung(Mat) :- 
  finde_unabhaengige_nullen(Mat),
  loesung_gefunden(Mat).

suche_loesung(Mat) :- 
  finde_ueberdeckung(Mat),
  kostentranformiere(Mat),
 % print_mat(Mat),
  suche_loesung(Mat).
  
finde_ueberdeckung(Mat) :-
  findall(X, (matrix(Mat, X, _, _), \+ matrix(Mat, X, _, '*')), XsL),
  list_to_set(XsL, Xs),
  forall(member(X, Xs), assert(marked(row, X))),
  repeat,
  markiere(Mat, Marks),
  Marks == 0.
    
markiere(Mat, Marks) :-
  findall(Y, (matrix(Mat, X, Y, x), \+ marked(col, Y), marked(row, X)), YsL),
  list_to_set(YsL, Ys),
  forall(member(Y, Ys), assert(marked(col, Y))),
  findall(X, (matrix(Mat, X, Y, '*'), \+ marked(row, X), marked(col, Y)), XsL),
  list_to_set(XsL, Xs),
  forall(member(X, Xs), assert(marked(row, X))),
  length(Ys, YL),
  length(Xs, XL),
%  print_mat(Mat),
  Marks is YL + XL.
  
loesung_gefunden(Mat) :-
  findall(X, matrix(Mat, X, 0, _), Xs),
  length(Xs, Dim),
  findall(X, matrix(Mat, X, _, '*'), Stars),
  length(Stars, StarLength),
%  write(StarLength), write(' Loesungen gefunden, aber '), write(Dim), write(' gesucht!'),nl,
  Dim == StarLength.

finde_unabhaengige_nullen(Mat) :-
  repeat,
  findall(
    [X,Y], 
    (
        unabhaengige_zeile(Mat, X, Y);
        unabhaengige_spalte(Mat, X, Y)
    ),
    Nullen
  ),
  Nullen == [],
  !. 
 
 
minimale_nullen(Mat, MinNull) :-
  findall(
      LY0,
      (matrix(Mat, X, Y, 0), findall(Y0, (member(V, ['*', 0]), matrix(Mat, X, Y0, V)), Y0s), length(Y0s, LY0)),
      LY0s
  ),
  findall(
      LX0,
      (matrix(Mat, X, Y, 0), findall(X0, (member(V, ['*', 0]), matrix(Mat, X0, Y, V)), X0s), length(X0s, LX0)),
      LX0s
  ),
  append(LY0s, LX0s, L0s),
  sort(L0s, [MinNull|_]),
  !.
minimale_nullen(_, -1).
 
unabhaengige_zeile(Mat, X, Y) :-
 % print_mat(Mat),
  minimale_nullen(Mat, MinNull),
  matrix(Mat, X, Y, 0), 
  findall(Y0, (member(V, ['*', 0]), matrix(Mat, X, Y0, V)), Ys),
  length(Ys, MinNull),
  retract(matrix(Mat, X, Y, 0)),
  assert(matrix(Mat, X, Y, '*')),
  streiche_nullen(Mat, X, Y),
%  nl,
%  write('deleted: '), write(X), write('/'), write(Y), nl,
%  print_mat(Mat),
%  nl,
  !.
  
  
streiche_nullen(Mat, X, Y) :-
  forall(
    (matrix(Mat, X0, Y, 0), X0 \== X), 
    (
      retract(matrix(Mat, X0, Y, 0)),
      assert(matrix(Mat, X0, Y, x))
    )
  ),
  forall(
    (matrix(Mat, X, Y0, 0), Y0 \== Y), 
    (
      retract(matrix(Mat, X, Y0, 0)),
      assert(matrix(Mat, X, Y0, x))
    )
  ).
  
unabhaengige_spalte(Mat, X, Y) :-
 % print_mat(Mat),
  minimale_nullen(Mat, MinNull),
  matrix(Mat, X, Y, 0), 
  findall(X0, (member(V, ['*', 0]), matrix(Mat, X0, Y, V)), Xs),
  length(Xs, MinNull),
  retract(matrix(Mat, X, Y, 0)),
  assert(matrix(Mat, X, Y, '*')),
  streiche_nullen(Mat, X, Y),
%  nl,
%  write('deleted: '), write(X), write('/'), write(Y), nl,
%  print_mat(Mat),
%  nl,
  !.

reduzier_matrix(A, B) :-
  get_matrix_dim0(A, MaxX, MaxY),
  uid_hungarian(X),
  reduzier_zeile(A, X, MaxX),
  reduzier_spalte(X, B, MaxY),
  retractall(matrix(X, _, _, _)).
  
get_matrix_dim0(Mat, MaxX, MaxY) :-
  findall(X, matrix(Mat, X, _, _), Xs),
  max_list(Xs, MaxX),
  findall(Y, matrix(Mat, _, Y, _), Ys),
  max_list(Ys, MaxY).

get_matrix_dim1(Mat, MaxX, MaxY) :-
  (
	\+ matrix(Mat, 0, _, _) ->
		 MaxX is 0;
		(findall(X, matrix(Mat, X, _, _), Xs), max_list(Xs, MaxX0), MaxX is MaxX0 + 1)
  ),
  (
    \+  matrix(Mat, _, 0, _) ->
		MaxY is 0;
		(findall(Y, matrix(Mat, _, Y, _), Ys), max_list(Ys, MaxY0), MaxY is MaxY0 + 1)
  ).
  
reduzier_spalte(_,_,-1).
reduzier_spalte(A, B, Y) :-
  findall(V, matrix(A, _, Y, V), Vs),
  min_list(Vs, MV),
  forall(
    matrix(A, X, Y, V), 
    (
	NewV is V - MV,
	assert(matrix(B, X, Y, NewV))
    )
  ),
  NextY is Y - 1,
  reduzier_spalte(A, B, NextY).
  
  
reduzier_zeile(_,_,-1).  
reduzier_zeile(A, B, X) :-
  findall(V, matrix(A, X, _, V), Vs),
  min_list(Vs, MV),
  forall(
    matrix(A, X, Y, V), 
    (
	NewV is V - MV,
	assert(matrix(B, X, Y, NewV))
    )
  ),
  NextX is X - 1,
  reduzier_zeile(A, B, NextX).
  
print_mat(Mat) :-
  get_matrix_dim0(Mat, MaxX, MaxY),
  numlist(0, MaxX, Xs),
  numlist(0, MaxY, Ys),
  write('Matrix: '), writeln(Mat),
  findall(V, (member(X, Xs), nl, member(Y, Ys), matrix(Mat, X, Y, V), print(V), print(' ')), _),
  findall(X, marked(row, X), Rows),
  findall(X, marked(col, X), Cols),
  nl,
  write('Markierte Zeilen : '), writeln(Rows),
  write('Markierte Spalten: '), writeln(Cols).
  
print_all_mats :-
	findall(Mat, matrix(Mat, _, _, _), Mats),
	list_to_set(Mats, MatsSet), 
	forall(member(Mat, MatsSet), print_mat(Mat)).

uid_hungarian(X) :-
        get_time(T1),
        random(T2),
        T3 is round(T1 * T2),
        T is T3**2,
        format(atom(X), 'p~16r', [T]).
		
%banzahl_debug(Name) :-
%	findall(A, matrix(Name, _, _, _), As),
%	length(As, ALength),
%	write('Länge der Matrix '),write(Name),write(' : '), writeln(ALength).
	
