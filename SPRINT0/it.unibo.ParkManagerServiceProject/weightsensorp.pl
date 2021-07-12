%====================================================================================
% weightsensorp description   
%====================================================================================
context(ctxparkmanager, "localhost",  "TCP", "8070").
context(ctxweightsensor, "192.168.1.68",  "TCP", "8071").
 qactor( weightsensor, ctxweightsensor, "it.unibo.weightsensor.Weightsensor").
msglogging.
