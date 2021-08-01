%====================================================================================
% thermometer description   
%====================================================================================
context(ctxthermometer, "127.0.0.1",  "TCP", "8073").
context(ctxfan, "localhost",  "TCP", "8074").
 qactor( thermometer, ctxthermometer, "it.unibo.thermometer.Thermometer").
  qactor( thermometerlogic, ctxthermometer, "it.unibo.thermometerlogic.Thermometerlogic").
msglogging.
