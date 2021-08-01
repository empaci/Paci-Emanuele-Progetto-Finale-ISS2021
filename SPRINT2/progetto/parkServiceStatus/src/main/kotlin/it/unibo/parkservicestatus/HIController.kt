package it.unibo.parkservicestatus

import com.andreapivetta.kolor.Color
import it.unibo.actor0.sysUtil
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.util.HtmlUtils
import org.springframework.ui.ModelMap

import org.springframework.validation.BindingResult

import org.springframework.web.bind.annotation.ModelAttribute

import org.springframework.web.bind.annotation.RequestMethod

import org.springframework.web.bind.annotation.RequestMapping





/*
The HIController USES the sonarresources via CoAP
See also it.unibo.boundaryWalk/userdocs/websocketInteraction.html
https://spring.io/guides/gs/messaging-stomp-websocket/

 */


@Controller
class HIController {
    //@Value("\${human.logo}")
    var appName: String?    = null

    var coap    = CoapSupport("coap://localhost:8070", "ctxparkmanager/parkingstate")
    var coapTrolley    = CoapSupport("coap://localhost:8070", "ctxparkmanager/trolleylogic")

    /*
     * Update the page vie socket.io when the application-resource changes.
     * See also https://www.baeldung.com/spring-websockets-send-message-to-user
     */
    @Autowired
    var  simpMessagingTemplate : SimpMessagingTemplate? = null

    //var ws         = IssWsHttpJavaSupport.createForWs ("localhost:8083")

    init{
        sysUtil.colorPrint("HumanInterfaceController | INIT", Color.GREEN)
        //connQakSupport = connQakCoap()
        //connQakSupport.createConnection()
        coap.observeResource( WebPageCoapHandler(this) ) //TODO: update the HTML page via socket
    }


    @GetMapping("/")    //defines that the method handles GET requests.
    fun entry(model: Model): String {
        model.addAttribute("arg", appName )
        sysUtil.colorPrint("HIController | entry model=$model", Color.GREEN)
        return  "parkservicestatus"
    }

    @RequestMapping(value = [ "/trolleycmd" ], method = [RequestMethod.GET], params = ["start"])
    fun  handleTrolleyStart(viewmodel : Model,  @RequestParam(name="start")v : String) : String{
        sysUtil.colorPrint("HIController | trolley command $v", Color.RED)
        coapTrolley.updateResourceWithValue("start")
        return  "parkservicestatus"
    }

    @RequestMapping(value = [ "/trolleycmd" ], method = [RequestMethod.GET], params = ["stop"])
    fun  handleTrolleyStop(viewmodel : Model,  @RequestParam(name="stop")v : String) : String{
        sysUtil.colorPrint("HIController | trolley command $v", Color.RED)
        coapTrolley.updateResourceWithValue("stop")
        return  "parkservicestatus"
    }

    fun getWebPageRep(): ResourceRep {
        val resourceRep: String = coap.readResource()
        println("HIController | resourceRep=$resourceRep")
        return ResourceRep("" + HtmlUtils.htmlEscape(resourceRep))
    }

}