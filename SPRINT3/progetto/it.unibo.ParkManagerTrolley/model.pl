%====================================================================================
% model description   
%====================================================================================
context(ctxparkmanager, "localhost",  "TCP", "8070").
context(ctxbasicrobot, "127.0.0.1",  "TCP", "8020").
 qactor( basicrobot, ctxbasicrobot, "external").
  qactor( parkmanagerservice, ctxparkmanager, "it.unibo.parkmanagerservice.Parkmanagerservice").
  qactor( transporttrolley, ctxparkmanager, "it.unibo.transporttrolley.Transporttrolley").
  qactor( trolleylogic, ctxparkmanager, "it.unibo.trolleylogic.Trolleylogic").
  qactor( trolleyintermediary, ctxparkmanager, "it.unibo.trolleyintermediary.Trolleyintermediary").
  qactor( parkingstate, ctxparkmanager, "it.unibo.parkingstate.Parkingstate").
msglogging.
