package it.uniroma3.siw.siwfood.controller.restController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import it.uniroma3.siw.siwfood.model.Ingredient;
import it.uniroma3.siw.siwfood.service.IngredientService;

@RestController
public class IngredientRestController {

    @Autowired
    public IngredientService ingredientService;

    @GetMapping("/rest/allIngredients")
    public List<Ingredient> getAllIngredients() {
        return (List<Ingredient>) this.ingredientService.findAll();
    }

    @GetMapping("/rest/ingredient/{id}")
    public Ingredient getIngredient(@PathVariable("id") Long id) {
        return this.ingredientService.findById(id);
    }

}
