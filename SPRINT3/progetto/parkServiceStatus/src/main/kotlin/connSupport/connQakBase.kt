package connSupport
import it.unibo.kactor.ApplMessage
 
enum class ConnectionType {
    TCP, COAP, HTTP
}

abstract class connQakBase() {
lateinit var currQakConn  : connQakBase
	
	companion object{
	fun create(connType: ConnectionType) : connQakBase {
		  showSystemInfo()
		  when( connType ){
				 ConnectionType.TCP ->   {return connQakTcp( ) }
				 ConnectionType.COAP ->  {return connQakCoap( )
				 }
  				 ConnectionType.HTTP ->  {return connQakHttp( )
				 }
			  	//ConnectionType.MQTT ->  {return connQakMqtt( ) }
  				 //else -> return null
 		  }		
	}
	fun showSystemInfo(){
		println(
			"connQakBase  | COMPUTER memory="+ Runtime.getRuntime().totalMemory() +
					" num of processors=" +  Runtime.getRuntime().availableProcessors());
		println(
			"connQakBase  | NUM of threads="+ Thread.activeCount() +
					" currentThread=" + Thread.currentThread() );
	}
	}//object

	
	  abstract fun createConnection(   )
      abstract fun forward( msg : ApplMessage )
      abstract fun request(msg: ApplMessage) : String
      abstract fun emit( msg : ApplMessage )
	
}

 
 
 