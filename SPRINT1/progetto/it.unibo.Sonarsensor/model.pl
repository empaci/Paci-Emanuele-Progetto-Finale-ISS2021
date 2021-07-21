%====================================================================================
% model description   
%====================================================================================
context(ctxparkmanager, "192.168.1.68",  "TCP", "8070").
context(ctxsonarsensor, "127.0.0.1",  "TCP", "8072").
 qactor( sonarsensor, ctxparkmanager, "it.unibo.sonarsensor.Sonarsensor").
msglogging.
