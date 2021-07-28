% prevDist(0).
delta(5).
dtfree(5000).
statusFree(free).
statusOccupied(occupied).

true(true).
false(false).

init(X) :- assert(prevDist(0)), statusFree(X).
reassign(X) :- retract(prevDist(_)), assert(prevDist(X)).

% X is true is the state needs to be updated
modifyStatus(DISTANCE,X) :-  delta(DELTA), prevDist(PD), V is PD-DISTANCE, abs(V,RES), RES > DELTA,!, reassign(DISTANCE), true(X).
modifyStatus(_,X) :- false(X).

% return the current status
getStatus(DISTANCE,X) :- DISTANCE == 0, !, statusFree(X).
getStatus(_,X) :- statusOccupied(X).
