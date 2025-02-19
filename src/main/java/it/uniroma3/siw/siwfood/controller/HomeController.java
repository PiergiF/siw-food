package it.uniroma3.siw.siwfood.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.siwfood.model.Administrator;
import it.uniroma3.siw.siwfood.model.Chef;
import it.uniroma3.siw.siwfood.model.Courses;
import it.uniroma3.siw.siwfood.model.Credentials;
import it.uniroma3.siw.siwfood.model.Customer;
import it.uniroma3.siw.siwfood.model.Recipe;
import it.uniroma3.siw.siwfood.service.AdministratorService;
import it.uniroma3.siw.siwfood.service.ChefService;
import it.uniroma3.siw.siwfood.service.CredentialsService;
import it.uniroma3.siw.siwfood.service.CustomerService;
import it.uniroma3.siw.siwfood.service.RecipeService;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;




@Controller
public class HomeController {

    @Autowired
	private CredentialsService credentialsService;

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private ChefService chefService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private AdministratorService administratorService;

    @GetMapping(value = "/")
    public String getHomePage(Model model){
        model.addAttribute("courses", Courses.values());
        model.addAttribute("recipes", this.recipeService.getRandomRecipes(5));
        return "homePage.html";
    }

    @GetMapping("/search")
    public String search(@RequestParam("query") String query, Model model) {
        List<Recipe> recipes = recipeService.searchRecipes(query);
        List<Chef> chefs = chefService.searchChefs(query);
        model.addAttribute("recipes", recipes);
        model.addAttribute("chefs", chefs);
        model.addAttribute("query", query);
        return "all/searchResultsPage.html";
    }

    @GetMapping("/logged/settingsPage/{loggedId}/{role}")
    public String getSettingsPage(@PathVariable("loggedId") Long userId, @PathVariable("role") String role, Model model) {
        
        UserDetails userDetails = GlobalController.getUserDetails(); //(UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(userDetails != null){
            Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());

            if(role.equals("CUSTOMER")){
                Customer customer = customerService.findById(userId);
                if(customer != null){
                    model.addAttribute("user", customer);
                }
            }else if(role.equals("CHEF")){
                Chef chef = chefService.findById(userId);
                if(chef!=null){
                    model.addAttribute("user", chef);
                }
            }else if(role.equals("ADMINISTRATOR")){
                Administrator administrator = administratorService.findById(userId);
                if(administrator != null){
                    model.addAttribute("user", administrator);
                }
            }

            model.addAttribute("credentials", credentials);
            model.addAttribute("newCredentials", new Credentials());
            return "logged/settingsPage.html";
        }
        else{
            return "/";
        }
    }

    @PostMapping("/settingsData")
    public String changeSettings(@Valid @ModelAttribute("credentials") Credentials nCredentials, BindingResult nCredentialsBindingResult,
                                @Valid @ModelAttribute("newCredentials") Credentials newCredentials, BindingResult newCredentialsBindingResult,
                                @RequestParam("changePassword") String changePassword, @RequestParam("changeUsername") String changeUsername,
                                @RequestParam(required = false, value = "name") String name, @RequestParam(required = false, value = "surname") String surname,
                                @RequestParam(required = false, value = "email") String email, @RequestParam(required = false, value = "dateOfBirth") LocalDate dateOfBirth,
                                @RequestParam(required = false, value = "chefImage") MultipartFile chefImage) 
    {
        
    //passwordEncoder.matches cripta la password inserita e verifica se è uguale a quella già criptata sul database
        if(!nCredentialsBindingResult.hasErrors() && !newCredentialsBindingResult.hasErrors()) {

            UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());

            String role = credentials.getRole();

            if(role.equals("CUSTOMER")){
                Customer customer = credentials.getCustomer();
                customer.setName(name);
                customer.setSurname(surname);
                customer.setEmail(email);
                customer.setDateOfBirth(dateOfBirth);
                customerService.saveCustomer(customer);
            }
            else if(role.equals("CHEF")){
                Chef existingChef = credentials.getChef();
                existingChef.setName(name);
                existingChef.setSurname(surname);
                existingChef.setEmail(email);
                existingChef.setDateOfBirth(dateOfBirth);

                if(!chefImage.isEmpty()){
                    try {
                        byte[] byteFoto = chefImage.getBytes();
                        existingChef.setImageBase64(Base64.getEncoder().encodeToString(byteFoto));
                    }catch (IOException e) {
                        //model.addAttribute("message", "Chef upload failed!");
                        return "redirect:/logged/settingsPage/" + existingChef.getId() + "/" + role ;
                    }
                }

                chefService.saveChef(existingChef);
            }
            else if(role.equals("ADMINISTRATOR")){
                Administrator administrator = credentials.getAdministrator();
                administrator.setName(name);
                administrator.setSurname(surname);
                administrator.setEmail(email);
                administrator.setDateOfBirth(dateOfBirth);
                administratorService.saveAdministrator(administrator);
            }

            if(changePassword.equals("true")){
                credentials.setPassword(newCredentials.getPassword());
                credentialsService.saveCredentials(credentials, credentials.getRole());
            }
            //SecurityContextHolder.getContext().setAuthentication(null);
            //Authentication authentication = new Authentication();
            //authentication.

            if(changeUsername.equals("true")){
                credentials.setUsername(newCredentials.getUsername());
                credentialsService.saveCredentialsNoPassword(credentials);
                return "redirect:/logout";
            }
        }
        
        return "redirect:/";
        //return "redirect:/logout";
    }
    
}

