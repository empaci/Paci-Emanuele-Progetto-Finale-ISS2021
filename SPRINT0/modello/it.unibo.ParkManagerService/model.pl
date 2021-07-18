%====================================================================================
% model description   
%====================================================================================
context(ctxparkmanager, "localhost",  "TCP", "8070").
context(ctxbasicrobot, "192.168.1.68",  "TCP", "8020").
 qactor( basicrobot, ctxbasicrobot, "external").
  qactor( client, ctxparkmanager, "it.unibo.client.Client").
  qactor( parkmanagerservice, ctxparkmanager, "it.unibo.parkmanagerservice.Parkmanagerservice").
  qactor( weightsensor, ctxparkmanager, "it.unibo.weightsensor.Weightsensor").
  qactor( transporttrolley, ctxparkmanager, "it.unibo.transporttrolley.Transporttrolley").
msglogging.
