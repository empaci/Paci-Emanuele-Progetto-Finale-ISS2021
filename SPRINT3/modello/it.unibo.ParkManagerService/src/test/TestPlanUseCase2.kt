package test

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
import test.kotlin.CoapObserverForTesting3
 
 
class TestPlanUseCase2 {
		
	companion object{
		var systemStarted         = false
		val channelSyncStart      = Channel<String>()
		var testingObserver       : CoapObserverForTesting3? = null
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
					myactor=QakContext.getActor("thermometer")
				}				
				delay(1000)	//Give time to set up
				channelSyncStart.send("starttesting")
				testingObserver = CoapObserverForTesting3()
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
		testingObserver!!.removeObserver()
	}
	
    @Test
    fun testUC2(){
		var result = ""
		runBlocking{
			val channelForObserverT = Channel<String>()
			testingObserver!!.addObserver( channelForObserverT,"received")
			
			var temp = MsgUtil.buildDispatch("tester","tempData","tempData(40)","thermometer")
			MsgUtil.sendMsg(temp, myactor!!)
			
			result = channelForObserverT.receive()
			assertEquals( result, "received: above")
			
			println("===================== Received above ok")
			
			temp = MsgUtil.buildDispatch("tester","tempData","tempData(10)","thermometer")
			MsgUtil.sendMsg(temp, myactor!!)
			
			result = channelForObserverT.receive()
			assertEquals( result, "received: below")
			
			println("===================== Received below ok")
		}	
	}
}
