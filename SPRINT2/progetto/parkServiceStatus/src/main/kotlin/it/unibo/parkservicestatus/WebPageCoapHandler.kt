package it.unibo.parkservicestatus

import com.andreapivetta.kolor.Color
import it.unibo.actor0.sysUtil
import org.eclipse.californium.core.CoapHandler
import org.eclipse.californium.core.CoapResponse
import org.json.JSONObject
import org.springframework.web.util.HtmlUtils
import java.lang.Exception

/*
An object of this class is registered as observer of the resource
 */
    class WebPageCoapHandler(val controller: HIController) : CoapHandler {
    var counter = 0
    override fun onLoad(response: CoapResponse) {
        val content: String = StateAdapter.convertToJSON(response.getResponseText())
        sysUtil.colorPrint("WebPageCoapHandler | response content=$content}", Color.GREEN )
        //response={"sonarvalue":"D"} or {"info":"somevalue"}
        try {
            val jsonContent = JSONObject(content)
            if (jsonContent.has("thermometer")){
                val tempRep = ResourceRep(" temperature " + HtmlUtils.htmlEscape( jsonContent.getString("thermometer"))  )
                sysUtil.colorPrint("WebPageCoapHandler | sonar value=${tempRep.content}", Color.BLUE)
                controller.simpMessagingTemplate?.convertAndSend(WebSocketConfig.topicForClient, tempRep)
            }
            if (jsonContent.has("fan")){
                val fanRep = ResourceRep(" fan " + HtmlUtils.htmlEscape( jsonContent.getString("fan"))  )
                sysUtil.colorPrint("WebPageCoapHandler | sonar value=${fanRep.content}", Color.BLUE)
                controller.simpMessagingTemplate?.convertAndSend(WebSocketConfig.topicForClient, fanRep)
            }
            if (jsonContent.has("trolley")){
                val trolleyRep = ResourceRep(" trolley " + HtmlUtils.htmlEscape( jsonContent.getString("trolley"))  )
                sysUtil.colorPrint("WebPageCoapHandler | sonar value=${trolleyRep.content}", Color.BLUE)
                controller.simpMessagingTemplate?.convertAndSend(WebSocketConfig.topicForClient, trolleyRep)
            }
        }catch(e:Exception){
            sysUtil.colorPrint("WebPageCoapHandler | ERROR=${content}", Color.RED)
        }
    }

    override fun onError() {
        System.err.println("WebPageCoapHandler  |  FAILED  ")
    }
}