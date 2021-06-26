%====================================================================================
% model description   
%====================================================================================
context(ctxparkmanagerservice, "localhost",  "TCP", "8070").
 qactor( park_manager_service, ctxparkmanagerservice, "it.unibo.park_manager_service.Park_manager_service").
  qactor( client, ctxparkmanagerservice, "it.unibo.client.Client").
  qactor( weight_sensor, ctxparkmanagerservice, "it.unibo.weight_sensor.Weight_sensor").
  qactor( weight_mock, ctxparkmanagerservice, "it.unibo.weight_mock.Weight_mock").
  qactor( transport_trolley, ctxparkmanagerservice, "it.unibo.transport_trolley.Transport_trolley").
msglogging.
