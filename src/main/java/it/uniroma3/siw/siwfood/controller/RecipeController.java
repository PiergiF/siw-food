package it.uniroma3.siw.siwfood.controller;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import it.uniroma3.siw.siwfood.model.Quantity;
import it.uniroma3.siw.siwfood.model.Chef;
import it.uniroma3.siw.siwfood.model.Credentials;
import it.uniroma3.siw.siwfood.model.Ingredient;
import it.uniroma3.siw.siwfood.model.Recipe;
import it.uniroma3.siw.siwfood.model.RecipeIngredient;
import it.uniroma3.siw.siwfood.model.Unit;
import it.uniroma3.siw.siwfood.service.ChefService;
import it.uniroma3.siw.siwfood.service.CredentialsService;
import it.uniroma3.siw.siwfood.service.IngredientService;
import it.uniroma3.siw.siwfood.service.RecipeService;
import it.uniroma3.siw.siwfood.service.UnitService;
import it.uniroma3.siw.siwfood.model.User;
import it.uniroma3.siw.siwfood.repository.ChefRepository;
import it.uniroma3.siw.siwfood.repository.QuantityRepository;
import it.uniroma3.siw.siwfood.repository.RecipeRepository;
import it.uniroma3.siw.siwfood.repository.UnitRepository;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;




@Controller
public class RecipeController {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private IngredientService ingredientService;

    @Autowired
    private UnitRepository unitRepository;

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private UnitService unitService;

    @Autowired
    private QuantityRepository quantityRepository;

    @Autowired
    private CredentialsService credentialsService;

    @Autowired
    private ChefService chefService;

    @GetMapping("/admin/allRecipesPage")
    public String getAllRecipesPage(Model model) {
        model.addAttribute("recipes", this.recipeRepository.findAll());
        return "admin/allRecipesPage.html";
    }

    @GetMapping("/recipePage/{id}")
	public String getRecipePage(@PathVariable("id") Long id, Model model) {
		Recipe recipe = recipeService.findById(id);
        List<RecipeIngredient> recipeIngredients = recipeService.findRecipeIngredientsByRecipeId(id);
        model.addAttribute("recipe", recipe);
        model.addAttribute("recipeIngredients", recipeIngredients);
		return "recipePage.html";
	}

    @GetMapping("/chef_admin/addRecipePage")
    public String getAddRecipePage(Model model) {
        model.addAttribute("recipe", new Recipe());
        model.addAttribute("ingredients", ingredientService.findAll());
        model.addAttribute("units", unitRepository.findAll());

        //ottenere lo chef
        UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
        if(credentials.getRole().equals("CHEF")){
            model.addAttribute("chef", credentials.getChef());
        }else if(credentials.getRole().equals("ADMIN")){
            model.addAttribute("chefs", chefService.findAll());
        }
        return "chef_admin/addRecipePage.html";
    }

    //giusto
    @PostMapping("/chef_admin/recipeData")
    public String createRecipe(@Valid @ModelAttribute Recipe recipe, BindingResult result,
                                @RequestParam("ingredientsName") List<String> listIngredients,
                                @RequestParam("unitsName") List<String> listUnits,
                                @RequestParam("quantities") List<String> listQuantities)
    {
        if (result.hasErrors()) {
            return "/chef_admin/addRecipePage";
        }

        Recipe savedRecipe = recipeService.save(recipe);

        for (int i=0; i<listIngredients.size();i++) {
            RecipeIngredient recipeIngredient = new RecipeIngredient();
            recipeIngredient.setRecipe(savedRecipe);

            //recipeIngredient.setIngredient(ingredientService.findByName(listIngredients.get(i).getName()));
            String ingredientName = listIngredients.get(i);
            recipeIngredient.setIngredient(ingredientService.findByName(listIngredients.get(i))
            .orElseGet(() ->{
                Ingredient newIngredient = new Ingredient();
                newIngredient.setName(ingredientName);
                return ingredientService.save(newIngredient);
            })
            );
            

            double amount = Double.parseDouble(listQuantities.get(i));
            recipeIngredient.setQuantity( quantityRepository.findByAmount(amount)
                .orElseGet(() -> {
                    Quantity newQuantity = new Quantity();
                    newQuantity.setAmount(amount);
                    return quantityRepository.save(newQuantity);
                })
            );

            //recipeIngredient.setUnit(unitService.findByName(listUnits.get(i).getName()));
            String unitName = listUnits.get(i);
            recipeIngredient.setUnit(unitService.findByName(listUnits.get(i))
            .orElseGet(() -> {
                Unit newUnit = new Unit();
                newUnit.setName(unitName);
                return unitService.save(newUnit);
            })
            );
            recipeService.saveRecipeIngredient(recipeIngredient);
        }
        return "redirect:/recipePage/" + recipe.getId();
    }

    @GetMapping("/chef_admin/personalRecipesPage/{id}")
    public String getMethodName(@PathVariable("id") Long id, Model model) {
        model.addAttribute("recipes", recipeService.findAllByChefId(id));
        return "chef_admin/personalRecipesPage.html";
    }
    




    
    /*
    @PostMapping("/chef_admin/recipeData")
    public String addRecipe(@ModelAttribute Recipe recipe, @RequestParam Map<String, String> params) {
        Recipe savedRecipe = recipeRepository.save(recipe);
        
        // Assume params contains ingredient details (simplified for this example)
        // e.g., ingredient1Name, ingredient1Amount, ingredient1Unit
        for (int i = 0; ; i++) {
            String nameKey = "ingredient" + i + "Name";
            String amountKey = "ingredient" + i + "Amount";
            String unitKey = "ingredient" + i + "Unit";
            
            if (!params.containsKey(nameKey)) break;

            String name = params.get(nameKey);
            double amount = Double.parseDouble(params.get(amountKey));
            String unit = params.get(unitKey);

            ingredientService.addIngredientToRecipe(savedRecipe, name, amount, unit);
        }

        //return "redirect:/admin/allRecipesPage.html";
        return "redirect:/recipePage/" + recipe.getId();
    }

    
    @ModelAttribute("units2")
    public List<Unit> getUnits() {
        return (List<Unit>)unitRepository.findAll();
    }
    */
    
    
    

    //prova a caso
    /*
    @GetMapping("/chef_admin/addRecipePage")
    public String getAddRecipePage(Model model) {
        model.addAttribute("recipe", new Recipe());
        model.addAttribute("ingredient1", new Ingredient());
        model.addAttribute("ingredient2", new Ingredient());
        model.addAttribute("amount1", new Quantity());
        model.addAttribute("amount2", new Quantity());
        model.addAttribute("unit1", new UnitOfMeasurament());
        model.addAttribute("unit2", new UnitOfMeasurament());
        return "chef_admin/addRecipePage.html";
    }
    
    @PostMapping("/chef_admin/recipeData")
    public String addRecipe(@ModelAttribute("recipe") Recipe recipe, @ModelAttribute("ingredient1") Ingredient ingredient1,
                            @ModelAttribute("ingredient2") Ingredient ingredient2, @ModelAttribute("amount1") Quantity amount1, 
                            @ModelAttribute("amount2") Quantity amount2, @ModelAttribute("unit1") UnitOfMeasurament unit1,
                            @ModelAttribute("unit2") UnitOfMeasurament unit2, Model model) {
        List<Ingredient> ingredients = new LinkedList<Ingredient>();
        ingredients.add(ingredient1);
        ingredients.add(ingredient2);
        recipe.setIngredients(ingredients);
        List<Quantity> amounts = new LinkedList<Quantity>();
        amounts.add(amount1);
        amounts.add(amount2);
        recipe.setAmounts(amounts);
        List<UnitOfMeasurament> units = new LinkedList<UnitOfMeasurament>();
        units.add(unit1);
        units.add(unit2);
        recipe.setUnits(units);
        List<UnitOfMeasurament> units1 = new LinkedList<UnitOfMeasurament>();
        units1.add(unit1);
        amount1.setUnit(units1);
        List<UnitOfMeasurament> units2 = new LinkedList<UnitOfMeasurament>();
        units2.add(unit2);
        amount2.setUnit(units2);
        List<Quantity> amounts1 = new LinkedList<Quantity>();
        amounts1.add(amount1);
        unit1.setAmounts(amounts1);
        List<Quantity> amounts2 = new LinkedList<Quantity>();
        amounts2.add(amount2);
        unit2.setAmounts(amounts2);
        List<Quantity> amounts11 = new LinkedList<Quantity>();
        amounts11.add(amount1);
        ingredient1.setAmounts(amounts11);
        List<Quantity> amounts22 = new LinkedList<Quantity>();
        amounts22.add(amount2);
        ingredient2.setAmounts(amounts22);

        
        return "/recipePage.html";
    }
    */
}