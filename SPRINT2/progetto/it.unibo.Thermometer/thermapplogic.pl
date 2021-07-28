
% criticTemp(false).
tmax(35).
initTemp(0).

above(above).
below(below).
nothing(nothing).

init(X) :- assert(criticTemp(false)), initTemp(X).
reassign(X) :- retract(criticTemp(_)), assert(criticTemp(X)).


notice(TEMP,X) :- criticTemp(CT), CT == false, tmax(TMAX), TEMP>=TMAX,!, reassign(true), above(X).
notice(TEMP,X) :- criticTemp(CT), CT == true, tmax(TMAX), TEMP<TMAX,!, reassign(false), below(X).
notice(_,X) :- nothing(X).
