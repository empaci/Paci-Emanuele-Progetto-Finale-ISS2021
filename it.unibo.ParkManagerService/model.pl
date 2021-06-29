%====================================================================================
% model description   
%====================================================================================
context(ctxparkmanager, "localhost",  "TCP", "8070").
 qactor( client, ctxparkmanager, "it.unibo.client.Client").
  qactor( park_manager_service, ctxparkmanager, "it.unibo.park_manager_service.Park_manager_service").
msglogging.
