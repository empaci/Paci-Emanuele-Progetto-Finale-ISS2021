%dynamic(freeSlot/1).

home(0,0).
indoor(6,0).
outdoor(4,6).

% getCoordinates(SLOTNUM,Xpos,Ypos)
getCoordinates(1,1,1).

getFreeSlot(X) :- freeSlot(X), !.
getFreeSlot(0). %solo se non si sono freeSlot liberi unfica con lo 0

occupySlot(X) :- retract(freeSlot(X)).
unoccupySlot(X) :- X>0, X<7, assert(freeSlot(X)).
