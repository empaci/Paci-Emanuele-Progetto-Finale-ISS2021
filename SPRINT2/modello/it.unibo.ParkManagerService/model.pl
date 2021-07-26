%====================================================================================
% model description   
%====================================================================================
context(ctxparkmanager, "localhost",  "TCP", "8070").
context(ctxbasicrobot, "127.0.0.1",  "TCP", "8020").
 qactor( basicrobot, ctxbasicrobot, "external").
  qactor( client, ctxparkmanager, "it.unibo.client.Client").
  qactor( parkmanagerservice, ctxparkmanager, "it.unibo.parkmanagerservice.Parkmanagerservice").
  qactor( weightsensor, ctxparkmanager, "it.unibo.weightsensor.Weightsensor").
  qactor( transporttrolley, ctxparkmanager, "it.unibo.transporttrolley.Transporttrolley").
  qactor( sonarsensor, ctxparkmanager, "it.unibo.sonarsensor.Sonarsensor").
  qactor( thermometer, ctxparkmanager, "it.unibo.thermometer.Thermometer").
  qactor( fan, ctxparkmanager, "it.unibo.fan.Fan").
  qactor( parkingstate, ctxparkmanager, "it.unibo.parkingstate.Parkingstate").
  qactor( parkingservicestatusgui, ctxparkmanager, "it.unibo.parkingservicestatusgui.Parkingservicestatusgui").
msglogging.
