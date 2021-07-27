
% prevWeight(0).
delta(10).
statusFree(free).
statusOccupied(occupied).

true(true).
false(false).

init() :- assert(prevWeight(0)).
reassign(X) :- retract(prevWeight(_)), assert(prevWeight(X)).


modifyStatus(WEIGHT,X) :-  delta(DELTA), prevWeight(PW), V is PW-WEIGHT, abs(V,RES),!, RES > DELTA,
                   reassign(WEIGHT), true(X).
modiftStatus(_,X) :- false(X).

getStatus(WEIGHT,X) :- delta(DELTA), WEIGHT < DELTA, !, statusFree(X).
getStatus(_,X) :- statusOccupied(X).
