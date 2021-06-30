%====================================================================================
% model description   
%====================================================================================
context(ctxparkmanager, "localhost",  "TCP", "8070").
 qactor( client, ctxparkmanager, "it.unibo.client.Client").
  qactor( park_manager_service, ctxparkmanager, "it.unibo.park_manager_service.Park_manager_service").
  qactor( weight_sensor, ctxparkmanager, "it.unibo.weight_sensor.Weight_sensor").
  qactor( transport_trolley, ctxparkmanager, "it.unibo.transport_trolley.Transport_trolley").
msglogging.
