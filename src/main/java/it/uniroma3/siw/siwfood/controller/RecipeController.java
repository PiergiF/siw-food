package it.uniroma3.siw.siwfood.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
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
import it.uniroma3.siw.siwfood.model.Courses;
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
import it.uniroma3.siw.siwfood.service.UserService;
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
import org.springframework.web.multipart.MultipartFile;
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

    @Autowired
    private UserService userService;

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
        model.addAttribute("photos", recipe.getImagesBase64());
		return "recipePage.html";
	}

    @GetMapping("/savedRecipesPage/{id}/{role}")
    public String getSavedRecipesPage(@PathVariable("id") Long personId, @PathVariable("role") String role, Model model) {
        
        List<Recipe> savedRecipes=null;

        if(role.equals("CUSTOMER") || role.equals("ADMIN")){
            User user = userService.findById(personId);
            if(user!=null){
                savedRecipes = userService.getSavedRecipes(user);
            }
        }else if(role.equals("CHEF")){
            Chef chef = chefService.findById(personId);
            if(chef!=null){
                savedRecipes = chefService.getSavedRecipes(chef);
            }
        }else{
            return "/error";
        }
        model.addAttribute("savedRecipes", savedRecipes);
        return "savedRecipesPage.html";
    }

    @PostMapping("/saveRecipesData/{rId}/{pId}/{role}")
    public String saveRecipesData(@PathVariable("rId") Long recipeId, @PathVariable("pId") Long personId, @PathVariable("role") String role) {
        Recipe recipe = recipeService.findById(recipeId);

        if(role.equals("CUSTOMER") || role.equals("ADMIN")){
            User user = userService.findById(personId);
            if(user!=null){
                userService.saveRecipeForUser(user, recipe);
                return "redirect:/savedRecipesPage/" + user.getId() + "/" + role;
            }
        }else if(role.equals("CHEF")){
            Chef chef = chefService.findById(personId);
            if(chef!=null){
                chefService.saveRecipeForChef(chef, recipe);
                return "redirect:/savedRecipesPage/" + chef.getId() + "/" + role;
            }
        }
        return "/error";
    }
    

    @GetMapping("/removeSavedRecipe/{rId}/{pId}/{role}")
    public String removeSavedRecipe(@PathVariable("rId") Long recipeId, @PathVariable("pId") Long personId, @PathVariable("role") String role) {
        
        Recipe recipe = recipeService.findById(recipeId);

        if(role.equals("USER") || role.equals("ADMIN")){
            User user = userService.findById(personId);
            if(user!=null){
                userService.removeRecipeForUser(user, recipe);
                return "redirect:/savedRecipesPage/" + user.getId() + "/" + role;
            }
        }else if(role.equals("CHEF")){
            Chef chef = chefService.findById(personId);
            if(chef!=null){
                chefService.removeRecipeForChef(chef, recipe);
                return "redirect:/savedRecipesPage/" + chef.getId() + "/" + role;
            }
        }
        return "/error";
    }
    
       //per prendere tutti i valori della enum
    //Courses[] e = Courses.values();

    @GetMapping("/chef_admin/addRecipePage")
    public String getAddRecipePage(Model model) {
        model.addAttribute("recipe", new Recipe());
        model.addAttribute("ingredients", ingredientService.findAll());
        model.addAttribute("units", unitRepository.findAll());
        model.addAttribute("courses", Courses.values());

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
                                @RequestParam("quantities") List<String> listQuantities,
                                @RequestParam("images") MultipartFile[] files)
    {
        if (result.hasErrors()) {
            return "/chef_admin/addRecipePage";
        }
        if((listIngredients.size() == listUnits.size()) && (listIngredients.size() == listQuantities.size()) && (listUnits.size() == listQuantities.size())){

            try {
                List<String> fotosBase64 = new ArrayList<>();
                for(MultipartFile file : files){
                    byte[] byteFoto = file.getBytes();
                    fotosBase64.add(Base64.getEncoder().encodeToString(byteFoto));
                }
                recipe.setImagesBase64(fotosBase64);
            }catch (IOException e) {
                //model.addAttribute("message", "Recipe upload failed!");
                return "chef_admin/addRecipePage.html";
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
        }else{
            return "/chef_admin/addRecipePage";
        }
    }

    @GetMapping("/chef_admin/personalRecipesPage/{id}")
    public String getMethodName(@PathVariable("id") Long id, Model model) {
        model.addAttribute("recipes", recipeService.findAllByChefId(id));
        return "chef_admin/personalRecipesPage.html";
    }
    

    @GetMapping("/chef_admin/editRecipePage/{id}")
    public String getEditRecipePage(@PathVariable("id") Long id, Model model) {
        List<RecipeIngredient> recipeIngredients = recipeService.findRecipeIngredientsByRecipeId(id);
        model.addAttribute("recipe", recipeService.findById(id));
        model.addAttribute("recipeIngredients", recipeIngredients);
        model.addAttribute("ingredients", ingredientService.findAll());
        model.addAttribute("units", unitService.findAll());
        model.addAttribute("chefs", chefService.findAll());
        model.addAttribute("courses", Courses.values());
        return "chef_admin/editRecipePage.html";
    }

    @PostMapping("/chef_admin/recipeEditData/{id}")
    public String editRecipe(@PathVariable("id") Long id, @Valid @ModelAttribute Recipe recipe, BindingResult result,
                            @RequestParam("ingredientsName") List<String> listIngredients,
                            @RequestParam("unitsName") List<String> listUnits,
                            @RequestParam("quantities") List<String> listQuantities,
                            @RequestParam("newImages") MultipartFile[] newFiles,
                            @RequestParam(required = false, value = "removeImageIndexes") List<Integer> removeImageIndexes)
    {

        if (result.hasErrors()) {
            return "/chef_admin/editRecipePage/" + id;
        }
        if((listIngredients.size() == listUnits.size()) && (listIngredients.size() == listQuantities.size()) && (listUnits.size() == listQuantities.size())){

             // Recupera la ricetta esistente dal database
            Recipe existingRecipe = recipeService.findById(id);
            if (existingRecipe == null) {
                // Gestisci il caso in cui la ricetta non esiste
                return "redirect:/errorPage";
            }
            // Aggiorna i dettagli della ricetta
            existingRecipe.setName(recipe.getName());
            existingRecipe.setChef(recipe.getChef());
            existingRecipe.setDescription(recipe.getDescription());
            existingRecipe.setCourse(recipe.getCourse());

            //rimuovi immagini
            if(removeImageIndexes != null){
                for(int index : removeImageIndexes){
                    existingRecipe.getImagesBase64().remove(index);
                }
            }

            //aggiungi immagini
            if(newFiles != null){
                for(MultipartFile newFile : newFiles){
                    if(!newFile.isEmpty()){
                        try{
                            byte[] byteFoto = newFile.getBytes();
                            existingRecipe.getImagesBase64().add(Base64.getEncoder().encodeToString(byteFoto));
                        }catch(IOException e){
                            //model.addAttribute("message", "Recipe upload failed!");
                            return "chef_admin/addRecipePage.html";
                        }
                    }
                }
            }

            Recipe updatedRecipe = recipeService.save(existingRecipe);

            // Rimuovere gli ingredienti esistenti per questa ricetta
            recipeService.deleteRecipeIngredientsByRecipeId(id);

            for (int i=0; i<listIngredients.size();i++) {
                RecipeIngredient recipeIngredient = new RecipeIngredient();
                recipeIngredient.setRecipe(updatedRecipe);

                //Gestisce l'ingrediente
                String ingredientName = listIngredients.get(i);
                recipeIngredient.setIngredient(ingredientService.findByName(listIngredients.get(i))
                .orElseGet(() ->{
                    Ingredient newIngredient = new Ingredient();
                    newIngredient.setName(ingredientName);
                    return ingredientService.save(newIngredient);
                })
                );
                
                //Gestisce la quantità
                double amount = Double.parseDouble(listQuantities.get(i));
                recipeIngredient.setQuantity( quantityRepository.findByAmount(amount)
                    .orElseGet(() -> {
                        Quantity newQuantity = new Quantity();
                        newQuantity.setAmount(amount);
                        return quantityRepository.save(newQuantity);
                    })
                );

                // Gestisce l'unità
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
        }else{
            return "/chef_admin/addRecipePage";
        }
    }
    
    @GetMapping("/chef_admin/removeRecipe/{id}")
    public String deleteRecipe(@PathVariable("id") Long recipeId) {
        
        // Rimuovere gli ingredienti esistenti per questa ricetta
        recipeService.deleteRecipeIngredientsByRecipeId(recipeId);
        recipeService.deleteById(recipeId);
        return "redirect:/admin/allRecipesPage";
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