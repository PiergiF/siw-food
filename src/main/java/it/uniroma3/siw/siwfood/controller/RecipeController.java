package it.uniroma3.siw.siwfood.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import it.uniroma3.siw.siwfood.service.RecipeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class RecipeController {
    @Autowired private RecipeService recipeService;

    /*
    @GetMapping("/")
    public String getHomePage() {
        return "homePage.html";
    }

    @GetMapping("/prova")
    public String getProva() {
        return "prova.html";
    }*/
    
    

}
