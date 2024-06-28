package it.uniroma3.siw.siwfood.controller.restController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import it.uniroma3.siw.siwfood.model.Courses;
import it.uniroma3.siw.siwfood.model.Recipe;
import it.uniroma3.siw.siwfood.service.RecipeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
public class RecipeRestController {

    @Autowired
    public RecipeService recipeService;

    @GetMapping("/rest/allRecipes")
    public List<Recipe> getAllRecipes() {
        return (List<Recipe>) this.recipeService.findAll();
    }

    @GetMapping("/rest/recipe/{id}")
    public Recipe getRecipe(@PathVariable("id") Long id) {
        return this.recipeService.findById(id);
    }

    @GetMapping("/rest/courses")
    public Courses[] getAllCourses() {
        return Courses.values();
    }
    
    

}
