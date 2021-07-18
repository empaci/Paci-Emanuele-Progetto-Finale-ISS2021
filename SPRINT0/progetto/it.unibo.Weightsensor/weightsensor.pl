%====================================================================================
% weightsensor description   
%====================================================================================
context(ctxparkmanager, "192.168.1.78",  "TCP", "8070").
context(ctxweightsensor, "192.168.1.68",  "TCP", "8071").
 qactor( weightsensor, ctxweightsensor, "it.unibo.weightsensor.Weightsensor").
msglogging.
