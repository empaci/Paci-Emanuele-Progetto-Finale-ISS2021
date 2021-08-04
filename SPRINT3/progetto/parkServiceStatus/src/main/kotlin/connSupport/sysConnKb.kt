package connSupport

import connSupport.ConnectionType

val mqtthostAddr    = "localhost"	//broker.hivemq.com
val mqttport		= "1883"
val mqtttopic       = "unibo/parkmanagerservice"
var robothostAddr   = "localhost" //   172.17.0.2 "192.168.1.5" "localhost"
val robotPort     	= "8070"
val qakdestination 	= "parkmanagerservice"
val ctxqakdest      = "ctxparkmanager"
val connprotocol    = ConnectionType.TCP //TCP COAP HTTP MQTT

//fun main(){ println("consoles") }