%====================================================================================
% client description   
%====================================================================================
context(ctxclient, "192.168.1.68",  "TCP", "8069").
context(ctxparkmanager, "192.168.1.78",  "TCP", "8070").
 qactor( parkmanagerservice, ctxparkmanager, "external").
  qactor( client, ctxclient, "it.unibo.client.Client").
msglogging.
