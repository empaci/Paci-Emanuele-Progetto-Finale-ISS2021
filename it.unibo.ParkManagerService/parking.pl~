%dynamic(freeSlot/1).

getFreeSlot(X) :- freeSlot(X), !.
getFreeSlot(0). %solo se non si sono freeSlot liberi unfica con lo 0

occupySlot(X) :- retract(freeSlot(X)).
unoccupySlot(X) :- X>0, X<7, assert(freeSlot(X)).
