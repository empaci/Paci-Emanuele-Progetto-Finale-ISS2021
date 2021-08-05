package test.kotlin

import org.junit.Assert.*
import java.net.UnknownHostException
import org.junit.BeforeClass
import cli.System.IO.IOException
import org.junit.Test
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.delay
import kotlinx.coroutines.channels.Channel
import it.unibo.kactor.QakContext
import org.junit.Before
import it.unibo.kactor.ActorBasic
import it.unibo.kactor.MsgUtil
import org.junit.AfterClass
import it.unibo.kactor.sysUtil
import it.unibo.kactor.ApplMessage
import org.junit.After
import test.kotlin.CoapObserverForTesting
 
 
class TestPlanEnd {
		
	companion object{
		var systemStarted         = false
		val channelSyncStart      = Channel<String>()
		var testingObserver1       : CoapObserverForTesting? = null
		var myactor               : ActorBasic? = null

		@JvmStatic
        @BeforeClass
		//@Target([AnnotationTarget.FUNCTION]) annotation class BeforeClass
		//@Throws(InterruptedException::class, UnknownHostException::class, IOException::class)
		fun init() {
			GlobalScope.launch{
				it.unibo.ctxparkmanager.main() //keep the control
			}
			GlobalScope.launch{
				myactor=QakContext.getActor("parkmanagerservice")
 				while(  myactor == null )		{
					println("+++++++++ waiting for system startup ...")
					delay(500)
					myactor=QakContext.getActor("parkmanagerservice")
				}				
				delay(1000)	//Give time to set up
				channelSyncStart.send("starttesting")
				testingObserver1 = CoapObserverForTesting()
			}		 
		}//init
		
		@JvmStatic
	    @AfterClass
		fun terminate() {
			println("terminate the testing")
		}
		
	}//companion object
	
	@Before
	fun checkSystemStarted()  {
	    println("+++++++++ BEFOREEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE ")
		if( ! systemStarted ) {
			runBlocking{
				channelSyncStart.receive()
				systemStarted = true
			    println("+++++++++ checkSystemStarted resumed ")
			}			
		}
		runBlocking{ delay(300) } //Put some interval between tests
  	}
	
	@After
	fun removeObs(){
		println("+++++++++ AFTERRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR")
		testingObserver1!!.removeObserver()
	}
	
    @Test
    fun testUC1(){
		var result  = ""
		runBlocking{
 			val channelForObserverT = Channel<String>()
	
			testingObserver1!!.addObserver( channelForObserverT,"park")
			
			val reqin = MsgUtil.buildRequest("tester","clientRequest","clientRequest(in)","parkmanagerservice")
			MsgUtil.sendMsg(reqin, myactor!!)
			result = channelForObserverT.receive()
			assertEquals( result, "park(1)")
			
			
			val reqen = MsgUtil.buildRequest("tester","carenter","carenter(1)","parkmanagerservice")
			MsgUtil.sendMsg(reqen, myactor!!)
			
			
			result = channelForObserverT.receive()
			assertEquals( result, "park(10)")
			
			
			val reqin2 = MsgUtil.buildRequest("tester","clientRequest","clientRequest(in)","parkmanagerservice")
			MsgUtil.sendMsg(reqin2, myactor!!)
			
			result = channelForObserverT.receive()
			assertEquals( result, "park(2)")
			
			delay(40000)
			
			val reqin3 = MsgUtil.buildRequest("tester","clientRequest","clientRequest(in)","parkmanagerservice")
			MsgUtil.sendMsg(reqin3, myactor!!)
			
			result = channelForObserverT.receive()
			assertEquals( result, "park(2)") //senza delay dovrebbe essere 3
			
			val reqen2 = MsgUtil.buildRequest("tester","carenter","carenter(2)","parkmanagerservice")
			MsgUtil.sendMsg(reqen2, myactor!!)
			
			result = channelForObserverT.receive()
			assertEquals( result, "park(21)")
			
			
			//testingObserver1!!.removeObserver()
			
		}
	}
}

