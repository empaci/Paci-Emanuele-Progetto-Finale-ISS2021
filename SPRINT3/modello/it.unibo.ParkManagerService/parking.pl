%dynamic(freeSlot/1).
%dynamic(occupied/3).

home(0,0).
indoor(6,0).
outdoor(6,4).

% getCoordinates(SLOTNUM,Xpos,Ypos)
getCoordinates(1,1,1).
getCoordinates(2,3,0).
getCoordinates(3,1,2).
getCoordinates(5,4,2).
getCoordinates(4,1,3).
getCoordinates(6,3,4).

timemax(1000).

true(true).
false(false).

% verifica che il parcheggio è libero, oppure se non completamente occupato
isFree(SLOTNUM,X) :- freeSlot(SLOTNUM), !, true(X).
isFree(SLOTNUM,X) :- occupied(0,_,SLOTNUM), !, true(X).
isFree(_,X) :- false(X).

init(X) :- unoccupySlot(1), unoccupySlot(2), unoccupySlot(3), unoccupySlot(4), unoccupySlot(5), unoccupySlot(6), X is 1.

%ritorna il freeSlot con numero minore
getFreeSlot(X) :- findall(Y,freeSlot(Y),Xs), list_min(Xs,X), !.
getFreeSlot(0). %solo se non si sono freeSlot liberi unfica con lo 0

occupySlot(X,TOKEN,STARTIME) :- retract(freeSlot(X)), assert(occupied(TOKEN,STARTIME,X)).
unoccupySlot(X) :- X>0, X<7, assert(freeSlot(X)).
pickup(TOKEN,X) :- retract(occupied(TOKEN,_,X)).

assignToken(TOKEN,X) :- retract(occupied(0,STARTIME,X)), assert(occupied(TOKEN,STARTIME,X)).

timedout(CURRENTIME) :- findall(X,occupied(0,_,X),LIST), timedout(CURRENTIME,LIST).
timedout(CURRENTIME,[H|T]) :- occupied(0,TIME,H), timemax(TMAX),  CURRENTIME > TIME + TMAX, pickup(0,TIME,H), unoccupySlot(H), timedout(CURRENTIME,T).
timedout(CURRENTIME, [_|T]) :- timedout(CURRENTIME,T).
timedout(_,[]).

% Trova il minimo in una lista
list_min([T|C], Min) :- list_min(C, T, Min).

list_min([], Min, Min).
list_min([T|C], Min0, Min) :- T<Min0,!, Min1 is T, list_min(C, Min1, Min).
list_min([_|C],Min0,Min) :- list_min(C,Min0,Min).
