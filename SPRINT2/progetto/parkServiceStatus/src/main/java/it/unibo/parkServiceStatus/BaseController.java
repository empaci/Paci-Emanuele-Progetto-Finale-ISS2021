package it.unibo.parkServiceStatus;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;

@Controller 
public class BaseController { 
    @Value("${spring.application.name}")
    String appName;

  @GetMapping("/") 		 
  public String homePage(Model model) {
	  System.out.println("------------------- BaseController homePage " + model  );
	  model.addAttribute("arg", appName);
	  return "dervicestatus";
   } 
    
    @ExceptionHandler 
    public ResponseEntity handle(Exception ex) {
    	HttpHeaders responseHeaders = new HttpHeaders();
        return new ResponseEntity(
        		"BaseController ERROR " + ex.getMessage(), responseHeaders, HttpStatus.CREATED);
    }

}