%====================================================================================
% weightsensor description   
%====================================================================================
context(ctxparkmanager, "localhost",  "TCP", "8070").
context(ctxweightsensor, "127.0.0.1",  "TCP", "8071").
 qactor( weightsensor, ctxweightsensor, "it.unibo.weightsensor.Weightsensor").
msglogging.
