package it.uniroma3.siw.siwfood.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import it.uniroma3.siw.siwfood.repository.RecipeRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@Controller
public class RecipeController {

    @Autowired
    private RecipeRepository recipeRepository;

    @GetMapping("/admin/allRecipesPage")
    public String getAllRecipesPage(Model model) {
        model.addAttribute("recipes", this.recipeRepository.findAll());
        return "admin/allRecipesPage.html";
    }

    @GetMapping("/recipePage/{id}")
	public String getRecipePage(@PathVariable("id") Long id, Model model) {
		model.addAttribute("recipe", this.recipeRepository.findById(id).get());
		return "recipePage.html";
	}
    
}