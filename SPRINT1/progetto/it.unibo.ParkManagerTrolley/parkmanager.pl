%====================================================================================
% parkmanager description   
%====================================================================================
context(ctxclient, "192.168.1.68",  "TCP", "8069").
context(ctxparkmanager, "localhost",  "TCP", "8070").
context(ctxbasicrobot, "127.0.0.1",  "TCP", "8020").
 qactor( basicrobot, ctxbasicrobot, "external").
  qactor( parkmanagerservice, ctxparkmanager, "it.unibo.parkmanagerservice.Parkmanagerservice").
  qactor( transporttrolley, ctxparkmanager, "it.unibo.transporttrolley.Transporttrolley").
msglogging.
