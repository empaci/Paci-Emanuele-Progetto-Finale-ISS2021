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
		var testingObserver2       : CoapObserverForTesting2? = null
		var myactor               : ActorBasic? = null
		val nfree				  = 2

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
				testingObserver2 = CoapObserverForTesting2()
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
 			val channelForObserverW = Channel<String>()
			
			testingObserver1!!.addObserver( channelForObserverT,"park")
			testingObserver2!!.addObserver( channelForObserverW,"weight")
			
			val weightactor : ActorBasic? = QakContext.getActor("weightsensor")
			
			result = channelForObserverW.receive()
			assertEquals( result, "free")
				
			
			//inizializzazione: occupa i primi nfree parcheggi
			for (i in 1..nfree) {
				
				val reqin = MsgUtil.buildRequest("tester","clientRequest","clientRequest(in)","parkmanagerservice")
				MsgUtil.sendMsg(reqin, myactor!!)
				
				result = channelForObserverT.receive()
				assertEquals( result, "park($i)")
				
				
				var wdata = MsgUtil.buildDispatch("tester","weightData","weightData(100)","weightsensor")
				MsgUtil.sendMsg(wdata, weightactor!!)
				
				result = channelForObserverW.receive()
				assertEquals( result, "occupied")
				
				delay(1000)
				
				val reqen = MsgUtil.buildRequest("tester","carenter","carenter($i)","parkmanagerservice")
				MsgUtil.sendMsg(reqen, myactor!!)
				
				val j  = i - 1 
				
				result = channelForObserverT.receive()
				assertEquals( result, "park($i$j)")
				
				delay(1000)
				
				wdata = MsgUtil.buildDispatch("tester","weightData","weightData(0)","weightsensor")
				MsgUtil.sendMsg(wdata, weightactor!!)
				
				result = channelForObserverW.receive()
				assertEquals( result, "free")
				
				delay(1000)
			}
			
			delay(10000) //wait for the robot
			
			val reqin3 = MsgUtil.buildRequest("tester","clientRequest","clientRequest(in)","parkmanagerservice")
			MsgUtil.sendMsg(reqin3, myactor!!)
			
			val expectedSlot = nfree+1
			
			result = channelForObserverT.receive()
			assertEquals( result, "park($expectedSlot)")
			
			delay(2000)
			
			var wdata = MsgUtil.buildDispatch("tester","weightData","weightData(100)","weightsensor")
				MsgUtil.sendMsg(wdata, weightactor!!)
			
			result = channelForObserverW.receive()
			assertEquals( result, "occupied")
			
			delay(1000)
			
			val reqen2 = MsgUtil.buildRequest("tester","carenter","carenter($expectedSlot)","parkmanagerservice")
			MsgUtil.sendMsg(reqen2, myactor!!)
			
			val expectedToken = "" + "$expectedSlot"+"$nfree"
			
			result = channelForObserverT.receive()
			assertEquals( result, "park($expectedToken)")
			
			delay(1000)
				
			wdata = MsgUtil.buildDispatch("tester","weightData","weightData(0)","weightsensor")
			MsgUtil.sendMsg(wdata, weightactor!!)
			
			result = channelForObserverW.receive()
			assertEquals( result, "free")
			
			delay(1000)
			
			val reqen3 = MsgUtil.buildDispatch("tester","outTokenid","outTokenid($expectedToken)","parkmanagerservice")
			MsgUtil.sendMsg(reqen3, myactor!!)
			
			delay(30000)
			
		}
	}
}

