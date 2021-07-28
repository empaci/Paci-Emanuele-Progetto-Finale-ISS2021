
% prevWeight(0).
delta(10).
statusFree(free).
statusOccupied(occupied).

true(true).
false(false).

init(X) :- assert(prevWeight(0)), statusFree(X).
reassign(X) :- retract(prevWeight(_)), assert(prevWeight(X)).

% X is true is the state needs to be updated
modifyStatus(WEIGHT,X) :-  delta(DELTA), prevWeight(PW), V is PW-WEIGHT, abs(V,RES), RES > DELTA, reassign(WEIGHT), true(X).
modifyStatus(_,X) :- false(X).

% return the current status
getStatus(WEIGHT,X) :- delta(DELTA), WEIGHT < DELTA, !, statusFree(X).
getStatus(_,X) :- statusOccupied(X).
