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
 
 
class TestPlanUseCase1 {
		
	companion object{
		var systemStarted         = false
		val channelSyncStart      = Channel<String>()
		var testingObserver1       : CoapObserverForTesting? = null
		var testingObserver2       : CoapObserverForTesting2? = null
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
				//delay(1000)	//Give time to set up
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
		testingObserver2!!.removeObserver()
	}
	
    @Test
    fun testUC1(){
		println("+++++++++ testslotnum ")
		//Send a command and look at the result
		var result  = ""
		runBlocking{
 			val channelForObserverT = Channel<String>()
			val channelForObserverF = Channel<String>()
			testingObserver1!!.addObserver( channelForObserverF,"on")
			testingObserver2!!.addObserver( channelForObserverT,"stopped")
			
			var temp = MsgUtil.buildDispatch("tester","tempData","tempData(40)","thermometer")
			MsgUtil.sendMsg(temp, QakContext.getActor("thermometer")!!)
			
			result = channelForObserverF.receive()
			assertEquals( result, "on")
			
			
			//simulate the park-manager sending the stop
			temp = MsgUtil.buildDispatch("tester","stop","stop(stop)","parkingservicestatusgui")
			MsgUtil.sendMsg(temp, myactor!!)
			
			
			result = channelForObserverT.receive()
			assertEquals( result, "stopped")
			
			testingObserver1!!.removeObserver()
			testingObserver2!!.removeObserver()
			
			testingObserver1!!.addObserver( channelForObserverF,"off")
			testingObserver2!!.addObserver( channelForObserverT,"idle")
			
			temp = MsgUtil.buildDispatch("tester","tempData","tempData(15)","thermometer")
			MsgUtil.sendMsg(temp, QakContext.getActor("thermometer")!!)
			
			result = channelForObserverF.receive()
			assertEquals( result, "off")
			
			
			//simulate the park-manager sending the stop
			temp = MsgUtil.buildDispatch("tester","stop","stop(start)","parkingservicestatusgui")
			MsgUtil.sendMsg(temp, myactor!!)
			
			
			result = channelForObserverT.receive()
			assertEquals( result, "idle")
			
		}
	}
}