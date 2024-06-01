package it.uniroma3.siw.siwfood.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;

import it.uniroma3.siw.siwfood.controller.validator.IngredientValidator;
import it.uniroma3.siw.siwfood.model.Ingredient;
import it.uniroma3.siw.siwfood.service.IngredientService;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@Controller
public class IngredientController {

    @Autowired
    private IngredientService ingredientService;

    @Autowired
    private IngredientValidator ingredientValidator;

    @GetMapping("/chef_admin/allIngredientsPage")
    public String getAllIngredientsPage(Model model) {
        model.addAttribute("ingredients",ingredientService.findAll());
        return "chef_admin/allIngredientsPage.html";
    }

    @GetMapping("/chef_admin/ingredientPage/{id}")
    public String getIngredientPage(@PathVariable("id") Long id, Model model) {
        model.addAttribute("ingredient", ingredientService.findById(id));
        return "chef_admin/ingredientPage.html";
    }
    

    @GetMapping("/chef_admin/addIngredientPage")
    public String getAddIngredientPage(Model model) {
        model.addAttribute("newIngredient", new Ingredient());
        return "chef_admin/addIngredientPage.html";
    }

    @PostMapping("/chef_admin/ingredientData")
    public String postMethodName(@Valid @ModelAttribute("newIngredient") Ingredient newIngredient, BindingResult ingredientBindingResult,
                                Model model) {
        this.ingredientValidator.validate(newIngredient, ingredientBindingResult);
        if(!ingredientBindingResult.hasErrors()){
            ingredientService.save(newIngredient);
            model.addAttribute("ingredient", newIngredient);
            return "redirect:/chef_admin/ingredientPage/" + newIngredient.getId();
        }else{
            //vorrei gestire meglio l'errore se ci arrivo con i tempi
            //posso pure fare una verifica e se già è stato inserito skippo l'inserimento
            model.addAttribute("duplicateError", "L'ingrediente esiste già");
            return "chef_admin/addIngredientPage.html";
        }
    }
    
    
    
}
