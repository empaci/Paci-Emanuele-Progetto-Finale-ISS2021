%====================================================================================
% model description   
%====================================================================================
context(ctxweightsensor, "127.0.0.1",  "TCP", "8071").
 qactor( weightsensor, ctxweightsensor, "it.unibo.weightsensor.Weightsensor").
  qactor( weightlogic, ctxweightsensor, "it.unibo.weightlogic.Weightlogic").
msglogging.
