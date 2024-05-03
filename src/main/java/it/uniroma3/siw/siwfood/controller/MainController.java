package it.uniroma3.siw.siwfood.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class MainController {

    
    @GetMapping("/")
    public String getHomePage() {
        return "homePage.html";
    }
    
}
