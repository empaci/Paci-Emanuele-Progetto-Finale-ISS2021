%====================================================================================
% carparking description   
%====================================================================================
context(ctxcarparking, "localhost",  "TCP", "8002").
 qactor( client, ctxcarparking, "it.unibo.client.Client").
  qactor( park_manager_service, ctxcarparking, "it.unibo.park_manager_service.Park_manager_service").
