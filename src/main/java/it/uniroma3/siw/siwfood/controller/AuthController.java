package it.uniroma3.siw.siwfood.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import it.uniroma3.siw.siwfood.controller.validator.ChefValidator;
import it.uniroma3.siw.siwfood.model.Administrator;
import it.uniroma3.siw.siwfood.model.Chef;
import it.uniroma3.siw.siwfood.model.Credentials;
import it.uniroma3.siw.siwfood.model.Customer;
import it.uniroma3.siw.siwfood.service.AdministratorService;
import it.uniroma3.siw.siwfood.service.ChefService;
import it.uniroma3.siw.siwfood.service.CredentialsService;
import it.uniroma3.siw.siwfood.service.CustomerService;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RequestBody;



@Controller
public class AuthController {

    @Autowired
	private CustomerService customerService;

    @Autowired
	private CredentialsService credentialsService;

    @Autowired
    private ChefService chefService;

    @Autowired
    private AdministratorService administratorService;

    @Autowired
    private ChefValidator chefValidator;

    /*
    @GetMapping(value = "/")
    public String getHomePage(Model model) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {	
			UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
            model.addAttribute("accountRole",credentials.getRole());
		}
        return "homePage.html";
    }
    */

    @GetMapping("/loginPage")
    public String getLoginPage() {
        return "loginPage.html";
    }

    @GetMapping(value = "/registrationPage")
    public String getRegistrationPage(Model model) {
        //model.addAttribute("customer", new Customer());
		model.addAttribute("credentials", new Credentials());
        return "registrationPage.html";
    }

    //prova estetica gpt
    /*
    @GetMapping(value = "/registrationPageGPT")
    public String getRegistrationPageGPT(Model model) {
        model.addAttribute("customer", new Customer());
		model.addAttribute("credentials", new Credentials());
        return "registrationPageGPT.html";
    }
    */

    /*
    @PostMapping("/registrationData")
    public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult userBindingResult,
                                @Valid @ModelAttribute("credentials") Credentials credentials, BindingResult credentialsBindingResult,
                                Model model) {
        // se user e credential hanno entrambi contenuti validi, memorizza User e the Credentials nel DB
        if(!userBindingResult.hasErrors() && !credentialsBindingResult.hasErrors()) {
            userService.saveUser(user);
            credentials.setUser(user);
            credentialsService.saveCredentials(credentials);
            model.addAttribute("RegistrationCompleted", true); //non funziona perché c'è il redirect di mezzo
            return "redirect:/loginPage";
        }
        return "registrationPage.html";
    }
    */

    //2
    //@Valid BindingResult userBindingResult
    //@Valid BindingResult credentialsBindingResult
    //se non metto tutti i dati pure su user non va. provare con modelAttribute
    @PostMapping("/registrationData")
    public String registerUser(@RequestParam(required = false, name = "name") String name, 
                                @RequestParam(required = false, name = "surname") String surname, 
                                @RequestParam(required = false, name = "email") String email, 
                                @RequestParam(required = false, name = "dateOfBirth") LocalDate dateOfBirth,
                                @RequestParam(required = false, name = "image") MultipartFile file,
                                @Valid @ModelAttribute("credentials") Credentials credentials, BindingResult credentialsBindingResult,
                                @RequestParam("role") String role,
                                Model model) {

        //se name è presente nell'invio al controller ma senza valore equivale ad una stringa vuota, se non è proprio presente equivale a NULL
        
        // se user e credential hanno entrambi contenuti validi, memorizza User e the Credentials nel DB
        if(!credentialsBindingResult.hasErrors()) {
            if(role.equals("CUSTOMER")){
                Customer customer = new Customer(name, surname, email, dateOfBirth);
                customerService.saveCustomer(customer);
                credentials.setCustomer(customer);
            }
            else if(role.equals("CHEF")){
                Chef chef = new Chef(name, surname, email, dateOfBirth);

                try {
                byte[] byteFoto = file.getBytes();
                chef.setImageBase64(Base64.getEncoder().encodeToString(byteFoto));
                chefService.saveChef(chef);
                credentials.setChef(chef);
                } catch (IOException e) {
                    model.addAttribute("message", "Chef upload failed!");
                    return "/registrationPage";
                }

            }
            /*
            else if(role.equals("ADMINISTRATOR")){
                Administrator administrator = new Administrator(name, surname, email, dateOfBirth);
                administratorService.saveAdministrator(administrator);
                credentials.setAdministrator(administrator);
            }
            */
            
            //userService.saveUser(user);
            //credentials.setUser(user);
            credentialsService.saveCredentials(credentials, role);
            model.addAttribute("RegistrationCompleted", true); //non funziona perché c'è il redirect di mezzo
            return "redirect:/loginPage";
        }
        return "registrationPage.html";
    }

    @GetMapping("/admin/creationPage")
    public String getAddChefPage(Model model) {
        //model.addAttribute("newChef", new Chef());
        model.addAttribute("newCredentials", new Credentials());
        //model.addAttribute("newImage", new Image());
        return "admin/creationPage.html";
    }

    @PostMapping("/admin/creationData")
    public String creationUser(@RequestParam(required = false, name = "name") String name, 
                                @RequestParam(required = false, name = "surname") String surname, 
                                @RequestParam(required = false, name = "email") String email, 
                                @RequestParam(required = false, name = "dateOfBirth") LocalDate dateOfBirth,
                                @RequestParam(required = false, name = "image") MultipartFile file,
                                @Valid @ModelAttribute("credentials") Credentials credentials, BindingResult credentialsBindingResult,
                                @RequestParam("role") String role,
                                Model model) {
        //se name è presente nell'invio al controller ma senza valore equivale ad una stringa vuota, se non è proprio presente equivale a NULL
        
        // se user e credential hanno entrambi contenuti validi, memorizza User e the Credentials nel DB
        if(!credentialsBindingResult.hasErrors()) {
            if(role.equals("CUSTOMER")){
                Customer customer = new Customer(name, surname, email, dateOfBirth);
                customerService.saveCustomer(customer);
                credentials.setCustomer(customer);
            }
            else if(role.equals("CHEF")){
                Chef chef = new Chef(name, surname, email, dateOfBirth);

                try {
                byte[] byteFoto = file.getBytes();
                chef.setImageBase64(Base64.getEncoder().encodeToString(byteFoto));
                chefService.saveChef(chef);
                credentials.setChef(chef);
                } catch (IOException e) {
                    model.addAttribute("message", "Chef upload failed!");
                    return "/admin/creationPage";
                }

            }
            else if(role.equals("ADMINISTRATOR")){
                Administrator administrator = new Administrator(name, surname, email, dateOfBirth);
                administratorService.saveAdministrator(administrator);
                credentials.setAdministrator(administrator);
            }
            
            //userService.saveUser(user);
            //credentials.setUser(user);
            credentialsService.saveCredentials(credentials, role);
            model.addAttribute("RegistrationCompleted", true); //non funziona perché c'è il redirect di mezzo
            return "redirect:/";
        }
        return "admin/creationPage.html";
    }
    

    /*
    @PostMapping("admin/addChefData")
    public String newChef(@Valid @ModelAttribute("newChef") Chef chef, BindingResult chefBindingResult, 
                            @Valid @ModelAttribute("newCredentials") Credentials credentials, BindingResult credentialsBindingResult,
                            @RequestParam("image") MultipartFile file, Model model) {
        
        this.chefValidator.validate(chef, chefBindingResult);
        if(!chefBindingResult.hasErrors()){
            try {
                byte[] byteFoto = file.getBytes();
                chef.setImageBase64(Base64.getEncoder().encodeToString(byteFoto));
                chefService.saveChef(chef);
                credentials.setChef(chef);
                credentialsService.saveCredentials(credentials, "CHEF");
                model.addAttribute("message", "Chef uploaded successfully!");
                return "redirect:/all/allChefsPage";
            } catch (IOException e) {
                model.addAttribute("message", "Chef upload failed!");
                return "admin/addChefData.html";
            }
        }
        
        return "admin/addChefData.html";
    }
    */

}
