package connSupport

import com.andreapivetta.kolor.Color
import it.unibo.supports.FactoryProtocol
import it.unibo.`is`.interfaces.protocols.IConnInteraction
import it.unibo.actor0.sysUtil
import it.unibo.kactor.ApplMessage

class connQakTcp(  ) : connQakBase( ){
	lateinit var conn   : IConnInteraction
	
	override fun createConnection(   ){ //hostIP: String, port: String
		val fp    = FactoryProtocol(null,"TCP","connQakTcp")
	    conn      = fp.createClientProtocolSupport(robothostAddr, robotPort.toInt() )
		sysUtil.colorPrint("connQakTcp | connected with $robothostAddr", Color.GREEN)
	}
	
	override fun forward( msg: ApplMessage ){
		println("connQakTcp | forward= $msg")
		conn.sendALine( msg.toString()  )
	}
	
	override fun request(msg: ApplMessage) : String {
		println("connQakTcp | request= $msg")
 		conn.sendALine( msg.toString()  )
		//Acquire the answer	
		val answer = conn.receiveALine()
		println("connQakTcp | answer= $answer")
		return answer
	}
	
	override fun emit( msg: ApplMessage ){
 		conn.sendALine( msg.toString()  )			
	}	
}