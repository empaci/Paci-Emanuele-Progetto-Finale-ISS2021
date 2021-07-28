%====================================================================================
% model description   
%====================================================================================
context(ctxsonarsensor, "localhost",  "TCP", "8071").
 qactor( sonarsensor, ctxsonarsensor, "it.unibo.sonarsensor.Sonarsensor").
  qactor( sonarlogic, ctxsonarsensor, "it.unibo.sonarlogic.Sonarlogic").
msglogging.
