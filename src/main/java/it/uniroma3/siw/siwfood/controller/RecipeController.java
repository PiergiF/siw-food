package it.uniroma3.siw.siwfood.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import it.uniroma3.siw.siwfood.model.Quantity;
import it.uniroma3.siw.siwfood.model.Administrator;
import it.uniroma3.siw.siwfood.model.Chef;
import it.uniroma3.siw.siwfood.model.Courses;
import it.uniroma3.siw.siwfood.model.Credentials;
import it.uniroma3.siw.siwfood.model.Ingredient;
import it.uniroma3.siw.siwfood.model.Recipe;
import it.uniroma3.siw.siwfood.model.RecipeIngredient;
import it.uniroma3.siw.siwfood.model.Unit;
import it.uniroma3.siw.siwfood.service.AdministratorService;
import it.uniroma3.siw.siwfood.service.ChefService;
import it.uniroma3.siw.siwfood.service.CredentialsService;
import it.uniroma3.siw.siwfood.service.IngredientService;
import it.uniroma3.siw.siwfood.service.RecipeService;
import it.uniroma3.siw.siwfood.service.UnitService;
import it.uniroma3.siw.siwfood.service.CustomerService;
import it.uniroma3.siw.siwfood.model.Customer;
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
    private CustomerService customerService;

    @Autowired
    private AdministratorService administratorService;

    @GetMapping("/all/allRecipesPage")
    public String getAllRecipesPage(Model model) {
        model.addAttribute("recipes", this.recipeRepository.findAll());
        return "all/allRecipesPage.html";
    }

    @GetMapping("/all/recipePage/{id}")
	public String getRecipePage(@PathVariable("id") Long id, Model model) {
		Recipe recipe = recipeService.findById(id);
        List<RecipeIngredient> recipeIngredients = recipeService.findRecipeIngredientsByRecipeId(id);
        model.addAttribute("recipe", recipe);
        model.addAttribute("recipeIngredients", recipeIngredients);
        model.addAttribute("photos", recipe.getImagesBase64());

        UserDetails userDetails = GlobalController.getUserDetails();
        if(userDetails!=null){
            Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
            String role = credentials.getRole();
            List<Recipe> savedRecipes=null;
            if(role.equals("CUSTOMER")){
                Customer customer = customerService.findById(credentials.getCustomer().getId());
                savedRecipes = customerService.getSavedRecipes(customer);
            }else if(role.equals("CHEF")){
                Chef chef = chefService.findById(credentials.getChef().getId());
                savedRecipes = chefService.getSavedRecipes(chef);
            }else if(role.equals("ADMINISTRATOR")){
                Administrator administrator = administratorService.findById(credentials.getAdministrator().getId());
                savedRecipes = administratorService.getSavedRecipes(administrator);
            }
            if((savedRecipes != null) && (!savedRecipes.contains(recipe))){
                model.addAttribute("canSave", true);
                System.out.println("AOOOOOOOOO");
            }else{
                model.addAttribute("canSave", false);
            }
        }else{
            model.addAttribute("canSave", false);
        }
		return "all/recipePage.html";
	}

    @GetMapping("/all/coursePage/{course}")
    public String getMethodName(@PathVariable Courses course, Model model) {
        model.addAttribute("recipesCourse", recipeService.findAllByCourse(course));
        model.addAttribute("course", course);
        return "all/coursePage.html";
    }
    

    @GetMapping("/logged/savedRecipesPage/{id}/{role}")
    public String getSavedRecipesPage(@PathVariable("id") Long personId, @PathVariable("role") String role, Model model) {
        
        List<Recipe> savedRecipes=null;

        if(role.equals("CUSTOMER")){
            Customer customer = customerService.findById(personId);
            if(customer != null){
                savedRecipes = customerService.getSavedRecipes(customer);
            }
        }else if(role.equals("CHEF")){
            Chef chef = chefService.findById(personId);
            if(chef!=null){
                savedRecipes = chefService.getSavedRecipes(chef);
            }
        }else if(role.equals("ADMINISTRATOR")){
            Administrator administrator = administratorService.findById(personId);
            if(administrator != null){
                savedRecipes = administratorService.getSavedRecipes(administrator);
            }
        }
        else{
            return "/error";
        }
        model.addAttribute("savedRecipes", savedRecipes);
        return "logged/savedRecipesPage.html";
    }

    @PostMapping("/saveRecipesData/{rId}/{pId}/{role}")
    public String saveRecipesData(@PathVariable("rId") Long recipeId, @PathVariable("pId") Long personId, @PathVariable("role") String role) {
        Recipe recipe = recipeService.findById(recipeId);

        if(role.equals("CUSTOMER")){
            Customer customer = customerService.findById(personId);
            if(customer != null){
                customerService.saveRecipeForCustomer(customer, recipe);
                return "redirect:/logged/savedRecipesPage/" + customer.getId() + "/" + role;
            }
        }else if(role.equals("CHEF")){
            Chef chef = chefService.findById(personId);
            if(chef!=null){
                chefService.saveRecipeForChef(chef, recipe);
                return "redirect:/logged/savedRecipesPage/" + chef.getId() + "/" + role;
            }
        }else if(role.equals("ADMINISTRATOR")){
            Administrator administrator = administratorService.findById(personId);
            if(administrator != null){
                administratorService.saveRecipeForAdministrator(administrator, recipe);
                return "redirect:/logged/savedRecipesPage/" + administrator.getId() + "/" + role;
            }
        }
        return "/error";
    }
    

    @GetMapping("/removeSavedRecipe/{rId}/{pId}/{role}")
    public String removeSavedRecipe(@PathVariable("rId") Long recipeId, @PathVariable("pId") Long personId, @PathVariable("role") String role) {
        
        Recipe recipe = recipeService.findById(recipeId);

        if(role.equals("CUSTOMER")){
            Customer customer = customerService.findById(personId);
            if(customer != null){
                customerService.removeRecipeForCustomer(customer, recipe);
                return "redirect:/logged/savedRecipesPage/" + customer.getId() + "/" + role;
            }
        }else if(role.equals("CHEF")){
            Chef chef = chefService.findById(personId);
            if(chef!=null){
                chefService.removeRecipeForChef(chef, recipe);
                return "redirect:/logged/savedRecipesPage/" + chef.getId() + "/" + role;
            }
        }else if(role.equals("ADMINISTRATOR")){
            Administrator administrator = administratorService.findById(personId);
            if(administrator != null){
                administratorService.removeRecipeForAdministrator(administrator, recipe);
                return "redirect:/logged/savedRecipesPage/" + administrator.getId() + "/" + role;
            }
        }
        return "/error";
    }
    

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
        }else if(credentials.getRole().equals("ADMINISTRATOR")){
            model.addAttribute("chefs", chefService.findAll());
        }
        return "chef_admin/addRecipePage.html";
    }

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
            return "redirect:/all/recipePage/" + recipe.getId();
        }else{
            return "/chef_admin/addRecipePage";
        }
    }

    @GetMapping("/chef_admin/personalRecipesPage/{id}")
    public String getMethodName(@PathVariable("id") Long id, Model model) {
        model.addAttribute("recipes", recipeService.findAllByChefId(id));
        model.addAttribute("chef", chefService.findById(id));
        return "chef_admin/personalRecipesPage.html";
    }
    

    @GetMapping("/chef_admin/editRecipePage/{id}")
    public String getEditRecipePage(@PathVariable("id") Long id, Model model) {
        Recipe existingRecipe = this.recipeService.findById(id);
        if((model.getAttribute("accountRole").equals("ADMINISTRATOR")) || (model.getAttribute("accountRole").equals("CHEF") && existingRecipe.getChef().getId() == model.getAttribute("loggedId"))){
            List<RecipeIngredient> recipeIngredients = recipeService.findRecipeIngredientsByRecipeId(id);
            model.addAttribute("recipe", recipeService.findById(id));
            model.addAttribute("recipeIngredients", recipeIngredients);
            model.addAttribute("ingredients", ingredientService.findAll());
            model.addAttribute("units", unitService.findAll());
            model.addAttribute("chefs", chefService.findAll());
            model.addAttribute("courses", Courses.values());
            return "chef_admin/editRecipePage.html";
        }
        else{
            return "redirect:/";
        }
    }

    @PostMapping("/chef_admin/recipeEditData/{id}")
    public String editRecipe(@PathVariable("id") Long id, @Valid @ModelAttribute Recipe recipe, BindingResult result,
                            @RequestParam("ingredientsName") List<String> listIngredients,
                            @RequestParam("unitsName") List<String> listUnits,
                            @RequestParam("quantities") List<String> listQuantities,
                            @RequestParam("newImages") MultipartFile[] newFiles,
                            @RequestParam(required = false, value = "removeImageIndexes") List<Integer> removeImageIndexes, Model model)
    {

        if (result.hasErrors()) {
            return "/chef_admin/editRecipePage/" + id;
        }
        // Recupera la ricetta esistente dal database
        Recipe existingRecipe = recipeService.findById(id);
        if((model.getAttribute("accountRole").equals("ADMINISTRATOR")) || (model.getAttribute("accountRole").equals("CHEF") && existingRecipe.getChef().getId() == model.getAttribute("loggedId"))){
            if((listIngredients.size() == listUnits.size()) && (listIngredients.size() == listQuantities.size()) && (listUnits.size() == listQuantities.size())){

                if (existingRecipe == null) {
                    // Gestisce il caso in cui la ricetta non esiste
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

                // Rimuove gli ingredienti esistenti per questa ricetta
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
                return "redirect:/all/recipePage/" + recipe.getId();
            }else{
                return "/chef_admin/addRecipePage";
            }
        }else{
            return "redirect:/";
        }
    }
    
    @GetMapping("/chef_admin/removeRecipe/{id}")
    public String deleteRecipe(@PathVariable("id") Long recipeId) {
        
        // Rimuove gli ingredienti esistenti per questa ricetta
        recipeService.deleteRecipeIngredientsByRecipeId(recipeId);
        recipeService.deleteById(recipeId);
        return "redirect:/all/allRecipesPage";
    }

}