%====================================================================================
% client description   
%====================================================================================
context(ctxclient, "localhost",  "TCP", "8069").
context(ctxparkmanager, "127.0.0.1",  "TCP", "8070").
 qactor( parkmanagerservice, ctxparkmanager, "external").
  qactor( client, ctxclient, "it.unibo.client.Client").
msglogging.
