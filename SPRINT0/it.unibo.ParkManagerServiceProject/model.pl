%====================================================================================
% model description   
%====================================================================================
context(ctxclient, "192.168.1.68",  "TCP", "8069").
context(ctxparkmanager, "localhost",  "TCP", "8070").
context(ctxbasicrobot, "192.168.1.68",  "TCP", "8020").
context(ctxweightsensor, "192.168.1.68",  "TCP", "8071").
 qactor( basicrobot, ctxbasicrobot, "external").
  qactor( client, ctxclient, "it.unibo.client.Client").
  qactor( parkmanagerservice, ctxparkmanager, "it.unibo.parkmanagerservice.Parkmanagerservice").
  qactor( weightsensor, ctxweightsensor, "it.unibo.weightsensor.Weightsensor").
  qactor( transporttrolley, ctxparkmanager, "it.unibo.transporttrolley.Transporttrolley").
msglogging.
